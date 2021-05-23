package com.fbe.item;

import com.fbe.sym.Sym;

/**
 * 非推奨。追加処理など上手くいかない。
 *
 */
public class RoundFlow extends Flow {

	public RoundFlow() {
		super();
	}

	@Override
	public Sym getSymBeforeOf(Arrow ar) {
		if(arrows.indexOf(ar) >= 0 && arrows.indexOf(ar) < syms.size()) {
			return syms.get(arrows.indexOf(ar));
		}else {
			return null ;
		}
	}
	@Override
	public Sym getSymAfterOf(Arrow ar) {
		if(arrows.indexOf(ar)+1 >= 0 && arrows.indexOf(ar)+1 < syms.size()) {
			return syms.get(arrows.indexOf(ar)+1);
		}else {
			return null ;
		}
	}

	@Override
	public void addSym(int index ,Sym sym) {



		//addAnimeで追加時のアニメーション
	}
	@Override
	public void addSym(Sym befSym ,Sym sym) {

	}
	@Override
	public void removeSym(Sym sym) {
	}

	@Override
	public void disable() {
	}



}
