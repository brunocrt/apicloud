package com.game.bean;

import com.game.exception.ErrorCode;
import com.game.exception.ServiceException;

public class RestResult {
    private int state;

    private String message;

    private String value;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RestResult() {

    }

    public RestResult(int state, String message, String value) {

        this.state = state;
        this.message = message;
        this.value = value;
    }

    public RestResult(ErrorCode errorCode) {
        this.state = errorCode.getCode();
        this.message = errorCode.getDesc();
    }
    public RestResult(ServiceException e) {
        this.state = e.getErrorCode().getCode();
        this.message = e.getErrorCode().getDesc();
    }


}
