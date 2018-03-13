package com.jlc.buried.network;

import com.jlc.buried.network.model.ResponseData;

public interface NetWorkCallBack {

    void onResponse(ResponseData data);

    void onNetWorkError(String str);
}