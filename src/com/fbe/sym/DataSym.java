package com.fbe.sym;

import java.util.Arrays;

import com.fbe.exe.FBEExecutor;

import javafx.scene.canvas.GraphicsContext;

public class DataSym extends Sym {

	public DataSym(String type,String target) {
		this.options.put("タイプ",type);
		this.optionsValueList.put("タイプ",Arrays.asList("キーボード入力","ファイル入力","出力","表示"));
		this.options.put("対象",target);

		redraw();
	}
	public DataSym() {
		this("キーボード入力","");
	}

	@Override
	public void execute(FBEExecutor exe) {
		//
		System.out.println("exe :"+this);
		String tar = this.options.get("対象");
		exe.msgBox(exe.eval(tar).toString());
	}

	@Override
	public void reflectOption() {
		String target = this.options.get("対象") ;
		if(this.options.get("タイプ").equals("キーボード入力")) {
			this.setText(target+"を入力");
		}else if(this.options.get("タイプ").equals("ファイル入力")){
			this.setText("ファイルから"+target+"を読む");
		}else if(this.options.get("タイプ").equals("出力")){
			this.setText(target+"を出力");
		}else if(this.options.get("タイプ").equals("表示")){
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
