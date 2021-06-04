package com.fbe.exe;

import java.util.List;

import com.fbe.FBEApp;
import com.fbe.item.Flow;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoggerFBEExecutor extends FBEExecutor {
	static double fontSize = 20 ;

	Stage loggerSt ;
	ScrollPane rootSp = new ScrollPane();
	VBox lines = new VBox() ;
	public LoggerFBEExecutor(Flow mainFlow, List<Flow> flows) {
		super(mainFlow, flows);
	}

	@Override public void onInit() {
		loggerSt = new Stage();
		loggerSt.initOwner(getOwner());
		loggerSt.setScene(new Scene(rootSp));
		rootSp.viewportBoundsProperty().addListener(e->{
			lines.setPrefSize(rootSp.getViewportBounds().getWidth(),rootSp.getViewportBounds().getHeight());
		});
		rootSp.setContent(lines);
		rootSp.setStyle("-fx-background-color:red;");
		lines.setStyle("-fx-background-color:black;");

		loggerSt.show();
		loggerSt.sizeToScene();
		loggerSt.setMinWidth(500);
		loggerSt.setMinHeight(300);
		loggerSt.setWidth(500);
		loggerSt.setHeight(300);

		Label lb = new Label("### このウィンドウに実行結果が表示されます ###");
		lb.setFont(Font.font(fontSize));
		lb.setTextFill(Color.WHITE);
		lines.getChildren().add(lb);
	}

	@Override public void onDiscard() {
	//	loggerSt.close();
	}

	@Override public void print(String formula,Object...args) {
		String str = String.valueOf(this.eval(formula));
		String[] split = str.split("\n");
		if(split.length <= 0) {
			split = new String[]{" "} ;
		}
		for(String line:split) {
			Label lb = new Label(line.replaceAll("\\s", " "));
			lb.setFont(Font.font(fontSize));
			lb.setTextFill(Color.WHITE);
			lines.getChildren().add(lb);
			new Thread(()->{
				FBEApp.sleep(150);
				Platform.runLater(()->{
					rootSp.setVvalue(Double.MAX_VALUE);
				});
			}).start();

		}
	}

	@Override public String input(Object...msg) {
		String ans = this.inputMsgBox(msg[0].toString());
		Label lb = new Label(" 入力>>"+ans);
		lb.setFont(Font.font(fontSize));
		lb.setTextFill(Color.BLUE);
		lines.getChildren().add(lb);
		return ans ;
	}

	@Override public void msgBox(String data) {
		Label lb = new Label(" ダイアログ："+data);
		lb.setFont(Font.font(fontSize));
		lb.setTextFill(Color.GREEN);
		lines.getChildren().add(lb);
		super.msgBox(data);
	}
	public void addLine(String msg,Color c) {
		Label lb = new Label(msg);
		lb.setFont(Font.font(fontSize));
		lb.setTextFill(c);
		lines.getChildren().add(lb);
	}

}
