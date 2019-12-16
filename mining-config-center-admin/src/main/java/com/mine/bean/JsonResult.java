package com.mine.bean;

/**
 * zyp
 * 用来封装返回给web端的数据
 */
public class JsonResult<T> {

    /**  执行成功状态        **/
    public static final int OK_STATUS = 200;

    public static final String OK_MSG =  "Success";

    /**  服务端异常        **/
    public static final int FAIL_STATUS = 500;

    public static final String FAIL_MSG = "error";

    public static final int USER_VALIDATE_ERROR = 401;

    public static final int FORBIDDEN_ERROR = 403;

    public static final int  PARAM_ERROR = 400;

    public static final int  BUSINESS_ERROR = 501;

    public JsonResult(int status, String msg, T data, Integer totalCount) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.totalCount = totalCount;
    }

    public JsonResult(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public JsonResult(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    /**  响应状态         **/
    private int status;

    /**    状态描述       **/
    private String msg;

    /**  返回数据     **/

    private T data;

    /**  分页查询返回数据条数       **/
    private Integer totalCount;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public boolean ok(){

        return status ==  OK_STATUS;
    }


}
