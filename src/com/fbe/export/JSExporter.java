package com.fbe.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fbe.item.Flow;
import com.fbe.item.Item;
import com.fbe.sym.BranchSym;
import com.fbe.sym.CalcSym;
import com.fbe.sym.DataSym;
import com.fbe.sym.ForSym;
import com.fbe.sym.PrepareSym;
import com.fbe.sym.ProcessSym;
import com.fbe.sym.TerminalSym;
import com.fbe.sym.WhileSym;

public class JSExporter extends FBEExporter{

	//FBEでの変数名,JSでの変数名
	Map<String ,String> variableName ;
	BufferedWriter bw ;
	int indent ;
	protected void init() {
		variableName = new LinkedHashMap<>() ;
		indent = 0 ;
	}

	@Override
	public void export(Flow mainFlow, List<Flow> flows, File file) throws Exception {
		synchronized(bw) {
			init() ;
			bw = new BufferedWriter(new FileWriter(file)) ;

			//flowsなどを変換

			bw.flush();
			bw.close() ;
			bw = null ;
		}
	}

	protected void addLine(String str) throws Exception{
		String line = "\t".repeat(indent)+str ;
		bw.write(line);
		bw.newLine();
	}
	protected void writeItem(Item item ) {
		/*
		Terminal	1
		Calc		2
		Data		3
		Branch		1
		For			2
		While		3
		Prepare		4
		Process		5
		Flow		6.
		*/
		if(item instanceof TerminalSym) {

		}else if(item instanceof CalcSym) {

		}else if(item instanceof DataSym) {

		}else if(item instanceof BranchSym) {

		}else if(item instanceof ForSym) {

		}else if(item instanceof WhileSym) {

		}else if(item instanceof PrepareSym) {

		}else if(item instanceof ProcessSym) {

		}else if(item instanceof Flow) {

		}


	}
}
