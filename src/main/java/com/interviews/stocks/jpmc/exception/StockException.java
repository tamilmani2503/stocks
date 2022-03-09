package com.interviews.stocks.jpmc.exception;

public class StockException extends Exception {
	
	private String message;

	public StockException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
