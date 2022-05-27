package com.example.demo.Exception;

public class AccountUpdateException extends Exception{
	
	private static final long serialVersionUID = 1L;
	String errorCode;
	String errorMessage;
	
	public AccountUpdateException()
	{
		super();
	}
	
	public AccountUpdateException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public AccountUpdateException(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public AccountUpdateException(Throwable t)
	{
		super(t);
	}
	
	public AccountUpdateException(Throwable t, String errorMessage) {
		super(errorMessage,t);
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
	
	

}
