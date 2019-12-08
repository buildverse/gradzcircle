/**
 * 
 */
package com.drishika.gradzcircle.exception;

/**
 * @author abhinav
 *
 */
public class NoPreviousJobException extends Exception{
	
	public NoPreviousJobException() {
		super();
	}

	public NoPreviousJobException(String message) {
		super(message);
	}

	public NoPreviousJobException(String message, Throwable cause) {
		super(message, cause);
	}

}
