package com.fbe.exe.factory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fbe.exe.FBEExecutor;
import com.fbe.exe.Game2DGridFBEExecutor;
import com.fbe.item.Flow;
import com.fbe.option.OptionTable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Game2DGridFBEExecutorFactory extends ExecutorFactory<Game2DGridFBEExecutor> {

	public Game2DGridFBEExecutorFactory() {
		super("2Dゲーム１");
		Image image = new Image(getClass().getResourceAsStream("grid.png"),100,100,false,false);
		ImageView iv = new ImageView(image) ;
		this.setGraphic(iv);
		this.optionPut("配列名", "ゲーム画面を描画するための2次元配列を指定します", OptionTable.Type.TEXTFIELD, "ARR");
		this.optionPut("配列の横幅", "配列名オプションで指定した配列の横方向の要素数を指定します。", OptionTable.Type.TEXTFIELD, "10");
		this.optionPut("配列の縦幅", "配列名オプションで指定した配列の縦方向の要素数を指定します。", OptionTable.Type.TEXTFIELD, "20");
		this.optionPut("配列の要素の初期値", "配列名オプションで指定した配列のゲーム開始時点での初期値を指定します。", OptionTable.Type.TEXTFIELD,"0");
		this.optionPut("表示ルール", "画面を描画するときに配列の値に対しての表示する文字列を指定します。例えば配列に10が入っているときは\"☆\"と表示させたいときは「10:\"☆\"」と指定します。各ルールは「&」で区切ってください", OptionTable.Type.TEXTAREA, "0:\"\";");
		this.description = "設定画面で指定した配列を元にゲーム画面を出力するモードです。オリジナルの表示ルールも設定できます。" ;
	}

	@Override
	public FBEExecutor createExecutor(Flow mainFlow, List<Flow> flows) {
		Game2DGridFBEExecutor ans = new Game2DGridFBEExecutor(mainFlow,flows) ;
		if("1つずつ実行".equals(this.options.get("実行タイプ"))) {
			ans.setExecuteAll(false);
		}else if("すべて実行する".equals(this.options.get("実行タイプ"))){
			ans.setExecuteAll(true);
		}else {
			//エラー
		}
		ans.setArrName(this.options.get("配列名")) ;
		ans.setRowCnt(Integer.parseInt(this.options.get("配列の縦幅")));
		ans.setColumnCnt(Integer.parseInt(this.options.get("配列の横幅")));
		ans.setInitValueFormula(this.options.get("配列の要素の初期値"));
		Map<String,String> outputRules = new LinkedHashMap<>();
		String[] work = this.options.get("表示ルール").replace(" ", "").split("&");
		for(String line:work) {
			String[] work2 = line.split(":");
			outputRules.put(work2[0], work2[1]);
		}
		ans.setOutputRules(outputRules);
		return ans ;
	}

}
