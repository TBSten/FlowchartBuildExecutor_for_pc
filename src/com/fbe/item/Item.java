package com.fbe.item;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public abstract class Item extends AnchorPane {
	public static double baseWidth = 180 ;
	public static double baseHeight = 40 ;
	public static double baseLineWidth = 3 ;
	public static Color baseLineColor = Color.BLACK ;
	public static Color baseFillColor = Color.WHITE ;
	public static Color focusedLineColor = Color.web("#bad3ff");
	public static Color focusedFillColor = baseFillColor;


	protected Label symLabel = new Label("[DEFAULT]");
	protected Canvas symCanvas = new Canvas();
	protected Flow parentFlow = null ;
	public double itemLineWidth = baseLineWidth ;
	public Color itemLineColor = baseLineColor ;
	public Color itemFillColor = baseFillColor ;


	public Item() {

		//子の設定
		this.symLabel.setWrapText(true);
		this.symLabel.setAlignment(Pos.CENTER);

		//サイズ設定
		this.widthProperty().addListener( e -> redraw() );
		this.heightProperty().addListener( e -> redraw() );
		this.symLabel.prefWidthProperty().bind(this.widthProperty());
		this.symLabel.prefHeightProperty().bind(this.heightProperty());
		this.symLabel.minWidthProperty().bind(this.widthProperty());
		this.symLabel.minHeightProperty().bind(this.heightProperty());
		this.symCanvas.widthProperty().bind(this.widthProperty());
		this.symCanvas.heightProperty().bind(this.heightProperty());
//		this.widthProperty().addListener( e ->{redraw();});
//		this.heightProperty().addListener( e ->{redraw();});

		this.setWidth(baseWidth);
		this.setHeight(baseHeight);


		this.getChildren().add(this.symCanvas);
		this.getChildren().add(this.symLabel);

		this.focusedProperty().addListener(e->{
			if(isFocused()) {
				changeFocusedDesign();
//				System.out.println("focused:"+this);
			}else {
				changeUnfocusedDesign();
//				System.out.println("unfocused:"+this);
			}
			redraw();
		});

		redraw();
	}

	protected void changeFocusedDesign() {
		this.itemLineColor = focusedLineColor;
		this.itemFillColor = focusedFillColor;
	}
	protected void changeUnfocusedDesign() {
		this.itemLineColor = baseLineColor;
		this.itemFillColor = baseFillColor;

	}
	public void redraw() {
/*
		this.setWidth(this.getPrefWidth());
		this.setHeight(this.getPrefHeight());
*/
		this.requestParentLayout();
		this.requestLayout();
		draw();
	}

	protected void setText(String text) {
		this.symLabel.setText(text);
	}
	protected String getText() {
		return this.symLabel.getText();
	}



	public abstract void draw() ;

	//出力結果を返すメソッドを用意

	@Override public String toString() {
		return this.getClass().getName()+"["+symLabel.getText()+"]" ;
	}

	public void setParentFlow(Flow f) {
		this.parentFlow = f ;
	}
	public Flow getParentFlow() {
		return this.parentFlow ;
	}


}
