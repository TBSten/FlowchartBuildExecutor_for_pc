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
		loggerSt.setMinWidth(300);
		loggerSt.setMinHeight(200);
		loggerSt.initOwner(getOwner());
		loggerSt.setScene(new Scene(rootSp));
		rootSp.viewportBoundsProperty().addListener(e->{
			lines.setPrefSize(rootSp.getViewportBounds().getWidth(),rootSp.getViewportBounds().getHeight());
		});
		rootSp.setContent(lines);
		rootSp.setStyle("-fx-background-color:red;");
		lines.setStyle("-fx-background-color:black;");

		loggerSt.show();
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


}
