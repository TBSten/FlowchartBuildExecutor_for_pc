package com.fbe.item;

import java.util.ArrayList;
import java.util.List;

import com.fbe.sym.Sym;

import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Flow extends Item {

	public ArrayList<Sym> syms = new ArrayList<>();
	public ArrayList<Arrow> arrows = new ArrayList<>();

	public VBox vb = new VBox(0);


	public Flow(){
		this.getChildren().add(vb);
		this.setText("");
		Arrow firstArrow = new Arrow(this);
		arrows.add(firstArrow);
		vb.getChildren().add(firstArrow);
		vb.setSpacing(0);
		redraw();
	}

	public void addSym(int index,Sym sym) {
		//syms,arrows,vb.getChildren()にadd
		Arrow arrow = new Arrow(this);
		syms.add(index, sym);
		arrows.add(index+1,arrow);
		List<Node> ch = vb.getChildren();
		System.out.println(arrows.indexOf(arrow));
		ch.add(ch.indexOf(arrows.get(Math.max(arrows.indexOf(arrow)-1,0)))+1, sym);
		ch.add(ch.indexOf(sym)+1, arrow);

		this.prefHeightProperty().bind(this.vb.heightProperty());

		redraw();

	}
	public void addSym(Sym befSym , Sym sym) {
		this.addSym(this.syms.indexOf(befSym)+1, sym);
	}
	public void removeSym(Sym sym) {}

	@Override
	public void draw() {
		System.out.println(this+"の"+getWidth()+"*"+getHeight()+"を描画");
		System.out.println("  Canvas :"+symCanvas.getWidth()+"*"+symCanvas.getHeight());
		System.out.println("  Label  :"+symLabel.getWidth()+"*"+symLabel.getHeight());
		GraphicsContext gc = symCanvas.getGraphicsContext2D();
		gc.setFill(Color.BLUE);
		gc.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override public void redraw() {
		this.autosize();
		super.redraw();
	}

}
