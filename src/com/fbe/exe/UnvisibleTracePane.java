package com.fbe.exe;

public class UnvisibleTracePane extends VarTracePane {

	public UnvisibleTracePane(FBEExecutor exe, String name, Object value,String text) {
		super(exe, name, value);
		this.valueLb.setText("# この変数は表示できません :"+text);
	}

	public void redraw() {
	}

}
