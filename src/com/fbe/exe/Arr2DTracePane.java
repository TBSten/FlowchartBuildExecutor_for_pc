package com.fbe.exe;

import java.lang.reflect.Array;

import javafx.scene.control.Label;

public class Arr2DTracePane extends Arr1DTracePane {

	protected Label[][] lb2d ;
	public Arr2DTracePane(FBEExecutor exe, String name, Object value) {
		super(exe, name, value);
		this.gp.getChildren().clear();
		int xlen = Array.getLength(value) ;
		int ylen = Array.getLength(Array.get(value,0)) ;
		lb2d = new Label[ylen][xlen] ;
		for(int y = 0;y < ylen;y++) {
			for(int x = 0;x < xlen;x++) {
				lb2d[y][x] = new Label(String.valueOf(Array.get(Array.get(value,x),y))) ;
				toTracePaneDesign(lb2d[y][x]);
				gp.add(lb2d[y][x], y, x);
			}
		}
	}

	@Override
	public void redraw() {
		Object value = this.exe.getVar(this.getName());
		int xlen = Array.getLength(value) ;
		int ylen = Array.getLength(Array.get(value,0)) ;
		for(int y = 0;y < ylen;y++) {
			for(int x = 0;x < xlen;x++) {
				lb2d[y][x].setText(String.valueOf(Array.get(Array.get(value,x),y))) ;
			}
		}
	}
}
