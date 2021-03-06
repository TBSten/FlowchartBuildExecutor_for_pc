package com.fbe.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fbe.FBEApp;
import com.fbe.sym.Sym;
import com.fbe.sym.TerminalSym;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Flow extends Item {
	ArrayList<Sym> syms = new ArrayList<>();
	ArrayList<Arrow> arrows = new ArrayList<>();
	VBox vb = new VBox();
	Label label = new Label("") ;
	boolean nonSymDelete = true ;
	String tag = "" ;
	Runnable onDisabled = null ;
	Runnable onRedraw = null ;
	boolean ableToDisable = true ;

	public Flow() {
		this.getChildren().add(vb);
		this.setText("");
		this.setOnMouseClicked(e->{
			double x = e.getX() ;
			double y = e.getY() ;
			for(Sym n:syms) {
				if(x >= n.getLayoutX() && x <= n.getLayoutX()+n.getWidth() &&
						y >= n.getLayoutY() && y <= n.getLayoutY()+n.getHeight() ) {
					n.requestFocus();
				}
			}
		});

//		this.setStyle("-fx-background-color:red;");
		this.maxWidthProperty().bind(vb.widthProperty());
		this.maxHeightProperty().bind(vb.heightProperty());
		this.prefWidthProperty().bind(vb.widthProperty());
		this.prefHeightProperty().bind(vb.heightProperty());
		this.minWidthProperty().bind(vb.widthProperty());
		this.minHeightProperty().bind(vb.heightProperty());

		this.setOnMouseReleased(e->{
			this.onClicked(e,this);
		});

	}

	public void onClicked(MouseEvent e,Node target) {
		if(e.getTarget() == target){
			FBEApp.selectItem(this);
			if(e.getButton() == MouseButton.SECONDARY) {
				ContextMenu menu = new ContextMenu() ;
				MenuItem mi1 = new MenuItem("????????????????????????");
				mi1.setOnAction(ev->{
					Alert al = new Alert(AlertType.CONFIRMATION) ;
					al.setContentText("??????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
					Optional<ButtonType> result = al.showAndWait();
					if(result.get() == ButtonType.OK) {
						this.disable();
					}
				});
				if(this.isAbleToDisable()) {
					menu.getItems().addAll(mi1);
				}
				MenuItem mi2 = new MenuItem("?????????????????????");
				mi2.setOnAction(ev->{
					Stage st = new Stage() ;
					st.initOwner(FBEApp.window);
					st.initModality(Modality.WINDOW_MODAL);
					VBox root = new VBox();
					root.getChildren().add(new Label("????????????????????????????????????????????????"));
					TextField tf = new TextField(this.getTag()) ;
					root.getChildren().add(tf);
					ButtonBar bb = new ButtonBar();
					Button okB = new Button("OK");
					Button canB = new Button("???????????????");
					okB.setOnAction(eve->{
						if(tf.getText() != null) {
							this.setTag(tf.getText());
						}
						st.hide();
						this.redraw();
					});
					canB.setOnAction(eve->{
						st.hide();
						this.redraw();
					});
					bb.getButtons().addAll(okB,canB);
					root.getChildren().add(bb);
					st.setScene(new Scene(root));
					st.showAndWait();
					Flow.this.redraw();
					if(Flow.this.getParentFlow() != null) {
						Flow.this.getParentFlow().redraw();
					}
				});
				menu.getItems().add(mi2);

				if(menu.getItems().size() > 0) {
					menu.show(this,e.getScreenX() , e.getScreenY());
				}
			}
		}
	}

	public Sym getSymBeforeOf(Arrow ar) {
		Sym ans = null ;
		if(arrows.contains(ar)) {
			ans = syms.get(arrows.indexOf(ar)) ;
		}
		return ans ;
	}
	public Sym getSymAfterOf(Arrow ar) {
		Sym ans = null ;
		if(arrows.contains(ar)) {
			ans = syms.get(arrows.indexOf(ar)+1) ;
		}
		return ans ;
	}

	public void addSym(int index ,Sym sym) {
//		System.out.printf("addSym(%d , %s)\n",index,sym);
		List<Node> child = vb.getChildren() ;
		Arrow ar = new Arrow() ;
		ar.setParentFlow(this);
		int symIdx = index*2 ;
		int arIdx = index*2 - 1;
		if(symIdx == 0) {
			arIdx = symIdx + 1;
		}

		syms.add(symIdx /2, sym);
		arrows.add(arIdx/2,ar);

		if(syms.size() >= 2 && index >= 1) {
			child.add(arIdx , ar);
		}
		child.add(symIdx,sym);
		if(syms.size() >= 2 && index == 0) {
			child.add(arIdx , ar);
		}

//		System.out.println("log: sym:"+sym+"  parent:"+this);
		sym.setParentFlow(this);

		//sym????????????????????????
		addAnime(sym);
		sym.requestFocus();

	//	this.test();

		redraw();

		sym.onAddFlow();
	}
	public void addSym(Sym befSym ,Sym sym) {
		this.addSym(syms.indexOf(befSym)+1 , sym);
	}
	public void addSym(Sym sym) {
		this.addSym(this.getSyms().size(), sym);
	}

	public void addAnime(Sym sym) {
		FadeTransition animation = new FadeTransition(Duration.seconds(0.2), sym);
		animation.setFromValue(0);
		animation.setToValue(1);
		animation.play();
		sym.redraw();

	}
	public void removeSym(Sym sym) {
		int idx = syms.indexOf(sym) ;
//		System.out.println("isNonSymDelete() :"+isNonSymDelete());
		if(idx <= 0 && isNonSymDelete()) {
			Alert dialog = new Alert(AlertType.CONFIRMATION);
			dialog.setTitle("????????????????????????");
			dialog.setHeaderText(null);
			dialog.setContentText("?????????????????????????????????????????????????????????????????????????????????????????????");
			dialog.show();
			dialog.setOnHidden(e2->{
				if(dialog.getResult() == ButtonType.OK){
					disable();
				}
			});
		}else {
			/*
			//sym????????????????????????
			FadeTransition animation = new FadeTransition(Duration.seconds(0.2), sym);
			animation.setFromValue(1);
			animation.setToValue(0);
			animation.play();
			animation.setOnFinished(e->{
				Platform.runLater(()->{
					FBEApp.sleep(200);
					//????????????
					syms.remove(idx);
					vb.getChildren().remove(sym);
					Arrow ar ;
					if(idx < arrows.size() ) {
						ar = arrows.remove(idx);
						vb.getChildren().remove(ar);
					}else {
						ar = arrows.remove(idx-1);
						vb.getChildren().remove(ar);
					}
					if(syms.size() <= 0) {
						//??????????????????
						disable();
					}
					sym.onRemoveFlow();
				});
			});
			*/
			//????????????
			syms.remove(idx);
			vb.getChildren().remove(sym);
			Arrow ar ;
			if(idx < arrows.size() ) {
				ar = arrows.remove(idx);
				vb.getChildren().remove(ar);
			}else {
				ar = arrows.remove(idx-1);
				vb.getChildren().remove(ar);
			}
			if(syms.size() <= 0) {
				//??????????????????
				disable();
			}
			sym.onRemoveFlow();
		}



	}

	public void disable() {
		/*
		if(this.parentFlow == null) {
			FBEApp.app.removeFlow(this);
		}else if(this.parentFlow instanceof GettableFlow){
			((GettableFlow)this.parentFlow).removeFlow(this);
		}
		*/

		if(this.ableToDisable) {
			FadeTransition animation = new FadeTransition(Duration.seconds(0.6), this);
			animation.setFromValue(1);
			animation.setToValue(0);
			animation.play();
			animation.setOnFinished(e->{
				if(this.parentFlow == null) {
					FBEApp.app.removeFlow(this);
				}else if(this.parentFlow instanceof GettableFlow){
					((GettableFlow)this.parentFlow).removeFlow(this);
				}
				if(this.onDisabled != null) {
					this.onDisabled.run();
				}
			});
		}
	}

	@Override public void redraw() {
		this.autosize();
		if(vb != null) {
			this.vb.autosize();
			this.vb.requestLayout();
		}
		super.redraw();

		if(syms != null) {
			for(Sym sy:syms) {
				sy.redraw();
			}
			for(Arrow arr: arrows) {
				arr.redraw();
			}
		}

		if(this.onRedraw != null) {
			this.onRedraw.run();
		}

	//	this.test();
	}

	@Override public void draw() {
//		System.out.println("flow w*h :"+getWidth()+"*"+getHeight());
	}

/*
	@Override public void execute(FBEExecutor exe) {
		for(Sym s :this.syms) {
			exe.executeItem(s);
		}
	}
*/
	public ArrayList<Sym> getSyms() {
		return syms;
	}

	public ArrayList<Arrow> getArrows() {
		return arrows;
	}

	public String getLabel() {
		return label.getText();
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}

	public boolean isNonSymDelete() {
		return nonSymDelete;
	}

	public void setNonSymDelete(boolean nonSymDelete) {
		this.nonSymDelete = nonSymDelete;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Runnable getOnDisabled() {
		return onDisabled;
	}

	public void setOnDisabled(Runnable onDisabled) {
		this.onDisabled = onDisabled;
	}

	public boolean isAbleToDisable() {
		return ableToDisable;
	}

	public void setAbleToDisable(boolean ableToDisable) {
		this.ableToDisable = ableToDisable;
	}

	@Override public void toBaseLook() {
		this.setStyle("-fx-border-color:#00000000;-fx-border-width:3;");
	}
	@Override public void toSelectLook() {
		this.setStyle("-fx-border-color:cyan;");
	}

	public Runnable getOnRedraw() {
		return onRedraw;
	}

	public void setOnRedraw(Runnable onRedraw) {
		this.onRedraw = onRedraw;
	}

	public String getProcessName() {
		if(this.getSyms().get(0) instanceof TerminalSym) {
			return ((TerminalSym)this.getSyms().get(0)).getProcessName() ;
		}else {
			return null ;
		}
	}
	public String[] getProcessArgNames() {
		if(this.getSyms().get(0) instanceof TerminalSym) {
			return ((TerminalSym)this.getSyms().get(0)).getArgNames() ;
		}else {
			return null ;
		}
	}


}
