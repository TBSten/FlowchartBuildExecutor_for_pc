package com.fbe.sym.factory;

import com.fbe.sym.DataSym;

public class OutputDataSymFactory extends SymFactory<DataSym> {

	public OutputDataSymFactory() {
		super(new DataSym("表示","データ"));
		this.setText("表示");
		this.setOpenSetting(true);
	}

	@Override
	public DataSym createSym() {
		DataSym ans = new DataSym() ;
		ans.optionPut("タイプ","表示");
		ans.optionPut("対象","");
		return ans;
	}

}
