package com.fbe.option;

import javafx.scene.control.TextField;

public class InputableTextField extends TextField implements Inputable {

	public InputableTextField() {
		super();
	}
	public InputableTextField(String arg0) {
		super(arg0);
	}
	@Override
	public void put(Object value) {
		this.setText((String)value);
	}

	@Override
	public Object get() {
		return this.getText();
	}
	@Override
	public String getString() {
		return this.getText();
	}


}
