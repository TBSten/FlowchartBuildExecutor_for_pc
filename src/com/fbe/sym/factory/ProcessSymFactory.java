package com.fbe.sym.factory;

import com.fbe.sym.ProcessSym;

public class ProcessSymFactory extends SymFactory<ProcessSym> {

	public ProcessSymFactory() {
		super(new ProcessSym("定義済み処理"));
		this.setText("定義済み処理");
		this.setOpenSetting(true);

	}

	@Override
	public ProcessSym createSym() {
		ProcessSym ans = new ProcessSym("定義済み処理");

		return ans ;
	}

}
