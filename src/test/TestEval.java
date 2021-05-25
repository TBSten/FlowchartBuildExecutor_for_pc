package test;

import java.util.ArrayList;
import java.util.List;

import jp.hishidama.eval.ExpRuleFactory;
import jp.hishidama.eval.Expression;
import jp.hishidama.eval.Rule;
import jp.hishidama.eval.exp.AbstractExpression;
import jp.hishidama.eval.exp.LetPowerExpression;
import jp.hishidama.eval.exp.PowerExpression;
import jp.hishidama.eval.lex.LexFactory;
import jp.hishidama.eval.lex.comment.CommentLex;
import jp.hishidama.eval.lex.comment.LineComment;
import jp.hishidama.eval.oper.JavaExOperator;
import jp.hishidama.eval.var.MapVariable;

public class TestEval {

	public static void main(String[] args) {
		//変数定義
		/*
		MapVariable<String,Long> varMap = new MapVariable<>(String.class,Long.class);
		varMap.put("x", 2L);
		varMap.put("y", 10L);
		 */

		MapVariable<String,Object> varMap = new MapVariable<>(String.class,Object.class);
		varMap.put("S1", "TEST1");
		varMap.put("S2", "TEST2");
		varMap.put("L1", 1L);
		varMap.put("L2", 10L);
		varMap.put("D1", 0.1);
		varMap.put("D2", 6.5);

		//通常の式解析
		String str = "S1*S2+(L1+L2)+(D1+D2)" ;
		BasicPowerRuleFactory factory = new BasicPowerRuleFactory() ;
		Rule rule = factory.getRule();
		Expression exp = rule.parse(str);
		exp.setVariable(varMap);
		exp.setOperator(new StringOperator());
		Object result = exp.eval();
		System.out.println(str+" = "+result);
		/*
		//通常の式解析
		String str = "x+y" ;
		System.out.println("式　："+str);
		Rule rule = ExpRuleFactory.getDefaultRule();
		Expression exp = rule.parse(str);
		//式に変数を適用
		exp.setVariable(varMap);
		Object result = exp.eval();
		System.out.println(" 結果　："+result);
*/
/*
		//BasicPowerRuleFactoryでの式解析
		String str = "'aaa'" ;
		System.out.println("式　："+str);
		BasicPowerRuleFactory factory = new BasicPowerRuleFactory() ;
		Rule rule = factory.getRule();
		Expression exp = rule.parse(str);
		//式に変数を適用
		exp.setVariable(varMap);
		long result = exp.evalLong();
		System.out.println(" 結果　："+result);
*/
/*
		//StringOperatorでの''および""の独自設定
		MapVariable<String, String> var = new MapVariable<String, String>();
		Rule rule = ExpRuleFactory.getDefaultRule();
//		Expression exp = rule.parse("v1='ABC', v2=\"DEF\"");
		Expression exp = rule.parse("'ABC'+'DEF'");
		exp.setVariable(var);
		System.out.println("結果:"+exp.eval());

		// デフォルトでは、シングルクォーテーションで囲んだ文字列は、解釈時は先頭1文字を切り出している
		exp.eval();
		System.out.println("デフォルト：" + var.getMap());

		exp.setOperator(new StringOperator());
		exp.eval();
		System.out.println("変更後　　：" + var.getMap());
 */


	}
	static class StringOperator extends JavaExOperator {

		//シングルクォーテーションで囲まれた文字列
		@Override
		public Object character(String word, AbstractExpression exp) {
			return word;
		}

		//ダブルクォーテーションで囲まれた文字列
		@Override
		public Object string(String word, AbstractExpression exp) {
			return word;
		}
	}
	static class BasicPowerRuleFactory extends ExpRuleFactory {

		public BasicPowerRuleFactory() {
			super();
		}

		@Override
		protected AbstractExpression createBitXorExpression() {
			// 「^」を排他的論理和では使わないようにする
			return null;
		}

		@Override
		protected AbstractExpression createLetXorExpression() {
			// 「^=」を排他的論理和では使わないようにする
			return null;
		}

		@Override
		protected AbstractExpression createPowerExpression() {
			// 「^」を指数演算子とする
			AbstractExpression e = new PowerExpression();
			e.setOperator("^");
			return e;
		}

		@Override
		protected AbstractExpression createLetPowerExpression() {
			// 「^=」を指数演算の代入演算子とする
			AbstractExpression e = new LetPowerExpression();
			e.setOperator("^=");
			return e;
		}

		@Override
		protected LexFactory getLexFactory() {
			// 「'」を行コメントとする
			List<CommentLex> list = new ArrayList<CommentLex>();
			list.add(new LineComment("'"));

			LexFactory factory = super.getLexFactory();
			factory.setDefaultCommentLexList(list);
			return factory;
		}
	}
}
