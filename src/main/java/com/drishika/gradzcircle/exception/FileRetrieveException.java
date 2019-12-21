package com.drishika.gradzcircle.exception;

public class FileRetrieveException extends Exception {
	

	public FileRetrieveException() {

	}

	public FileRetrieveException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public FileRetrieveException(String exceptionMessage, String exceptionCode) {
		super(exceptionMessage + " --- " + exceptionCode);
	}
	
	public FileRetrieveException(String exceptionMessage, String bucket, String key) {
		super(exceptionMessage + " --- " + bucket + " --- "+ key);
	}

}