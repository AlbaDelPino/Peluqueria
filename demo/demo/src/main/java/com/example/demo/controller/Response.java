package com.example.demo.controller;


public class Response {

    public static final int NO_ERROR = 0;
    public static final int NOT_FOUND = 101;

    public static final String NO_MESSAGE = "";

    private Error error;

    public Response(){
    }

    public Response(Error error){
        this.error=error;
    }
    public static Response noErrorResponse() {
        return new Response(new Error(NO_ERROR, NO_MESSAGE));
    }

    public static Response errorResonse(int errorCode, String
            errorMessage) {
        return new Response(new Error(errorCode, errorMessage));
    }
}
