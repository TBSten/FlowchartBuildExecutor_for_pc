package com.fbe.item;

import java.util.ArrayList;

import com.fbe.FBEApp;
import com.fbe.sym.Sym;
import com.fbe.sym.factory.SymFactory;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Arrow extends Item {


	public static ArrayList<SymFactory<? extends Sym>> factorys = new ArrayList<>();

	public static int cnt = 0 ;
	public int id = 0 ;

	public Arrow() {
		setText("");
		/*
		this.setHeight(baseHeight/4);
		this.setPrefHeight(baseHeight/4);
		this.setMaxHeight(baseHeight/4);
		this.setMinHeight(baseHeight/4);
		 */


		FlowPane pane = new FlowPane();
		pane.setAlignment(Pos.CENTER);
		this.getChildren().add(pane);
		pane.maxWidthProperty().bind(this.widthProperty());
		pane.minWidthProperty().bind(this.widthProperty());
		pane.maxHeightProperty().bind(this.heightProperty());
		pane.minHeightProperty().bind(this.heightProperty());
		Button addB = new Button("＋") ;
		addB.setEllipsisString("");
		addB.prefWidthProperty().bind(this.heightProperty().multiply(0.7));
		addB.prefHeightProperty().bind(this.heightProperty().multiply(0.7));
		addB.setGraphicTextGap(0);
		addB.setPadding(new Insets(0,0,0,0));
		pane.getChildren().add(addB);
		pane.setOnMouseClicked(e->{
			this.getParentFlow().onClicked(e,pane) ;
			/*
			FBEApp.selectItem(this.getParentFlow());
			if(e.getButton() == MouseButton.SECONDARY) {
				ContextMenu menu = new ContextMenu() ;
				MenuItem mi1 = new MenuItem("フローを削除する");
				mi1.setOnAction(ev->{
					this.getParentFlow().disable();
				});
				if(this.getParentFlow().isAbleToDisable()) {
					menu.getItems().addAll(mi1);
				}
				MenuItem mi2 = new MenuItem("タグを変更する");
				mi2.setOnAction(ev->{
					Stage st = new Stage() ;
					st.initOwner(FBEApp.window);
					st.initModality(Modality.WINDOW_MODAL);
					VBox root = new VBox();
					root.getChildren().add(new Label("フローのタグ名を入力してください"));
					TextField tf = new TextField(this.getParentFlow().getTag()) ;
					root.getChildren().add(tf);
					ButtonBar bb = new ButtonBar();
					Button okB = new Button("OK");
					Button canB = new Button("キャンセル");
					okB.setOnAction(eve->{
						this.getParentFlow().setTag(tf.getText());
						st.hide();
						this.getParentFlow().redraw();
					});
					canB.setOnAction(eve->{
						st.hide();
						this.getParentFlow().redraw();
					});
					bb.getButtons().addAll(okB,canB);
					root.getChildren().add(bb);
					st.setScene(new Scene(root));
					st.showAndWait();
				});
				menu.getItems().add(mi2);

				if(menu.getItems().size() > 0) {
					menu.show(this,e.getScreenX() , e.getScreenY());
				}
			}
			*/
		});
		addB.setOnAction( e -> {

			try {
				//Sym追加画面を表示
				Stage st = new Stage();
				st.initModality(Modality.WINDOW_MODAL);
				st.initOwner(FBEApp.window);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("FBEAddSym.fxml"));
				Parent root = loader.load();
				ScrollPane addZoneSp = ((AddSymController)loader.getController()).addZoneSp ;
				FlowPane flowPane = ((AddSymController)loader.getController()).addZone ;
				addZoneSp.viewportBoundsProperty().addListener((ev)->{
					flowPane.setPrefWidth(addZoneSp.getViewportBounds().getWidth());
				});
				for(SymFactory<? extends Sym> fact:factorys) {
					//((HBox)root.lookup("")).getChildren().add(fact1);
					flowPane.getChildren().add(fact);
					fact.setOnAction((ev)->{
/*
						System.out.println(f.arrows.contains(Arrow.this));
						System.out.println(f.arrows.indexOf(Arrow.this));
						System.out.println(f.vb.getChildren().indexOf(Arrow.this));
						System.out.println(f.getSymBeforeOf(Arrow.this));
						System.out.println(f.arrows.contains(Arrow.this));
*/
						Sym newSym = fact.createSym() ;
						this.addSymAfterThis(newSym,fact);
						st.hide();

					});


				}

				Scene sc = new Scene(root);
				st.setScene(sc);
//				AddSymController cont = loader.getController();
				st.showAndWait();
			}catch(Exception exc) {
				exc.printStackTrace();
			}

		});


	}

	public void addSymAfterThis(Sym sym,SymFactory<?> fact) {
		Flow f = this.getParentFlow() ;
		f.addSym(f.getSymBeforeOf(Arrow.this), sym);
		if(this.parentFlow != null) {
			this.parentFlow.redraw();
		}
		if(fact != null && fact.isOpenSetting() ) {
			sym.openSettingWindow();
		}

	}

	@Override
	public void draw() {
		GraphicsContext gc = this.symCanvas.getGraphicsContext2D();
/*
		gc.setFill(Color.BLUE);
		gc.fillRect(0,0,getWidth(),getHeight());
*/
		gc.setStroke(itemLineColor);
		gc.setLineWidth(itemLineWidth);
		gc.strokeLine(getWidth()/2, 0, getWidth()/2, getHeight());
	}

	//未検証
	@Override
	public Node getExportView() {
		Canvas ans = new Canvas(this.getWidth() , this.getHeight());
		GraphicsContext gc = this.symCanvas.getGraphicsContext2D();
		gc.setStroke(itemLineColor) ;
		gc.setLineWidth(itemLineWidth);
		gc.strokeLine(getWidth()/2, 0, getWidth()/2, getHeight());
		return ans ;
	}

}
