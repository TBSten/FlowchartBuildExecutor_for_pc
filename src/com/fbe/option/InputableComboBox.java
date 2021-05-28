package com.fbe.option;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class InputableComboBox<T> extends ComboBox<T> implements Inputable {

	public InputableComboBox() {
		super();
	}

	public InputableComboBox(ObservableList<T> arg0) {
		super(arg0);
	}

	@Override
	public void put(Object value) {
		this.setValue((T)value);
	}


	@Override
	public Object get() {
		return this.getValue() ;
	}

	@Override
	public String getString() {
		return String.valueOf(this.get()) ;
	}

	@Override
	public void args(Object arg) {
		this.getItems().add((T)arg);
	}

}
