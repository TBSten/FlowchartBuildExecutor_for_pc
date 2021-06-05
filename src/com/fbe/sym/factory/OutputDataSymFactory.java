package com.fbe.sym.factory;

import com.fbe.sym.DataSym;

public class OutputDataSymFactory extends SymFactory<DataSym> {

	public OutputDataSymFactory() {
		super(new DataSym("出力","変数"));
		this.setText("表示");
		this.setOpenSetting(true);
	}

	@Override
	public DataSym createSym() {
		DataSym ans = new DataSym() ;
		ans.optionPut("タイプ","出力");
		ans.optionPut("対象","変数");
		return ans;
	}

}
