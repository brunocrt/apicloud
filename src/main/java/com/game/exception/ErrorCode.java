package com.game.exception;

public enum  ErrorCode {

    //未考虑多语言
    SUCCESS(0, "成功"),

    SERVER_ERROR(500, "服务器异常"),


    USER_NO_REGISTER(1001, "用户未注册");




    private ErrorCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    private int code;

    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
