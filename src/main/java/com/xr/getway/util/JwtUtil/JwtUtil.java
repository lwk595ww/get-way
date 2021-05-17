package com.xr.getway.util.JwtUtil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.xr.getway.configration.BaseDTOUtil.BaseDTOUtil;
import com.xr.getway.configration.base.BaseResponse;
import lombok.SneakyThrows;
import sun.misc.BASE64Decoder;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : lwk
 * @Description
 * @Date: Created in 14:00 2021/1/22
 */
public class JwtUtil {

    /**
     *          JWT = header + payLoad + sign
     *          sign = 加密(header + payLoad + 私钥)
     *         //创建一个jwt实例 header省略 默认是jwt
     *         String sign = JWT.create()         //创建实例
     *                .withClaim("id", "12")      //设置自包含体
     *                .withClaim("name", "12")
     *                .withExpiresAt(calendar.getTime()) //设置过期时间
     *                .sign(Algorithm.HMAC256("1EWQFQ143124")); //设置加密算法以及加密私钥
     * @param payLoad
     * @param calendar
     * @param privateKey
     * @return
     */
    public static BaseResponse getJwt(Map<String,String> payLoad,Calendar calendar,String privateKey){
        //创建JWT实例
        JWTCreator.Builder jwtCreate = JWT.create();
        //设置payLoad 自包含体
        for (String s : payLoad.keySet()) {
            jwtCreate.withClaim(s,payLoad.get(s));
        }
        //设置过期时间 以及加密算法
        return BaseDTOUtil.getBaseResponseSuccess(jwtCreate.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(privateKey))) ;
    }


    public static void main(String[] args) {
        Map<String,String> payLoad = new HashMap<>();
        payLoad.put("111","2222");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,1);
        BaseResponse jwt = getJwt(payLoad, calendar, "QQ@qqqq");
        System.out.println(jwt.getData());

        BaseResponse baseResponse = verificationToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyIxMTEiOiIyMjIyIiwiZXhwIjoxNjUxMTI4NzAzfQ.1G725xAjMDfZ30TshNpmELgp9b_E7-Hqo97mQJo3KFc", "QQ@qqqq");
        System.err.println(baseResponse);
    }

    @SneakyThrows
    public static BaseResponse verificationToken(String token, String privateKey){
        String jsonStr;
        try {
            //验签
            JWTVerifier build = JWT.require(Algorithm.HMAC256(privateKey)).build();
            //验证token
            DecodedJWT verify = build.verify(token);
            //获取到payLoad部分的Base64字符串
            String payload = verify.getPayload();
            //进行Base64解码
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(payload);
            String str = new String(b, StandardCharsets.UTF_8);
            //处理成正确的json
            String[] split = str.split("}");
            jsonStr = split[0] + "}";
            return BaseDTOUtil.getBaseResponseSuccess(jsonStr);
        }catch (SignatureVerificationException s){
            return BaseDTOUtil.getBaseResponseFail(401,"您的秘钥错误，token无效！验签失败！！请重新登录"+s);
        }catch (TokenExpiredException t){
            return BaseDTOUtil.getBaseResponseFail(401,"您的token已过期！！请重新登录！！"+t);
        }catch (Exception e){
            return BaseDTOUtil.getBaseResponseFail(401,"您的token是无效token！！请重新登录！！"+e);
        }
    }
}
