package com.fbe.sym;

import com.fbe.exe.Constant;
import com.fbe.exe.FBEExecutor;

import javafx.scene.canvas.GraphicsContext;

public class CalcSym extends Sym {

	public CalcSym(String formula,String variable){
		this.options.put("式", formula);
		this.options.put("代入先変数", variable);
		redraw();
	}

	@Override
	public void execute(FBEExecutor exe) {
		//
		System.out.println("exe:"+this);
		exe.putVar(this.options.get("代入先変数"),  new Constant(exe.eval(this.options.get("式"))) );
	}

	@Override public void reflectOption() {
		if(this.options != null) {
			this.setText(options.get("式")+" → "+options.get("代入先変数"));
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
