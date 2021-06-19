package test;

import java.util.ArrayList;
import java.util.Arrays;
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
import jp.hishidama.eval.ref.RefactorVarName;
import jp.hishidama.eval.var.MapVariable;

public class TestEval {

	public static void main(String[] args) {
		MapVariable<String,Object> vars = new MapVariable<>(String.class,Object.class) ;
		vars.put("v1", 0);
		vars.put("v2", 0);
		vars.put("v3", 0);
		vars.put("合計", 0);
		String[] formulas = {"v1+v2","v3","合計+v3"} ;
		for(String fo:formulas) {
			Rule rule = ExpRuleFactory.getJavaRule();
			Expression exp = rule.parse(fo);
			exp.setVariable(vars);

			for(String key:vars.getMap().keySet()) {
				System.out.println("  "+key);
			}
			System.out.println("変更前：" + exp.toString());
			exp.refactorName(new RefactorVarName(null, "合計", "v4"));	//変数名bbをfooに変更
			System.out.println("変更後：" + exp.toString());
			for(String key:vars.getMap().keySet()) {
				System.out.println("  "+key);
			}
			System.out.println("=================");
		}

		String regex = "\\s*\\[(.*)\\]\\s*" ;
		String formula = "  [10,[20,[80,90],30],40]   " ;
		/*
		Pattern p = Pattern.compile(regex) ;
		Matcher m = p.matcher(formula);
		if(m.matches()) {
			String f = m.group(1) ;
			System.out.println(f);
			//,で分割
			List<String> ans = new ArrayList<>();
			int kakko = 0 ;
			String work = "" ;
			for(int i = 0;i < f.length();i++) {
				String s= Character.toString(f.charAt(i));
				if(s.equals("[")) {
					kakko ++;
				}else if(s.equals("]")){
					kakko --;
				}
				if(s.equals(",") && kakko == 0){
					ans.add(work);
					work = "" ;
				}else {
					work+=s;
				}
			}
			ans.add(work);
			ans.forEach(e->{
				System.out.print(e+"|") ;
			});
			System.out.println();
		}else {
			System.out.println("don't match");
		}
		*/

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
		varMap.put("arr", new Object[] {10,20,30});

		//通常の式解析
		String str = "arr[0]=99" ;
		BasicPowerRuleFactory factory = new BasicPowerRuleFactory() ;
		Rule rule = factory.getRule();
		Expression exp = rule.parse(str);
		exp.setVariable(varMap);
		exp.setOperator(new StringOperator());
		Object result = exp.eval();
		System.out.println(str+" = "+result);
		System.out.println(Arrays.toString((Object[])varMap.get("arr")));


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
		public AbstractExpression createArrayExpression() {
			return super.createArrayExpression() ;
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
