package com.fbe.sym.factory;

import com.fbe.sym.Sym;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;

public abstract class SymFactory<TYPE extends Sym> extends Button {

	boolean openSetting = true ;
	public SymFactory(Sym sym) {
		//本当はsymのスナップイメージ
		super("記号");
		Button b = this ;
		b.setEllipsisString("???");
		b.setMinHeight( 80);

		Node n = new ImageView(sym.snapshot()) ;
		b.setGraphic(n);

		b.setWrapText(true);
		b.setContentDisplay(ContentDisplay.TOP);
	}
	public abstract TYPE createSym() ;

	public boolean isOpenSetting() {
		return openSetting;
	}
	public void setOpenSetting(boolean openSetting) {
		this.openSetting = openSetting;
	}

}
