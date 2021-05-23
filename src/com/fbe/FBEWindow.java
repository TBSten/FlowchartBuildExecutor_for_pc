package com.fbe;

import java.util.ArrayList;

import com.fbe.item.Flow;
import com.fbe.item.GettableFlow;
import com.fbe.sym.CalcSym;
import com.fbe.sym.DataSym;
import com.fbe.sym.Sym;
import com.fbe.sym.TerminalSym;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class FBEWindow extends Application implements GettableFlow{

	AnchorPane ap ;
	HBox flowHb = new HBox(10);

	/**
	 * 最初と最後が必ずTerminalSymの1処理（関数）を表すフローの集まり。実行時はflows.get(0)を実行する。
	 */
	ArrayList<Flow> flows = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) {
		FBEApp.app = this ;
		FBEApp.window = primaryStage ;
		primaryStage.setTitle("FBE");

		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("FBEBase.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			final int PADX = 200 ;
			final int PADY = PADX*1/4 ;

			ap = FBEApp.controllers.get("Base").mainPane ;
			ScrollPane sp = FBEApp.controllers.get("Base").mainSp ;
			root.getChildren().add(ap);

			flowHb.setLayoutX(PADX);
			flowHb.setLayoutY(PADY);
			ap.getChildren().add(flowHb);

			Flow f = new Flow() ;
			addFlow(f);


/*
			ap.prefWidthProperty().bind(f.widthProperty().add(sp.widthProperty().multiply(2)));
			ap.prefHeightProperty().bind(f.heightProperty().add(sp.heightProperty().multiply(2)));
*/
			ap.prefWidthProperty().bind(flowHb.widthProperty().add(PADX*2));
			ap.prefHeightProperty().bind(flowHb.heightProperty().add(PADY*2));


//			flows.add(f);

			Sym[] syms = {
					new TerminalSym(TerminalSym.Type.START),
					new DataSym("キーボード入力","変数") ,
					new CalcSym("変数*3","変数") ,
					new DataSym("表示","\"3倍は\"+変数") ,
					new TerminalSym(TerminalSym.Type.END),
			};
			for(int i = 0;i < syms.length;i++) {
				f.addSym(i, syms[i]);
			}
			syms[0].requestFocus();

			primaryStage.show();



		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void addFlow(Flow f) {
		flowHb.getChildren().add(f);
		flows.add(f);
	}
	public void removeFlow(Flow f) {
		flowHb.getChildren().remove(f);
		flows.remove(f);
	}


	public static void main(String[] args) {
//		launch(args);
		launch(FBEWindow.class);
	}
}
