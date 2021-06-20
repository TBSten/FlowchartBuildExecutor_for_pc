package com.fbe.sym;

import java.util.regex.Matcher;

import com.fbe.FBEApp;

import jp.hishidama.eval.ExpRuleFactory;
import jp.hishidama.eval.Rule;

public class ArrayTemplate {
	String type = "2次元配列" ;
	String size = "10,20" ;
	/*
	 * 「10=>(1,1),(all)=>99」のように,区切りで操作を記録
	 * 操作の種類
	 * 1次元配列
	 * Num=>(IDX)  (IDX)にNumを代入する
	 * Num=>(all)  すべての要素にNumを代入する
	 * 2次元配列
	 * Num=>(X,Y)  (X,Y)にNumを代入する
	 * Num=>(all)  すべての要素にNumを代入する
	 */
	String operations = "" ;
	public Object createArray() {
		Rule rule = ExpRuleFactory.getJavaRule() ;
	//	Expression exp = rule.parse("");

		Object ans = null ;
		if(type.equals("1次元配列")) {
			Object[] arr = new Object[Integer.parseInt(size)] ;
			String[] opes = operations.split(",");
			for(String op :opes) {
				Matcher m = FBEApp.matcher("(.+)=>\\((.+)\\)", op);
				if(m.find()) {
					String m1 = m.group(1);
					String m2 = m.group(2);
					if("all".equals(m.group(2))) {
						//全要素にm.group(1)を代入
						for(int i = 0;i < arr.length;i++) {
							arr[i] = rule.parse(m1).eval() ;
						}
					}else {
						//m.group(2)にm.group(1)を代入
						arr[Integer.parseInt(m2)] = rule.parse(m1).eval() ;
					}
				}else {
					//エラー
				}
			}
			ans = arr ;
		}else if(type.equals("2次元配列")) {
			String[] sizes = size.split(",");
			Object[][] arr = new Object[Integer.parseInt(sizes[0])][Integer.parseInt(sizes[0])] ;
			String[] opes = operations.split(",");
			for(String op :opes) {
				Matcher m = FBEApp.matcher("(.+)=>\\((.+)\\)", op);
				if(m.find()) {
					String m1 = m.group(1);
					String m2 = m.group(2);
					if("all".equals(m.group(2))) {
						//全要素にm.group(1)を代入
						for(int i = 0;i < arr.length;i++) {
							for(int j = 0;j < arr[i].length;j++) {
								arr[i][j] = rule.parse(m1).eval() ;
							}
						}
					}else {
						String[] work = m2.split(",");
						//m.group(2)にm.group(1)を代入
						arr[Integer.parseInt(work[0])][Integer.parseInt(work[1])] = rule.parse(m1).eval() ;
					}
				}else {
					//エラー
				}
			}
			ans = arr ;
		}else {
			//エラー
			ans = null ;
		}
		return ans ;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getOperations() {
		return operations;
	}
	public void setOperations(String operations) {
		this.operations = operations;
	}
	public void addOperations(String operations) {
		this.operations += operations ;
	}


}
