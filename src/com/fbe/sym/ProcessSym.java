package com.fbe.sym;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fbe.FBEApp;
import com.fbe.exe.FBEExecutable;
import com.fbe.exe.FBEExecutor;
import com.fbe.item.Flow;
import com.fbe.option.OptionTable;

import javafx.scene.canvas.GraphicsContext;

public class ProcessSym extends Sym {

	public static List<Flow> defaultFunctions = new ArrayList<>();
	static {
		//sleepF
		Flow sleepF = new Flow() ;
		TerminalSym sleepTS = new TerminalSym() {
			@Override public void execute(FBEExecutor exe) {
				super.execute(exe);
				System.out.println("sleep :"+exe.getVar("_sleeptime"));
				FBEApp.sleep((long)Double.parseDouble(String.valueOf(exe.getVar("_sleeptime"))));
			}
		} ;
		sleepTS.optionPut("タイプ", "はじめ");
		sleepTS.optionPut("テキスト", "sleep(_sleeptime)");
		sleepF.addSym(sleepTS);
		defaultFunctions.add(sleepF);


	}

	public ProcessSym(String processName) {
		super();
		this.optionPut("処理名", "実行する定義済み処理を指定します。処理名の後ろに (引数1 , 引数2 , ...) と指定すると、引数も指定できます。", OptionTable.Type.TEXTFIELD, processName);
	}

	@Override
	public void execute(FBEExecutor exe) {
		List<FBEExecutable> exeList = exe.getExecuteList() ;
		String name = this.optionGet("処理名");
		String[] args = {} ;
		Pattern p = Pattern.compile("(.*)\\((.*)\\)");
		Matcher m = p.matcher(name);
		if(m.matches()) {
			name = m.group(1);
			args = m.group(2).split(",");
		}
		boolean flg = false ;
		final String[] ARGS = args ;
		List<Flow> flows = FBEApp.app.flows ;
		flows.addAll(ProcessSym.defaultFunctions);
		for(Flow flow : flows) {
			if(flow.getSyms().get(0) instanceof TerminalSym) {
				TerminalSym first = (TerminalSym) flow.getSyms().get(0) ;
				System.out.println(first+">>"+first.getProcessName()+"("+Arrays.toString(first.getArgNames())+")");
				if(name.equals(first.getProcessName())) {
					List<FBEExecutable> list = new ArrayList<>() ;
					String[] argNames = first.getArgNames() ;
					for(int i = 0;i < argNames.length;i++) {
						exe.putVar(argNames[i].replace(" ", ""), exe.eval(ARGS[i]));
					//	System.out.println(argNames[i].replace(" ", "")+" :: "+exe.eval(ARGS[i]));
					}
					list.addAll(flow.getSyms());
					exe.getExecuteList().addAll(exeList.indexOf(this)+1 , list);
					System.out.println(first.getProcessName()+"を実行");
					flg = true ;
					break ;
				}
			}
		}
		if(!flg) {
			System.out.println("ProcessSymで処理が見つからなかった");
		}

	}

	@Override public void reflectOption() {
		if(this.getOptions() != null) {
			String name = optionGet("処理名");
			this.setText(name);
		}
	}

	@Override
	public void draw() {
		GraphicsContext gc = this.symCanvas.getGraphicsContext2D() ;
		gc.setFill(itemFillColor);
		gc.setStroke(itemLineColor);
		gc.setLineWidth(itemLineWidth);

		gc.fillRect(0,0, getWidth(), getHeight());
		gc.strokeRect(0 + itemLineWidth/2, 0 + itemLineWidth/2, getWidth() - itemLineWidth, getHeight() - itemLineWidth);

		gc.strokeLine(getWidth()*0.05, 0, getWidth()*0.05, getHeight());
		gc.strokeLine(getWidth()*0.95, 0, getWidth()*0.95, getHeight());
	}

}
