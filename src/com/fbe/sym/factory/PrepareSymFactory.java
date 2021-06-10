package com.fbe.sym.factory;

import com.fbe.sym.PrepareSym ;

public class PrepareSymFactory extends SymFactory<PrepareSym> {

	public PrepareSymFactory(){
		super(new PrepareSym("1次元配列","10","ARR","0"));
		this.setText("配列を準備");
		this.setOpenSetting(true);

	}

	@Override
	public PrepareSym createSym() {
		PrepareSym ans = new PrepareSym("1次元配列","10","ARR","0") ;
		return ans ;
	}

}




