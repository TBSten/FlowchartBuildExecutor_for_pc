package com.fbe.sym;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fbe.exe.FBEExecutor;
import com.fbe.option.OptionTable;

import javafx.scene.canvas.GraphicsContext;

public class TerminalSym extends Sym {

	public enum Type{
		START("はじめ"),
		END("おわり");
		String txt = "???";
		Type(String txt){
			this.txt = txt ;
		}
	}

	protected Type type ;
	public TerminalSym(Type t) {
		this.type = t ;
		this.setMovable(false);
		this.setDeletable(false);

		if(this.type == Type.END) {
			this.getMenu().getItems().remove(0);	//削除できない
		}


//		options.put("タイプ", t == Type.START ? "はじめ" :"おわり");
//		optionsValueList.put("タイプ",Arrays.asList("はじめ","おわり"));
//		options.put("テキスト", "");
//		options.put("戻り値", "");
		optionPut("タイプ","はじめまたはおわりを指定します。",OptionTable.Type.COMBOBOX,t == Type.START ? "はじめ" :"おわり");
		getOptionsValueList().put("タイプ",Arrays.asList("はじめ","おわり"));
		optionPut("テキスト","タイプがはじめの場合に表示されるテキストを指定します。",OptionTable.Type.TEXTFIELD,"");
		optionPut("戻り値","タイプが終わりの場合に返す値を式で指定します。",OptionTable.Type.TEXTFIELD,"");




		redraw();
	}
	public TerminalSym() {
		this(Type.START);
	}
	@Override
	public void execute(FBEExecutor exe) {
		//type == Type.ENDの時は戻り値を返す
		if(this.type == Type.END && this.optionGet("戻り値").equals("")) {
			exe.putVar(this.optionGet("テキスト")+"::RETURN", exe.eval(this.optionGet("戻り値")));
		}
	}

	@Override public void reflectOption() {
		String type = optionGet("タイプ");


		if("はじめ".equals(type)) {
			this.type = Type.START ;
		}else if("おわり".equals(type)) {
			this.type = Type.END ;
		}else {
			System.out.println("不正なオプション: タイプ="+type);
			this.type = Type.START ;
			optionPut("タイプ","はじめ");
		}

		if(this.type != null) {
			this.setText(this.type.txt);
		}

		if(!this.optionGet("テキスト").matches("\\s*")) {
			this.setText(this.optionGet("テキスト"));
		}

		if(this.optionGet("タイプ").equals("おわり") && !this.optionGet("戻り値").matches("\\s*")) {
			this.setText(this.optionGet("戻り値")+" を返す");
		}


	}

	@Override
	public void draw() {

		GraphicsContext gc = symCanvas.getGraphicsContext2D();
		gc.setStroke(itemLineColor);
		gc.setFill(itemFillColor);
		gc.setLineWidth(itemLineWidth);
		gc.fillRoundRect(0+itemLineWidth/2, 0+itemLineWidth/2, getWidth()-itemLineWidth, getHeight()-itemLineWidth, getHeight(), getHeight());
		gc.strokeRoundRect(0+itemLineWidth/2, 0+itemLineWidth/2, getWidth()-itemLineWidth, getHeight()-itemLineWidth, getHeight(), getHeight());
	}

	public String getProcessName() {
		String ans = this.optionGet("テキスト");
		Pattern p = Pattern.compile("(.*)\\((.*)\\)");
		Matcher m = p.matcher(ans);
		if(m.matches()) {
			ans = m.group(1);
		}
		return ans ;

	}
	public String[] getArgNames() {
		String[] ans = {} ;
		Pattern p = Pattern.compile("(.*)\\((.*)\\)");
		Matcher m = p.matcher(this.optionGet("テキスト"));
		if(m.matches()) {
			ans = m.group(2).split(",");
		}
		return ans ;

	}

}
