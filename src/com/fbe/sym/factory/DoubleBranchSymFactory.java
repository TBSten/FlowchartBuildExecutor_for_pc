package com.fbe.sym.factory;

import com.fbe.sym.DoubleBranchSym;

public class DoubleBranchSymFactory extends SymFactory<DoubleBranchSym> {

	public DoubleBranchSymFactory(){
		super(new DoubleBranchSym("変数 < 10"));
		this.setText("分岐１");
		this.setOpenSetting(true);

	}

	@Override
	public DoubleBranchSym createSym() {
		DoubleBranchSym ans = new DoubleBranchSym("変数 < 10") ;
		return ans ;
	}



}
