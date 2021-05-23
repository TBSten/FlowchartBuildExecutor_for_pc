package com.fbe.sym;

import com.fbe.exe.FBEExecutor;

import javafx.scene.canvas.GraphicsContext;

public class DataSym extends Sym {

	public DataSym() {
		/*
			type…”in”または”out”、タイプ
			target...代入先または出力元、対象
			(inType)...”keyboard”または”file”、入力タイプ
		 */

		this.options.put("タイプ","入力");
		this.options.put("対象","");
		this.options.put("入力タイプ","キーボード");

	}

	@Override
	public void execute(FBEExecutor exe) {
		//
		System.out.println("exe :"+this);
		String tar = this.options.get("対象");
		exe.output(exe.eval(tar).toString());
	}

	@Override
	public void reflectOption() {
		String target = this.options.get("対象") ;
		if(this.options.get("タイプ").equals("入力")) {
			this.setText(target+"を入力");
		}else if(this.options.get("タイプ").equals("出力")){
			this.setText(target+"を表示");
		}else {
			this.setText("#ERROR :タイプが不正です");
		}
	}

	@Override
	public void draw() {
		GraphicsContext gc = symCanvas.getGraphicsContext2D() ;
		gc.setFill(itemFillColor);
		gc.setStroke(itemLineColor);
		gc.setLineWidth(itemLineWidth);
		double w = getWidth();
		double h = getHeight() ;
		double lw = itemLineWidth ;

		gc.beginPath();
		gc.moveTo(h/2, 0+lw/2);
		gc.lineTo(w-lw/2, 0+lw/2);
		gc.lineTo(w-h/2, h-lw/2);
		gc.lineTo(0+lw/2, h-lw/2);
		gc.closePath();
		gc.fill();
		gc.stroke();

	}

}
