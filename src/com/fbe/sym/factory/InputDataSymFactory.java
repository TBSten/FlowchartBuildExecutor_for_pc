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
		ans.optionPut("タイプ","キーボード入力");
		ans.optionPut("対象","変数");
//		ans.optionPut("タイプ", "入出力の種類を指定します。", OptionTable.Type.COMBOBOX, "キーボード入力");
//		ans.optionPut("対象", "入力ならばどこから入力するか、出力ならば何を出力するかを指定します。", OptionTable.Type.COMBOBOX, "変数");
		return ans;
	}

}
