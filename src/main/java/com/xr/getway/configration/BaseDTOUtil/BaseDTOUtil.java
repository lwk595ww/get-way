package com.xr.getway.configration.BaseDTOUtil;


import com.github.pagehelper.PageInfo;
import com.xr.getway.configration.base.*;
import lombok.Data;

import java.util.Collection;

/**
 * @Author : lwk
 * @Description
 * @Date: Created in 14:15 2021/1/13
 */
@Data
public class BaseDTOUtil {

    /**
     *
     * @return 成功响应 （状态200）
     */
    public static BaseResponse getBaseResponseSuccess() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(200);
        return baseResponse;
    }

    /**
     *
     * @param data  需要返回的数据
     * @param <T>   类型
     * @return      成功响应 （状态200 并返回数据）
     */
    public static <T> BaseResponse<T> getBaseResponseSuccess(T data) {
        BaseResponse<T> baseResponse = new BaseResponse();
        baseResponse.setStatus(200);
        baseResponse.setData(data);
        return baseResponse;
    }

    /**
     *
     * @param status     自定义状态
     * @param message    自定义提示消息
     * @return           失败响应结果
     */
    public static BaseResponse getBaseResponseFail(Integer status, String message) {
        BaseResponse<String> baseResponse = new BaseResponse();
        baseResponse.setStatus(status);
        baseResponse.setMessage(message);
        return baseResponse;
    }

    /**
     *
     * @param message 自定义提示消息
     * @return        失败响应码固定（用户特定的响应码做需求）
     */
    public static BaseResponse getBaseResponseFail(String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(9008);
        baseResponse.setMessage(message);
        return baseResponse;
    }

    /**
     *
     * @param status    自定义失败状态
     * @param message   自定义失败提示
     * @param data      失败返回数据
     * @param <T>       相应的类型
     * @return          失败后返回相应的 状态 消息 数据
     */
    public static <T> BaseResponse<T> getBaseResponseFail(Integer status, String message, T data) {
        BaseResponse<T> baseResponse = new BaseResponse();
        baseResponse.setStatus(status);
        baseResponse.setMessage(message);
        baseResponse.setData(data);
        return baseResponse;
    }

    /**
     *
     * @param message  自定义失败提示
     * @param data     失败返回数据
     * @param <T>
     * @return         返回特定失败码状态
     */
    public static <T> BaseResponse<T> getBaseResponseFail(String message, T data) {
        BaseResponse<T> baseResponse = new BaseResponse();
        baseResponse.setStatus(9008);
        baseResponse.setMessage(message);
        baseResponse.setData(data);
        return baseResponse;
    }

    /**
     * 普通分页响应
     * @param pageInfo  分页类
     * @param <T>
     * @return          返回成功后 相应的分页各个属性数据
     */
    public static <T> BasePageResponseInfo<T> getBasePageResponseInfo(PageInfo<T> pageInfo) {
        BasePageResponseInfo<T> basePageResponseInfo = new BasePageResponseInfo();
        basePageResponseInfo.setPage(pageInfo.getPageNum());
        basePageResponseInfo.setTotalPage(pageInfo.getPages());
        basePageResponseInfo.setRecords(pageInfo.getTotal());
        basePageResponseInfo.setRows(pageInfo.getList());
        return basePageResponseInfo;
    }

    /**
     *  两个结果集分页响应
     * @param pageInfo 分页类
     * @param sunRow   第二结果集
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> BasePageSumRowResponseInfo<T, V> getBasePageSumRowResponseInfo(PageInfo<T> pageInfo, V sunRow) {
        BasePageSumRowResponseInfo<T, V> basePageSumRowResponseInfo = new BasePageSumRowResponseInfo();
        basePageSumRowResponseInfo.setPage(pageInfo.getPageNum());
        basePageSumRowResponseInfo.setTotalPage(pageInfo.getPages());
        basePageSumRowResponseInfo.setRecords(pageInfo.getTotal());
        basePageSumRowResponseInfo.setRows(pageInfo.getList());
        basePageSumRowResponseInfo.setSumRow(sunRow);
        return basePageSumRowResponseInfo;
    }

    /**
     * 适用于 不需要分页 但是需要多个结果集的类型
     * @param rows     第一个结果集
     * @param sunRow   第二个结果集
     * @param <V>
     * @return
     */
    public static <T,V> BaseSumRowResponseInfo<T, V> getBaseSumRowResponseInfo(T rows, V sunRow) {
        BaseSumRowResponseInfo<T, V> baseSumRowResponseInfo = new BaseSumRowResponseInfo();
        if (rows instanceof  Collection){
            baseSumRowResponseInfo.setRecords(((Collection) rows).size());
        }else {
            baseSumRowResponseInfo.setRecords(1);
        }

        if (sunRow instanceof Collection){
            baseSumRowResponseInfo.setRecords2(((Collection) sunRow).size());
        }else {
            baseSumRowResponseInfo.setRecords2(1);
        }
        baseSumRowResponseInfo.setRows(rows);
        baseSumRowResponseInfo.setSumRow(sunRow);
        return baseSumRowResponseInfo;
    }

    public static <T> BaseRequest<T> getBaseRequest(T data) {
        BaseRequest<T> baseRequest = new BaseRequest();
        baseRequest.setData(data);
        return baseRequest;
    }

    public static <T> BaseRequest<T> getBaseRequest(T data, String operation) {
        BaseRequest<T> baseRequest = new BaseRequest();
        baseRequest.setData(data);
        return baseRequest;
    }

    public static <T> BasePageRequestInfo<T> getBasePageRequestInfo(Integer page, Integer rows, String orderBy, String searchText, T condition) {
        BasePageRequestInfo<T> basePageRequestInfo = new BasePageRequestInfo();
        basePageRequestInfo.setPage(page);
        basePageRequestInfo.setRows(rows);
        basePageRequestInfo.setOrderBy(orderBy);
        basePageRequestInfo.setSearchText(searchText);
        basePageRequestInfo.setCondition(condition);
        return basePageRequestInfo;
    }
}
