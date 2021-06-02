package com.fbe.exe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fbe.FBEApp;
import com.fbe.FBERunnable;
import com.fbe.item.Flow;
import com.fbe.sym.Sym;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jp.hishidama.eval.ExpRuleFactory;
import jp.hishidama.eval.Expression;
import jp.hishidama.eval.Rule;
import jp.hishidama.eval.exp.AbstractExpression;
import jp.hishidama.eval.exp.EqualExpression;
import jp.hishidama.eval.exp.LetPowerExpression;
import jp.hishidama.eval.exp.PowerExpression;
import jp.hishidama.eval.lex.LexFactory;
import jp.hishidama.eval.lex.comment.CommentLex;
import jp.hishidama.eval.lex.comment.LineComment;
import jp.hishidama.eval.oper.JavaExOperator;
import jp.hishidama.eval.var.MapVariable;

/**
 * メモ 手軽さを重視するため、型は全面不採用。
 *
 * フローチャートを実行するためのクラス。
 * 表示や入力はすべてメッセージボックスが出てきてそれを使う。
 * 表示や入力の方法をカスタマイズしたい場合はprint,inputメソッドを使う。
 *
 * このクラスを継承してオリジナルの出力方法（例えば事前に設定された配列を使って2D表示をするなど）を実装する。
 * 後々各表示タイプをFactory(extends Button)で生成できるようにする。→設定ウィンドウで選択できる。
 *
 */
public class FBEExecutor extends FBERunnable {
	public enum Status{
		BEFORE_START(false),		//実行前
		EXECUTING(false),			//実行中
		STOPPING(false),			//一時停止中
		SAFE_FINISHED(true),		//通常終了時
		ERROR_FINISHED(true),	//エラーで終了時
		WARN_FINISHED(true);		//警告付き終了時（停止ボタンで停止など）


		boolean finish = false ;
		Status(boolean finish){
			this.finish = finish ;
		}
	}

	public static FBEExecutor runningExecutor = null ;

	//変数一覧
	protected Map<String,Object> vars = new LinkedHashMap<>();
	protected List<Sym> executeList = new ArrayList<>() ;
	protected Flow mainFlow ;
	protected List<Flow> flows ;
	protected Status status = Status.BEFORE_START ;
	protected boolean executeAll = false ;

	public FBEExecutor(Flow mainFlow,List<Flow> flows ){
		this.mainFlow = mainFlow ;
		this.flows = flows ;
	}

	//数式解析
	public Object eval(String formula) {
		//変数定義
		MapVariable<String,Object> varMap = new MapVariable<>(String.class,Object.class);
		for(Map.Entry<String,Object> ent:vars.entrySet()) {
			varMap.put(ent.getKey(), ent.getValue());
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
	public Object toValidType(String str) {
		if(str != null) {
			if(str.matches("(\\d)*(.(\\d)+)??")) {
				//Double
				return Double.parseDouble(str);
			}else {
				//String
				return str ;
			}
		}else {
			return null ;
		}
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
		protected AbstractExpression createEqualExpression() {
			AbstractExpression e = new EqualExpression() ;
			e.setOperator("=");
			return e ;
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



	//オーバーライド可能なメソッド群

	//Executor初期化時の挙動
	public void onInit() {
	}
	//Executor終了時の挙動
	public void onDiscard() {
	}
	//プログラム実行中の表示
	public void print(String formula,Object...args) {
		String data = String.valueOf(this.eval(formula)) ;
		this.msgBox(data);
	}
	//プログラム実行中の入力
	public String input(Object...msg) {
		return this.inputMsgBox(msg[0].toString());
	}
	//ファイルに書き込む
	public void outputFile(String formula) {
		//ファイルなどに出力する
	}
	//ファイルから読み込む
	public String inputFile(String fileName) {
		//ファイルなどから入力する
		return "#DEVELOPPING..." ;
	}



	//実行制御（コントロール）メソッド。ControlController等から呼ばれる。
	//実行モード起動処理
	public static void toExecuteMode(Flow mainFlow,List<Flow> flows) {
		if(FBEExecutor.runningExecutor == null) {
			FBEExecutor exe = new FBEExecutor(mainFlow,flows) ;
			FBEExecutor.runningExecutor = exe ;
			exe.initExecutor();
		}else {
			FBEExecutor.runningExecutor.msgBox("新しく実行し始めるには現在の実行を中止してください。");
		}
	}

	protected Stage controlStage = null ;
	protected Stage settingStage = null ;
	public void initExecutor() {
		//--実行制御・設定ウィンドウ表示
		try {
			FBEExecutor exe = this ;
			FXMLLoader loader_control = new FXMLLoader(exe.getClass().getResource("ExeControl.fxml"));
			FXMLLoader loader_setting = new FXMLLoader(exe.getClass().getResource("ExeSetting.fxml"));
			AnchorPane ap_control = (AnchorPane)loader_control.load() ;
			AnchorPane ap_setting = (AnchorPane)loader_setting.load();
			ControlController con_control = loader_control.getController() ;
			SettingController con_setting = loader_setting.getController() ;
			con_control.exe = exe ;
			con_setting.exe = exe ;
			Stage st_control = new Stage();
			Scene sc_control = new Scene(ap_control);
			st_control.setX(0);
			st_control.setY(0);
			st_control.setWidth(250);
			st_control.initOwner(FBEApp.window);
			st_control.setScene(sc_control);
			Stage st_setting = new Stage();
			Scene sc_setting = new Scene(ap_setting);
			System.out.println(FBEApp.window.getWidth());
			st_setting.setX(0);
			st_setting.setY(st_control.getY()+st_control.getHeight()+10);
//			st_setting.initOwner(FBEApp.window);
			st_setting.setScene(sc_setting);

			this.controlStage = st_control ;
			this.settingStage = st_setting ;
			this.controlStage.setOnCloseRequest(e->{
				finish() ;
			});

			st_setting.show();
			st_control.show();

		}catch(Exception exc) {
			exc.printStackTrace();
		}
		//executeListにmainFlow.symsを追加
		this.executeList.addAll(this.getMainFlow().getSyms());
		this.status = Status.BEFORE_START ;
		//初期化時処理
		this.onInit();
	}
	private Sym beforeExeSym = null ;
	//開始ボタン押下時
	public void start() {
		if(!this.status.finish) {
			this.status = Status.EXECUTING ;
			boolean skipF = false ;
			Sym s = executeList.get(0);
			if(s != null) {
				try {
					if(!s.isSkip()) {
						System.out.println("実行:"+s);
						if(beforeExeSym != null) {
							beforeExeSym.toBaseLook();
							beforeExeSym.redraw();
						}
						s.toExeLook();
						s.redraw();
						beforeExeSym = s ;
						s.execute(this);
					}else {
						System.out.println("!Skip "+s);
						skipF = true ;
					}
					executeList.remove(s);
					System.out.println("実行終了"+s);
					if(executeList.size() <= 0) {
						this.msgBox("実行が終了しました");
						this.finish();
						this.status = Status.SAFE_FINISHED ;
						skipF = false ;
					}
				}catch(Throwable t) {
					t.printStackTrace();
					this.msgBox("エラーが発生しました");
					this.status = Status.ERROR_FINISHED ;
				}
			}
			if(skipF) {
				start();
			}
		}else {
			this.msgBox("すでに実行は終了しました");
		}
	}
	//一時停止ボタン押下時
	public void stop() {

	}
	//終了ボタン押下時
	public void finish() {
		if(beforeExeSym != null) {
			beforeExeSym.toBaseLook();
			beforeExeSym.redraw();
		}
		if(this.status == Status.EXECUTING || !this.status.finish) {
			this.status = Status.WARN_FINISHED ;
			this.onDiscard();
			FBEExecutor.runningExecutor = null ;
		}else if(this.status.finish){
			this.controlStage.hide();
			this.settingStage.hide();
		}
	}


	//メッセージボックスで表示
	protected void msgBox(String data) {
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



	//変数設定
	public void putVar(String name,Object value) {
		vars.put(name,value);
	}
	public void putVar(String name,String value) {
		vars.put(name,toValidType(value));
	}
	//変数取得
	public Object getVar(String name) {
		if(vars.containsKey(name)) {
			return null ;
		}
		return vars.get(name);
	}

	public void openSettingWindow() {
		//設定ウィンドウを開く
	}


	//キーボードから入力
	protected String inputMsgBox(String msg,String defaultText) {
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
//		System.out.println("keyborad inputing ...");
		st.showAndWait();
//		System.out.println("keyborad inputed ...");
		if(inputTf.getText() != null) {
			return inputTf.getText();
		}else {
			return "" ;
		}
	}
	protected String inputMsgBox(String name) {
		return this.inputMsgBox(name+"を入力してください", "");
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


	public Map<String, Object> getVars() {
		return vars;
	}

	public List<Sym> getExecuteList() {
		return executeList;
	}

}



