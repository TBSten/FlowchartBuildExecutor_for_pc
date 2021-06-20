package com.fbe;

import java.util.ArrayList;
import java.util.List;

import com.fbe.item.Flow;
import com.fbe.item.GettableFlow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class FBEWindow extends Application implements GettableFlow{

	SplitPane mainSplitPane ;
	AnchorPane ap ;
	HBox flowHb = new HBox(10);

	/**
	 * 最初と最後が必ずTerminalSymの1処理（関数）を表すフローの集まり。実行時はflows.get(0)を実行する。
	 */
	public final List<Flow> flows = new ArrayList<>();

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
//			final int PADX = 200 ;
//			final int PADY = PADX*1/4 ;

			ap = FBEApp.controllers.get("Base").mainPane ;
			ScrollPane sp = FBEApp.controllers.get("Base").mainSp ;
			root.getChildren().add(ap);
			SplitPane split = FBEApp.controllers.get("Base").mainSplitPane ;
			this.mainSplitPane = split ;


			double base = 30 ;
			flowHb.setLayoutX(base);
			flowHb.setLayoutY(base);
			ap.getChildren().add(flowHb);
			sp.viewportBoundsProperty().addListener(e->{
				flowHb.setMinSize(sp.getViewportBounds().getWidth()+base,sp.getViewportBounds().getHeight()+base);
			});

/*
			Flow f = new Flow() ;
			addFlow(f);

			Sym[] syms = {
					new TerminalSym(TerminalSym.Type.START),
					new TerminalSym(TerminalSym.Type.END),
			};
			for(int i = 0;i < syms.length;i++) {
				f.addSym(i, syms[i]);
			}

*/


//			syms[0].requestFocus();


			primaryStage.show();

			primaryStage.setOnHidden(e->{
				FBEApp.shutdown();

				System.out.printf("app end [startTime=%d]\n",System.currentTimeMillis()) ;
			});

			FBEApp.init();

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

	public SplitPane getMainSplitPane() {
		return mainSplitPane;
	}
}
