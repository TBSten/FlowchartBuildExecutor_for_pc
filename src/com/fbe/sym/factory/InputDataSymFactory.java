package com.fbe.sym.factory;

import com.fbe.sym.DataSym;

public class InputDataSymFactory extends SymFactory<DataSym> {

	public InputDataSymFactory() {
		super(new DataSym("キーボード入力","変数"));
		this.setText("入力");
		this.setOpenSetting(true);
	}

	@Override
	public DataSym createSym() {
		DataSym ans = new DataSym() ;
		ans.options.put("タイプ","キーボード入力");
		ans.options.put("対象","変数");
		return ans;
	}

}
