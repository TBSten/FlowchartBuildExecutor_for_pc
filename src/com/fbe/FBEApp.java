package com.fbe;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fbe.item.Flow;

import javafx.application.Application;
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
//	public static String fbePath = "" ;

	/**
	 * FBEアプリケーションを起動します。
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		/**
	      * アプリケーションを起動するAPIの引数に
	      * プロジェクト作成時に自動作成されたMain.classを設定
	      */

	    Application.launch(FBEWindow.class);
/*
	    System.out.println("test1");
	    invoke(()->{
	    	System.out.println("start");
	    	sleep(5000);
	    	System.out.println("end");
	    });
	    System.out.println("test2");
*/
/*
	    FBERunnable run = new FBERunnable(()->{
	    	System.out.println("test1");
	    	sleep(5000);
	    	System.out.println("test2");
	    });
	    run.setName("Thread1");
	    invoke(run);
	    System.out.println("test3");
*/
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

}
/*test comment*/

