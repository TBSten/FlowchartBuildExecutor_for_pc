package com.fbe.option;

import javafx.scene.layout.Region;

public interface Inputable {
	public void put(Object value) ;
	public Object get() ;
	public String getString() ;
	public default Region getRegion() {
		return (Region) this ;
	}
	public default void args(Object arg) {
	}
}





