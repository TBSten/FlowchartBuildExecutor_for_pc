package com.fbe.sym.factory;

import com.fbe.sym.Sym;

import javafx.scene.Node;
import javafx.scene.control.Button;

public abstract class SymFactory<TYPE extends Sym> extends Button {
	public SymFactory(Sym sym) {
		super("",sym);
		Button b = this ;
		b.setEllipsisString("");
		b.setPrefSize(80, 80);
		Node n = sym.getExportView() ;
		b.setGraphic(n);
	}
	public abstract TYPE createSym() ;
}
