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
		this.setText("");
	}

	public Sym getSymBeforeOf(Arrow ar) {
		Sym ans = null ;
		if(arrows.contains(ar)) {
			ans = syms.get(arrows.indexOf(ar)) ;
		}
		return ans ;
	}
	public Sym getSymAfterOf(Arrow ar) {
		Sym ans = null ;
		if(arrows.contains(ar)) {
			ans = syms.get(arrows.indexOf(ar)+1) ;
		}
		return ans ;
	}

	public void addSym(int index ,Sym sym) {
//		System.out.printf("addSym(%d , %s)\n",index,sym);
		List<Node> child = vb.getChildren() ;
		Arrow ar = new Arrow() ;
		ar.setParentFlow(this);
		int symIdx = index*2 ;
		int arIdx = index*2 - 1;
		if(symIdx == 0) {
			arIdx = symIdx + 1;
		}

		syms.add(symIdx /2, sym);
		arrows.add(arIdx/2,ar);

		if(syms.size() >= 2 && index >= 1) {
			child.add(arIdx , ar);
		}
		child.add(symIdx,sym);
		if(syms.size() >= 2 && index == 0) {
			child.add(arIdx , ar);
		}

//		System.out.println("log: sym:"+sym+"  parent:"+this);
		sym.setParentFlow(this);
	}
	public void addSym(Sym befSym ,Sym sym) {
		this.addSym(syms.indexOf(befSym)+1 , sym);
	}
	public void removeSym(Sym sym) {
		int idx = syms.indexOf(sym) ;
		this.syms.remove(sym);
		vb.getChildren().remove(sym);

		if(vb.getChildren().size() >= 1) {
			if(idx <= 0) {
				this.arrows.remove(idx);
				vb.getChildren().remove(0);
			}else if(this.arrows.size() > idx) {
				this.arrows.remove(idx);
				vb.getChildren().remove(idx*2-1);
			}
		}
		sym.setParentFlow(null);
	}

	@Override public void redraw() {
		this.autosize();
		super.redraw();
	}
	@Override public void draw() {
	}

}
