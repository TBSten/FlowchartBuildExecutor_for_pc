package com.fbe.sym.factory;

import com.fbe.sym.DataSym;

public class OutputDataSymFactory extends SymFactory<DataSym> {

	public OutputDataSymFactory() {
		super(new DataSym());
		this.setText("出力");
		this.setOpenSetting(true);
	}

	@Override
	public DataSym createSym() {
		DataSym ans = new DataSym() ;
		ans.options.put("タイプ","出力");
		ans.options.put("対象","");
		ans.options.put("入力タイプ","");
		return ans;
	}

}
