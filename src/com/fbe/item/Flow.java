package com.fbe.item;

import java.util.ArrayList;
import java.util.List;

import com.fbe.exe.FBEExecutable;
import com.fbe.exe.FBEExecutor;
import com.fbe.sym.Sym;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Flow extends Item implements FBEExecutable{
	ArrayList<Sym> syms = new ArrayList<>();
	ArrayList<Arrow> arrows = new ArrayList<>();
	VBox vb = new VBox();

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
		FadeTransition animation = new FadeTransition(Duration.seconds(0.2), sym);
		animation.setFromValue(0);
		animation.setToValue(1);
		animation.play();
		sym.requestFocus();

	//	this.test();

	}
	public void addSym(Sym befSym ,Sym sym) {
		this.addSym(syms.indexOf(befSym)+1 , sym);
	}
	public void removeSym(Sym sym) {
/*
		int idx = syms.indexOf(sym) ;
		this.syms.remove(sym);
		vb.getChildren().remove(sym);

		if(vb.getChildren().size() >= 1) {
			if(idx <= 0) {
				this.arrows.remove(idx);
				vb.getChildren().remove(0);
			}else if(this.arrows.size() > idx) {
				this.arrows.remove(idx);
				vb.getChildren().remove(idx*2-1);
			}
		}
		sym.setParentFlow(null);

	//	this.test();
*/
		//symにアニメーション
		FadeTransition animation = new FadeTransition(Duration.seconds(0.2), sym);
		animation.setFromValue(1);
		animation.setToValue(0);
		animation.play();
		animation.setOnFinished(e->{
			//外す処理
			int idx = syms.indexOf(sym) ;
			syms.remove(idx);
			vb.getChildren().remove(sym);
			if(idx < arrows.size() ) {
				Arrow ar = arrows.remove(idx);
				vb.getChildren().remove(ar);
			}else {
				Arrow ar = arrows.remove(idx-1);
				vb.getChildren().remove(ar);
			}
		});



	}

	@Override public void redraw() {
		this.autosize();
		super.redraw();

	//	this.test();
	}
	@Override public void draw() {
	}

	public void test() {
		System.out.println("=====================");
		if(syms != null) {
			System.out.printf("%6s :","syms");
			for(int i = 0;i < syms.size();i++) {
				System.out.printf("  %20s |",syms.get(i));
			}
			System.out.println();
		}
		if(arrows != null) {
			System.out.printf("%6s :","arrows");
			for(int i = 0;i < arrows.size();i++) {
				System.out.printf("  %20s |",arrows.get(i));
			}
			System.out.println();
		}
		System.out.println("=====================");
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

}
