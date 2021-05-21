package com.fbe.sym;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fbe.FBEExecutor;
import com.fbe.item.Item;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class Sym extends Item {

	ContextMenu menu = new ContextMenu() ;
	public final Map<String,String> options = new LinkedHashMap<>();

	public Sym() {
		this.setOnMousePressed((e)->{
			if(e.getClickCount() == 2) {
				//設定を開く
				openSettingWindow();

			}
			if(e.isSecondaryButtonDown()) {
				menu.show(this,e.getScreenX() , e.getScreenY());
			}
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
		menu.getItems().addAll(items);

		symLabel.prefWidthProperty().unbind();
		symLabel.prefHeightProperty().unbind();
		symLabel.prefHeightProperty().unbind();
		symLabel.setPrefWidth(baseWidth);
		symLabel.setPrefHeight(baseHeight);
		symLabel.getParent().layout();

	}
	public abstract void execute(FBEExecutor exe);
	public ContextMenu getMenu() {
		return menu;
	}
	public void openSettingWindow() {
		//設定ウィンドウ
		System.out.println("設定ウィンドウを開くよ");
		showExportViewWindow(this);
	}
	public Node getExportView() {
		WritableImage wi = this.snapshot(new SnapshotParameters(), null);
		ImageView iv = new ImageView(wi);
		return iv ;
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


