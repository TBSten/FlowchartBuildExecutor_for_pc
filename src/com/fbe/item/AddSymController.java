package com.fbe.item;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class AddSymController implements Initializable{
	@FXML Button cancelB ;
	@FXML FlowPane addZone ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		System.out.println("initialize");
		cancelB.setOnAction(e->{
//			System.out.println("キャンセル");
			cancelB.getScene().getWindow().hide();
		});


		/*
		//必要なSymインスタンスのgetExportViewメソッドを呼び出し、それ用のボタンを作成する。
		Sym[] syms = new Sym[] {
				new TestSym("test-1"),
				new TestSym("test-2"),
				new TestSym("test-3"),
				new TestSym("test-4"),
				new TestSym("test-5")
			} ;
		for(int i = 0;i < syms.length;i++) {
			Button b = new Button("",syms[i]) ;
			b.setEllipsisString("");
			b.setPrefSize(80, 80);
			addZone.getChildren().add(b);
			Node n = syms[i].getExportView() ;
			b.setGraphic(n);
		}
		*/





	}


}
