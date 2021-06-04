package com.fbe.exe.factory;

import java.util.List;

import com.fbe.exe.FBEExecutor;
import com.fbe.item.Flow;

public class BaseExecutorFactory extends ExecutorFactory<FBEExecutor> {

	public BaseExecutorFactory() {
		super("デフォルト");
		this.description = "通常の表示タイプです。プログラムの入出力はすべてメッセージダイアログを使用します。" ;
	}

	@Override
	public FBEExecutor createExecutor(Flow mainFlow,List<Flow> flows) {
		FBEExecutor ans = new FBEExecutor(mainFlow,flows) ;
		if("1つずつ実行".equals(this.options.get("実行タイプ"))) {
			ans.setExecuteAll(false);
		}else if("すべて実行する".equals(this.options.get("実行タイプ"))){
			ans.setExecuteAll(true);
		}else {
			//エラー
		}
		System.out.println("実行タイプ："+this.options.get("実行タイプ"));
		return ans;
	}

}
