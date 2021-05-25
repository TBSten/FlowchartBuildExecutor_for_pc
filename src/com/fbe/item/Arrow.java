package com.fbe.item;

import java.util.ArrayList;

import com.fbe.FBEApp;
import com.fbe.sym.Sym;
import com.fbe.sym.factory.CalcSymFactory;
import com.fbe.sym.factory.InputDataSymFactory;
import com.fbe.sym.factory.OutputDataSymFactory;
import com.fbe.sym.factory.SymFactory;
import com.fbe.sym.factory.WhileSymFactory;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Arrow extends Item {


	public static ArrayList<SymFactory<? extends Sym>> factorys = new ArrayList<>();
	static {
		factorys.add(new CalcSymFactory());
		factorys.add(new OutputDataSymFactory());
		factorys.add(new InputDataSymFactory());
		factorys.add(new WhileSymFactory());
	}

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
		pane.getChildren().add(addB);
		addB.setOnAction( e -> {

			try {
				//Sym追加画面を表示
				Stage st = new Stage();
				st.initModality(Modality.WINDOW_MODAL);
				st.initOwner(FBEApp.window);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("FBEAddSym.fxml"));
				Parent root = loader.load();

				for(SymFactory<? extends Sym> fact:factorys) {
					//((HBox)root.lookup("")).getChildren().add(fact1);
					((AddSymController)loader.getController()).addZone.getChildren().add(fact);
					fact.setOnAction((ev)->{
						Flow f = this.getParentFlow() ;
/*
						System.out.println(f.arrows.contains(Arrow.this));
						System.out.println(f.arrows.indexOf(Arrow.this));
						System.out.println(f.vb.getChildren().indexOf(Arrow.this));
						System.out.println(f.getSymBeforeOf(Arrow.this));
						System.out.println(f.arrows.contains(Arrow.this));
*/
						Sym newSym = fact.createSym() ;
						f.addSym(f.getSymBeforeOf(Arrow.this), newSym);
						st.hide();
						if(this.parentFlow != null) {
							this.parentFlow.redraw();
						}
						if(fact.isOpenSetting() ) {
							newSym.openSettingWindow();
						}

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
