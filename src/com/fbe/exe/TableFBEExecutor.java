package com.fbe.exe;

import java.util.List;

import com.fbe.item.Flow;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TableFBEExecutor extends FBEExecutor {

	protected String[] heads ;
	protected int rowCnt = 0 ;
	protected boolean isBorder ;
	protected String align = "RIGHT" ;

	public TableFBEExecutor(Flow mainFlow, List<Flow> flows,String[] heads) {
		super(mainFlow, flows);
		this.heads = heads ;
	}

	Stage st ;
	GridPane gp ;
	@Override public void onInit() {
		st = createStage() ;
		gp = new GridPane();
		Label[] lbs = addMultiColumnLine(heads) ;
		for(int i = 0;i < heads.length;i++) {
			ColumnConstraints gc = new ColumnConstraints() ;
			gc.setFillWidth(true);
			gc.setHgrow(Priority.ALWAYS);
			gc.setHalignment(HPos.CENTER);
			gc.setPercentWidth(100.0/heads.length);
			Label lb = lbs[i] ;
			lb.setStyle("-fx-border-color:black;-fx-border-width:0 1 3 1;");
			lb.setPadding(new Insets(5,0,5,0));
			lb.setTextAlignment(TextAlignment.CENTER);
			lb.setAlignment(Pos.CENTER);
		}
		ScrollPane root = new ScrollPane(gp) ;
		root.viewportBoundsProperty().addListener(e->{
			gp.setMinWidth(root.getViewportBounds().getWidth());
		});
		st.setScene(new Scene(root));
		st.setHeight(300);
		st.setMinHeight(300);
		st.show();
	}
	@Override public void onDiscard() {
	}

	@Override public void print(String formula,Object...args) {
		String[] arr = formula.split(",") ;
		for(int i = 0;i < arr.length;i++) {
			arr[i] = String.valueOf(eval(arr[i])) ;
		}
		Label[] lbs = addMultiColumnLine(arr);
		if(arr.length != this.heads.length) {
			for(Label lb:lbs) {
				if(this.isBorder()) {
					lb.setStyle(lb.getStyle()+";-fx-border-width:0;");
				}
			}
		}
	}

	public Label[] addMultiColumnLine(String...arg) {
		Label[] lbArr = new Label[arg.length] ;
		for(int i = 0;i < arg.length;i++) {
			lbArr[i] = new Label(arg[i]) ;
			labelToValidDesign(lbArr[i]) ;
		}
		gp.addRow(gp.getRowCount(), lbArr);
		this.rowCnt ++;
		return lbArr ;
	}
	/*
	public Label addSingleColumnLine(String str) {
		Label lb = new Label(str) ;
		labelToValidDesign(lb) ;
//		gp.addRow(gp.getRowCount(), lb);
		gp.add(lb, 0, gp.getRowCount()+1,heads.length,1);
		lb.setAlignment(Pos.BASELINE_LEFT);
		lb.setTextAlignment(TextAlignment.LEFT);
		lb.setStyle(lb.getStyle()+";-fx-border-width:0;");
		return lb ;
	}
	*/
	protected void labelToValidDesign(Label lb) {
		if(this.isBorder()) {
			lb.setStyle("-fx-border-color:black;-fx-border-width:1 1 1 1;");
		}
		if(this.align.equals("LEFT")) {
			lb.setAlignment(Pos.BASELINE_LEFT);
			lb.setTextAlignment(TextAlignment.LEFT);
		}else if(this.align.equals("CENTER")) {
			lb.setAlignment(Pos.BASELINE_CENTER);
			lb.setTextAlignment(TextAlignment.CENTER);
		}else if(this.align.equals("RIGHT")) {
			lb.setAlignment(Pos.BASELINE_RIGHT);
			lb.setTextAlignment(TextAlignment.RIGHT);
		}
		lb.setMaxWidth(Double.MAX_VALUE);
		GridPane.setFillWidth(lb, true);
		GridPane.setFillHeight(lb, true);
		GridPane.setValignment(lb,VPos.CENTER );
		GridPane.setVgrow(lb, Priority.ALWAYS);
		GridPane.setHalignment(lb,HPos.CENTER );
		GridPane.setHgrow(lb, Priority.ALWAYS);
		lb.setMinWidth(50);
	}

	public String[] getHeads() {
		return heads;
	}
	public void setHeads(String[] heads) {
		this.heads = heads;
	}
	public boolean isBorder() {
		return isBorder;
	}
	public void setIsBorder(boolean border) {
		this.isBorder = border;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}

}
