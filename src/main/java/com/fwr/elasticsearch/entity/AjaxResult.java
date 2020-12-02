package com.fwr.elasticsearch.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fomjar
 */
@Slf4j
public class AjaxResult {

    public static AjaxResult success() {
        return success(null);
    }

    public static AjaxResult success(Object datas) {
        return new AjaxResult()
                .setSuccess(true)
                .setErrCode(0)
                .setErrMsg("success")
                .setErrName(null)
                .setDatas(datas);
    }

    public static AjaxResult fail(int errCode) {
        return fail(errCode, null, null, null);
    }

    public static AjaxResult fail(int errCode, String errMsg) {
        return fail(errCode, errMsg, null, null);
    }
    public static AjaxResult fail(int errCode, String errMsg,String datas) {
        return fail(errCode, errMsg, null, datas);
    }
    public static AjaxResult fail(String errMsg) {
        return fail(201, errMsg, null, null);
    }
    public static AjaxResult fail(int errCode, String errMsg, String errName, String datas) {
        return new AjaxResult()
                .setSuccess(false)
                .setErrCode(errCode)
                .setErrMsg(errMsg)
                .setErrName(errName)
                .setDatas(datas);
    }


    private boolean success;
    private int     errCode;
    private String  errMsg;
    private String  errName;
    private int     totalCount;
    private Object  datas;
    private int     pageNum;
    private int     pageSize;

    private AjaxResult() {
        this.success    = true;
        this.errCode    = 0;
        this.errMsg     = "success";
        this.errName    = "";
        this.totalCount = 0;
        this.datas      = null;
        this.pageNum    = 1;
        this.pageSize   = 10;
    }

    public boolean  isSuccess()     {return this.success;}
    public int      getErrCode()    {return this.errCode;}
    public String   getErrMsg()     {return this.errMsg;}
    public String   getErrName()    {return this.errName;}
    public int      getTotalCount() {return this.totalCount;}
    public Object   getDatas()      {return this.datas;}
    public int      getPageNum()    {return this.pageNum;}
    public int      getPageSize()   {return this.pageSize;}


    public AjaxResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public AjaxResult setErrCode(int errCode) {
        this.errCode = errCode;
        return this;
    }

    public AjaxResult setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    public AjaxResult setErrName(String errName) {
        this.errName = errName;
        return this;
    }

    public AjaxResult setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public AjaxResult setDatas(Object datas) {
        this.datas = datas;
        return this;
    }

    public AjaxResult setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public AjaxResult setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            String error = "convert object to json failed";
            log.error(error, e);
            throw new IllegalArgumentException(error);
        }
    }

}
