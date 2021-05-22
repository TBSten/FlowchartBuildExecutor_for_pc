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

/*
			Rectangle r = new Rectangle(20,20,50,50);
			r.setFill(Color.CYAN);
			root.getChildren().add(r);
			TranslateTransition tt = new TranslateTransition(new Duration(1000), r);
			tt.setFromX(20);
			tt.setToX(100);
			tt.setAutoReverse(true);
			tt.setCycleCount(30000);
			tt.play();
*/
/*
			Rectangle r = new Rectangle(20, 20, 100, 50);
		    r.setFill(Color.CYAN);
		    root.getChildren().add(r);

		    Timeline tl = new Timeline();
		    tl.setAutoReverse(true);
		    tl.setCycleCount(10);

		    // 横幅操作のKeyFrame作成
		    KeyFrame key_a1 = new KeyFrame(
		        new Duration(0),
		        new KeyValue(r.widthProperty(),100));
		    KeyFrame key_a2 = new KeyFrame(
		        new Duration(2500),
		        new KeyValue(r.widthProperty(),200));

		    // 塗りつぶし色操作のKeyFrame作成
		    KeyFrame key_b1 = new KeyFrame(
		        new Duration(0),
		        new KeyValue(r.fillProperty(),Color.rgb(255, 0, 0)));
		    KeyFrame key_b2 = new KeyFrame(
		        new Duration(2500),
		        new KeyValue(r.fillProperty(),Color.rgb(0, 0, 255)));
		    tl.getKeyFrames().add(key_a1);
		    tl.getKeyFrames().add(key_a2);
		    tl.getKeyFrames().add(key_b1);
		    tl.getKeyFrames().add(key_b2);
		    tl.play();
*/
			final int PAD = 300 ;

			ap = FBEApp.controllers.get("Base").mainPane ;
			ScrollPane sp = FBEApp.controllers.get("Base").mainSp ;
			root.getChildren().add(ap);
//			ap.setStyle("-fx-background-color:red;");


			Flow f = new Flow() ;
			ap.getChildren().add(f);
			f.setLayoutX(PAD);
			flows.add(f);

			f.setLayoutY(PAD*2/3);

			Sym[] syms = {
					new TerminalSym(TerminalSym.Type.START),
					new CalcSym("1+2+3+4+5","test1") ,
					new CalcSym("1*2*3*4*5","test2") ,
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
