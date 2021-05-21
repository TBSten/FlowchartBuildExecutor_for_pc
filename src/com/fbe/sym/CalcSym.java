package com.fbe.sym;

import com.fbe.FBEExecutor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CalcSym extends Sym {

	public CalcSym(String formula,String variable){
		this.options.put("式", formula);
		this.options.put("代入先変数", variable);
		redraw();
	}

	@Override
	public void execute(FBEExecutor exe) {
		//
	}

	@Override
	public void draw() {
		if(this.options != null) {
			this.setText(options.get("式")+" → "+options.get("代入先変数"));
		}

		GraphicsContext gc = this.symCanvas.getGraphicsContext2D() ;
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(baseLineWidth);

		gc.fillRect(0,0, getWidth(), getHeight());
		gc.strokeRect(0 + baseLineWidth/2, 0 + baseLineWidth/2, getWidth() - baseLineWidth, getHeight() - baseLineWidth);
	}

}
