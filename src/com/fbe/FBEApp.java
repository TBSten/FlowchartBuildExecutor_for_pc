package com.fbe;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fbe.exe.factory.ExecutorFactory;
import com.fbe.exe.factory.Game2DGridFBEExecutorFactory;
import com.fbe.exe.factory.LoggerExecutorFactory;
import com.fbe.exe.factory.MsgBoxExecutorFactory;
import com.fbe.exe.factory.TableExecutorFactory;
import com.fbe.format.FBEFormat;
import com.fbe.format.FBEFormat1_0;
import com.fbe.format.FBEFormatApp;
import com.fbe.item.Arrow;
import com.fbe.item.Flow;
import com.fbe.item.Item;
import com.fbe.sym.ArrayTemplate;
import com.fbe.sym.factory.CalcSymFactory;
import com.fbe.sym.factory.DoubleBranchSymFactory;
import com.fbe.sym.factory.ForSymFactory;
import com.fbe.sym.factory.InputDataSymFactory;
import com.fbe.sym.factory.MultiBranchSymFactory;
import com.fbe.sym.factory.OutputDataSymFactory;
import com.fbe.sym.factory.PrepareSymFactory;
import com.fbe.sym.factory.ProcessSymFactory;
import com.fbe.sym.factory.WhileSymFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


/**
 * FBEアプリケーション関連のstaticメソッドを定義します。
 * @author TBS
 *
 */
public class FBEApp {


	public static FBEWindow app ;
	public static Stage window ;
	public static HashMap<String,BaseController> controllers = new HashMap<>();

	public static long startTime ;

	private static Item nowSelectingItem = null ;
//	public static String fbePath = "" ;

	/**
	 * FBEアプリケーションを起動します。
	 */
	public static void main(String[] args) {

		startTime = System.currentTimeMillis();
		System.out.printf("app start [startTime=%d]\n",startTime) ;

		/**
	      * アプリケーションを起動するAPIの引数に
	      * プロジェクト作成時に自動作成されたMain.classを設定
	      */

	    Application.launch(FBEWindow.class);

	    //コマンドライン引数があればここでインポート

	}

	protected static List<Runnable> executingRunnables = new ArrayList<>();
	/**
	 * 非同期で処理を実行します。Runnableインスタンスを指定できますが、FBERunnableインスタンスを渡すことをお勧めします。
	 * @param r
	 * 非同期で実行する処理。
	 */
	public static void invoke(Runnable r) {
		executingRunnables.add(r);
		Thread t = new Thread(r);
		t.start();
		Thread waitThread = new Thread(()->{
			try {
				t.join();
				executingRunnables.remove(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}) ;
		waitThread.start();
	}
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	public static void saveFBE(List<Flow> flows,String path) throws Exception{
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		for(Flow f :flows) {
			bw.write( "" /* f.toFBE() または symToFBE(f)  */);
			bw.newLine();
		}
		bw.flush();
	}
/*
	public static void executeFlows(List<Flow> flows) {
		//flowsを実行
		FBEExecutor exe = new FBEExecutor(flows.get(0),flows) ;
		exe.executeAll();
	}
*/
	public static void selectItem(Item i) {
		if(FBEApp.nowSelectingItem != null) {
			FBEApp.nowSelectingItem.toBaseLook();
			FBEApp.nowSelectingItem.redraw();
		}
		if(i != null) {
			i.toSelectLook();
			i.redraw();
		}
		FBEApp.nowSelectingItem = i ;
	}
	public static Item getNowSelectingItem() {
		return nowSelectingItem;
	}

	public static void msgBox(String msg) {
		Platform.runLater(()->{
			/*
			Stage st = new Stage() ;
			st.initOwner(FBEApp.window);
			st.initModality(Modality.WINDOW_MODAL);
			Label root = new Label(msg);
			root.setFont(Font.font(25));
			root.setPadding(new Insets(30,10,30,10));
			st.setScene(new Scene(root));
			st.showAndWait();
			*/
			System.out.println("FBEApp.msgBox :"+msg);
			Alert alert = new Alert(Alert.AlertType.INFORMATION,msg);
			alert.showAndWait()
		      .filter(response -> response == ButtonType.OK)
		      .ifPresent(response -> {});
		});
	}

	public static void init() {
		SaveApp.init();

		//ExecutorFactoryを登録
		ExecutorFactory.factorys.add(new MsgBoxExecutorFactory());
		ExecutorFactory.factorys.add(new LoggerExecutorFactory());
		ExecutorFactory.factorys.add(new TableExecutorFactory());
		ExecutorFactory.factorys.add(new Game2DGridFBEExecutorFactory());

		//SymFacotoryを登録
		Arrow.factorys.add(new CalcSymFactory());
		Arrow.factorys.add(new OutputDataSymFactory());
		Arrow.factorys.add(new WhileSymFactory());
		Arrow.factorys.add(new DoubleBranchSymFactory());
		Arrow.factorys.add(new ForSymFactory());
		Arrow.factorys.add(new InputDataSymFactory());
		Arrow.factorys.add(new MultiBranchSymFactory());
		Arrow.factorys.add(new ProcessSymFactory());
		Arrow.factorys.add(new PrepareSymFactory());

		//ArrayTemplateを登録
		ArrayTemplate tem1 = new ArrayTemplate() ;
			tem1.setName("1次元配列のテンプレート例"); tem1.setType("1次元配列"); tem1.setSize("5"); tem1.setOperations("0=>(all)");
		ArrayTemplate tem2 = new ArrayTemplate() ;
			tem2.setName("2次元配列のテンプレート例"); tem2.setType("2次元配列"); tem2.setSize("5,3"); tem2.setOperations("0=>(all)");
		ArrayTemplate.putTemplate(tem1);
		ArrayTemplate.putTemplate(tem2);

		//FBEFormatAppにFBEFormatを登録 , defaultFormatに代入
		FBEFormat[] formats = {
				new FBEFormat1_0()
		} ;
		for(FBEFormat format:formats) {
			FBEFormatApp.formats.put(format.getVer(),format) ;
		}
		FBEFormatApp.defaultFormat = formats[0] ;

		boolean flg = true ;
		String[] needDirs = {"./work","./file","./export"} ;
		try {
			for(String dir:needDirs) {
				boolean dirNotExists = !(Files.exists(Paths.get(dir)) && Files.isDirectory(Paths.get(dir))) ;
				if(dirNotExists && flg) {
					FBEApp.msgBox("初期化します");
					flg = false ;
				}
				if(dirNotExists) Files.createDirectory(Paths.get(dir));
			}
		}catch(Exception exc) {
			FBEApp.msgBox("初期化に失敗しました。エラーが発生する可能性があります。");
			exc.printStackTrace();
		}
	}

	public static void shutdown() {
		SaveApp.shutdown();
	}

	public static Matcher matcher(String regex, String str) {
		return Pattern.compile(regex).matcher(str) ;
	}


}

