package com.example.demo.Exception;

import lombok.Data;

@Data
public class CustomRequiredException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public CustomRequiredException(String message) {
		super();
		this.message = message;
	}
	
}
