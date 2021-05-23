package com.fbe.exe;

public class Constant extends Variable {


	Object con = null ;
	public Constant(Object con) {
		super(null, String.valueOf(con));
		this.con = con ;
	}
	@Override public Object get() {
		return this.con;
	}
	@Override public Object parse() {
		return this.con ;
	}

}
