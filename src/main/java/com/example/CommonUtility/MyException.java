package com.example.CommonUtility;

public class MyException extends Exception {
    String exceptionCode;
    String details;
    public MyException(String exceptionCode){
        this.exceptionCode = exceptionCode;
    }

    public String getMessage(){
        return exceptionCode;
    }
}
