package com.fbe;

import java.util.ArrayList;

import com.fbe.item.Flow;
import com.fbe.sym.CalcSym;
import com.fbe.sym.Sym;
import com.fbe.sym.TerminalSym;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class FBEWindow extends Application {

	AnchorPane ap ;
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
			final int PAD = 300 ;

			ap = FBEApp.controllers.get("Base").mainPane ;
			ScrollPane sp = FBEApp.controllers.get("Base").mainSp ;
			root.getChildren().add(ap);


			Flow f = new Flow() ;
			ap.getChildren().add(f);
			f.setLayoutX(PAD);
			flows.add(f);

			f.setLayoutY(PAD*2/3);

			Sym[] syms = {
					new TerminalSym(TerminalSym.Type.START),
					new CalcSym("0","total") ,
					new CalcSym("\"開発中\"","status") ,
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

	public static void main(String[] args) {
//		launch(args);
		launch(FBEWindow.class);
	}
}
