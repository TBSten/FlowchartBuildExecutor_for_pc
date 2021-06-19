package com.fbe.exe.factory;

import java.util.Arrays;
import java.util.List;

import com.fbe.exe.TableFBEExecutor;
import com.fbe.item.Flow;
import com.fbe.option.OptionTable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TableExecutorFactory extends ExecutorFactory<TableFBEExecutor> {

	public TableExecutorFactory() {
		super("表形式");
		this.description = "データを表形式で出力します。データを出力する際に「列1の値,列2の値,...」というように「,」区切りで値を並べてください。" ;
		this.optionPut("表示する列", "表示する列のタイトルを「,」区切りで指定します。", OptionTable.Type.TEXTFIELD, "列1,列2,列3");
		this.optionPut("罫線で囲む", "各セルを罫線で囲むかを指定します。なお表示する列数に合わないデータが出力されるときはこのオプションにかかわらず罫線は表示されません。", OptionTable.Type.CHECKBOX, "true");
		this.optionPut("文字の位置", "各セルの文字のそろえ方を指定します。", OptionTable.Type.COMBOBOX, "右揃え");
		this.optionValueLists.put("文字の位置", Arrays.asList("左揃え","中央揃え","右揃え"));
		Image image = new Image(getClass().getResourceAsStream("table.png"),100,100,false,false);
		ImageView iv = new ImageView(image) ;
		this.setGraphic(iv);

	}
	@Override
	public TableFBEExecutor createExecutor(Flow mainFlow,List<Flow> flows) {
		String[] heads = this.options.get("表示する列").split(",");
		TableFBEExecutor ans = new TableFBEExecutor(mainFlow,flows,heads) ;
		ans.setIsBorder(Boolean.valueOf(this.options.get("罫線で囲む")).booleanValue());
		if(this.options.get("文字の位置").equals("左揃え")) {
			ans.setAlign("LEFT");
		}else if(this.options.get("文字の位置").equals("中央揃え")) {
			ans.setAlign("CENTER");
		}else if(this.options.get("文字の位置").equals("右揃え")){
			ans.setAlign("RIGHT");
		}
		if("1つずつ実行".equals(this.options.get("実行タイプ"))) {
			ans.setExecuteAll(false);
		}else if("すべて実行する".equals(this.options.get("実行タイプ"))){
			ans.setExecuteAll(true);
		}else {
			//エラー
		}


		return ans ;
	}


}
