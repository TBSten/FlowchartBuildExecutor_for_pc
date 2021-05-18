package test;

import jp.hishidama.eval.ExpRuleFactory;
import jp.hishidama.eval.Expression;
import jp.hishidama.eval.Rule;
import jp.hishidama.eval.var.MapVariable;

public class TestEval {

	public static void main(String[] args) {
		//変数定義
		MapVariable<String,Long> varMap = new MapVariable<>(String.class,Long.class);
		varMap.put("x", 2L);
		varMap.put("y", 100L);


		//式解析
		String str = "x*y" ;
		System.out.println("式　："+str);
		Rule rule = ExpRuleFactory.getDefaultRule();
		Expression exp = rule.parse(str);
		//式に変数を適用
		exp.setVariable(varMap);
		long result = exp.evalLong();
		System.out.println(" 結果　："+result);
	}

}
