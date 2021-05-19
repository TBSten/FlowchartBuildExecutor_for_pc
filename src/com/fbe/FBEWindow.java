package com.fbe;

import com.fbe.item.Flow;
import com.fbe.item.Item;
import com.fbe.sym.TestSym;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class FBEWindow extends Application {

	AnchorPane ap ;

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
			ap = FBEApp.controllers.get("Base").mainPane ;
			root.getChildren().add(ap);
/*
			VBox vb = new VBox();
			ap.getChildren().add(vb);
			TestSym ts = new TestSym();
			vb.getChildren().add(ts);
*/

			Flow f = new Flow();
			ap.getChildren().add(f);

			for(int i = 0;i < 100;i++) {
				TestSym ts = new TestSym() ;
				f.addSym(i, ts);
			}

			/*
			VBox vb = new VBox();
			vb.setPadding(new Insets(0));
			ap.getChildren().add(vb);

			for(int i = 0;i < 20;i++) {
				vb.getChildren().add(new TestSym());
				vb.getChildren().add(new Allow());
			}
			vb.getChildren().add(new TestSym());
			*/


			/*
			VBox vb = new VBox();
			ap.getChildren().add(vb);
			for(int i = 0;i < 10;i++) {
				if(i%4 == 3) {
					HBox hb = new HBox();
					for(int j = 0;j < 4;j++) {
						VBox vb2 = new VBox();
						Button b = new Button("よこ"+i+"-"+j) ;
						b.setPrefWidth(100);
						vb2.getChildren().add(b);
						hb.getChildren().add(vb2);
						if(j % 2 == 0) {
							for(int k = 0;k < Math.random()*10;k++) {
								b = new Button("ついか"+j+"-"+k);
								b.setPrefWidth(100);
								vb2.getChildren().add(b);
							}
						}
					}
					vb.getChildren().add(hb);
				}else {
					Button b = new Button("ぼたん"+(i+1)) ;
					b.setPrefWidth(100);
					vb.getChildren().add(b);
				}
			}

*/



			primaryStage.show();
			System.out.println("f.hei :"+f.getHeight());
			System.out.println("f.vb.hei :"+f.vb.getHeight());
			for(Node n:f.vb.getChildren()) {
				System.out.println("  sym :"+n);
				System.out.println("    hei :"+((Item)n).getHeight());
			}



		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		launch(args);
		launch(FBEWindow.class);
	}
}
