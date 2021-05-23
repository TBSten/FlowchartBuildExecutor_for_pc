package com.fbe.sym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fbe.FBEApp;
import com.fbe.exe.FBEExecutable;
import com.fbe.exe.FBEExecutor;
import com.fbe.item.Item;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 * sym:hover{
 *     -fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 );
 * }
 */
public abstract class Sym extends Item implements FBEExecutable{


	ContextMenu menu = new ContextMenu() ;
	public final Map<String,String> options = new LinkedHashMap<>();
	public final Map<String,List<String>> optionsValueList = new LinkedHashMap<>();



	public Sym() {

		this.setOnKeyPressed(e->{
			switch(e.getCode()) {
			case ENTER:
			case SPACE:
				//ENTER,SPACEがおされたとき
				break;
			}
		});
		this.setOnMousePressed((e)->{
			if(e.getClickCount() == 2) {
				//設定を開く
				openSettingWindow();

			}
			if(e.isSecondaryButtonDown()) {
				menu.show(this,e.getScreenX() , e.getScreenY());
			}
			if(this.isFocused()) {
				Sym.this.requestFocus();
			}
//			System.out.println("requestFocus:"+isFocused());
			redraw();
		});
		ArrayList<MenuItem> items = new ArrayList<>();
		MenuItem i = new MenuItem("削除");
		i.setOnAction(e ->{
			//削除
			this.getParentFlow().removeSym(this);
		});
		items.add(i);
		i = new MenuItem("オプション");
		i.setOnAction(e ->{
			//オプションを開く
			openSettingWindow();
		});
		items.add(i);
		i = new MenuItem("再読み込み");
		i.setOnAction(e ->{
			//オプションを開く
			redraw();
		});
		items.add(i);
		menu.getItems().addAll(items);

		symLabel.prefWidthProperty().unbind();
		symLabel.prefHeightProperty().unbind();
		symLabel.prefHeightProperty().unbind();
		symLabel.setPrefWidth(baseWidth);
		symLabel.setPrefHeight(baseHeight);
		symLabel.getParent().layout();

		this.getStyleClass().add("sym");
		this.setFocusTraversable(true);


	}
	public abstract void execute(FBEExecutor exe);
	public ContextMenu getMenu() {
		return menu;
	}
	public void openSettingWindow() {
		//設定ウィンドウ
//		System.out.println("設定ウィンドウを開くよ");
//		showExportViewWindow(this);
		Stage st = new Stage();
		st.setTitle(Sym.this.getClass().getSimpleName()+"の設定");
		st.initModality(Modality.WINDOW_MODAL);
		st.initOwner(FBEApp.window);
		BorderPane root = new BorderPane();
		Scene sc = new Scene(root);
		st.setScene(sc);

		root.setPrefWidth(300);

		Label titleL = new Label("設定") ;
		titleL.setStyle(titleL.getStyle()+"; -fx-font-size:20;-fx-padding:5;");
		root.setTop(titleL);

		Map<String,Node> tfs = new HashMap<>();
		//root.centerに一覧を表示
		VBox vb = new VBox();
		vb.prefWidthProperty().bind(root.widthProperty());
		root.setCenter(vb);
		if(options.size() > 0) {
			for(Map.Entry<String, String> ent:options.entrySet()) {
			//	System.out.println(ent.getKey()+":"+ent.getValue());
				HBox hb = new HBox();
				hb.prefWidthProperty().bind(vb.widthProperty());
				vb.getChildren().add(hb);

				Label name = new Label(ent.getKey());
				name.prefWidthProperty().bind(hb.widthProperty().multiply(0.3));

				Node value = null ;
				if(optionsValueList.containsKey(ent.getKey())) {
					value = new ComboBox() ;
					ComboBox cb = (ComboBox)value ;
					cb.setItems(FXCollections.observableArrayList(optionsValueList.get(ent.getKey())));
					cb.setValue(ent.getValue());
					cb.prefWidthProperty().bind(hb.widthProperty().multiply(0.7));
				}else {
					value = new TextField(ent.getValue());
					TextField tf =(TextField) value ;
					tf.prefWidthProperty().bind(hb.widthProperty().multiply(0.7));
				}

				hb.getChildren().addAll(name,value);
				tfs.put(ent.getKey(),value);
			}
		}else {
			vb.getChildren().add(new Label("設定可能なオプションはありません"));
		}
		Button saveB = new Button("保存して戻る");
		saveB.setOnAction(e->{
			//保存処理
			for(Map.Entry<String , Node> entry :tfs.entrySet()) {
				Node n = entry.getValue() ;
				if(n instanceof TextField) {
					options.put(entry.getKey(), ((TextField)n).getText());
				}else if(n instanceof ComboBox) {
					options.put(entry.getKey(), (String)((ComboBox)n).getValue());
				}else {
					//エラー
					System.out.println("#ERROR");
				}
			}

			st.close();
		});
		Button returnB = new Button("保存せずに戻る");
		returnB.setOnAction(e->{
			st.hide();
		});
		ButtonBar bb = new ButtonBar();
		bb.setPadding(new Insets(30,0,0,0));
		bb.getButtons().addAll(saveB,returnB);
		root.setBottom(bb);

		root.autosize();

		st.sizeToScene();
//		st.showAndWait();
		st.show();


		st.setOnHidden(e->{
			requestFocus();
			redraw();
		});



	}


	public Node getExportView() {
		changeUnfocusedDesign();
		redraw();
		WritableImage wi = this.snapshot(new SnapshotParameters(), null);
		ImageView iv = new ImageView(wi);
		return iv ;
	}

	/**
	 * optionの値をこの記号に反映します。
	 */
	public void reflectOption() {
	}

	public void redraw() {
		if(options != null) {
			reflectOption();
		}
		super.redraw();
	}

	public static void showExportViewWindow(Sym sym) {


		sym.requestLayout();

		Stage st = new Stage();
		AnchorPane root = new AnchorPane();
		Scene sc = new Scene(root);
		st.setScene(sc);
		root.getChildren().add(sym.getExportView());
		st.show();

	}


}


