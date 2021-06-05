package com.fbe.sym.factory;

import com.fbe.sym.ForSym;

public class ForSymFactory extends SymFactory<ForSym> {
	public ForSymFactory(){
		super(new ForSym("CNT","0","CNT < 10","1"));
		this.setText("繰り返し２");
		this.setOpenSetting(true);

	}

	@Override
	public ForSym createSym() {
		ForSym ans = new ForSym("CNT","0","CNT < 10","1") ;
		return ans ;
	}

}
