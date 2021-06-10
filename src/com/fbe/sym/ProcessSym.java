package com.fbe.sym;

import java.util.ArrayList;
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
		final String[] ARGS = args ;
		for(Flow flow : FBEApp.app.flows) {
			if(flow.getSyms().get(0) instanceof TerminalSym) {
				TerminalSym first = (TerminalSym) flow.getSyms().get(0) ;
				if(name.equals(first.getProcessName())) {
					List<FBEExecutable> list = new ArrayList<>() ;
/*
					list.add(ex->{
						String[] argNames = first.getArgNames() ;
						for(int i = 0;i < argNames.length;i++) {

							ex.putVar(argNames[i], ex.eval(ARGS[i]));
						}
					});

*/
					String[] argNames = first.getArgNames() ;
					for(int i = 0;i < argNames.length;i++) {
						exe.putVar(argNames[i].replace(" ", ""), exe.eval(ARGS[i]));
						System.out.println(argNames[i].replace(" ", "")+" :: "+exe.eval(ARGS[i]));
					}
					list.addAll(flow.getSyms());
					exe.getExecuteList().addAll(exeList.indexOf(this)+1 , list);
					System.out.println(first.getProcessName()+"を実行");
					break ;
				}
			}
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
