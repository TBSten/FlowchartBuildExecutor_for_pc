package com.fbe.sym;

import com.fbe.exe.FBEExecutor;
import com.fbe.item.RoundFlow;

public class DoubleBranchSym extends BranchSym {

	public RoundFlow yesFlow = new RoundFlow() ;
	public RoundFlow noFlow = new RoundFlow() ;

	public DoubleBranchSym(String condition) {
		super(condition);
		this.addFlow(yesFlow);
		this.addFlow(noFlow);
		yesFlow.setTag("Yes");
		noFlow.setTag("No");
		this.getMenu().getItems().remove(this.getMenu().getItems().get(3));
	}

	@Override
	public RoundFlow decideFlow(FBEExecutor exe) {
		boolean con = (boolean) exe.eval(this.optionGet("条件")) ;
		if(con) {
			return yesFlow ;
		}else {
			return noFlow ;
		}
	}

	@Override
	public void addFlow(RoundFlow flow) {
		super.addFlow(flow);
		flow.setAbleToDisable(false);
		flow.setOnDisabled(null);
	}



}
