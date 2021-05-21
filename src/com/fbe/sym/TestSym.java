package com.fbe.sym;

import com.fbe.FBEExecutor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TestSym extends Sym {
	public TestSym() {}
	public TestSym(String text) {
		this();
		this.setText(text);
	}

	public void draw() {
		GraphicsContext gc = this.symCanvas.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0,0,getWidth(),getHeight());
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(baseLineWidth);
		gc.strokeRect(0+baseLineWidth/2, 0+baseLineWidth/2, getWidth()-baseLineWidth, getHeight()-baseLineWidth);
	}

	@Override
	public void execute(FBEExecutor exe) {
		System.out.println("["+this.symLabel.getText()+"]を実行しました");
	}



}
