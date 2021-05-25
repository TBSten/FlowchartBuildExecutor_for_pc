package com.fbe.sym.factory;

import com.fbe.sym.WhileSym;

public class WhileSymFactory extends SymFactory<WhileSym> {

	public WhileSymFactory(){
		super(new WhileSym("変数 < 10"));
		this.setText("繰り返し");
		this.setOpenSetting(true);

	}

	@Override
	public WhileSym createSym() {
		WhileSym ans = new WhileSym("変数 < 10") ;
		return ans ;
	}

}




