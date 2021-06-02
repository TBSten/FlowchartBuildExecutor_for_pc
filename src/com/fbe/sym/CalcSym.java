package com.fbe.sym;

import com.fbe.exe.FBEExecutor;
import com.fbe.option.OptionTable;

import javafx.scene.canvas.GraphicsContext;

public class CalcSym extends Sym {

	public CalcSym(String formula,String variable){
//		this.options.put("式", formula);
//		this.options.put("代入先変数", variable);
		this.optionPut("式","式に代入する値を指定します。",OptionTable.Type.TEXTAREA,formula);
		this.optionPut("代入先変数","式を代入する変数を指定します。",OptionTable.Type.TEXTFIELD,variable);
		redraw();
	}

	@Override
	public void execute(FBEExecutor exe) {
		//
		System.out.println("exe:"+this);
		String variable = this.optionGet("代入先変数");
		String formula = this.optionGet("式");
		exe.putVar(variable, exe.eval(formula) );
	}

	@Override public void reflectOption() {
		if(this.getOptions() != null) {
			String formula = optionGet("式");
			String variable = optionGet("代入先変数");
			this.setText(formula+" → "+variable);
		}
	}

	@Override
	public void draw() {

		GraphicsContext gc = this.symCanvas.getGraphicsContext2D() ;
		gc.setFill(itemFillColor);
		gc.setStroke(itemLineColor);
		gc.setLineWidth(itemLineWidth);

		gc.fillRect(0,0, getWidth(), getHeight());
		gc.strokeRect(0 + itemLineWidth/2, 0 + itemLineWidth/2, getWidth() - itemLineWidth, getHeight() - itemLineWidth);
	}

}
