package com.fbe;


import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.fbe.exe.FBEExecutor;
import com.fbe.exe.FileApp;
import com.fbe.export.JSExporter;
import com.fbe.format.FBEFormat;
import com.fbe.format.FBEFormatApp;
import com.fbe.item.Flow;
import com.fbe.option.OptionTable;
import com.fbe.sym.ArrayTemplate;
import com.fbe.sym.TerminalSym;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class BaseController implements Initializable{
/*
	@FXML MenuItem menu_test1 ;
	@FXML MenuItem menu_test2 ;
	@FXML MenuItem menu_test3 ;
	@FXML MenuItem menu_test4 ;
	@FXML MenuItem menu_test5 ;
	@FXML MenuItem menu_test6 ;
*/
	@FXML MenuItem menu_new ;
	@FXML MenuItem menu_import ;
	@FXML MenuItem menu_save ;
	/*
	@FXML MenuItem menu_print ;
	@FXML MenuItem menu_toImage ;
	*/

	@FXML MenuItem menu_newFlow ;
	@FXML MenuItem menu_loopNumber ;

	@FXML MenuItem menu_exe ;
	@FXML MenuItem menu_template ;
	@FXML MenuItem menu_files ;

	@FXML MenuItem menu_java ;
	@FXML MenuItem menu_javascript ;
	@FXML MenuItem menu_python ;

	@FXML MenuItem menu_check ;

	@FXML AnchorPane mainPane ;
	@FXML ScrollPane mainSp ;
	@FXML SplitPane mainSplitPane ;


	int wx = 0 ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		FBEApp.controllers.put("Base",this);

		menu_new.setOnAction((ActionEvent)->{
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"現在のプロジェクトを閉じますか？");
			alert.showAndWait()
				.filter(response -> response == ButtonType.OK)
				.ifPresent(response -> {
			    	FBEWindow app = FBEApp.app ;
			    	List<Flow> flows = List.copyOf(FBEApp.app.flows);
					for(Flow f :flows) {
						app.removeFlow(f);
					}
			    	app.addFlow(app.defaultFlow());
			});
		});
/*
		menu_print.setOnAction((ae)->{

			//今は簡易的に全てをスナップしているが、本番はアイテムをいい感じに区切ってイメージを作る。
			WritableImage img = FBEApp.app.ap.snapshot(new SnapshotParameters(), null);
			Canvas node = new Canvas(img.getWidth(),img.getHeight());
			node.getGraphicsContext2D().drawImage(img, 0,0);


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
			System.out.println("----");
		});
*/
/*
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
*/
		menu_exe.setOnAction( e ->{
			//
//			FBEApp.executeFlows(FBEApp.app.flows);
//			ExeApp.execute();
			/*
			FBEExecutor exe = new FBEExecutor(FBEApp.app.flows.get(0), FBEApp.app.flows ) ;
			exe.openSettingWindow() ;
			exe.execute_Test();
			*/
			//実行モードにする(実行用ウィンドウを表示)
			if(FBEApp.app.flows != null && FBEApp.app.flows.size() >= 1) {
				FBEExecutor.toExecuteMode(FBEApp.app.flows.get(0), FBEApp.app.flows);
			}else {
				Stage dialog = new Stage() ;
				Label lb = new Label("処理がありません。処理を追加してください。");
				lb.setPadding(new Insets(30,5,30,5));
				dialog.setScene(new Scene(lb));
				dialog.show();
			}
		});

		menu_newFlow.setOnAction(e->{
			TerminalSym ssym = new TerminalSym(TerminalSym.Type.START);
			int num = FBEApp.app.flows.size() ;		//すでに同じ処理名があったら使わないような配慮ができるといいな
//			ssym.options.put("テキスト", "処理"+num);
			ssym.optionPut("テキスト", "（説明文）", OptionTable.Type.TEXTFIELD, "処理"+num);
			TerminalSym esym = new TerminalSym(TerminalSym.Type.END);
			Flow f = new Flow() ;
			f.addSym(0, ssym);
			f.addSym(1, esym);
			FBEApp.app.addFlow(f);

			ssym.openSettingWindow();
		});
		menu_import.setOnAction(e->{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("開く");
			for(String key:FBEFormatApp.formats.keySet()) {
				FBEFormat format = FBEFormatApp.formats.get(key) ;
				fileChooser.getExtensionFilters().add(new ExtensionFilter("FBE形式 "+format.getVer(),"*."+format.getExtension()));
			}
			if(FBEFormatApp.lastUpdatePath != null) {
				File fi = new File(FBEFormatApp.lastUpdatePath) ;
				if(fi.isDirectory()) {
					fileChooser.setInitialDirectory(fi);
				}else {
					fileChooser.setInitialDirectory(fi.getParentFile());
				}
			}
		//	fileChooser.setInitialFileName("新規FBEプロジェクト");
			File selectedFile = fileChooser.showOpenDialog(FBEApp.window);
			if (selectedFile != null) {
				try {
					System.out.println("開く："+selectedFile.getAbsolutePath());
					List<Flow> flows = FBEFormatApp.defaultFormat.importFrom(selectedFile);
					flows.forEach(ele->{
						FBEApp.app.addFlow(ele);
						ele.redraw();
					});
					//更新
					FBEApp.selectItem(flows.get(0));
					flows.get(0).redraw();
				}catch(Exception ex) {
					ex.printStackTrace();
				}

			}
		});
		menu_save.setOnAction(e->{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("保存");
			for(String key:FBEFormatApp.formats.keySet()) {
				FBEFormat format = FBEFormatApp.formats.get(key) ;
				fileChooser.getExtensionFilters().add(new ExtensionFilter("FBE形式 "+format.getVer(),"*."+format.getExtension()));
			}
			if(FBEFormatApp.lastUpdatePath != null) {
				File fi = new File(FBEFormatApp.lastUpdatePath) ;
				if(fi.isDirectory()) {
					fileChooser.setInitialDirectory(fi);
				}else {
					fileChooser.setInitialDirectory(fi.getParentFile());
				}
			}
			fileChooser.setInitialFileName("新規FBEプロジェクト");
			File selectedFile = fileChooser.showSaveDialog(FBEApp.window);
			if (selectedFile != null) {
				try {
					System.out.println("保存："+selectedFile.getAbsolutePath());
					FBEFormatApp.defaultFormat.save(selectedFile.getAbsolutePath(), FBEApp.app.flows.get(0), FBEApp.app.flows);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}

		});
		menu_javascript.setOnAction(e->{
			try {
				JSExporter ex = new JSExporter() ;
				DirectoryChooser dc = new DirectoryChooser();
				File iniDir = new File(FBEFormatApp.lastUpdatePath) ;
				iniDir = iniDir.isDirectory() ? iniDir : iniDir.getParentFile() ;
				dc.setInitialDirectory(iniDir);
				dc.setTitle("JavaScriptのコードに変換");
				File dir = dc.showDialog(FBEApp.window);
				if(dir != null) {
					ex.export(FBEApp.app.flows.get(0), FBEApp.app.flows, dir);
					FBEApp.msgBox("書き込みが完了しました");
				}
			}catch(Exception exc) {
				FBEApp.msgBox("エラーが発生しました");
				exc.printStackTrace();
			}
		});
		menu_template.setOnAction(e->{
			//配列テンプレートを作成・管理する画面を表示
			ArrayTemplate.openManageStage();
		});
		menu_files.setOnAction(e->{
			//ファイル管理ウィンドウを表示
			FileApp.getInstance().openManageWindow();
		});




/*
		menu_test1.setOnAction(e->{
			//ズーム
			double w = Item.baseWidthProperty.get();
			double h = Item.baseHeightProperty.get();
			double lw = Item.baseLineWidthProperty.get();
			System.out.println(" up ================");
			Item.baseWidthProperty.set(w+10);
			Item.baseHeightProperty.set(h+10);
//			Item.baseLineWidthProperty.set(lw+1);
			System.out.println(w+"*"+h+" "+lw);
		});
		menu_test2.setOnAction(e->{
			//ズーム
			double w = Item.baseWidthProperty.get();
			double h = Item.baseHeightProperty.get();
			double lw = Item.baseLineWidthProperty.get();
			if(w-10 > 10 && h-10> 10) {
				System.out.println(" down ================");
				Item.baseWidthProperty.set(w-10);
				Item.baseHeightProperty.set(h-10);
//				Item.baseLineWidthProperty.set(lw-1);
			}
			System.out.println(w+"*"+h+" "+lw);
		});
		menu_test3.setOnAction(e->{
			//ズーム
			double w = Item.baseWidthProperty.get();
			double h = Item.baseHeightProperty.get();
			double lw = Item.baseLineWidthProperty.get();
			System.out.println(" default ================");
			Item.baseWidthProperty.set(180);
			Item.baseHeightProperty.set(40);
			Item.baseLineWidthProperty.set(3);
			System.out.println(w+"*"+h+" "+lw);
		});

		menu_test4.setOnAction(e->{
			//オプションテーブル系のテスト
			Stage st = new Stage() ;
			VBox vb = new VBox() ;
			Scene sc = new Scene(vb);
			vb.getChildren().addAll(
					new InputableTextField(),
					new InputableComboBox<String>(),
					new InputableCheckBox(),
					new InputableTextArea(),
					new InputableSlider(),
					new InputableSpinButton<String>(),
					new InputableFileChooser()
				);
			st.setScene(sc);
			st.show();
		});

		menu_test5.setOnAction(e->{
			//オプションテーブル系のテスト
			try {
				Stage st = new Stage() ;
				OptionTable table = new OptionTable();
				Scene sc = new Scene(table);
				Inputable i1 = table.put("TF","夕暮れ空を飛んで真下次第に小さくなっていくのは君のいた町だ", OptionTable.Type.TEXTFIELD, "text");
				Inputable i2 = table.put("タイプあああ","aa", OptionTable.Type.COMBOBOX, "combo");
				Inputable i3 = table.put("TA","aa", OptionTable.Type.TEXTAREA, "area");
				Inputable i4 = table.put("CB","aa", OptionTable.Type.CHECKBOX, true);
				Inputable i5 = table.put("SL","aa", OptionTable.Type.SLIDER, 0);
				Inputable i6 = table.put("SP","aa", OptionTable.Type.SPIN, 0);
				Inputable i7 = table.put("FI","aa", OptionTable.Type.FILE, "");
				Inputable i8 = table.put("DI","aa", OptionTable.Type.DIRECTORY, "");
				i2.args("そらにうたえば");
				i2.args("必然　必然");
				i2.args("未来へ、足掻け");
				i7.args(new FileChooser.ExtensionFilter("HTML", "*.html"));
				i7.args(new FileChooser.ExtensionFilter("CSS", "*.css"));
				i7.args(new FileChooser.ExtensionFilter("XML", "*.xml"));
				i7.args(new FileChooser.ExtensionFilter("JavaScript", "*.js"));
				i7.args(new FileChooser.ExtensionFilter("JSON", "*.json"));

				st.setScene(sc);
				table.autosize();
				st.show();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		});

		menu_test6.setOnAction(e->{
			//カレントディレクトリの表示
			System.out.println(new File("./").getAbsolutePath());
		});
*/

	}


}
