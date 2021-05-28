package com.fbe.option;

import javafx.scene.control.TextArea;

public class InputableTextArea extends TextArea implements Inputable {

	public InputableTextArea() {
	}

	public InputableTextArea(String arg0) {
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
		return (String)this.get() ;
	}

}
