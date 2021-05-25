package com.fbe;

public class FBERunnable implements Runnable {
	/**
	 * この処理の処理名を表す。
	 */
	protected String name = "デフォルト" ;
	protected Runnable r = null ;

	public FBERunnable(){
	}
	public FBERunnable(Runnable runnable) {
		this();
		this.r = runnable;
	}
	@Override public void run() {
		this.r.run();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Runnable getRunnable() {
		return r;
	}
	public void setRunnable(Runnable r) {
		this.r = r;
	}

}
