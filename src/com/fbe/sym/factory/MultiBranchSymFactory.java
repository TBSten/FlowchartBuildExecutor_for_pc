package com.fbe.sym.factory;

import com.fbe.item.RoundFlow;
import com.fbe.sym.MultiBranchSym;

public class MultiBranchSymFactory extends SymFactory<MultiBranchSym> {

	public MultiBranchSymFactory() {
		super(null);
		MultiBranchSym sym = new MultiBranchSym("変数") ;
		for(int i = 1;i <= 3;i++) {
			RoundFlow flow = new RoundFlow();
			flow.setTag(i+"");
			sym.addFlow(flow);
		}
		init(sym);
		this.setText("分岐２");
		this.setOpenSetting(true);

	}

	@Override
	public MultiBranchSym createSym() {
		MultiBranchSym ans = new MultiBranchSym("変数");
		for(int i = 1;i <= 2;i++) {
			RoundFlow rf = new RoundFlow() ;
			rf.setTag(""+i);
			ans.addFlow(rf);
		}
		ans.addDefaultFlow(new RoundFlow());
		return ans;
	}

}
