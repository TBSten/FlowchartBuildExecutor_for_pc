package com.fbe.sym;

import java.util.Arrays;

import com.fbe.exe.FBEExecutor;
import com.fbe.item.RoundFlow;

import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

/**
 * このクラスではsymLabelは上の記号の文字列を、追加されたフィールドのbottomLabelが下の記号の文字列を表す。
 *
 */
public class WhileSym extends Sym {
	public static int cnt = 1 ;

	RoundFlow flow = new RoundFlow() ;
	Label bottomLabel = new Label("TEST");
	int num ;

	public WhileSym(String condition) {
		super();

		this.num = cnt ;
		cnt ++;
		this.options.put("条件",condition);
		this.options.put("タイプ","前判定");
		this.optionsValueList.put("タイプ",Arrays.asList("前判定","後判定"));



		//		this.setStyle("-fx-background-color:red;");
//		this.flow.setStyle("-fx-background-color:yellow;");
//		this.symLabel.setStyle("-fx-background-color:gray;");
//		flow.addSym(0, new CalcSym("0","x"));

		this.heightProperty().addListener(e->{
			redraw();
		});
		this.symLabel.heightProperty().addListener(e->{
			redraw();
		});

		this.symLabel.prefWidthProperty().unbind();
		this.symLabel.prefHeightProperty().unbind();
		this.symLabel.minWidthProperty().unbind();
		this.symLabel.minHeightProperty().unbind();


		this.symLabel.maxWidthProperty().bind(baseWidthProperty);
		this.symLabel.maxHeightProperty().bind(baseHeightProperty);
		this.symLabel.minWidthProperty().bind(baseWidthProperty);
		this.symLabel.minHeightProperty().bind(baseHeightProperty);
		this.symLabel.prefWidthProperty().bind(baseWidthProperty);
		this.symLabel.prefHeightProperty().bind(baseHeightProperty);
//		this.symLabel.setMaxWidth(baseWidth) ;
//		this.symLabel.setMaxHeight(baseHeight) ;
		this.symLabel.setLayoutX(0);
		this.symLabel.setLayoutY(0);


		this.bottomLabel.prefWidthProperty().bind(baseWidthProperty);
		this.bottomLabel.prefHeightProperty().bind(baseHeightProperty);
		this.bottomLabel.minWidthProperty().bind(baseWidthProperty);
		this.bottomLabel.minHeightProperty().bind(baseHeightProperty);
		this.bottomLabel.maxWidthProperty().bind(baseWidthProperty);
		this.bottomLabel.maxHeightProperty().bind(baseHeightProperty);
//		this.bottomLabel.setPrefWidth(baseWidth);
//		this.bottomLabel.setPrefHeight(baseHeight);
//		this.bottomLabel.setMinWidth(baseWidth);
//		this.bottomLabel.setMinHeight(baseHeight);
//		this.bottomLabel.setMaxWidth(baseWidth);
//		this.bottomLabel.setMaxHeight(baseHeight);
		this.bottomLabel.setWrapText(true);
		this.bottomLabel.setAlignment(Pos.CENTER);
		this.bottomLabel.setTextAlignment(TextAlignment.CENTER);
		this.bottomLabel.setLayoutX(0);
		this.getChildren().add(this.getChildren().indexOf(symLabel)+1,bottomLabel);



		this.prefWidthProperty().unbind();
		this.prefHeightProperty().unbind();
		this.prefWidthProperty().bind(flow.widthProperty());
		this.prefHeightProperty().bind(flow.heightProperty().add(baseHeightProperty.multiply(2)));
		this.maxHeightProperty().bind(flow.heightProperty().add(baseHeightProperty.multiply(2)));
		this.minHeightProperty().bind(flow.heightProperty().add(baseHeightProperty.multiply(2)));
//		this.prefHeightProperty().bind(flow.heightProperty().add(baseHeight*2));
//		this.maxHeightProperty().bind(flow.heightProperty().add(baseHeight*2));
//		this.minHeightProperty().bind(flow.heightProperty().add(baseHeight*2));
		this.flow.heightProperty().addListener(e->{
		//	System.out.println("flow.height changed::");
			redraw() ;
		});

		flow.setLayoutX(0);
//		flow.layoutYProperty().bind(baseHeightProperty);
//		flow.setLayoutY(baseHeightProperty.get());
//		flow.layoutYProperty().bind(baseHeightProperty);		//おそらくここでlayoutYの変更エラーが出る
		flow.translateYProperty().bind(baseHeightProperty);
//		flow.setLayoutY(baseHeight);
		this.getChildren().add(flow);
		this.setFocusTraversable(false);
/*
		this.clickPane.setOnMouseClicked((e)->{

			System.out.println("mouse pressed :"+this);
			if(e.getClickCount() == 2) {
				//設定を開く
				openSettingWindow();
			}
			if(e.isSecondaryButtonDown()) {
				menu.show(this,e.getScreenX() , e.getScreenY());
			}
			redraw();
		});
*/
	}

	public WhileSym() {
		this("条件式");
	}

	@Override
	public void execute(FBEExecutor exe) {
		Object con = exe.eval(this.options.get("条件"));
		if(options.get("タイプ").equals("前判定")) {
			while((boolean) con) {
				flow.execute(exe);
				con = exe.eval(this.options.get("条件"));
			}
		}else if(options.get("タイプ").equals("後判定")) {
			do {
				flow.execute(exe);
				con = exe.eval(this.options.get("条件"));
			}while((boolean) con);
		}else {

		}
	}

	@Override
	public void reflectOption() {
		String lbText = String.format("ループ%d\n %s の間", this.num, this.options.get("条件")) ;
		if(this.options.get("タイプ").equals("後判定")) {
			this.symLabel.setText("ループ"+this.num);
			this.bottomLabel.setText(lbText);
		}else {
			this.symLabel.setText(lbText);
			this.bottomLabel.setText("ループ"+this.num);
		}
		this.bottomLabel.setLayoutY(this.getHeight()-baseHeightProperty.get());
	}

	@Override
	public void redraw() {
		if(flow != null) {
			flow.layout();
			flow.requestLayout();
			flow.autosize();
		}
		super.redraw();
	}

	@Override
	public void draw() {

		if(flow != null) {
			//------------------------------------------------------------------??? 青の後に緑が来る

/*
			System.out.println("==============");
			System.out.println("this.h :"+this.getHeight());
			System.out.println("canvas.h :"+symCanvas.getHeight());
			System.out.println("flow.h :"+flow.getHeight());
			System.out.println("label.h :"+symLabel.getHeight());
*/
			GraphicsContext gc = symCanvas.getGraphicsContext2D() ;

			gc.setFill(itemFillColor);
			gc.setStroke(itemLineColor);
			gc.setLineWidth(itemLineWidth);
			double w = baseHeightProperty.get()*1/3;
			double bw = baseWidthProperty.get() ;
			double bh = baseHeightProperty.get() ;
			double blw = baseLineWidthProperty.get() ;

			/*0,0 baseWidth*baseHeight に上向き台形を描画*/
			double[] arrx = new double[]{w,bw-w,bw-blw,bw-blw,0+blw/2 ,0+blw/2} ;
			double[] arry = new double[]{0+blw/2,0+blw/2,w,bh-blw,bh-blw,w} ;
			gc.fillPolygon(arrx, arry, 6);
			gc.strokePolygon(arrx,arry,6);
/*
			gc.setFill(new Color(0.2, 0.2, 0.2, 0.5));
			gc.setStroke(Color.GREEN);
			gc.setLineWidth(itemLineWidth);
*/
			/*0,getHeight()-baseHeight baseWidth*baseHeight に上向き台形を描画*/
			arrx = new double[]{0+blw/2,bw-blw,bw-blw,bw-w,w,0+blw/2} ;
			arry = new double[]{0+blw/2+getHeight()-bh,0+blw/2+getHeight()-bh,bh-w+getHeight()-bh,bh-blw+getHeight()-bh,bh-blw+getHeight()-bh,bh-w+getHeight()-bh} ;
			gc.fillPolygon(arrx, arry, 6);
			gc.strokePolygon(arrx,arry,6);


//			System.out.println("green area:"+(getHeight()-baseHeight));
/*
			System.out.println("==============");
			System.out.println("this.h :"+this.getHeight());
			System.out.println("canvas.h :"+symCanvas.getHeight());
			System.out.println("flow.h :"+flow.getHeight());
			System.out.println("label.h :"+symLabel.getHeight());

			System.out.println();
*/
		}
	}
/*
	private void drawTrapezoid(GraphicsContext gc,double sx,double sy,double width,double height) {
		gc.fillRect(sx+itemLineWidth/2, sy+itemLineWidth/2, width-itemLineWidth, height-itemLineWidth);
		gc.strokeRect(sx+itemLineWidth/2, sy+itemLineWidth/2, width-itemLineWidth, height-itemLineWidth);
	}
*/

	public RoundFlow getFlow() {
		return flow;
	}

	@Override public void finalize() {
		cnt --;
	}

}
