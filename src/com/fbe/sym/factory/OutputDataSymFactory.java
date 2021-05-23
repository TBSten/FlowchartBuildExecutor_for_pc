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
		ans.options.put("タイプ","表示");
		ans.options.put("対象","");
		return ans;
	}

}
