package com.fbe.format;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fbe.FBEApp;
import com.fbe.item.Flow;
import com.fbe.item.Item;
import com.fbe.sym.BranchSym;
import com.fbe.sym.CalcSym;
import com.fbe.sym.DataSym;
import com.fbe.sym.ForSym;
import com.fbe.sym.Sym;
import com.fbe.sym.TerminalSym;
import com.fbe.sym.WhileSym;

public class FBEFormat1_0 extends FBEFormat {

	public FBEFormat1_0() {
		super("1.0","fbe");
	}

	@Override public boolean save(String path,Flow mainFlow, List<Flow> flows) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.newDocument() ;
			Element root = doc.createElement("FBE");
			for(Flow flow:flows) {
				Element ele = item2Element(doc,flow) ;
				root.appendChild(ele);
			}
			doc.appendChild(root);
			writexml(new File(path),doc);
			return true ;
		}catch(Exception e) {
			e.printStackTrace();
			FBEApp.msgBox("保存エラー");
			return false;

		}
	}
	protected Element item2Element(Document doc , Item item) {
		Element ans = null ;
		if(item instanceof TerminalSym) {
			TerminalSym ter = (TerminalSym)item;
			String type = ter.optionGet("タイプ") ;
			String ret = ter.optionGet("戻り値") ;
			String text = ter.optionGet("テキスト") ;
			ans = doc.createElement("Terminal") ;
			ans.setAttribute("type", type);
			ans.setAttribute("return", ret);
			ans.setAttribute("text", text);
		}else if(item instanceof CalcSym) {
			CalcSym ter = (CalcSym)item;
			String form = ter.optionGet("式") ;
			String vari = ter.optionGet("代入先変数") ;
			ans = doc.createElement("Calc") ;
			ans.setAttribute("formula", form);
			ans.setAttribute("variable", vari);
		}else if(item instanceof DataSym) {
			DataSym sym = (DataSym)item;
			String v1 = sym.optionGet("タイプ") ;
			String v2 = sym.optionGet("対象") ;
			String v3 = sym.optionGet("条件") ;
			ans = doc.createElement("Data") ;
			ans.setAttribute("type", v1);
			ans.setAttribute("target", v2);
			ans.setAttribute("condition", v3);
		}else if(item instanceof BranchSym) {
			BranchSym sym = (BranchSym)item;
			String v1 = sym.optionGet("条件") ;
			ans = doc.createElement("Branch") ;
			ans.setAttribute("condition", v1);
			//子要素を追加
			for(Flow flow:sym.getFlowsForReference()) {
				Element child = item2Element(doc,flow);
				ans.appendChild(child);
			}
		}else if(item instanceof ForSym) {
			ForSym sym = (ForSym)item;
			String v1 = sym.optionGet("ループ変数") ;
			String v2 = sym.optionGet("初期値") ;
			String v3 = sym.optionGet("条件") ;
			String v4 = sym.optionGet("増分") ;
			ans = doc.createElement("For") ;
			ans.setAttribute("variable", v1);
			ans.setAttribute("start", v2);
			ans.setAttribute("condition", v3);
			ans.setAttribute("up", v3);
			//子要素を追加
			Element child = item2Element(doc,sym.getFlow());
			ans.appendChild(child);
		}else if(item instanceof WhileSym) {
			WhileSym sym = (WhileSym)item;
			String v1 = sym.optionGet("条件") ;
			String v2 = sym.optionGet("タイプ") ;
			ans = doc.createElement("While") ;
			ans.setAttribute("condition", v1);
			ans.setAttribute("type", v2);
			//子要素を追加
			Element child = item2Element(doc,sym.getFlow());
			ans.appendChild(child);
		}else if(item instanceof Flow) {
			Flow flow = (Flow)item;
			String v1 = flow.getTag() ;
			ans = doc.createElement("For") ;
			ans.setAttribute("tag", v1);
			//子要素を追加
			for(Sym sym:flow.getSyms()) {
				Element child = item2Element(doc,sym);
				ans.appendChild(child);
			}
		}else {
			return null ;
		}
		//PrepareSym,ProcessSymも追加予定#############################
		return ans ;
	}
	protected boolean writexml(File file, Document doc) {
        Transformer tf = null;
        try {
             TransformerFactory factory = TransformerFactory
                       .newInstance();
             tf = factory.newTransformer();
        } catch (TransformerConfigurationException e) {
             e.printStackTrace();
             return false;
        }
        tf.setOutputProperty("indent", "yes");
        tf.setOutputProperty("encoding", "UTF-8");
        try {
             tf.transform(new DOMSource(doc), new StreamResult(file));
        } catch (TransformerException e) {
             e.printStackTrace();
             return false;
        }
        return true;
   }

	@Override public List<Flow> importFrom(URL url) throws Exception {
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = factory.newDocumentBuilder();
		 Document document = builder.parse(url.getFile());
		 NodeList flowList = document.getChildNodes();
		 //flowListをList<Flow>に
		 List<Flow> ans = new ArrayList<>();
		 for(int i = 0;i < flowList.getLength();i++) {
			 if(flowList.item(i) instanceof Element) {
				 Flow flow = (Flow)element2Item((Element)flowList.item(i)) ;
				 ans.add(flow);
			 }
		 }
		return ans ;
	}
	protected Item element2Item(Element ele){
		Item ans = null ;
		if(ele.getTagName().equals("Terminal")) {
			TerminalSym ter = new TerminalSym();
			ter.optionPut("タイプ", ele.getAttribute("type"));
			ter.optionPut("戻り値", ele.getAttribute("return"));
			ter.optionPut("テキスト", ele.getAttribute("text"));
			ans = ter ;
		}else if(ele.getTagName().equals(#TEMPLATE#)){
			Sym ter = new Sym();
			ter.optionPut(#オプション#, ele.getAttribute(#オプションのFBE形式属性#));
			ans = ter ;
		}else {
			return null ;
		}
		//################################################そのほかのSymもここで追加
		return ans ;
	}

}
