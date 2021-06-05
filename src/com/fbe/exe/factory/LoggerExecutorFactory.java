package com.fbe.exe.factory;

import java.util.List;

import com.fbe.exe.LoggerFBEExecutor;
import com.fbe.item.Flow;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoggerExecutorFactory extends ExecutorFactory<LoggerFBEExecutor> {

	public LoggerExecutorFactory() {
		super("実行コンソール");
		this.description = "ログ方式の表示タイプです。Windowsのコマンドプロンプト、Macなどのターミナルに似た実行画面に1行ずつ出力されます。" ;
		Image image = new Image(getClass().getResourceAsStream("terminal.png"),100,100,false,false);
		ImageView iv = new ImageView(image) ;
		this.setGraphic(iv);

	}
	@Override
	public LoggerFBEExecutor createExecutor(Flow mainFlow,List<Flow> flows) {
		LoggerFBEExecutor ans = new LoggerFBEExecutor(mainFlow,flows) ;
		if("1つずつ実行".equals(this.options.get("実行タイプ"))) {
			ans.setExecuteAll(false);
		}else if("すべて実行する".equals(this.options.get("実行タイプ"))){
			ans.setExecuteAll(true);
		}else {
			//エラー
		}
		return ans ;
	}


}
