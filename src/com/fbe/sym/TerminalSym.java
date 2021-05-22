package com.fbe.sym;

import com.fbe.FBEExecutor;

import javafx.scene.canvas.GraphicsContext;

public class TerminalSym extends Sym {
	public enum Type{
		START("はじめ"),
		END("おわり");
		String txt = "???";
		Type(String txt){
			this.txt = txt ;
		}
	}

	protected Type type ;
	public TerminalSym(Type t) {
		this.type = t ;
		this.getMenu().getItems().remove(0);	//削除できない

		redraw();
	}
	public TerminalSym() {
		this(Type.START);
	}
	@Override
	public void execute(FBEExecutor exe) {
		//なし
	}

	@Override
	public void draw() {
		if(this.type != null) {
			setText(this.type.txt);
		}

		GraphicsContext gc = symCanvas.getGraphicsContext2D();
		gc.setStroke(itemLineColor);
		gc.setFill(itemFillColor);
		gc.setLineWidth(itemLineWidth);
		gc.fillRoundRect(0+itemLineWidth/2, 0+itemLineWidth/2, getWidth()-itemLineWidth, getHeight()-itemLineWidth, getHeight(), getHeight());
		gc.strokeRoundRect(0+itemLineWidth/2, 0+itemLineWidth/2, getWidth()-itemLineWidth, getHeight()-itemLineWidth, getHeight(), getHeight());
	}


}
