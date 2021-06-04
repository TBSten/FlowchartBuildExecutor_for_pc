package com.fbe.exe.factory;

import java.util.List;

import com.fbe.exe.LoggerFBEExecutor;
import com.fbe.item.Flow;

public class LoggerExecutorFactory extends ExecutorFactory<LoggerFBEExecutor> {

	public LoggerExecutorFactory() {
		super("ログ");
		this.description = "ログ方式の表示タイプです。\"\\n\"を出力すると改行します。" ;
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
