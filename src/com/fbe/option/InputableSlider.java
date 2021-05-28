package com.fbe.option;

import javafx.scene.control.Slider;

public class InputableSlider extends Slider implements Inputable {

	public InputableSlider() {
	}

	public InputableSlider(double arg0, double arg1, double arg2) {
		super(arg0, arg1, arg2);
	}

	@Override
	public void put(Object value) {
		if(value instanceof Double) {
			this.setValue((double)value);
		}else if(value instanceof Integer) {
			this.setValue((int)value);
		}
	}

	@Override
	public Object get() {
		return this.getValue();
	}

	@Override
	public String getString() {
		return (String)(this.get());
	}

}
