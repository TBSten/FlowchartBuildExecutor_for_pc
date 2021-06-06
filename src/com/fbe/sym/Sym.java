package com.fbe.sym;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fbe.FBEApp;
import com.fbe.exe.FBEExecutor;
import com.fbe.item.Flow;
import com.fbe.item.Item;
import com.fbe.option.Inputable;
import com.fbe.option.OptionTable;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 * sym:hover{
 *     -fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 );
 * }
 */
public abstract class Sym extends Item {


	ContextMenu menu = new ContextMenu() ;
	private final Map<String,String> options = new LinkedHashMap<>();
	private final Map<String,OptionTable.Type> optionTypes = new LinkedHashMap<>();
	private final Map<String,String> optionDescriptions = new LinkedHashMap<>();
	private final Map<String,List<String>> optionsValueList = new LinkedHashMap<>();
	private boolean isSkip = false ;
	private boolean movable = true ;
	private boolean deletable = true ;


	public AnchorPane clickPane = new AnchorPane() ;
	public Sym() {


		clickPane.prefWidthProperty().bind(this.widthProperty());
		clickPane.prefHeightProperty().bind(this.heightProperty());
//		clickPane.setStyle("-fx-background-color:yellow;");
		this.getChildren().add(clickPane);


		this.setOnKeyPressed(e->{
			switch(e.getCode()) {
			case ENTER:
			case SPACE:
				//ENTER,SPACEがおされたとき
				break;
			}
		});
		/*
		this.setOnMousePressed((e)->{
			if(e.getClickCount() == 2) {
				//設定を開く
				openSettingWindow();

			}
			if(e.isSecondaryButtonDown()) {
				menu.show(this,e.getScreenX() , e.getScreenY());
			}
			if(this.isFocused()) {
				Sym.this.requestFocus();
			}
//			System.out.println("requestFocus:"+isFocused());
			redraw();
		});
		*/
//		this.symLabel.setOnMousePressed((e)->{
//		this.setOnMousePressed((e)->{
		this.clickPane.setOnMouseClicked((e)->{

			if(e.getTarget() == this.clickPane) {
			//	System.out.println("mouse pressed :"+this);
				if(e.getClickCount() == 2) {
					//設定を開く
					openSettingWindow();
				}
				if(e.getButton() == MouseButton.SECONDARY) {
					menu.show(this,e.getScreenX() , e.getScreenY());
				}
				FBEApp.selectItem(this);
				redraw();

			//	System.out.println("w*h :"+getWidth()+"*"+getHeight());
			}

		});
		this.clickPane.setOnDragDetected(e->{
			Dragboard dragboard = this.clickPane.startDragAndDrop(TransferMode.ANY);

	        ClipboardContent content = new ClipboardContent();
	        content.putString("FBE_ITEM_ID:"+this.itemId);
	        dragboard.setContent(content);

	        e.consume();
		});
		this.clickPane.setOnDragOver(e->{
			Matcher m = Pattern.compile("FBE_ITEM_ID:(\\d*)").matcher(e.getDragboard().getString());
			if(m.find()) {
				e.acceptTransferModes(TransferMode.ANY);
			}
	        e.consume();
		});
		this.clickPane.setOnDragDropped(e->{
			Dragboard dragboard = e.getDragboard();
	        String string = dragboard.getString();
			Matcher m = Pattern.compile("FBE_ITEM_ID:(\\d*)").matcher(string);
			if(m.find()) {
				Item i = Item.items.get(Long.parseLong(m.group(1)));
				if(i instanceof Sym ) {
					Sym sym = (Sym)i ;
					if(this.isMovable() &&  sym.isMovable() && this.getParentFlow() != null) {
						//symを親から削除
						Flow symP = sym.getParentFlow();
						if(symP != null) {
							symP.removeSym(sym);
						}
						//symを追加
						this.getParentFlow().addSym(this.getParentFlow().getSyms().indexOf(this)+1, sym);
					}
				}
			}
		});


		ArrayList<MenuItem> items = new ArrayList<>();
		MenuItem i = new MenuItem("削除");
		i.setOnAction(e ->{
			//削除
			this.getParentFlow().removeSym(this);
		});
		if(this.isDeletable()) {
			items.add(i);
		}
		i = new MenuItem("オプション");
		i.setOnAction(e ->{
			//オプションを開く
			openSettingWindow();
		});
		items.add(i);
		i = new MenuItem("再読み込み");
		i.setOnAction(e ->{
			//オプションを開く
			redraw();
		});
		items.add(i);
		menu.getItems().addAll(items);

		symLabel.prefWidthProperty().unbind();
		symLabel.prefHeightProperty().unbind();
		symLabel.prefHeightProperty().unbind();

		symLabel.prefWidthProperty().bind(baseWidthProperty);
		symLabel.prefHeightProperty().bind(baseHeightProperty);
//		symLabel.setPrefWidth(baseWidth);
//		symLabel.setPrefHeight(baseHeight);
		symLabel.getParent().layout();

		this.getStyleClass().add("sym");
		this.setFocusTraversable(true);


	}
	public abstract void execute(FBEExecutor exe);
	public ContextMenu getMenu() {
		return menu;
	}
	public void openSettingWindow() {
		//設定ウィンドウ
		Stage st = new Stage();
		st.setMinWidth(200);
		st.setMinHeight(200);
		st.setTitle(Sym.this.getClass().getSimpleName()+"の設定");
		st.initModality(Modality.WINDOW_MODAL);
		st.initOwner(FBEApp.window);
//		BorderPane root = new BorderPane() ;
		VBox root = new VBox() ;
		Scene sc = new Scene(root);
		st.setScene(sc);



		Label titleL = new Label("設定") ;

		OptionTable table = new OptionTable();
//		table.setStyle("-fx-background-color:red;");

		if(options.size() > 0) {

			for(Map.Entry<String, String> ent:options.entrySet()) {
				String name = ent.getKey();
				String value = ent.getValue();
				String desc = this.optionDescriptions.get(name);
				OptionTable.Type type = this.optionTypes.get(name);
				List<String> list = this.optionsValueList.get(name) ;

				Inputable inputable = table.put(name,desc,type,value);
				if(type.haveList) {
					for(String item:this.optionsValueList.get(name)) {
						inputable.args(item);
					}
				}
			}
		}else {
		//	vb.getChildren().add(new Label("設定可能なオプションはありません"));
		}
		Button saveB = new Button("保存して戻る");
		saveB.setOnAction(e->{
			for(String name:table.namesSet()) {
				optionPut(name,table.getAsString(name));
			}
			st.close();
		});
		Button returnB = new Button("保存せずに戻る");
		returnB.setOnAction(e->{
			st.hide();
		});
		ButtonBar bb = new ButtonBar();
		bb.getButtons().addAll(saveB,returnB);

		titleL.prefWidthProperty().bind(root.widthProperty());
		titleL.minWidthProperty().bind(root.widthProperty());
		titleL.maxWidthProperty().bind(root.widthProperty());
		titleL.setPrefHeight(USE_COMPUTED_SIZE);
		titleL.setMaxHeight(USE_COMPUTED_SIZE);
		titleL.setMinHeight(USE_COMPUTED_SIZE);
		table.prefWidthProperty().bind(root.widthProperty());
		table.minWidthProperty().bind(root.widthProperty());
		table.maxWidthProperty().bind(root.widthProperty());
		table.prefHeightProperty().bind(root.heightProperty().subtract(titleL.heightProperty()).subtract(bb.heightProperty()));
		table.minHeightProperty().bind(root.heightProperty().subtract(titleL.heightProperty()).subtract(bb.heightProperty()));
		table.maxHeightProperty().bind(root.heightProperty().subtract(titleL.heightProperty()).subtract(bb.heightProperty()));
		bb.prefWidthProperty().bind(root.widthProperty());
		bb.minWidthProperty().bind(root.widthProperty());
		bb.maxWidthProperty().bind(root.widthProperty());
		bb.setPrefHeight(USE_COMPUTED_SIZE);
		bb.setMaxHeight(USE_COMPUTED_SIZE);
		bb.setMinHeight(USE_COMPUTED_SIZE);

		root.getChildren().add(titleL);
		root.getChildren().add(table);
		root.getChildren().add(bb);
/*
		root.setTop(titleL);
		root.setCenter(table);
		root.setBottom(bb);
*/


/*
		table.widthProperty().addListener(e->{
			System.out.println("  table.width :"+table.getWidth());
			System.out.println("    prefwidth :"+table.getPrefWidth());
			System.out.println("     maxwidth :"+table.getMaxWidth());
			System.out.println("     minwidth :"+table.getMinWidth());
		});
		table.heightProperty().addListener(e->{
			System.out.println("  table.height :"+table.getHeight());
			System.out.println("    prefheight :"+table.getPrefHeight());
			System.out.println("     maxheight :"+table.getMaxHeight());
			System.out.println("     minheight :"+table.getMinHeight());
		});
*/


//		root.prefWidthProperty().bind(sc.widthProperty());
//		root.prefHeightProperty().bind(sc.heightProperty());
		root.setPrefSize(400, 300);
//		root.autosize();

		st.sizeToScene();
		st.show();

		st.setOnHidden(e->{
			requestFocus();
			redraw();
		});



	}

	public void optionPut(String name,String defValue) {
		this.options.put(name, defValue);
	}
	public void optionPut(String name,String desc,OptionTable.Type type,String defValue) {
		this.options.put(name, defValue);
		this.optionDescriptions.put(name, desc);
		this.optionTypes.put(name, type);
		if(type.haveList) {
			this.optionsValueList.put(name, new ArrayList<>());
		}
	}
	public String optionGet(String name) {
		return this.options.get(name);
	}
	public String optionDescriptionGet(String name) {
		return this.optionDescriptions.get(name);
	}
	public OptionTable.Type optionTypeGet(String name) {
		return this.optionTypes.get(name);
	}


	public Node getExportView() {
//		changeUnfocusedDesign();
		redraw();
		WritableImage wi = this.snapshot();
		ImageView iv = new ImageView(wi);
		return iv ;
	}


	/**
	 * optionの値をこの記号に反映します。
	 */
	public void reflectOption() {
	}

	public void redraw() {
		try {
			baseWidthProperty.set(baseWidthProperty.get());
			baseHeightProperty.set(baseHeightProperty.get());
			if(options != null) {
				reflectOption();
			}
			super.redraw();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void showExportViewWindow(Sym sym) {


		sym.requestLayout();

		Stage st = new Stage();
		AnchorPane root = new AnchorPane();
		Scene sc = new Scene(root);
		st.setScene(sc);
		root.getChildren().add(sym.getExportView());
		st.show();

	}
	public Map<String, String> getOptions() {
		return options;
	}
	public Map<String, OptionTable.Type> getOptionTypes() {
		return optionTypes;
	}
	public Map<String, String> getOptionDescriptions() {
		return optionDescriptions;
	}
	public Map<String, List<String>> getOptionsValueList() {
		return optionsValueList;
	}
	public boolean isSkip() {
		return isSkip;
	}
	public void setSkip(boolean isSkip) {
		this.isSkip = isSkip;
	}
	public void onAddFlow() {}
	public void onRemoveFlow() {}
	public boolean isMovable() {
		return movable;
	}
	public void setMovable(boolean movable) {
		this.movable = movable;
	}
	public boolean isDeletable() {
		return deletable;
	}
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}


}


