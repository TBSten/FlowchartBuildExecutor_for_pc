package com.fbe.format;

import com.fbe.item.Item;

public class FBEFormatException extends Exception {
	Item item ;

	public FBEFormatException() {
	}

	public FBEFormatException(String message) {
		super(message);
	}
	public FBEFormatException(String message,Item item) {
		super(message);
		this.item = item ;
	}

	public FBEFormatException(Throwable cause) {
		super(cause);
	}

	public FBEFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public FBEFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


}
