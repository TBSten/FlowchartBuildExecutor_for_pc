package com.fbe.sym;

import java.util.ArrayList;

import com.fbe.FBEExecutor;
import com.fbe.item.Item;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public abstract class Sym extends Item {

	ContextMenu menu = new ContextMenu() ;

	public Sym() {
		//親の設定
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

	}
	public abstract void execute(FBEExecutor exe);
	public ContextMenu getMenu() {
		return menu;
	}
	public void openSettingWindow() {
		//設定ウィンドウ
		System.out.println("設定ウィンドウを開くよ");
	}
}
