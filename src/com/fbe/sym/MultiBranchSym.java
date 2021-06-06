package com.fbe.sym;

import java.util.HashMap;
import java.util.Map;

import com.fbe.exe.FBEExecutor;
import com.fbe.item.RoundFlow;

import javafx.scene.control.MenuItem;

public class MultiBranchSym extends BranchSym {

	public MultiBranchSym(String condition) {
		super(condition);
		MenuItem mi = new MenuItem("\"その他\"を追加") ;
		mi.setOnAction(e->{
			RoundFlow flow = new RoundFlow() ;
			this.addDefaultFlow(flow);
		});
		menu.getItems().add(mi);
/*
		RoundFlow rf = new RoundFlow() ;
		rf.setTag("1");
		this.addDefaultFlow(rf);
*/
	}

	@Override
	public RoundFlow decideFlow(FBEExecutor exe) {
		String con = this.optionGet("条件") ;
		Map<String,RoundFlow> map = new HashMap<>() ;
		for(RoundFlow f:this.getFlowsForReference()) {
			map.put(f.getTag(), f);
		}
		for(Map.Entry<String, RoundFlow> ent:map.entrySet()) {
			if(!ent.getKey().equals("その他") && (boolean)exe.eval(ent.getKey()+" = "+con)) {
				return ent.getValue() ;
			}
		}
		return map.get("その他") ;
	}

	public void addDefaultFlow(RoundFlow flow) {
		flow.setTag("その他");
		super.addFlow(flow);
	}


}
