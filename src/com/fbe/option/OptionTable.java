package com.fbe.option;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OptionTable extends AnchorPane {
	public enum Type{
		TEXTFIELD,
		COMBOBOX(true),
		TEXTAREA,
		CHECKBOX,
		SLIDER,
		SPIN,
		FILE,
		DIRECTORY;
		public boolean haveList = false ;
		Type(){
			this(false);
		}
		Type(boolean haveList){
			this.haveList = haveList ;
		}
	}

	private VBox vb = new VBox() ;
	private Map<String ,Inputable> inputNodes = new LinkedHashMap<>();
	private Map<String ,String> descriptions = new LinkedHashMap<>();
	private Map<Inputable,HBox> inp2HBox = new LinkedHashMap<>();
	private DoubleProperty namePer = new SimpleDoubleProperty(0.15);
	private DoubleProperty descPer = new SimpleDoubleProperty(0.40);
	private DoubleProperty inpPer = new SimpleDoubleProperty(0.45);
	public OptionTable() {
//		vb.setStyle("-fx-background-color:green;");

		this.getChildren().add(vb);
		vb.setFillWidth(true);
		vb.prefWidthProperty().bind(this.widthProperty());
		vb.prefHeightProperty().bind(this.heightProperty());

		HBox hb = new HBox();
		hb.setFillHeight(true);
		hb.prefWidthProperty().bind(vb.widthProperty());
		Label l1 = new Label("プロパティ");
		Label l2 = new Label("説明");
		Label l3 = new Label("値");
		l1.setAlignment(Pos.CENTER);
		l2.setAlignment(Pos.CENTER);
		l3.setAlignment(Pos.CENTER);
		l1.prefWidthProperty().bind(hb.widthProperty().multiply(namePer));
		l2.prefWidthProperty().bind(hb.widthProperty().multiply(descPer));
		l3.prefWidthProperty().bind(hb.widthProperty().multiply(inpPer));
		l1.prefHeightProperty().bind(hb.heightProperty());
		l2.prefHeightProperty().bind(hb.heightProperty());
		l3.prefHeightProperty().bind(hb.heightProperty());
		l1.minWidthProperty().set(0);
		l2.minWidthProperty().set(0);
		l3.minWidthProperty().set(0);
		hb.getChildren().addAll(l1,l2,l3);
		l1.setStyle("-fx-border-style: none none solid none; -fx-border-width: 2; -fx-border-color: black;");
		l2.setStyle("-fx-border-style: none none solid none; -fx-border-width: 2; -fx-border-color: black;");
		l3.setStyle("-fx-border-style: none none solid none; -fx-border-width: 2; -fx-border-color: black;");
		vb.getChildren().add(hb);


	}

	public Inputable put(String name,String desc,Type type,Object defValue) throws OptionException {
		if(type == null) {
			throw new OptionException("typeが不正です name:「"+name+"」 type:「"+type+"」") ;
		}

		HBox hb = new HBox();
//		hb.setStyle("-fx-background-color:blue;");
		Label nlb = new Label(name);
		nlb.setWrapText(true);
		Label dlb = new Label(desc);
		dlb.setWrapText(true);
		dlb.setPadding(new Insets(0,3,0,3));
		Inputable inp = null ;

		switch(type) {
		case TEXTFIELD:
			inp = new InputableTextField();
			break;
		case COMBOBOX:
			inp = new InputableComboBox<String>();
			break;
		case TEXTAREA:
			inp = new InputableTextArea();
			break;
		case CHECKBOX:
			inp = new InputableCheckBox();
			break;
		case SLIDER:
			inp = new InputableSlider();
			break;
		case SPIN:
			inp = new InputableSpinButton<Integer>();
			break;
		case FILE:
			inp = new InputableFileChooser();
			((InputableFileChooser)inp).type = InputableFileChooser.Type.FILE;
			break;
		case DIRECTORY:
			inp = new InputableFileChooser();
			((InputableFileChooser)inp).type = InputableFileChooser.Type.DIRECTORY;
			break;
		}

		nlb.prefWidthProperty().bind(hb.widthProperty().multiply(namePer));
		dlb.prefWidthProperty().bind(hb.widthProperty().multiply(descPer));
		inp.getRegion().prefWidthProperty().bind(hb.widthProperty().multiply(inpPer));

//		nlb.prefHeightProperty().bind(hb.heightProperty());
//		dlb.prefHeightProperty().bind(hb.heightProperty());
//		inp.getRegion().prefHeightProperty().bind(hb.heightProperty());
		hb.getChildren().addAll(nlb,dlb,inp.getRegion());
		Label lb = new Label(" ");
		lb.setMinSize(0, 0);
		vb.getChildren().addAll(hb,lb);

		this.inputNodes.put(name, inp);
		this.descriptions.put(name, desc);
		vb.getChildren().remove(this.inp2HBox.get(inp));
		inp.put(defValue);



		return inp ;
	}

	public Object get(String name) {
		return this.inputNodes.get(name).get();
	}
	public String getAsString(String name) {
		return this.inputNodes.get(name).getString();
	}

	public Set<String> namesSet(){
		return this.inputNodes.keySet();
	}

	public Inputable remove(String name) {
		Inputable inp = this.inputNodes.get(name);
		this.inputNodes.remove(name);
		this.descriptions.remove(name);
		vb.getChildren().remove(inp.getRegion());
		return inp ;
	}

	public Map<String, Inputable> getInputNodes() {
		return inputNodes;
	}

	public Map<String, String> getDescriptions() {
		return descriptions;
	}


}
