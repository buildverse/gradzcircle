package com.drishika.gradzcircle.exception;

public class FileUploadException extends Exception {

    public FileUploadException() {

    }

    public FileUploadException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public FileUploadException(String exceptionMessage,String exceptionCode) {
        super(exceptionMessage+" --- "+exceptionCode);
    }
}