package com.fbe.exe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fbe.item.Flow;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game2DGridFBEExecutor extends FBEExecutor {
	String arrName ;
	int rowCnt = 10 ;
	int columnCnt = 10 ;
	String initValueFormula ;
	Map<String,String> outputRules = new HashMap<>() ;

	Label[][] labels ;

	public Game2DGridFBEExecutor(Flow mainFlow, List<Flow> flows) {
		super(mainFlow, flows);
	}
	@Override
	public void onInit() {
		Stage st = createStage();
		BorderPane root = new BorderPane() ;
		GridPane gp = new GridPane() ;
		gp.setAlignment(Pos.CENTER);
		gp.setStyle("-fx-background-color:black;-fx-border-color:white;-fx-border-width:2;");
		root.setCenter(gp);
		FlowPane fp = new FlowPane() ;
		fp.setMinHeight(100);
		root.setBottom(fp);
		labels = new Label[rowCnt][columnCnt] ;

		Object[][] arr = new Object[rowCnt][columnCnt] ;
		for(int y = 0 ;y < rowCnt ;y++) {
			for(int x = 0;x < columnCnt ;x++){
				arr[y][x] = eval(initValueFormula) ;
				Label lb = new Label(toLabelText(arr[y][x])) ;
				gp.add(lb, y, x);
				double size = 550 / Math.max(rowCnt, columnCnt) ;
				lb.setMinSize(size, size);
//				lb.setPrefSize(size, size);
				lb.setStyle("-fx-background-color:black;");
				lb.setTextFill(Color.WHITE);
				lb.setAlignment(Pos.CENTER);
				lb.setPadding(new Insets(3,3,3,3));
				labels[y][x] = lb ;
			}
		}
		st.setScene(new Scene(root));
		this.putArrVar(arrName, arr);
		st.show();
	}
	@Override
	public void onExecuted(FBEExecutable exe) {
		//配列
		Object obj = this.getVar(this.arrName) ;
		Object[][] arr = (Object[][]) obj ;
		for(int y = 0;y < arr.length;y++) {
			for(int x = 0;x < arr[y].length;x++) {
				labels[y][x].setText(toLabelText(arr[y][x]));
			}
		}
	}
	//outputRulesを「0-9」等のルールにも対応させる
	public String toLabelText(Object value) {
		String valueStr = String.valueOf(value) ;
		if(this.outputRules.containsKey( valueStr )) {
			//ルールにあれば
			return String.valueOf(eval(outputRules.get(valueStr)));
		}else {
			//ルールになければ
			return valueStr ;
		}
	}
	public void outputOnStatusZone(String name,Object value) {
	}
	public String getArrName() {
		return arrName;
	}
	public void setArrName(String arrName) {
		this.arrName = arrName;
	}
	public int getRowCnt() {
		return rowCnt;
	}
	public void setRowCnt(int rowCnt) {
		this.rowCnt = rowCnt;
	}
	public int getColumnCnt() {
		return columnCnt;
	}
	public void setColumnCnt(int columnCnt) {
		this.columnCnt = columnCnt;
	}
	public String getInitValueFormula() {
		return initValueFormula;
	}
	public void setInitValueFormula(String initValueFormula) {
		this.initValueFormula = initValueFormula;
	}
	public Map<String, String> getOutputRules() {
		return outputRules;
	}
	public void setOutputRules(Map<String, String> outputRules) {
		this.outputRules = outputRules;
	}

	/*
	 * onInit()						初期化時
	 * onPutVar(name,value)			変数代入時
	 * print(value)					出力時
	 * input(value)					入力時
	 * onDiscard()					終了時
	 */




}
