package com.fbe.sym;

import java.util.ArrayList;
import java.util.List;

import com.fbe.exe.FBEExecutable;
import com.fbe.exe.FBEExecutor;
import com.fbe.option.OptionTable;

import javafx.scene.text.Font;

public class ForSym extends WhileSym {

	public ForSym(String variable,String initValue,String condition,String inc) {
		super(condition);
		this.symLabel.setFont(Font.font(this.symLabel.getFont().getSize()*8/10));

		this.getOptions().clear();
		this.getOptionsValueList().clear();
		this.getOptionTypes().clear();
		this.getOptionDescriptions().clear();

		this.optionPut("ループ変数", "ループに使用する変数を指定します。", OptionTable.Type.TEXTFIELD, variable);
		this.optionPut("初期値", "ループ変数の初期値を指定します。", OptionTable.Type.TEXTFIELD, initValue);
		this.optionPut("条件", "繰り返し処理の条件式を指定します。", OptionTable.Type.TEXTFIELD, condition);
		this.optionPut("増分", "ループ変数の1ループごとの増分値を指定します。", OptionTable.Type.TEXTFIELD, inc);
//		this.optionPut("タイプ", "どのタイミングで条件を判定するかを指定します。", OptionTable.Type.COMBOBOX, "前判定");
//		this.getOptionsValueList().put("タイプ",Arrays.asList("前判定","後判定"));

	}

	@Override
	public void reflectOption() {
		String lbText = String.format("ループ%d\n %sを%sから%sずつ増やして%s の間", this.num, this.optionGet("ループ変数"), this.optionGet("初期値"), this.optionGet("増分"), this.optionGet("条件")) ;
		this.symLabel.setText(lbText);
		this.bottomLabel.setText("ループ"+this.num);
		this.bottomLabel.setLayoutY(this.getHeight()-baseHeightProperty.get());
	}

	@Override
	public void execute(FBEExecutor exe) {
		System.out.println(exe.executeOptions.get(this));
		if(exe.executeOptions.get(this) == null) {
			System.out.println("For Start");
			exe.putVar(this.optionGet("ループ変数"),exe.eval(this.optionGet("初期値")));
		}
		Object con = exe.eval(this.optionGet("条件"));
		if((boolean)con) {
			List<FBEExecutable> exeList = exe.getExecuteList() ;
			int idx = exeList.indexOf(this);
			List<FBEExecutable> list = new ArrayList<>();
			list.addAll(this.getFlow().getSyms());
			list.add(new ForEndSym());
			list.add(this);
			exeList.addAll(idx+1,list);
			exe.executeOptions.put(this,"already init");
		}else {
			exe.executeOptions.put(this,null) ;
		}


	}
	protected class ForEndSym extends Sym{
		@Override public void execute(FBEExecutor exe) {
			String hen = ForSym.this.optionGet("ループ変数") ;
			Object zou = exe.eval(ForSym.this.optionGet("増分"));
			exe.putVar(hen, exe.eval(hen+"+"+zou));
			ForSym.this.toExeLook();
		}
		@Override
		public void draw() {}
	}
}
