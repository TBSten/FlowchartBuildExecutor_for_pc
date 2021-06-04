package com.fbe.sym;

import java.util.Arrays;

import com.fbe.exe.FBEExecutor;
import com.fbe.option.OptionTable;

import javafx.scene.canvas.GraphicsContext;

public class DataSym extends Sym {

	public DataSym(String type,String target) {
//		this.options.put("タイプ",type);
//		this.optionsValueList.put("タイプ",Arrays.asList("キーボード入力","ファイル入力","出力","表示"));
//		this.options.put("対象",target);
		optionPut("タイプ", "入出力の種類を指定します。", OptionTable.Type.COMBOBOX, type);
		optionPut("対象", "入力なら何に入力するか、出力なら何を出力するかを指定します。", OptionTable.Type.TEXTFIELD, target);
		this.getOptionsValueList().put("タイプ",Arrays.asList("キーボード入力","ファイル入力","出力","表示"));


		redraw();
	}
	public DataSym() {
		this("キーボード入力","");
	}

	@Override
	public void execute(FBEExecutor exe) {

		String tar = this.optionGet("対象");
		String typ = this.optionGet("タイプ");
		if(typ.equals("表示")) {
			exe.print(tar);
		}else if(typ.equals("出力")) {
			exe.print(tar);
		}else if(typ.equals("キーボード入力")) {
			String input = exe.input(tar);
			exe.putVar(tar, input);
		}else if(typ.equals("ファイル入力")) {
			String input = exe.inputFile(tar);
			exe.putVar(tar, input);
		}
	}

	@Override
	public void reflectOption() {
		String target = this.optionGet("対象") ;
		String type = this.optionGet("タイプ") ;
		if(type.equals("キーボード入力")) {
			this.setText(target+"を入力");
		}else if(type.equals("ファイル入力")){
			this.setText("ファイルから"+target+"を読む");
		}else if(type.equals("出力")){
			this.setText(target+"を出力");
		}else if(type.equals("表示")){
			if(target == null || target.matches("\\s*")) {
				this.setText("表示");
			}else{
				this.setText(target+"を表示");
			}
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
