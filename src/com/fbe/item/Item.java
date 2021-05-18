package com.fbe.item;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public abstract class Item extends AnchorPane {
	public static double baseWidth = 180 ;
	public static double baseHeight = 40 ;
	public static double baseLineWidth = 3 ;

	protected Label symLabel = new Label("[DEFAULT]");
	protected Canvas symCanvas = new Canvas();

	public Item() {

		//子の設定
		this.symLabel.setWrapText(true);
		this.symLabel.setAlignment(Pos.CENTER);

		//サイズ設定
		this.widthProperty().addListener( e -> redraw() );
		this.heightProperty().addListener( e -> redraw() );
		this.symLabel.prefWidthProperty().bind(this.widthProperty());
		this.symLabel.prefHeightProperty().bind(this.heightProperty());
		this.symCanvas.widthProperty().bind(this.widthProperty());
		this.symCanvas.heightProperty().bind(this.heightProperty());
//		this.widthProperty().addListener( e ->{redraw();});
//		this.heightProperty().addListener( e ->{redraw();});

		this.setWidth(baseWidth);
		this.setHeight(baseHeight);

		this.getChildren().add(this.symCanvas);
		this.getChildren().add(this.symLabel);

		redraw();
	}

	public void redraw() {
		draw();
	}

	protected void setText(String text) {
		this.symLabel.setText(text);
	}
	protected String getText() {
		return this.symLabel.getText();
	}



	public abstract void draw() ;



}
