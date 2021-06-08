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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fbe.FBEApp;
import com.fbe.item.Flow;
import com.fbe.item.Item;
import com.fbe.item.RoundFlow;
import com.fbe.sym.BranchSym;
import com.fbe.sym.CalcSym;
import com.fbe.sym.DataSym;
import com.fbe.sym.DoubleBranchSym;
import com.fbe.sym.ForSym;
import com.fbe.sym.MultiBranchSym;
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
		}catch(Exception e) {
			e.printStackTrace();
			FBEApp.msgBox("保存エラー");
			return false;
		}
		FBEFormatApp.lastUpdatePath = path ;
		System.out.println("lastUpdatePath:"+FBEFormatApp.lastUpdatePath);
		return true ;
	}
	protected Element item2Element(Document doc , Item item) throws FBEFormatException {
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
			ans = doc.createElement("Data") ;
			ans.setAttribute("type", v1);
			ans.setAttribute("target", v2);
		}else if(item instanceof BranchSym) {
			BranchSym sym = (BranchSym)item;
			String v1 = sym.optionGet("条件") ;
			if(sym instanceof MultiBranchSym) {
				ans = doc.createElement("MultiBranch") ;
			}else if(sym instanceof DoubleBranchSym) {
				ans = doc.createElement("DoubleBranch") ;
			}else {
				//エラー
				throw new FBEFormatException("不正なBranchSymインスタンス") ;
			}
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
			ans.setAttribute("up", v4);
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
			ans = doc.createElement("Flow") ;
			ans.setAttribute("tag", v1);
			ans.setAttribute("round","false");
			if(flow instanceof RoundFlow) {
				ans.setAttribute("round","true");
			}
			//子要素を追加
			for(Sym sym:flow.getSyms()) {
				Element child = item2Element(doc,sym);
				ans.appendChild(child);
			}
		}else {
			throw new FBEFormatException("不正なItemインスタンス："+item.getClass().getName());
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
		 NodeList flowList = document.getElementsByTagName("FBE").item(0).getChildNodes();
		 //flowListをList<Flow>に
		 List<Flow> ans = new ArrayList<>();
		 for(int i = 0;i < flowList.getLength();i++) {
			 if(flowList.item(i) instanceof Element) {
				 Flow flow = (Flow)element2Item((Element)flowList.item(i)) ;
				 ans.add(flow);
			 }
		 }
		FBEFormatApp.lastUpdatePath = url.getPath() ;
		System.out.println("lastUpdatePath:"+FBEFormatApp.lastUpdatePath);
		return ans ;
	}
	protected Item element2Item(Element ele) throws FBEFormatException{
		Item ans = null ;
		if(ele.getTagName().equals("Terminal")) {
			TerminalSym ter = new TerminalSym() ;
			ter.optionPut("タイプ", ele.getAttribute("type"));
			ter.optionPut("戻り値", ele.getAttribute("return"));
			ter.optionPut("テキスト", ele.getAttribute("text"));
			ans = ter ;
		}else if(ele.getTagName().equals("Calc")){
			CalcSym ter = new CalcSym(ele.getAttribute("formula"),ele.getAttribute("variable"));
			ans = ter ;
		}else if(ele.getTagName().equals("Data")){
			DataSym ter = new DataSym();
			ter.optionPut("タイプ", ele.getAttribute("type"));
			ter.optionPut("対象", ele.getAttribute("target"));
			ans = ter ;
		}else if(ele.getTagName().equals("DoubleBranch")){
			DoubleBranchSym ter = new DoubleBranchSym(ele.getAttribute("condition"));
			ans = ter ;
			//子要素をflowに追加
			for(int i = 0 ;i < ele.getChildNodes().getLength();i++) {
				Node node = ele.getChildNodes().item(i) ;
				if(node instanceof Element) {
					if(((Element) node).getAttribute("tag").equals("Yes")) {
						ter.yesFlow = (RoundFlow)element2Item((Element)node) ;
					}else if(((Element) node).getAttribute("tag").equals("No")) {
						ter.noFlow = (RoundFlow)element2Item((Element)node) ;
					}else {
						//エラー
						throw new FBEFormatException("DoubleBranchの子要素にYes,Noタグ以外のタグが付いています："+((Element) node).getAttribute("tag")) ;
					}
				}
			}
		}else if(ele.getTagName().equals("MultiBranch")){
			MultiBranchSym ter = new MultiBranchSym(ele.getAttribute("condition"));
			ans = ter ;
			//子要素をflowに追加
			for(int i = 0 ;i < ele.getChildNodes().getLength();i++) {
				Node node = ele.getChildNodes().item(i) ;
				if(node instanceof Element) {
					ter.addFlow((RoundFlow)element2Item((Element)node));
				}
			}
		}else if(ele.getTagName().equals("While")){
			WhileSym ter = new WhileSym();
			ter.optionPut("条件", ele.getAttribute("condition"));
			ter.optionPut("タイプ", ele.getAttribute("type"));
			ans = ter ;
			//子要素をflowに追加
			NodeList node = ele.getChildNodes() ;
			for(int i = 0;i < node.getLength();i++) {
				if(node.item(i) instanceof Element) {
					RoundFlow work = (RoundFlow)element2Item((Element) node.item(i));
					for(Sym sym : work.getSyms()) {
						ter.getFlow().addSym(sym);
					}
					ter.getFlow().setTag(work.getTag());
				}
			}
		}else if(ele.getTagName().equals("For")){
			ForSym ter = new ForSym(ele.getAttribute("variable"),ele.getAttribute("start"),ele.getAttribute("condition"),ele.getAttribute("up"));
			ans = ter ;
			//子要素をflowに追加
			NodeList node = ele.getChildNodes() ;
			for(int i = 0;i < node.getLength();i++) {
				if(node.item(i) instanceof Element) {
					RoundFlow work = (RoundFlow)element2Item((Element) node.item(i));
					for(Sym sym : work.getSyms()) {
						ter.getFlow().addSym(sym);
					}
					ter.getFlow().setTag(work.getTag());
				}
			}
		}else if(ele.getTagName().equals("Flow")){
			Flow flow = ele.getAttribute("round").equals("true") ? new RoundFlow() : new Flow() ;
			flow.setTag(ele.getAttribute("tag"));
			ans = flow ;
			//子要素をflowに追加
			for(int i = 0;i < ele.getChildNodes().getLength();i++) {
				if(ele.getChildNodes().item(i) instanceof Element) {
					flow.addSym( (Sym)element2Item((Element)ele.getChildNodes().item(i)) );
				}
			}
		}else {
			throw new FBEFormatException("不正なタグ："+ele.getTagName()) ;
		}
		//################################################そのほかのSymもここで追加
		return ans ;


	}

}
