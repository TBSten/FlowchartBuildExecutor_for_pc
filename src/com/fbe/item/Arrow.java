package com.fbe.item;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Arrow extends Item {

	//parentフロー

	public static int cnt = 0 ;
	public int id = 0 ;

	public Arrow() {
		setText("");
		this.setHeight(baseHeight/4);
		this.setPrefHeight(baseHeight/4);
		this.setMaxHeight(baseHeight/4);
		this.setMinHeight(baseHeight/4);
	}

	@Override
	public void draw() {
		GraphicsContext gc = this.symCanvas.getGraphicsContext2D();
/*
		gc.setFill(Color.BLUE);
		gc.fillRect(0,0,getWidth(),getHeight());
*/
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(baseLineWidth);
		gc.strokeLine(getWidth()/2, 0, getWidth()/2, getHeight());
	}

}
