package test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestXml {

	public static void main(String[] args) throws Exception{
		new TestXml();
	}
	TestXml() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = factory.newDocumentBuilder();
		 Document document = builder.parse(this.getClass().getResourceAsStream("test.xml"));
		 Element root = document.getDocumentElement();
		 //flowListをList<Flow>に
		 List<Object> ans = new ArrayList<>();
		 for(int i = 0;i < root.getChildNodes().getLength();i++) {
			 if(root.getChildNodes().item(i) instanceof Element){
				 Element str = (Element)root.getChildNodes().item(i);
				 System.out.println(str+"-"+str.getAttribute("tag"));
			 }
		 }
	}

}
