package test;

import com.fbe.export.JSExporter;

public class TestExport {

	public static void main(String[] args) {
		JSExporter ex = new JSExporter() ;
		String[] fs = {
				"avg+sum+3",
				"合計",
				"件数+v1+3",
				"ああ+いい-うう*ええ/おお%かか^きき+(くくああ+3)",
				"avg+3+合計",
				"$30*d3",
				"var_1+ああ+いい",
				"10 = 3",
				"20 >= a",
				"bcd < 30",
				"ふつう または いいね",
				"ふつう かつ いいね",
				"ふつう = いいね"
			} ;
		for(String f :fs) {
			ex.init();
			System.out.println(f+" ==>> "+ex.toValidFormula(f)) ;
/*			for(Map.Entry<String, String> ent :ex.variableName.entrySet()) {
				System.out.printf("%20s %20s  \n",ent.getKey(),ent.getValue()) ;
			}*/
			System.out.println("======================================================");
		}
/*
		System.out.println("/".matches("/"));	// true
		System.out.println("/".matches("\\/"));	// true
		System.out.println(Arrays.toString( "(a+b)-c*d/e%f^g".split("[\\+,\\-,\\*,/,%,^,\\(,\\)]") ));
*/
	}

}
