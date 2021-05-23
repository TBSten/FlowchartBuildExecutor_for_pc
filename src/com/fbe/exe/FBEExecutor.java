package com.fbe.exe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fbe.FBERunnable;
import com.fbe.item.Flow;
import com.fbe.sym.Sym;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
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

/**
 * メモ 手軽さを重視するため、型は全面不採用。
 */
public class FBEExecutor extends FBERunnable {
	public enum Status{
		BEFORE_START,
		EXECUTING,
		SAFE_FINISHED,
		ERROR_FINISHED;
	}

	//変数一覧
	protected Map<String,Variable> vars = new LinkedHashMap<>();
	protected Flow mainFlow ;
	protected List<Flow> flows ;
	protected Status status = Status.BEFORE_START ;
	protected Sym exeCursor = null ;

	public FBEExecutor(Flow mainFlow,List<Flow> flows ){
		this.mainFlow = mainFlow ;
		this.flows = flows ;
	}

	//数式解析
	public Object eval(String formula) {
		//変数定義
		MapVariable<String,Object> varMap = new MapVariable<>(String.class,Object.class);
		for(Map.Entry<String,Variable> ent :vars.entrySet()) {
			Object v = ent.getValue().parse() ;
			v = Double.parseDouble(String.valueOf(v)) ;

			varMap.put(ent.getKey(), v);
		}
		String str = formula ;
		BasicPowerRuleFactory factory = new BasicPowerRuleFactory() ;
		Rule rule = factory.getRule();
		Expression exp = rule.parse(str);
		exp.setVariable(varMap);
		exp.setOperator(new StringOperator());
		Object result = exp.eval();
		return result ;
	}
	public boolean isAbleToEval(String formula) {
		try {
			eval(formula);
			return true ;
		}catch(Throwable t) {
			return false ;
		}
	}
	private static class StringOperator extends JavaExOperator {
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
		//数値はすべてdoubleで返す
		public Object number(String word, AbstractExpression exp) {
			return Double.parseDouble(word) ;
		}
	}

	private static class BasicPowerRuleFactory extends ExpRuleFactory {

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

	//出力
	public void msgBox(String data) {
		System.out.println("表示:"+data);

		Stage st = new Stage();
		st.setTitle("データの表示");
		VBox root = new VBox();
		root.setMinSize(300, 50);
		Scene sc = new Scene(root);
		st.setScene(sc);
		Label tit = new Label("表示") ;
		tit.setFont(Font.font(20));
		tit.setAlignment(Pos.CENTER);
		tit.prefWidthProperty().bind(root.widthProperty());
		root.getChildren().add(tit);
		Label mes = new Label(data) ;
		mes.setFont(Font.font(25));
		mes.setAlignment(Pos.CENTER);
		mes.prefWidthProperty().bind(root.widthProperty());
		mes.setMinHeight(30);
		root.getChildren().add(mes);
		ButtonBar bb = new ButtonBar();
		Button okB = new Button("OK");
		okB.setOnAction(e->{
			st.close();
		});
		bb.getButtons().add(okB);
		root.getChildren().add(bb);
		st.showAndWait();
	}
	public void output(String data) {
		//ファイルなどに出力する
	}


	//変数設定
	public void putVar(String name,Variable value) {
		vars.put(name,value);
	}
	public void putVar(String name,String value) {
		putVar(name,new Variable(name,value));
	}
	//変数取得
	public Variable getVar(String name) {
		if(vars.containsKey(name)) {
			return null ;
		}
		return vars.get(name);
	}
	public Object getVarAsObject(String name) {
		if(vars.containsKey(name)) {
			return null ;
		}
		return getVar(name).parse() ;
	}

	//流れ図実行
	public void executeAll() {
		//ここで実行ウィンドウ（停止ボタンなどを含める）を表示

		if(this.status == Status.BEFORE_START) {
			try {
				System.out.println("実行-開始");
				this.setExeCursor(mainFlow.getSyms().get(0));
				mainFlow.execute(this);
				if(this.status == Status.EXECUTING) {
					this.status = Status.SAFE_FINISHED ;
				}
				System.out.println("実行-終了");
			}catch(Throwable t) {
				this.status = Status.ERROR_FINISHED ;
				System.out.println("エラー");
				t.printStackTrace();
				msgBox("エラー");
			}
		}
	}

	//入力
	public String inputKeyboard(String msg,String defaultText) {
		Stage st = new Stage();
		st.setTitle("データの入力");
		VBox root = new VBox();
		root.setMinWidth(300);
		Scene sc = new Scene(root);
		st.setScene(sc);
		Label mesL = new Label(msg) ;
		mesL.setAlignment(Pos.TOP_LEFT);
		root.getChildren().add(mesL);
		TextField inputTf = new TextField(defaultText);
		inputTf.prefWidthProperty().bind(root.widthProperty());
		root.getChildren().add(inputTf);
		ButtonBar bb = new ButtonBar();
		Button okB = new Button("OK");
		okB.setOnAction(e->{
			st.close();
		});
		bb.getButtons().add(okB);
		root.getChildren().add(bb);
		st.showAndWait();
		if(inputTf.getText() != null) {
			return inputTf.getText();
		}else {
			return "" ;
		}
	}
	public String inputKeyboard(String name) {
		return this.inputKeyboard(name+"を入力してください", "");
	}
	public String inputFile() {
		return "DEPLOYING..." ;
	}



	//ゲッター・セッター
	public Flow getMainFlow() {
		return mainFlow;
	}

	public void setMainFlow(Flow mainFlow) {
		this.mainFlow = mainFlow;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Sym getExeCursor() {
		return exeCursor;
	}

	public void setExeCursor(Sym exeCursor) {
		this.exeCursor = exeCursor;
	}

	public Map<String, Variable> getVars() {
		return vars;
	}

}



