package com.fbe;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

import com.fbe.format.FBEFormatApp;
import com.fbe.item.Flow;
import com.fbe.sym.Sym;
import com.fbe.sym.TerminalSym;

import javafx.application.Platform;

public class SaveApp {
	public static void init() {
		try {
			//settings.properties
			if(new File("./settings.properties").exists()) {
				//入力フェーズ
				Properties pro = new Properties() ;
				pro.load(new FileReader("./settings.properties"));
				String lastUpdatePath = pro.getProperty("lastUpdatePath");
				//反映フェーズ
				FBEFormatApp.lastUpdatePath = lastUpdatePath ;
			}else {
				FBEApp.msgBox("設定ファイルが見つかりません。");
			}
			Platform.runLater(()->{
				try {
					String lastUpdatePath = FBEFormatApp.lastUpdatePath ;
					//creating.fbe
					List<Flow> flows = FBEFormatApp.defaultFormat.importFrom("./creating.fbe");
					for(Flow flow:flows) {
						FBEApp.app.addFlow(flow);
					}
					FBEFormatApp.lastUpdatePath = lastUpdatePath ;
				}catch(Exception exc) {
					exc.printStackTrace();
					FBEApp.msgBox("前回の作成データが読み取れませんでした。新たに作成します。");
					Flow f = new Flow() ;
					FBEApp.app.addFlow(f);
					Sym[] syms = {
							new TerminalSym(TerminalSym.Type.START),
							new TerminalSym(TerminalSym.Type.END)
					};
					for(int i = 0;i < syms.length;i++) {
						f.addSym(i, syms[i]);
					}
				}
			});
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void shutdown() {
		try {
			//settings.properties
			Properties pro = new Properties() ;
			pro.setProperty("lastUpdatePath", FBEFormatApp.lastUpdatePath);
			pro.store(new FileWriter("./settings.properties"),"FBE設定ファイル");
			//creating.fbe
			FBEFormatApp.defaultFormat.save("./creating.fbe", FBEApp.app.flows.get(0), FBEApp.app.flows);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
