package com.fbe.option;

import javafx.scene.control.CheckBox;

public class InputableCheckBox extends CheckBox implements Inputable {

	public InputableCheckBox() {
	}

	public InputableCheckBox(String arg0) {
		super(arg0);
	}

	@Override
	public void put(Object value) {
		if(value instanceof Boolean) {
			this.setSelected((boolean)value);
		}else if(value instanceof String) {
			this.setSelected(Boolean.valueOf((String)value));
		}
	}

	@Override
	public Object get() {
		return this.isSelected() ;
	}

	@Override
	public String getString() {
		return String.valueOf(get()) ;
	}

}
