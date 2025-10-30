package com.example.demo.controller;


public class Error {
    private long errorCode;
    private String message;
    public Error(){

    }
    public Error(Error error){
        this.errorCode=error.errorCode;
        this.message= error.message;
    }
    public Error(long errorCode, String message){
        this.errorCode=errorCode;
        this.message=message;

    }
}
