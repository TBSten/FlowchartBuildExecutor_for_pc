package com.fbe.exe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fbe.FBEApp;
import com.fbe.FBERunnable;
import com.fbe.exe.factory.ExecutorFactory;
import com.fbe.item.Flow;
import com.fbe.sym.Sym;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jp.hishidama.eval.ExpRuleFactory;
import jp.hishidama.eval.Expression;
import jp.hishidama.eval.Rule;
import jp.hishidama.eval.exp.AbstractExpression;
import jp.hishidama.eval.exp.ArrayExpression;
import jp.hishidama.eval.exp.EqualExpression;
import jp.hishidama.eval.exp.LetExpression;
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

	public static ExecutorFactory<?> factory = null ;
	public static FBEExecutor runningExecutor = null ;

	//変数一覧
//	protected Map<String,Object> vars = new LinkedHashMap<>();
	protected MapVariable<String,Object> vars = new MapVariable<>(String.class,Object.class){
		@Override public Object getFieldValue(Object obj , String objName,String field,AbstractExpression exp) {
			System.out.println("  getFieldValue ::"+obj+" "+objName+" "+field+" "+exp);
			if(obj.getClass().isArray() && field.matches("length|len|LENGTH|LEN|")) {
				return java.lang.reflect.Array.getLength(obj) ;
			}
			return super.getFieldValue(obj,objName,field,exp);
		}
		@Override public void put(String name,Object value) {
			super.put(name, value);
			onPutVar(name,value);
		}

	};
	protected List<FBEExecutable> executeList = new ArrayList<>() ;
	protected Flow mainFlow ;
	protected List<Flow> flows ;
	protected Status status = Status.BEFORE_START ;
	protected boolean executeAll = true ;
	public Map<Sym,Object> executeOptions = new HashMap<>();

	public FBEExecutor(Flow mainFlow,List<Flow> flows ){
		this.mainFlow = mainFlow ;
		this.flows = flows ;
	}

	//数式解析
	public Object eval(String formula) {
		String regex = "\\s*\\[(.*)\\]\\s*" ;
		Pattern p = Pattern.compile(regex) ;
		Matcher m = p.matcher(formula);
		if(m.matches()) {
			System.out.println("配列");
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
			List<Object> ans2 = new ArrayList<>();
			for(String s:ans) {
				ans2.add(this.eval(s));
			}
			return ans2.toArray() ;
		}else {
			//変数定義
			/*
			MapVariable<String,Object> varMap = new MapVariable<>(String.class,Object.class) {
				@Override public Object getFieldValue(Object obj , String objName,String field,AbstractExpression exp) {
					System.out.println("  getFieldValue ::"+obj+" "+objName+" "+field+" "+exp);
					if(obj.getClass().isArray() && field.matches("length|len|LENGTH|LEN")) {
						return java.lang.reflect.Array.getLength(obj) ;
					}
					return super.getFieldValue(obj,objName,field,exp);
				}
			};
			for(Map.Entry<String,Object> ent:vars.getMap().entrySet()) {
				varMap.put(ent.getKey(), ent.getValue());
			}
			*/
			String str = formula ;
			FBERuleFactory factory = new FBERuleFactory() ;
			Rule rule = factory.getRule();
			Expression exp = rule.parse(str);
			exp.setVariable(this.vars);
			exp.setOperator(new StringOperator());
			/*
			DefaultVariable dv = new DefaultVariable() {
				@Override public Object getFieldValue(Object obj,String name,String field,AbstractExpression exp) {
					System.out.println("getFieldValue obj:"+obj+" name:"+name+" field:"+field+" exp:"+exp);
					return super.getFieldValue(obj, name, field, exp);
				}
			} ;
			exp.setVariable(dv);
			*/
			Object result = exp.eval();

			return result ;
		}
	}
	public Object toValidType(String str) {
		if(str != null) {
			if(str.matches("(\\d)*(.(\\d)+)??")) {
				//Double
				return Double.parseDouble(str);
			}else{
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

	private static class FBERuleFactory extends ExpRuleFactory {

		public FBERuleFactory() {
			super();
		}

		@Override
		protected AbstractExpression createArrayExpression() {
			AbstractExpression e = new ArrayExpression() ;
			e.setOperator("[");
			e.setEndOperator("]");
			return e ;
		}
		@Override
		protected AbstractExpression createEqualExpression() {
			AbstractExpression e = new EqualExpression() ;
			e.setOperator("=");
			return e ;
		}

		@Override
		protected AbstractExpression createLetExpression() {
			LetExpression e = new LetExpression() ;
			e.setOperator("<-");
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
			list.add(new LineComment("#"));
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
		System.out.println("print:"+formula+","+args);
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
		try {
			if(FBEExecutor.runningExecutor == null) {
				//設定画面を表示
				Stage st_setting = new Stage() ;
				st_setting.setTitle("実行の設定");
				FXMLLoader loader = new FXMLLoader(FBEExecutor.class.getResource("ExeSetting.fxml"));
				AnchorPane root = (AnchorPane)loader.load();
				SettingController cont = loader.getController();
				cont.stage = st_setting ;
				cont.hb_viewType.getChildren().addAll(ExecutorFactory.factorys);
				for(ExecutorFactory<?> fac:ExecutorFactory.factorys) {
					fac.toUnSelectMode();
					fac.setOnAction(e->{
						cont.setFactory(fac);
					});
				}
				cont.setFactory(ExecutorFactory.factorys.get(0));
				cont.setFactory(ExecutorFactory.factorys.get(0));
				st_setting.setScene(new Scene(root));
				st_setting.showAndWait();
				//exeを登録して初期化
//				FBEExecutor exe = new FBEExecutor(mainFlow,flows) ;	//factory.createExecutor(mainFlow,flows);
				if(cont.status) {
					FBEExecutor exe = cont.fact.createExecutor(mainFlow, flows) ;	//factory.createExecutor(mainFlow,flows);
					FBEExecutor.runningExecutor = exe ;
					exe.initExecutor();
				}
			}else {
				FBEExecutor.runningExecutor.msgBox("新しく実行し始めるには現在の実行を中止してください。");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected Stage controlStage = null ;
	protected static Pane variablePane ;
	protected static ScrollPane scroll ;
	public void initExecutor() {
		//--実行制御・設定ウィンドウ表示
		try {

			FBEExecutor exe = this ;
			exe.executeOptions.clear();
			FXMLLoader loader_control = new FXMLLoader(exe.getClass().getResource("ExeControl.fxml"));
			AnchorPane ap_control = (AnchorPane)loader_control.load() ;
			ControlController con_control = loader_control.getController() ;
			con_control.exe = exe ;
			Stage st_control = new Stage();
			Scene sc_control = new Scene(ap_control);
			st_control.setX(0);
			st_control.setY(0);
			st_control.setWidth(250);
			st_control.initOwner(FBEApp.window);
			st_control.setScene(sc_control);

			this.controlStage = st_control ;
			this.controlStage.setOnCloseRequest(e->{
				finish() ;
			});
			this.controlStage.setOnHidden(e->{
				FBEApp.app.getMainSplitPane().getItems().remove(scroll);
			});
			st_control.show();

		}catch(Exception exc) {
			exc.printStackTrace();
		}
		//executeListにmainFlow.symsを追加
		this.executeList.addAll(this.getMainFlow().getSyms());
		this.status = Status.BEFORE_START ;

		//必須実行準備
		this.vars.getMap().clear();
		VarTracePane.varTracePanes.clear();
		this.putVar("null", null );
		this.putVar("NULL", null );
		this.putVar("Null", null );
		this.putVar("true", true );
		this.putVar("false", false );
		this.putVar("TRUE", true );
		this.putVar("FALSE", false );
		this.putVar("True", true );
		this.putVar("False", false );
		SplitPane sp = FBEApp.app.getMainSplitPane() ;
		if(FBEExecutor.variablePane != null) {
			sp.getItems().remove(FBEExecutor.variablePane);
			System.out.println("Delete variablePane");
		}

		/*
		variablePane.setPrefWidth(250);
		variablePane.setMinWidth(50);
		*/
		variablePane = new FlowPane(10,5) ;
		scroll = new ScrollPane(variablePane) ;
		scroll.viewportBoundsProperty().addListener(e->{
			variablePane.setPrefWidth(scroll.viewportBoundsProperty().get().getWidth());
			variablePane.setPrefHeight(scroll.viewportBoundsProperty().get().getHeight());
		});
		sp.getItems().add(scroll);
		VarTracePane.varTracePanes.clear();

		//初期化時処理
		this.onInit();
	}

	private FBEExecutable beforeExeSym = null ;
	//開始ボタン押下時
	public void start() {
		if(!this.status.finish ) {
			this.status = Status.EXECUTING ;
			boolean skipF = false ;
			FBEExecutable s =  executeList.get(0);
			if(s != null) {
				try {
					System.out.println("実行:"+s);
					if(beforeExeSym != null) {
					beforeExeSym.toBaseLook();
						beforeExeSym.redraw();
					}
					s.toExeLook();
					s.redraw();
					beforeExeSym = s ;
					s.execute(this);
					executeList.remove(s);
					System.out.println("実行終了:"+s);
					if(executeList.size() <= 0) {
						this.msgBox("実行が終了しました");
						this.finish();
						this.status = Status.SAFE_FINISHED ;
						skipF = false ;
						/*
						if(this.executeAll) {
							this.finish();
						}
						*/
					}
				}catch(Throwable t) {
					this.status = Status.ERROR_FINISHED ;
					t.printStackTrace();
					this.msgBox("エラーが発生しました");
				}
			}
			if(this.status.finish) {
				//処理終了
			}if(skipF && this.status != Status.STOPPING) {
				new Thread(()->{
				//	FBEApp.sleep(300);
					Platform.runLater(()->{
						start();
					});
				}).start();
			}else if(this.executeAll && this.status == Status.EXECUTING && this.status != Status.STOPPING){
				new Thread(()->{
				//	FBEApp.sleep(300);
					Platform.runLater(()->{
						start();
					});
				}).start();
			}else {
			}
		}else {
			if(!this.executeAll) {		//よくわからないがifではじかないとallの時に出力される
				this.msgBox("すでに実行は終了しました。\n終了ボタンで終了してください。");
			}
		}
	}
	//一時停止ボタン押下時
	public void stop() {
		this.status = Status.STOPPING ;
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
		}else if(this.status.finish){
			this.controlStage.hide();
		}
		if(FBEExecutor.runningExecutor == this) {
			FBEExecutor.runningExecutor = null ;
		}else {
			//エラー
		}

	}


	//メッセージボックスで表示
	protected void msgBox(String data) {
		System.out.println("表示:"+data);
		Stage st = new Stage();
		st.setTitle("データの表示");
		st.initOwner(getOwner());
		st.initModality(Modality.WINDOW_MODAL);
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

		if(value == null || value == Boolean.TRUE || value == Boolean.FALSE || value.getClass().isArray() ) {
			this.putArrVar(name,value) ;
		}else {
			if(value instanceof String || value instanceof Character) {
				this.eval(String.format("%s <- \"%s\"",name, value));
			}else {
				this.eval(String.format("%s <- %s",name, value));
			}
		}


	}
	public void putArrVar(String name ,Object arr) {
		this.vars.put(name, arr);
	}
	//変数登録時
	protected void onPutVar(String name,Object value) {
		//
		if(variablePane != null) {
			if(!VarTracePane.varTracePanes.containsKey(name) && !name.matches(".*__RETURN")) {
				VarTracePane vtp = VarTracePane.createTracePane(this,name,value) ;
				VarTracePane.varTracePanes.put(name, vtp);
				((Pane)variablePane).getChildren().add(vtp);
			}
			for(Map.Entry<String , Object> ent:vars.getMap().entrySet()) {
				if( ent.getValue() != null && ent.getValue() != Boolean.TRUE && ent.getValue() != Boolean.FALSE){
					VarTracePane.varTracePanes.get(ent.getKey()).redraw();
				}
			}
		}
	}


	//変数取得
	public Object getVar(String name) {
		Map<String, Object> vars = this.vars.getMap();
		if(!vars.containsKey(name) ) {
			System.out.println("non contains var :"+name);
			System.out.println("  contains test :"+vars.containsKey(name));
			return null ;
		}
		return vars.get(name);
	}



	//キーボードから入力
	protected String inputMsgBox(String msg,String defaultText) {
		Stage st = new Stage();
		st.setTitle("データの入力");
		st.initOwner(getOwner());
		st.initModality(Modality.WINDOW_MODAL);
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


	public MapVariable<String, Object> getVars() {
		return vars;
	}

	public List<FBEExecutable> getExecuteList() {
		return executeList;
	}

	public boolean isExecuteAll() {
		return executeAll;
	}

	public void setExecuteAll(boolean executeAll) {
		this.executeAll = executeAll;
	}

	public Stage getOwner() {
		return this.controlStage ;
	}

}



