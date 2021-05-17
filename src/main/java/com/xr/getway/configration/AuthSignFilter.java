package com.xr.getway.configration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xr.getway.configration.base.BaseResponse;
import com.xr.getway.entity.BlackShop;
import com.xr.getway.mapper.BlackShopMapper;
import com.xr.getway.mapper.BlackUrlMapper;
import com.xr.getway.service.BlackShopService;
import com.xr.getway.util.JwtUtil.JwtUtil;
import io.netty.buffer.ByteBufAllocator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Author lwk
 * @Date 2021/4/27 14:28
 * @Version 1.0
 * @Description 鉴权
 * 问题 ： 当要获取到 post 请求参数body时 需要做全局缓存处理才能够在局部过滤器中获取到body参数
 */
//@Component
public class AuthSignFilter implements GlobalFilter, Ordered {

    //@Autowired
    private BlackShopService blackShopService;


    @SneakyThrows
    @Override
    public Mono filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取到总的请求数据
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        //获取到请求类型
        String method = serverHttpRequest.getMethodValue();
        //获取需要响应的数据
        ServerHttpResponse response = exchange.getResponse();
        //处理响应的结果乱码问题
        response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
        //如果是POST请求
        if ("POST".equals(method)) {
            //从全局缓存里获取Post请求体
            String bodyStr = resolveBodyFromRequest(serverHttpRequest);

            //TODO 得到Post请求的请求参数后，做你想做的事
            //获取到当前请求地址
            URI uri1 = exchange.getRequest().getURI();
            String path = uri1.getPath();
            //过滤出登录接口
            if (!"/rep/login".equals(path)) {
                //当不是登录接口的时候 验证是否在黑名单中
                Mono mono1 = blackShop(response, chain, exchange, bodyStr);
                HttpStatus statusCode1 = response.getStatusCode();
                if (statusCode1 != null) {
                    //获取到当前状态码
                    int value = statusCode1.value();
                    if (value != 200) {
                        return mono1;
                    }
                } else {
                    throw new Exception("发生了不知名的错误");
                }

                //当是POST请求的时候 拿到requestHeaders中的token
                //获取到请求头里面的所有的数据
                HttpHeaders headers = serverHttpRequest.getHeaders();
                String token = headers.getFirst("token");
                //拿到token后开始验证token
                Mono mono = yzToken(response, chain, exchange, token);
                HttpStatus statusCode = response.getStatusCode();
                if (statusCode != null) {
                    //获取到当前状态码
                    int value = statusCode.value();
                    if (value != 200) {
                        return mono;
                    }
                } else {
                    throw new Exception("发生了不知名的错误");
                }
            }
            //TODO END


            //下面的将请求体再次封装写回到request里，传到下一级，否则，由于请求体已被消费，后续的服务将取不到值
            URI uri = serverHttpRequest.getURI();
            ServerHttpRequest request = serverHttpRequest.mutate().uri(uri).build();
            DataBuffer bodyDataBuffer = stringBuffer(bodyStr);
            Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);

            request = new ServerHttpRequestDecorator(request) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return bodyFlux;
                }
            };
            //封装request，传给下一级
            return chain.filter(exchange.mutate().request(request).build());
        } else if ("GET".equals(method)) {
            //获取到get后面的所有参数
            MultiValueMap<String, String> requestQueryParams = serverHttpRequest.getQueryParams();
            //取到特定的 token 元素参数
            String token = requestQueryParams.getFirst("token");

            //TODO 得到Get请求的请求参数后，做你想做的事 (当前只添加了验证token的操作)
            URI uri1 = exchange.getRequest().getURI();
            String path = uri1.getPath();
            //过滤出登录接口
            if (!"/rep/login".equals(path)) {
                //当不是登录接口的时候 验证是否在黑名单中
                Mono mono1 = blackShop(response, chain, exchange, requestQueryParams.getFirst("shopId"));
                HttpStatus statusCode1 = response.getStatusCode();
                if (statusCode1 != null) {
                    //获取到当前状态码
                    int value = statusCode1.value();
                    if (value != 200) {
                        return mono1;
                    }
                } else {
                    throw new Exception("发生了不知名的错误");
                }


                //如果不是登录接口就需要token验证
                return yzToken(response, chain, exchange, token);
            }
            //如果是登录接口就直接放行
            return chain.filter(exchange);

        }
        return chain.filter(exchange);
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     *
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();

        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }

    private DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    //验证token
    public Mono yzToken(ServerHttpResponse response, GatewayFilterChain chain, ServerWebExchange exchange, String token) {
        String messageEr;
        //判断token是否为空
        if ("null".equalsIgnoreCase(token) || "".equals(token) || token == null) {
            messageEr = "您还没有登录系统，请先登录系统再做此操作！！";
            return errMessage(response, messageEr);
        } else {
            //当不为空的时候需要验证token
            BaseResponse baseResponse = JwtUtil.verificationToken(token, "QQ@qqqq");
            if (baseResponse.getStatus() == 401) {
                String message = baseResponse.getMessage();
                return errMessage(response, message);
            } else if (baseResponse.getStatus() == 200) {
                //直接放行
                return chain.filter(exchange);
            } else {
                messageEr = "出现了不知明的错误！请联系管理员！";
                return errMessage(response, messageEr);
            }
        }
    }

    /**
     * token 验证失败后 要输出的提示消息
     *
     * @param response Mono
     * @return
     */
    public Mono errMessage(ServerHttpResponse response, String messageEr) {
        JSONObject message = new JSONObject();
        message.put("code", 401);
        message.put("message", messageEr);
        //将自定义的异常消息转换成一个UTF-8编码的字符串
        byte[] bytes = message.toString().getBytes(StandardCharsets.UTF_8);
        //将自定义消息放到响应中
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.just(buffer));
    }


    //验证黑名单
    public Mono blackShop(ServerHttpResponse response, GatewayFilterChain chain, ServerWebExchange exchange, String bodyStr) {
        List<BlackShop> blackShops = blackShopService.selectList();
        //如果白名单为空 直接放行
        if (CollectionUtils.isEmpty(blackShops)) {
            //直接放行
            return chain.filter(exchange);
        } else {
            Set<BlackShop> blackSet = blackShops.stream().filter(s -> s.getShopId().equals(bodyStr)).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(blackSet)) {
                //直接放行
                return chain.filter(exchange);
            } else {
                JSONObject message2 = new JSONObject();
                message2.put("code", 401);
                message2.put("message", "您已经被加入黑名单，无法操作此系统！！");
                //将自定义的异常消息转换成一个UTF-8编码的字符串
                byte[] bytes = message2.toString().getBytes(StandardCharsets.UTF_8);
                //将自定义消息放到响应中
                DataBuffer buffer = response.bufferFactory().wrap(bytes);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.writeWith(Mono.just(buffer));
            }
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }


}
