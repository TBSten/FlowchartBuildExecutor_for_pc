package com.fbe.sym;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.fbe.FBEApp;
import com.fbe.option.OptionTable;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jp.hishidama.eval.ExpRuleFactory;
import jp.hishidama.eval.Rule;

public class ArrayTemplate {
//	private static Map<String,ArrayTemplate> templates = new LinkedHashMap<>() ;
	public static List<ArrayTemplate> templates = new ArrayList<>();

	String name = "テンプレート" ;
	String type = "2次元配列" ;
	String size = "3,3" ;
	/*
	 * 「10=>(1,1),99=>(all)」のように,区切りで操作を記録
	 * 操作の種類
	 * 1次元配列
	 * Num=>(IDX)  (IDX)にNumを代入する
	 * Num=>(all)  すべての要素にNumを代入する
	 * 2次元配列
	 * Num=>(X,Y)  (X,Y)にNumを代入する
	 * Num=>(all)  すべての要素にNumを代入する
	 */
	String operations = "0=>(all)" ;
	public Object[] createArray() {
		Rule rule = ExpRuleFactory.getJavaRule() ;

		Object[] ans = null ;
		if(type.equals("1次元配列")) {
			Object[] arr = new Object[Integer.parseInt(size)] ;
			String[] opes = operations.split(";");
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
			Object[][] arr = new Object[Integer.parseInt(sizes[0])][Integer.parseInt(sizes[1])] ;
			String[] opes = operations.split(";");
			for(String op :opes) {
				Matcher m = FBEApp.matcher("(.+)=>\\((.+)\\)", op);
				if(m.matches()) {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addOperations(String operations) {
		this.operations += operations ;

	}

	private static Stage manageSt = null ;
	private static ListView<String> manageList = null ;
	//管理画面を開く
	public static Stage openManageStage() {
		try {
			if(manageSt == null) {
				Stage st = new Stage() ;
				manageSt = st ;
				st.initOwner(FBEApp.window);
				st.initModality(Modality.NONE);
				st.setTitle("配列テンプレートの管理");
				FXMLLoader loader = new FXMLLoader(ArrayTemplate.class.getResource("TemplateManage.fxml")) ;
				BorderPane root = loader.load();
				TemplateManageController cont = loader.getController() ;
				cont.btn_new.setOnAction(e->{
					openNewStage() ;
				});
				cont.btn_edit.setOnAction(e->{
					openEditStage(cont.list.getSelectionModel().getSelectedItem());
				});
				cont.btn_delete.setOnAction(e->{
					String item = cont.list.getSelectionModel().getSelectedItem() ;
					boolean[] contains = {false} ;
					templates.forEach(ele->{
						if(ele.getName().equals(item)) {
							contains[0] = true ;
						}
					});
					if(item == null ) {
						FBEApp.msgBox("");
					}else if(contains[0]){
						Alert al = new Alert(Alert.AlertType.WARNING);
						al.setContentText("テンプレートを削除してもいいですか？");
						al.showAndWait()
					      .filter(response -> response == ButtonType.OK)
					      .ifPresent(response -> {
					    	  removeTemplate(item);
					      });
					}else {
//						FBEApp.msgBox("不正なテンプレートが選択されました："+item);
						Alert al = new Alert(Alert.AlertType.CONFIRMATION);
						al.setContentText("このテンプレートを削除しますか？");
						al.setHeaderText("不正なテンプレートが選択されました");
						al.showAndWait()
							.filter(response -> response == ButtonType.OK)
							.ifPresent(response -> {
								try {
									ArrayTemplate.removeTemplate(item);
								}catch(Exception ex) {
									ArrayTemplate.manageList.getItems().remove(item);
								}

							});
					}
				});
				cont.btn_close.setOnAction(e->{
					st.close();
				}) ;
				ListView<String> list = cont.list ;
				manageList = list ;
				List<String> tems = new ArrayList<>();
				templates.forEach(ele->{
					tems.add(ele.getName());
				});
				list.getItems().addAll(tems);
				st.setScene(new Scene(root));
				st.setOnHidden(e->{
					manageSt = null ;
				});
				st.show();
				return st ;
			}else {
				//既に開いている
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null ;
	}
	public static void openNewStage() {
		try {
			Stage st = new Stage() ;
			st.initOwner(FBEApp.window);
			st.initModality(Modality.NONE);
			st.setTitle("配列テンプレートの新規作成");
			FXMLLoader loader = new FXMLLoader(ArrayTemplate.class.getResource("TemplateNew.fxml")) ;
			BorderPane root = loader.load();
			TemplateNewController cont = loader.getController() ;
			cont.cb.getItems().addAll("1次元配列","2次元配列");
			cont.okB.setOnAction(e->{
				try {
					ArrayTemplate tem = new ArrayTemplate() ;
					tem.setType(cont.cb.getValue());
					tem.setSize(cont.tf.getText());
					openEditStage(tem);
					st.close();
				}catch(Exception ex) {
					ex.printStackTrace();
					FBEApp.msgBox("エラーが発生しました。入力内容が正しいか確認してください。");
				}
			});
			cont.canB.setOnAction(e->{
				st.close();
			});
			st.setScene(new Scene(root));
			st.show();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public static void openEditStage(String name) {
		if(name == null) {
			FBEApp.msgBox("テンプレートを選択してください。");
			return ;
		}
		boolean[] contains = {false} ;
		templates.forEach(ele->{
			if(ele.getName().equals(name)) {
				contains[0] = true ;
			}
		});
		if(contains[0]) {
			openEditStage(getTemplate(name));
		}else {
//			FBEApp.msgBox("不正なテンプレートが選択されました："+name);
			Alert al = new Alert(Alert.AlertType.CONFIRMATION);
			al.setContentText("このテンプレートを削除しますか？");
			al.setHeaderText("不正なテンプレートが選択されました");
			al.showAndWait()
				.filter(response -> response == ButtonType.OK)
				.ifPresent(response -> {
					try {
						ArrayTemplate.removeTemplate(name);
					}catch(Exception ex) {
						ArrayTemplate.manageList.getItems().remove(name);
					}

				});

		}
	}
	public static void openEditStage(ArrayTemplate temp) {
		try {
			Stage st = new Stage() ;
			st.setMinWidth(300);
			st.initOwner(FBEApp.window);
			st.initModality(Modality.NONE);
			st.setTitle("配列テンプレートの編集");
			BorderPane bp = new BorderPane();
			//bp.Centerに設定用のノードを追加
			VBox vb = new VBox() ;
			GridPane gp = new GridPane() ;
			ScrollPane sp = new ScrollPane(gp);
			sp.setMaxSize(1000,450);
			gp.setPadding(new Insets(10,10,10,10));
			gp.setAlignment(Pos.CENTER);
			Object[] array = temp.createArray() ;
			if(temp.getType().equals("1次元配列")) {
				int len = Integer.parseInt(temp.getSize().split(",")[0]);
				for(int i = 0;i < len;i++) {
					TemplateEditLabel lb = new TemplateEditLabel(temp,i+"",st);
					lb.setText(String.valueOf(array[i]));
					gp.add(lb, i, 0);
				}
			}else if(temp.getType().equals("2次元配列")) {
				int lenY = Integer.parseInt(temp.getSize().split(",")[0]);
				int lenX = Integer.parseInt(temp.getSize().split(",")[1]);
//				System.out.println(Arrays.deepToString(array));
				for(int y = 0;y < lenY;y++) {
					for(int x = 0;x < lenX;x++) {
						TemplateEditLabel lb = new TemplateEditLabel(temp,y+","+x,st);	//選択用ラベルに置き換える必要がある
						lb.setText(String.valueOf(((Object[])array[y])[x]));
						gp.add(lb, x, y);
					}
				}
			}else {
				//エラー
			}
			OptionTable table = new OptionTable();
			table.put("テンプレート名", "配列テンプレートの名前を指定します。すでにある場合新しい名前で上書きされます。", OptionTable.Type.TEXTFIELD, temp.getName());
			vb.setFillWidth(true);
			vb.getChildren().addAll(sp,table);
			bp.setCenter(vb);
			ButtonBar bb = new ButtonBar() ;
			Button okB = new Button("適用");
			okB.setOnAction(e->{
				temp.setName(String.valueOf(table.get("テンプレート名")));
				putTemplate(temp);
				st.close();
			});
			bb.getButtons().addAll(okB);
			bp.setBottom(bb);
			st.setScene(new Scene(bp));
			st.show();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	private static class TemplateEditLabel extends Label{
		ArrayTemplate temp ;
		String idx ;
		Stage editStage ;
		TemplateEditLabel(ArrayTemplate temp,String idx,Stage editStage){
			this.temp = temp ;
			this.idx = idx;
			this.editStage = editStage ;
			this.setFont(Font.font(22));
			this.setStyle("-fx-border-color:black;-fx-border-width:2;-fx-background-color:white;");
			this.setMinSize(30, 30);
			this.setTextAlignment(TextAlignment.CENTER);
			this.setAlignment(Pos.CENTER);
			this.setOnMouseClicked(e->{
				if(e.getClickCount() == 2) {
				//	System.out.println(temp.operations);
					Stage st = new Stage() ;
					st.initOwner(this.editStage);
					st.initModality(Modality.WINDOW_MODAL);
					VBox vb = new VBox() ;
					vb.setFillWidth(true);
					Label lb = new Label("変更内容を設定");
					TextField tf = new TextField();
					ButtonBar bb = new ButtonBar();
					boolean[] flg = {false} ;
					Button okB = new Button("OK");
					okB.setOnAction(eve->{
							flg[0] = true ;
							st.close();
						});
					bb.getButtons().addAll(okB);
					vb.getChildren().addAll(lb,tf,bb);
					st.setScene(new Scene(vb));
					st.showAndWait();
					if(flg[0]) {
						String data = tf.getText() ;
						String ope = String.format(";%s=>(%s);", data.replace("\\s", ""), this.idx) ;
						this.temp.addOperations(ope);		//10=>(idx)
						this.setText(data);
					//	System.out.println(ope);
					}
				}
			});
		}
	}
	public static void putTemplate(ArrayTemplate temp) {
		if(temp != null) {
			if(!templates.contains(temp)) {
				templates.add( temp);
			}
			if(manageList != null && !manageList.getItems().contains( temp.getName() )) {
				manageList.getItems().add(temp.getName());
			}
		}
	}
	public static ArrayTemplate getTemplate(String name) {
		ArrayTemplate[] ans = {null} ;
		templates.forEach(e->{
			if(e.getName().equals(name)) {
				ans[0] = e ;
			}
		});
		return ans[0] ;
	}
	public static void removeTemplate(String name) {
		removeTemplate(getTemplate(name));
	}
	public static void removeTemplate(ArrayTemplate template) {
		templates.remove(template);
		manageList.getItems().remove(template.getName());
	}
	public static List<String> getTemplateNames(){
		List<String> ans = new ArrayList<>();
		templates.forEach(e->{
			ans.add(e.getName());
		});
		return ans ;
	}

}
