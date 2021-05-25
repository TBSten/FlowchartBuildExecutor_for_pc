package com.fbe.item;

import java.util.ArrayList;
import java.util.List;

import com.fbe.FBEApp;
import com.fbe.exe.FBEExecutable;
import com.fbe.exe.FBEExecutor;
import com.fbe.sym.Sym;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Flow extends Item implements FBEExecutable{
	ArrayList<Sym> syms = new ArrayList<>();
	ArrayList<Arrow> arrows = new ArrayList<>();
	VBox vb = new VBox();
	Label label = new Label("") ;
	boolean nonSymDelete = true ;

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

		//symにアニメーション
		addAnime(sym);
		sym.requestFocus();

	//	this.test();

		redraw();

	}
	public void addSym(Sym befSym ,Sym sym) {
		this.addSym(syms.indexOf(befSym)+1 , sym);
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
			dialog.setTitle("先頭の記号の削除");
			dialog.setHeaderText(null);
			dialog.setContentText("先頭の記号を消すとこのフロー全てが消されます。よろしいですか？");
			dialog.show();
			dialog.setOnHidden(e2->{
				if(dialog.getResult() == ButtonType.OK){
					disable();
				}
			});
		}else {
			//symにアニメーション
			FadeTransition animation = new FadeTransition(Duration.seconds(0.2), sym);
			animation.setFromValue(1);
			animation.setToValue(0);
			animation.play();
			animation.setOnFinished(e->{
				//外す処理
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
					//フローを削除
					disable();
				}
			});
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
		});
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

	//	this.test();
	}

	@Override public void draw() {
//		System.out.println("flow w*h :"+getWidth()+"*"+getHeight());
	}


	@Override public void execute(FBEExecutor exe) {
		for(Sym s :this.syms) {
			exe.setExeCursor(s);
			s.requestFocus();
			s.execute(exe);
		}
	}

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

}
