package com.fbe.exe;

import java.lang.reflect.Array;
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
	public static VarTracePane createTracePane(FBEExecutor exe,String name, Object value) {
		try {
			if(value != null && value.getClass().isArray()) {
				Object w1 = Array.get(value, 0) ;
				if(w1.getClass().isArray()) {
					Object w2 = Array.get(w1, 0);
					if(w2.getClass().isArray()) {
						//3次元配列
						return new UnvisibleTracePane(exe,name,value,"3次元配列")  ;
					}else {
						//2次元配列
						return new Arr2DTracePane(exe,name,value) ;
					}
				}else {
					//1次元配列
					return new Arr1DTracePane(exe,name ,value) ;
				}
			}else {
				//変数
				return new VarTracePane(exe,name,value);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return new UnvisibleTracePane(exe,name,value,"不正な型") ;
		}
	}
	public static double baseSizeW = 40 ;
	public static double baseSizeH = 40 ;


	private String name ;
	protected FBEExecutor exe ;
	protected BorderPane borderPane = new BorderPane() ;
	protected Label valueLb ;
	public VarTracePane(FBEExecutor exe,String name,Object value){

		this.name = name ;
		this.exe = exe ;

		/*
		this.setMaxWidth(baseSizeW);
		this.setMinWidth(baseSizeW);
		this.setPrefWidth(baseSizeW);
		this.setMaxHeight(baseSizeH);
		this.setMinHeight(baseSizeH);
		this.setPrefHeight(baseSizeH);
		*/

			/*
			borderPane.prefWidthProperty().bind(this.widthProperty());
			borderPane.maxWidthProperty().bind(this.widthProperty());
			borderPane.minWidthProperty().bind(this.widthProperty());
			borderPane.prefHeightProperty().bind(this.heightProperty());
			borderPane.maxHeightProperty().bind(this.heightProperty());
			borderPane.minHeightProperty().bind(this.heightProperty());
			*/
		Label nameLb = new Label(name);
		nameLb.setMinWidth(USE_PREF_SIZE);
		nameLb.setMinHeight(USE_PREF_SIZE);
		this.valueLb = new Label();
			toTracePaneDesign(this.valueLb) ;
		this.getChildren().add(borderPane);
		borderPane.setTop(nameLb);
		borderPane.setCenter(valueLb);

		varTracePanes.put(name, this) ;
	}

	public static void toTracePaneDesign(Label lb) {
		lb.setStyle("-fx-border-color:black;-fx-border-width:1;-fx-border-style: solid;-fx-background-color:white;");
		lb.setFont(Font.font(20));
		lb.setMinWidth(baseSizeW);
		lb.setMinHeight(baseSizeH);
		lb.setTextAlignment(TextAlignment.CENTER);
		lb.setAlignment(Pos.CENTER);
		lb.setTextFill(Color.BLACK);
		lb.setWrapText(true);
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
