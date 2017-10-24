package com.drishika.gradzcircle.exception;

public class FileRemoveException extends Exception {

    public FileRemoveException() {

    }

    public FileRemoveException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public FileRemoveException(String exceptionMessage,String exceptionCode) {
        super(exceptionMessage+" --- "+exceptionCode);
    }

}