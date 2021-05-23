package com.fbe.sym.factory;

import com.fbe.sym.CalcSym;

public class CalcSymFactory extends SymFactory<CalcSym> {

	public CalcSymFactory(){
		super(new CalcSym("式","代入先変数"));
		this.setText("計算");

	}

	@Override
	public CalcSym createSym() {
		CalcSym ans = new CalcSym("0","変数") ;
		return ans ;
	}

}




