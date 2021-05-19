package com.fbe.item;

import java.util.ArrayList;
import java.util.List;

import com.fbe.sym.Sym;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class Flow extends Item {
	ArrayList<Sym> syms = new ArrayList<>();
	ArrayList<Arrow> arrows = new ArrayList<>();
	VBox vb = new VBox();

	public Flow() {
		this.getChildren().add(vb);
	}

	public void addSym(int index ,Sym sym) {
		System.out.printf("addSym(%3d,%s)\n",index,sym);
		List<Node> child = vb.getChildren() ;

		System.out.print(" before | ");
		for(Node n:child) {
			System.out.print(n+" | ");
		}
		System.out.println();

		Arrow ar = new Arrow();

		System.out.print(" after  | ");
		for(Node n:child) {
			System.out.print(n+" | ");
		}
		System.out.println();
	}
	public void addSym(Sym befSym ,Sym sym) {}
	public void removeSym(Sym sym) {}

	@Override public void redraw() {
		this.autosize();
		super.redraw();
	}
	@Override public void draw() {
	}

}
