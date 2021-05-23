package com.fbe.exe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
/*
	public static void main(String args[]) {
		String[] s = {
				"true  ",
				"false",
				"123456789",
				"12.345",
				"12.",
				".1",
				"\"abcdef\"",
				"   \"a b c\"   ",
				"abc"
		} ;
		for(String str : s) {
			Object ob = new Variable(str).parse() ;
			if(ob != null) {
				System.out.printf("%-30s -> %-30s : %s\n","<"+str+">","<"+ob+">",ob.getClass().getSimpleName());
			}else {
				System.out.printf("%-30s -> %-30s : %s\n","<"+str+">","<"+ob+">","null");
			}
		}
	}
*/

	String name ;
	/**
	 * 値を表す文字列表現
	 */
	String value ;

	public Variable(String name,String value){
		this.value = value ;
	}
	public String getAsString() {
		Pattern p = Pattern.compile("(.*)(\".*\")(.*)");
		Matcher m = p.matcher(value);

		if(m.matches()) {
			String ans = m.group(2);
			return ans ;
		}else {
			return null ;
		}
	}
	/*
	public Long getAsLong() {
		return Long.parseLong(value);
	}
	*/
	public Double getAsDouble() {
		return Double.parseDouble(value);
	}
	public Boolean getAsBoolean() {
		return Boolean.parseBoolean(value);
	}
	public Object get() {
		return parse() ;
	}
	public Object parse() {
		//Stringから適切な型に変換
		if(value != null) {
			//""内の空白は消したくない
			String work = value.replaceAll("\\s", "");
			if(work.matches("true|false")) {
				this.value = work ;
				return getAsBoolean();
			}else if(work.matches("([0-9])+")){
				this.value = work ;
			//	return getAsLong();
				return getAsDouble();
			}else if(work.matches("[0-9]*.[0-9]*")){
				this.value = work ;
				return getAsDouble();
			}else if(value.matches("\\s*\".*\"\\s*")){
				return getAsString();
			}else{
				return null ;
			}
		}else {
			return null ;
		}
	}
}
