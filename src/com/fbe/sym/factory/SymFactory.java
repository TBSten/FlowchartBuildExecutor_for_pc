package com.fbe.sym.factory;

import com.fbe.sym.Sym;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;

public abstract class SymFactory<TYPE extends Sym> extends Button {

	boolean openSetting = true ;
	public SymFactory(Sym sym) {
		super("記号",sym);
		Button b = this ;
		b.setEllipsisString("???");
		b.setMinHeight( 80);
/*
		Node n = sym.getExportView() ;
//		b.setGraphic(n);
 */
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
