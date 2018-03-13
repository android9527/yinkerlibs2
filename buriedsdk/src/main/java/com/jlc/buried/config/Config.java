package com.jlc.buried.config;

import android.os.Environment;

/**
 * 数据埋点SDK配置信息
 *
 * @author deliang.xie
 * @data 2016/5/19 0019
 * @time 上午 11:11
 */
public class Config {

    /**
     * 数据库版本号
     */
    public final static int DATABASE_VERSION = 1;
    /**
     * api-version
     */
    public final static double API_VERSION = 1.0;
    /**
     * 数据库名称
     */
    public final static String DATABASE_NAME = "buried_data.db";
    /**
     * 创建数据库SQl
     */
    public final static String SQL =
            "CREATE TABLE if not exists ACCESS(_Id BIGINT AUTO_INCREMENT PRIMARY KEY,AppKey VARCHAR(50), Ticket VARCHAR(50),Effect_Time BIGINT,Module_Status VARCHAR(100));" +
                    "CREATE TABLE if not exists MODULE(_Id BIGINT AUTO_INCREMENT PRIMARY KEY ,Module_DATA TEXT )";

    public final static String HOST = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/YK/Buried/";

    public static final int REQUEST_TIMEOUT = 30 * 1000;//设置请求超时30秒钟
}
