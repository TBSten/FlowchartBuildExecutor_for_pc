package com.fbe.option;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class InputableSpinButton<T> extends Spinner<T> implements Inputable {

	public InputableSpinButton() {
		this(0,1000,0);
	}

	public InputableSpinButton(double arg0, double arg1, double arg2, double arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public InputableSpinButton(double arg0, double arg1, double arg2) {
		super(arg0, arg1, arg2);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public InputableSpinButton(int arg0, int arg1, int arg2, int arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public InputableSpinButton(int arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public InputableSpinButton(ObservableList<T> arg0) {
		super(arg0);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public InputableSpinButton(SpinnerValueFactory<T> arg0) {
		super(arg0);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void put(Object value) {
		this.getValueFactory().setValue((T)value);	//???
	}

	@Override
	public Object get() {
		Node n = new TextField();
		return this.getValue();
	}

	@Override
	public String getString() {
		return (String)this.get();
	}

}
