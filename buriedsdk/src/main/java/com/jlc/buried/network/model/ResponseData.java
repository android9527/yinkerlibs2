package com.jlc.buried.network.model;

/**
 * @author deliang.xie
 * @data 2016/5/26 0026
 * @time 下午 3:09
 */
public class ResponseData extends Base {

    private int connectionType;

    private String responseData;


    public int getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

}
