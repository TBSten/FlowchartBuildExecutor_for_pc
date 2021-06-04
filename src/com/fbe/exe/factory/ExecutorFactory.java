package com.fbe.exe.factory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fbe.exe.FBEExecutor;
import com.fbe.item.Flow;
import com.fbe.option.Inputable;
import com.fbe.option.OptionTable;

import javafx.scene.control.Button;

public abstract class ExecutorFactory<E extends FBEExecutor> extends Button {
	public static final List<ExecutorFactory<?>> factorys = new ArrayList<>();
	static {
		factorys.add(new BaseExecutorFactory());
	}


	Map<String,String> options = new LinkedHashMap<>();
	Map<String,OptionTable.Type> optionTypes = new LinkedHashMap<>();
	Map<String,String> optionDescriptions = new LinkedHashMap<>();
	Map<String,List<String>> optionValueLists = new LinkedHashMap<>();

	protected String description = "" ;
	public ExecutorFactory(String text) {
		setText(text);
		setPrefSize(150,150);
		setMaxSize(150,150);
		setMinSize(150,150);
		optionPut("実行タイプ","フローチャートを1つずつ実行するか、全て一気に実行するかを指定します。",OptionTable.Type.COMBOBOX,"1つずつ実行");
		optionValueLists.get("実行タイプ").add("1つずつ実行");
		optionValueLists.get("実行タイプ").add("全て実行する");

		toUnSelectMode();
	}
	public void optionPut(String name,String desc,OptionTable.Type type,String defValue) {
		this.options.put(name, defValue);
		this.optionDescriptions.put(name, desc);
		this.optionTypes.put(name, type);
		if(type.haveList) {
			this.optionValueLists.put(name, new ArrayList<>());
		}
	}
	public void optionPut(String name,String value) {
		this.options.put(name, value);
	}
	public abstract FBEExecutor createExecutor(Flow mainFlow,List<Flow> flows) ;
	public void toSelectMode() {
		this.setStyle("-fx-background-color:white;-fx-border-color:blue;-fx-border-width:3px;-fx-border-radius:5px;");
	}
	public void toUnSelectMode() {
		this.setStyle("-fx-background-color:white;-fx-border-color:black;-fx-border-width:3px;-fx-border-radius:5px;");
	}
	public String getDescription() {
		return description;
	}
	public OptionTable createOptionTable() {
		OptionTable ans = new OptionTable();
		for(Map.Entry<String, String> ent:options.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			String desc = this.optionDescriptions.get(name);
			OptionTable.Type type = this.optionTypes.get(name);
			List<String> list = this.optionValueLists.get(name) ;

			Inputable inputable = ans.put(name,desc,type,value);
			if(type.haveList && this.optionValueLists.containsKey(name)) {
				for(String item:this.optionValueLists.get(name)) {
					inputable.args(item);
				}
			}
		}
		return ans ;
	}

	/**
	 * tableの値をこのインスタンスに反映します。
	 */
	public void reflectOptionTable(OptionTable table) {
		Map<String,Inputable> nodes = table.getInputNodes() ;
		for(Map.Entry<String, Inputable> ent: nodes.entrySet()) {
			this.optionPut(ent.getKey(),ent.getValue().getString());
		}
	}
}
