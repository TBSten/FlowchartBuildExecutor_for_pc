package com.fbe.sym;

import java.util.Arrays;

import com.fbe.exe.FBEExecutor;
import com.fbe.option.OptionTable;

import javafx.scene.canvas.GraphicsContext;

public class PrepareSym extends Sym {

	public PrepareSym(String type,String counts,String target,String start) {
		this.optionPut("タイプ", "準備するものの種類を指定します。", OptionTable.Type.COMBOBOX, type);
		getOptionsValueList().put("タイプ",Arrays.asList("1次元配列","2次元配列","3次元配列"));
		this.optionPut("要素数", "配列などの要素数を指定します。例えば要素数が3つの1次元配列なら「3」、縦が20・横が10の2次元配列なら「20*10」のように指定します。",
				OptionTable.Type.TEXTFIELD, counts);
		this.optionPut("対象", "準備する配列名などを指定します。",OptionTable.Type.TEXTFIELD, target);
		this.optionPut("初期値", "配列の各要素などの初期値を設定します。",OptionTable.Type.TEXTFIELD, start);
	}

	@Override
	public void reflectOption() {
		this.symLabel.setText(String.format("%s %sを初期値%sで準備",this.optionGet("タイプ"),this.optionGet("対象"),this.optionGet("初期値")));
	}

	@Override
	public void execute(FBEExecutor exe) {
		String type = optionGet("タイプ");
		String[] argsStr = optionGet("要素数").split(",");
		int[] args = new int[argsStr.length] ;
		for(int i = 0;i < args.length;i++) {
			args[i] = Integer.parseInt(argsStr[i].replace(" ", ""));
		}
		if("1次元配列".equals(type)) {
			Object[] ans = new Object[args[0]] ;
			for(int i = 0;i < ans.length;i++) {
				ans[i] = exe.eval(optionGet("初期値"));
			}
			exe.putVar(optionGet("対象"), ans);
		}else if("2次元配列".equals(type)) {
			Object[][] ans = new Object[args[0]][args[1]] ;
			for(int i = 0;i < ans.length;i++) {
				for(int j = 0;j < ans[i].length;j++) {
					ans[i][j] = exe.eval(optionGet("初期値"));
				}
			}
			exe.putVar(optionGet("対象"), ans);
		}else if("3次元配列".equals(type)) {
			Object[][][] ans = new Object[args[0]][args[1]][args[2]] ;
			for(int i = 0;i < ans.length;i++) {
				for(int j = 0;j < ans[i].length;j++) {
					for(int k = 0;k < ans[i][j].length;k++) {
						ans[i][j][k] = exe.eval(optionGet("初期値"));
					}
				}
			}
			exe.putVar(optionGet("対象"), ans);
		}
	}

	@Override
	public void draw() {
		GraphicsContext gc = this.symCanvas.getGraphicsContext2D() ;
		gc.setFill(itemFillColor);
		gc.setStroke(itemLineColor);
		gc.setLineWidth(itemLineWidth);

		double w = getWidth();
		double h = getHeight() ;
		double lw = this.itemLineWidth ;
		double[] x = {w*0.05 , w*0.95 , w-lw/2 , w*0.95 , w*0.05 , 0+lw/2 } ;
		double[] y = {0+lw/2 , 0+lw/2 , h/2 , h-lw/2 , h-lw/2 , h/2 } ;
		gc.fillPolygon(x, y, x.length);
		gc.strokePolygon(x, y, x.length);
	}

}
