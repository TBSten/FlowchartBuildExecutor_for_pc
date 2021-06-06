package com.fbe.sym.factory;

import com.fbe.sym.Sym;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public abstract class SymFactory<TYPE extends Sym> extends Button {

	boolean openSetting = true ;
	public SymFactory(Sym sym) {
		super("記号");
		if(sym != null) {
			init(sym);
		}
	}

	public void init(Sym sym) {
		Button b = this ;
		b.setEllipsisString("???");
		b.setMinHeight( 80);

		Stage st = new Stage();
		Scene sc = new Scene(sym) ;
		st.setScene(sc);
		sym.redraw();
		ImageView n = new ImageView(sym.snapshot()) ;
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
