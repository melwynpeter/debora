package com.mel.debora_v11.api;

public class ApiResponseModel {
    String message;

    public ApiResponseModel(){}
    public ApiResponseModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
