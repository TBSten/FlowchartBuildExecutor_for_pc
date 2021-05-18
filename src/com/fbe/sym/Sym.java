package com.fbe.sym;

import com.fbe.FBEExecutor;
import com.fbe.item.Item;

public abstract class Sym extends Item {
	public Sym() {
		//親の設定
		this.setOnMousePressed((e)->{
			if(e.getClickCount() == 2) {
				//設定を開く
				this.setText(getText()+"*");
			}
		});
	}
	public abstract void execute(FBEExecutor exe);
}
