package com.demo.front.view;

import org.example.common.constants.Constants;

import java.util.List;

/**
 * 统一的应答结果。controller方法的返回值都是它
 */
public class RespResult {
    //应答码
    private int code;
    //code的文字说明
    private String msg;
    //单个数据
    private Object data;
    //集合数据
    private List list;
    //分页
    private PageInfo page;


    //表示成功的RespResult对象
    public static RespResult ok(){
        RespResult result = new RespResult();
        result.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        result.setMsg("请求成功");
        return result;
    }

    //表示失败的RespResult对象
    public static RespResult fail(){
        RespResult result = new RespResult();
        result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
        result.setMsg("请求失败");
        return result;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
