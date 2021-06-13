package com.fbe.exe;

import java.lang.reflect.Array;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Arr1DTracePane extends VarTracePane {


	protected Pane valuePane ;
	protected Label[] lb ;
	protected GridPane gp = new GridPane();
	public Arr1DTracePane(FBEExecutor exe, String name,Object value) {
		super(exe, name,value);
		this.valueLb = null ;
		this.valuePane = gp ;
			this.valuePane.setMinWidth(baseSizeW);
			this.valuePane.setMinHeight(baseSizeH);
		int len = Array.getLength(value);
		lb = new Label[len] ;
		for(int i = 0;i < len;i++) {
			Label l = new Label(Array.get(value, i).toString()) ;
			lb[i] = l ;
			toTracePaneDesign(l);
			gp.add(l, i, 0);
			GridPane.setFillWidth(l, true);
			GridPane.setFillHeight(l, true);
		}
		this.borderPane.setCenter(valuePane);
	}

	public void redraw() {
		Object arr = this.exe.getVar(this.getName()) ;
		for(int i = 0;i < Array.getLength(arr);i++) {
			this.lb[i].setText(String.valueOf(Array.get(arr,i)));
		}
	}

}
