package com.fbe.option;

public class OptionException extends Exception {
	Inputable inputable ;

	public OptionException() {
	}

	public OptionException(String message) {
		super(message);
	}
	public OptionException(String message,Inputable inputable) {
		super(message);
		this.inputable = inputable ;
	}

	public OptionException(Throwable cause) {
		super(cause);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public OptionException(String message, Throwable cause) {
		super(message, cause);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public OptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO 自動生成されたコンストラクター・スタブ
	}

}
