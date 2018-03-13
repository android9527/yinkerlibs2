package com.jlc.buried.config;

public class EnumType {

    /**
     * 模块开关状态
     */
    public enum ModuleStatus {

        CLOSE(0), // 关闭
        OPEN(1);// 开启
        public final int value;

        ModuleStatus(int value) {
            this.value = value;
        }
    }


}