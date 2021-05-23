package com.fbe;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class BaseController implements Initializable{

	@FXML MenuItem menu_new ;
	@FXML MenuItem menu_import ;
	@FXML MenuItem menu_save ;
	@FXML MenuItem menu_print ;
	@FXML MenuItem menu_toImage ;
	@FXML MenuItem menu_newFlow ;
	@FXML MenuItem menu_copy ;
	@FXML MenuItem menu_paste ;
	@FXML MenuItem menu_exeAll ;
	@FXML MenuItem menu_exeLine ;
	@FXML MenuItem menu_java ;
	@FXML MenuItem menu_python ;
	@FXML MenuItem menu_check ;
	@FXML AnchorPane mainPane ;
	@FXML ScrollPane mainSp ;

	int wx = 0 ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		FBEApp.controllers.put("Base",this);

		menu_new.setOnAction((ActionEvent)->{
			System.out.println("新規作成");
		});

		menu_print.setOnAction((ae)->{

			//今は簡易的に全てをスナップしているが、本番はアイテムをいい感じに区切ってイメージを作る。
			WritableImage img = FBEApp.app.ap.snapshot(new SnapshotParameters(), null);
			Canvas node = new Canvas(img.getWidth(),img.getHeight());
			node.getGraphicsContext2D().drawImage(img, 0,0);

//			AnchorPane node = FBEApp.app.ap ;
/*
			VBox node = new VBox();
			for(int i = 0;i < 500;i++) {
				node.getChildren().add(new Button("test-"+i));
			}
*/



			System.out.println("EXPORT:"+node);
			PrinterJob job = PrinterJob.createPrinterJob();
			System.out.println("job:"+job);
			boolean doPrint = job.showPrintDialog(FBEApp.window);
			if(doPrint) {
				System.out.println("印刷実行");
				boolean success = job.printPage(node);
				if(success) {
					job.endJob();
					System.out.println("印刷修了");
				}else {
					System.out.println("印刷エラー");
				}
			}else {
				System.out.println("印刷キャンセル");
			}
			/*
			if(job != null) {
				boolean success = job.printPage(node);
				if(success) {
					System.out.println("終了 ");
					job.endJob();
				}
			}
			*/
			System.out.println("----");
		});

		menu_toImage.setOnAction((ae)->{
			WritableImage img = FBEApp.app.ap.snapshot(new SnapshotParameters(), null);
		    try{
		    	FileChooser fc = new FileChooser() ;
		    	fc.getExtensionFilters().addAll(
		    			new ExtensionFilter("PNG","*.png")
		    		);
		    	File out = fc.showSaveDialog(FBEApp.window);
		    	ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", out);
		    }catch(Exception ex){
		      System.out.println(ex);
		    }
		    System.out.println("OK");
		});

		menu_exeAll.setOnAction( e ->{
			//
			FBEApp.executeFlows(FBEApp.app.flows);
		});

//		mainPane.getChildren().add(new EditPane());
/*
		for(int i = 1;i <= 300;i++) {
			Button b = new Button("テストボタン"+i);
			b.setLayoutY(50*i);
			mainPane.getChildren().add(b);
		}
*/
	}


}
