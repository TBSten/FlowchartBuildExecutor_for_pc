package com.fbe.item;

import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class Arrow extends Item {

	//parentフロー

	public static int cnt = 0 ;
	public int id = 0 ;

	public Arrow() {
		setText("");
		/*
		this.setHeight(baseHeight/4);
		this.setPrefHeight(baseHeight/4);
		this.setMaxHeight(baseHeight/4);
		this.setMinHeight(baseHeight/4);
		 */


		FlowPane pane = new FlowPane();
		pane.setAlignment(Pos.CENTER);
		this.getChildren().add(pane);
		pane.maxWidthProperty().bind(this.widthProperty());
		pane.minWidthProperty().bind(this.widthProperty());
		pane.maxHeightProperty().bind(this.heightProperty());
		pane.minHeightProperty().bind(this.heightProperty());
		Button addB = new Button("＋") ;
		pane.getChildren().add(addB);
		addB.setOnAction( e -> {

			//追加
		});

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
