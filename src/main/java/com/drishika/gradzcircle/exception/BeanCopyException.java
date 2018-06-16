/**
 * 
 */
package com.drishika.gradzcircle.exception;

/**
 * @author abhinav
 *
 */
public class BeanCopyException extends Exception{

	public BeanCopyException() {
		super();
	}
	
	public BeanCopyException(String message) {
		super(message);
	}
	
	public BeanCopyException(String message, Throwable cause){
		super(message,cause);
	}
	
}
