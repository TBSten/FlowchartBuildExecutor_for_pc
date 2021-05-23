package test;

import java.util.ArrayList;

public class TestList {

	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<>();
		list.add("t0");
		list.add("t1");
		list.add("t2");

		list.add(3,"t3");
		for(String s:list) {
			System.out.println(s);
		}
	}

}
