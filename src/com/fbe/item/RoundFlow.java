package com.fbe.item;

import com.fbe.sym.Sym;

public class RoundFlow extends Flow {

	private Flow flow = new Flow() ;

	public RoundFlow() {
		//
		Arrow firstArrow = new Arrow();
		Arrow endArrow = new Arrow();
		vb.getChildren().add(firstArrow);
		vb.getChildren().add(flow);
		vb.getChildren().add(endArrow);
	}

	@Override public void addSym(int index,Sym sym) {
		flow.addSym(index, sym);
	}
	@Override public void removeSym(Sym sym) {
		flow.removeSym(sym);
	}
}
