package com.fbe.exe;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class VarTracePane extends AnchorPane {
	public static final Map<String,VarTracePane> varTracePanes = new HashMap<>() ;

	private String name ;
	private FBEExecutor exe ;
	private Label valueLb ;
	public VarTracePane(FBEExecutor exe,String name){

		this.name = name ;
		this.exe = exe ;

		double sizeW = 40 ;
		double sizeH = 40 ;
		/*
		this.setMaxWidth(sizeW);
		this.setMinWidth(sizeW);
		this.setPrefWidth(sizeW);
		this.setMaxHeight(sizeH);
		this.setMinHeight(sizeH);
		this.setPrefHeight(sizeH);
		*/

		BorderPane fp = new BorderPane() ;
			/*
			fp.prefWidthProperty().bind(this.widthProperty());
			fp.maxWidthProperty().bind(this.widthProperty());
			fp.minWidthProperty().bind(this.widthProperty());
			fp.prefHeightProperty().bind(this.heightProperty());
			fp.maxHeightProperty().bind(this.heightProperty());
			fp.minHeightProperty().bind(this.heightProperty());
			*/
		Label nameLb = new Label(name);
		nameLb.setMinWidth(USE_PREF_SIZE);
		nameLb.setMinHeight(USE_PREF_SIZE);
		this.valueLb = new Label();
			this.valueLb.setStyle("-fx-border-color:black;-fx-border-width:2;-fx-border-style: solid;-fx-background-color:white;");
			this.valueLb.setFont(Font.font(20));
			this.valueLb.setMinWidth(sizeW);
			this.valueLb.setMinHeight(sizeH);
			valueLb.setTextAlignment(TextAlignment.CENTER);
			valueLb.setAlignment(Pos.CENTER);
			valueLb.setTextFill(Color.BLACK);
			valueLb.setWrapText(true);
		this.getChildren().add(fp);
		fp.setTop(nameLb);
		fp.setCenter(valueLb);

		varTracePanes.put(name, this) ;
	}

	public void redraw() {
		String name = this.name ;
		String value = exe.getVar(name).toString() ;
		this.valueLb.setText(value);

	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return exe.getVar(this.name );
	}
}
