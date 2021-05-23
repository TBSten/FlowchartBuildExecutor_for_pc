package com.fbe.item;

import java.util.List;

import com.fbe.sym.Sym;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

/**
 * 非推奨。追加処理など上手くいかない。
 *
 */
public class RoundFlow extends Flow {

	public RoundFlow() {
		super();
		Arrow sa = new Arrow();
		sa.setParentFlow(this);
		arrows.add(sa);
		vb.getChildren().add(sa);

	}

	@Override
	public Sym getSymBeforeOf(Arrow ar) {
		int idx = arrows.indexOf(ar)-1 ;
		if(idx >= 0 && idx < syms.size()) {
			return syms.get(idx);
		}else {
			return null ;
		}
	}
	@Override
	public Sym getSymAfterOf(Arrow ar) {
		int idx = arrows.indexOf(ar) ;
		if(idx >= 0 && idx < syms.size()) {
			return syms.get(idx);
		}else {
			return null ;
		}
	}

	@Override
	public void addSym(int index ,Sym sym) {
		List<Node> child = this.vb.getChildren() ;
		sym.setParentFlow(this);
		Arrow ar = new Arrow() ;
		ar.setParentFlow(this);
		//child,arrows,syms に追加
		syms.add(index, sym);
		arrows.add(index+1,ar);
		child.add(index*2+1,sym);
		child.add(index*2+2,ar);

		//addAnimeで追加時のアニメーション
		addAnime(sym);
	}
	@Override
	public void addSym(Sym befSym ,Sym sym) {
		addSym(syms.indexOf(befSym)+1,sym);
	}
	@Override
	public void removeSym(Sym sym) {
		int idx = syms.indexOf(sym);
		if(idx > 0) {

			//symにアニメーション
			FadeTransition animation = new FadeTransition(Duration.seconds(0.2), sym);
			animation.setFromValue(1);
			animation.setToValue(0);
			animation.play();
			animation.setOnFinished(e->{
				Arrow ar = arrows.get(idx+1);

				syms.remove(sym);
				arrows.remove(ar);
				List<Node> child = this.vb.getChildren() ;
				child.remove(sym);
				child.remove(ar);
			});
		}else {
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
		}
	}



}
