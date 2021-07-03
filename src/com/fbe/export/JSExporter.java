package com.fbe.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.fbe.FBEApp;
import com.fbe.exe.FBEExecutor;
import com.fbe.exe.SettingController;
import com.fbe.exe.factory.ExecutorFactory;
import com.fbe.exe.factory.LoggerExecutorFactory;
import com.fbe.exe.factory.MsgBoxExecutorFactory;
import com.fbe.exe.factory.TableExecutorFactory;
import com.fbe.item.Flow;
import com.fbe.item.Item;
import com.fbe.item.RoundFlow;
import com.fbe.sym.CalcSym;
import com.fbe.sym.DataSym;
import com.fbe.sym.DoubleBranchSym;
import com.fbe.sym.ForSym;
import com.fbe.sym.MultiBranchSym;
import com.fbe.sym.PrepareSym;
import com.fbe.sym.ProcessSym;
import com.fbe.sym.Sym;
import com.fbe.sym.TerminalSym;
import com.fbe.sym.WhileSym;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class JSExporter extends FBEExporter{

	//FBEでの変数名,JSでの変数名
	public Map<String ,String> variableName ;
	public Map<String ,String> functionName ;
	BufferedWriter bw ;
	int indent ;

	int unvalidVariableCnt ;
	int unvalidFunctionCnt ;

	Flow mainFlow ;
	List<Flow> flows ;
	File file ;

//	protected void init() {
	public void init() {
		variableName = new LinkedHashMap<>() ;
		functionName = new LinkedHashMap<>() ;
		indent = 0 ;
		unvalidVariableCnt = 0 ;
	}

	@Override
	//fileは出力先ディレクトリ
	public void export(Flow mainFlow, List<Flow> flows, File file) throws Exception {
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
		ExecutorFactory<?> fact = cont.fact ;

		File outputF = new File(file.getAbsolutePath()+File.separator+"main.js") ;
		bw = new BufferedWriter(new FileWriter(outputF)) ;
		synchronized(bw) {
			init() ;
			this.mainFlow = mainFlow ;
			this.flows = flows ;
			this.file = file ;

			String outputDirPath = file.getAbsolutePath()+File.separator ;
			//index.htmlを出力
			Files.copy(Paths.get("./export/js/index.html"), Paths.get(outputDirPath+"index.html") ,StandardCopyOption.REPLACE_EXISTING);
			//style.cssを出力
			Files.copy(Paths.get("./export/js/style.css"), Paths.get(outputDirPath+"style.css") ,StandardCopyOption.REPLACE_EXISTING);
			//lib.jsを出力
			BufferedWriter libBw = new BufferedWriter(new FileWriter(outputDirPath+"lib.js")) ;
			BufferedReader libBr = new BufferedReader(new FileReader("export/js/libBase.js")) ;
			String line ;
			while((line = libBr.readLine()) != null) {
				//「//##startPoint##」を置き換え
				if(line.matches(".*//##startPoint##.*")) {
					String type = "msgBox" ;
					if(fact instanceof MsgBoxExecutorFactory) {
						type = "msgBox" ;
					}else if(fact instanceof TableExecutorFactory) {
						type = "table" ;
					}else if(fact instanceof LoggerExecutorFactory) {
						type = "log" ;
					}else {
						type = "unknown" ;
					}
					line = line.replace("//##startPoint##", String.format("const exeType = \"%s\";",type));
				}
				libBw.write(line);
				libBw.newLine();
			}
			libBw.flush();
			libBw.close();
			libBr.close();
			//main.jsを出力
			for(Flow f:flows) {
				if(f != mainFlow) {
					this.writeItem(f);
				}
			}
			this.addLine("");
			this.addLine("");
			this.writeItem(mainFlow);
			this.addLine("");

			bw.flush();
			bw.close() ;
			bw = null ;
		}
	}

	protected void addLine(String str) throws Exception{
		String line = "\t".repeat(indent)+str ;
		bw.write(line);
		bw.newLine();
//		System.out.println("addLine=========================");
//		System.out.println(line);
//		System.out.println("================================");
	}
	protected void writeItem(Item item ) throws Exception{
		/*
		Terminal	//おわりで戻り値ありならreturn
		Calc		//変数登録
		Data		//対象 = input(); など...
		Branch		//if(条件式){[yesFlow]}else{[noFlow]}
		For			//
		While		//
		Prepare		//
		Process		//
		Flow		//
		*/
		if(item instanceof TerminalSym) {
			TerminalSym sym = (TerminalSym) item ;
			if(sym.optionGet("タイプ").equals("おわり")) {
				if(sym.optionGet("戻り値").matches("\\s*")) {
					this.addLine("return ;");
				}else {
					this.addLine(String.format("return %s ;", sym.optionGet("戻り値")));
				}
			}
		}else if(item instanceof CalcSym) {
			CalcSym sym = (CalcSym) item ;
			String valiVar = this.toValidVarName(sym.optionGet("代入先変数")) ;
			String valiFor = this.toValidVarName(sym.optionGet("式")) ;
			this.addLine(this.letExpression(valiVar, valiFor));
		}else if(item instanceof DataSym) {
			DataSym sym = (DataSym) item ;
			if(sym.optionGet("タイプ").equals("キーボード入力")) {
				String valiVar = this.toValidVarName(sym.optionGet("対象")) ;
				this.addLine(this.letExpression(valiVar, "input() ;"));
			}else if(sym.optionGet("タイプ").equals("出力")) {
				System.out.println(" :: output :"+sym.optionGet("対象"));
				String valiVar = this.toValidFormula(sym.optionGet("対象")) ;
				System.out.println("    output :"+valiVar);
				this.addLine(String.format("output(%s) ;", valiVar));
			}else {
				throw new FBEExportException("データ記号のタイプに対応していません："+sym.optionGet("タイプ")) ;
			}
		}else if(item instanceof DoubleBranchSym) {
			DoubleBranchSym sym = (DoubleBranchSym) item ;
			//yesFlow,noFlowを書き込む
			this.addLine(String.format("if( %s ){", this.toValidFormula(sym.optionGet("条件"))));
			this.indent++;
			this.writeItem(sym.yesFlow);
			this.indent--;
			this.addLine("}else{");
			this.indent++;
			this.writeItem(sym.noFlow);
			this.indent--;
			this.addLine("}");
		}else if(item instanceof MultiBranchSym) {
			MultiBranchSym sym = (MultiBranchSym) item ;
			List<RoundFlow> flows = sym.getFlowsForReference() ;
			//yesFlow,noFlowを書き込む
			this.addLine(String.format("switch( %s ){", this.toValidFormula(sym.optionGet("条件"))));
			for(RoundFlow flow :flows) {
				this.indent ++ ;
				if("その他".equals(flow.getTag())) {
					this.addLine("default :");
				}else {
					this.addLine(String.format("case %s :", toValidFormula(flow.getTag())));
				}
				this.indent ++;
				this.writeItem(flow);
				this.addLine("break ;");
				this.indent -- ;
				this.indent -- ;
			}
		}else if(item instanceof ForSym) {
			ForSym sym = (ForSym) item ;
			RoundFlow flow = sym.getFlow() ;
			String lv = sym.optionGet("ループ変数") ;
			String st = sym.optionGet("初期値") ;
			String co = sym.optionGet("条件") ;
			String up = sym.optionGet("増分") ;
			this.addLine(String.format("for(let %s = %s ; %s ; %s += %s){", toValidFormula(lv), toValidFormula(st), toValidFormula(co), toValidFormula(lv), toValidFormula(up)));
			this.indent ++;
			this.writeItem(flow);
			this.indent --;
			this.addLine("}");
		}else if(item instanceof WhileSym) {
			WhileSym sym = (WhileSym) item ;
			RoundFlow flow = sym.getFlow() ;
			String co = sym.optionGet("条件") ;
			this.addLine(String.format("while( %s ){", toValidFormula(co)));
			this.indent ++;
			this.writeItem(flow);
			this.indent --;
			this.addLine("}");
		}else if(item instanceof PrepareSym) {
			PrepareSym sym = (PrepareSym)item ;
			String[] lens = sym.optionGet("要素数").split(",");
			String tar = sym.optionGet("対象");
			String ini = sym.optionGet("初期値");
			if("1次元配列".equals(sym.optionGet("タイプ"))) {
				String arr = this.repeatSplit(ini, Integer.parseInt(lens[0]), " , ") ;
				this.addLine(String.format("var %s = [ %s ];", toValidFormula(tar),arr));
			}else if("2次元配列".equals(sym.optionGet("タイプ"))){
				//var arr = [
				this.addLine(String.format("var %s = [", toValidFormula(tar)));
				//[/*rowN*/],
				String row = "[ "+this.repeatSplit(ini, Integer.parseInt(lens[0]), " , ")+" ]" ;
				this.indent ++;
				for(int i = 0;i < Integer.parseInt(lens[1]);i++) {
					if(Integer.parseInt(lens[1]) != i+1) {
						this.addLine(row+" , ");
					}else {
						this.addLine(row);
					}
				}
				//    ];
				this.indent --;
				this.indent ++;
				this.addLine("];");
				this.indent --;
			}else if("3次元配列".equals(sym.optionGet("タイプ"))) {
				//var arr = [
				this.addLine(String.format("var %s = [", toValidFormula(tar)));
				//[/*rowN*/],
				String row = "[ "+this.repeatSplit(ini, Integer.parseInt(lens[0]), ",")+" ]" ;
				row = this.repeatSplit(row, Integer.parseInt(lens[1]), " , ");
				this.indent ++;
				for(int i = 0;i < Integer.parseInt(lens[2]);i++) {
					if(Integer.parseInt(lens[1]) != i+1) {
						this.addLine(row+" , ");
					}else {
						this.addLine(row);
					}
				}
				//    ];
				this.indent --;
				this.indent ++;
				this.addLine("];");
				this.indent --;
			}else {
				throw new FBEExportException("準備記号のタイプに対応していません："+sym.optionGet("タイプ")) ;
			}
		}else if(item instanceof ProcessSym) {
			ProcessSym sym = (ProcessSym)item ;
			String name = sym.getProcessName() ;
			String args = "" ;
			String valiName = toValidFuncName(name) ;
			Matcher m = FBEApp.matcher("(.*)\\((.*)\\)",sym.optionGet("処理名"));
			if(m.matches()) {
				args = m.group(2) ;
			}
			this.addLine(String.format("await %s(%s);", valiName,args));
		}else if(item instanceof Flow) {
			Flow fl = (Flow)item ;
			//flowに含まれるならfunction ***(){
			Sym s = fl.getSyms().get(0) ;
			if(fl == this.mainFlow){
				this.addLine("async function main() {");
				this.indent++;
			}else  if(this.flows.contains(fl) && s instanceof TerminalSym) {
				TerminalSym ter = (TerminalSym)s ;
				this.addLine(String.format("function %s() {", toValidFuncName(ter.getProcessName())));
				this.indent++;
			}
			//Symsを書き出す
			for(Sym child:fl.getSyms()){
				this.writeItem(child);
			}
			//flowに含まれるなら}
			if(fl == this.mainFlow || this.flows.contains(fl) && s instanceof TerminalSym) {
				this.indent--;
				this.addLine("}");
			}
		}
	}
	protected String repeatSplit(String str,int cnt , String bw) {
		StringBuffer sb = new StringBuffer(str);
		for(int i = 0;i < cnt-1;i++) {
			sb.append(bw+str);
		}
		return sb.toString() ;
	}

	protected String[] ens = {"+","-","*","/","%","^","(",")","<",">","<=",">=","=","かつ","または",","} ;
	protected String[] enChangesBase = new String[]{"かつ","または","=","!=","<>"} ;
	protected String[] enChangesTo = new String[]{" && "," || "," === "," !== ","!=="} ;
	public String toValidFormula(String formula) {
		formula = formula.replace(" ", "");
		Arrays.sort(ens,(a,b)->{
			return b.length() - a.length() ;
		});
		String en = "\\Q"+ens[0]+"\\E" ;
		for(String e:ens) {
			en += String.format("|\\Q%s\\E", e) ;
		}
		String[] work1 = formula.split(en);
//		System.out.println(Arrays.toString(work1));
		String[] work2 = new String[work1.length];
		for(int i = 0;i < work1.length;i++) {
			if(!work1[i].equals("")) {
				work2[i] = toValidVarName(work1[i]) ;
			}
		}
		for(int i = 0;i < work1.length;i++) {
			for(int j = i+1;j < work1.length;j++) {
				if(work1[i] != null && work2[j] != null){
					if(work1[i].length() < work1[j].length()) {
						String wo = work1[i];
						work1[i] = work1[j] ;
						work1[j] = wo ;
						wo = work2[i];
						work2[i] = work2[j] ;
						work2[j] = wo ;
					}
				}
			}
		}
		//リテラルを置き換え
		for(int i = 0;i < work1.length;i++) {
			if(!work1[i].equals("") && work2[i] != null) {
				formula = formula.replace(work1[i],work2[i]) ;
			}
		}
		//演算子を置き換え
//		work1 = new String[]{"かつ",	"または",	"=",		"!="} ;
//		work2 = new String[]{" && ",	" || ",		" === ",	" !== "} ;
		work1 = this.enChangesBase;
		work2 = this.enChangesTo;
		for(int i = 0;i < work1.length;i++) {
			for(int j = i+1;j < work1.length;j++) {
				if(work1[i] != null && work2[i] != null){
					if(work1[i].length() < work1[j].length()) {
						String wo = work1[i];
						work1[i] = work1[j] ;
						work1[j] = wo ;
						wo = work2[i];
						work2[i] = work2[j] ;
						work2[j] = wo ;
					}
				}
			}
		}
		for(int i = 0;i < work1.length;i++) {
			formula = formula.replace(work1[i], work2[i]);
		}
		return formula ;
	}
	public String toValidVarName(String name) {
		if(isValidVarName(name)) {
//			System.out.println("put ::"+name+":"+name);
			if(!name.matches("\".*\"|\\d+(\\.\\d+)?")) {
				this.variableName.put(name, name);
			}
			return name ;
		}else {
			//適切な形に直して返す
			if(this.variableName.containsKey(name)) {
				return this.variableName.get(name);
			}else {
				String head = "fbeUnvalidVariable" ;
				String ans = head+unvalidVariableCnt ;
				while(this.variableName.containsValue(ans)) {
					unvalidVariableCnt ++;
					ans = head+unvalidVariableCnt ;
				}
//				System.out.println("put ::"+name+":"+ans);
				this.variableName.put(name, ans);
				unvalidVariableCnt ++;
				return ans ;
			}
		}
	}
	public boolean isValidVarName(String name) {
//		System.out.println(name+"::"+name.matches("\".*\"|\\d*(.\\d+)?"));
		return name.matches("\\w*") || name.matches("\".*\"|\\d+(\\.\\d+)?");
	}
	public String toValidFuncName(String name) {
		if(isValidVarName(name)) {
			return name ;
		}else {
			if(this.functionName.containsKey(name)) {
				return this.functionName.get(name);
			}else {
				String head = "fbeUnvalidFunction" ;
				String ans = head+unvalidFunctionCnt ;
				while(this.functionName.containsValue(ans)) {
					unvalidFunctionCnt ++;
					ans = head+unvalidFunctionCnt ;
				}
				this.functionName.put(name, ans);
				unvalidFunctionCnt ++;
				return ans ;
			}
		}
	}

	public String letExpression(String var,String formula) {
		return String.format("%s = %s ;", var,formula) ;
	}

}
