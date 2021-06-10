
package com.fbe.sym;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fbe.exe.FBEExecutable;
import com.fbe.exe.FBEExecutor;
import com.fbe.item.RoundFlow;
import com.fbe.option.OptionTable;

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

	public RoundFlow flow ;
	Label bottomLabel = new Label("TEST");
	int num ;

	public WhileSym(String condition,RoundFlow flow) {
		super();
		this.flow = flow ;

		init(condition);
	}
	public WhileSym(String condition) {
		this(condition,new RoundFlow());
	}

	protected void init(String condition) {
		this.num = cnt ;
		this.optionPut("条件", "繰り返し処理の条件式を指定します。", OptionTable.Type.TEXTFIELD, condition);
		this.optionPut("タイプ", "どのタイミングで条件を判定するかを指定します。", OptionTable.Type.COMBOBOX, "前判定");
		this.getOptionsValueList().put("タイプ",Arrays.asList("前判定","後判定"));

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
		this.symLabel.setLayoutX(0);
		this.symLabel.setLayoutY(0);


		this.bottomLabel.prefWidthProperty().bind(baseWidthProperty);
		this.bottomLabel.prefHeightProperty().bind(baseHeightProperty);
		this.bottomLabel.minWidthProperty().bind(baseWidthProperty);
		this.bottomLabel.minHeightProperty().bind(baseHeightProperty);
		this.bottomLabel.maxWidthProperty().bind(baseWidthProperty);
		this.bottomLabel.maxHeightProperty().bind(baseHeightProperty);
		this.bottomLabel.setWrapText(true);
		this.bottomLabel.setAlignment(Pos.CENTER);
		this.bottomLabel.setTextAlignment(TextAlignment.CENTER);
		this.bottomLabel.setLayoutX(0);
		this.getChildren().add(this.getChildren().indexOf(symLabel)+1,bottomLabel);



		this.prefWidthProperty().unbind();
		this.prefHeightProperty().unbind();
		this.maxWidthProperty().unbind();
		this.maxHeightProperty().unbind();
		this.minWidthProperty().unbind();
		this.minHeightProperty().unbind();
		this.prefWidthProperty().bind(flow.widthProperty());
		this.maxWidthProperty().bind(flow.widthProperty());
		this.minWidthProperty().bind(flow.widthProperty());
		this.prefHeightProperty().bind(flow.heightProperty().add(baseHeightProperty.multiply(2)));
		this.maxHeightProperty().bind(flow.heightProperty().add(baseHeightProperty.multiply(2)));
		this.minHeightProperty().bind(flow.heightProperty().add(baseHeightProperty.multiply(2)));
		this.flow.heightProperty().addListener(e->{
			redraw() ;
		});

		flow.setLayoutX(0);
		flow.translateYProperty().bind(baseHeightProperty);
		flow.setAbleToDisable(false);
		this.getChildren().add(flow);
		this.setFocusTraversable(false);

	}



	public WhileSym() {
		this("条件式");
	}

	@Override
	public void execute(FBEExecutor exe) {
		Object con = exe.eval(this.optionGet("条件"));
		if(optionGet("タイプ").equals("前判定")) {
			if((boolean)con) {
				List<FBEExecutable> exeList = exe.getExecuteList() ;
				int idx = exeList.indexOf(this);
				List<FBEExecutable> list = new ArrayList<>() ;
				list.addAll(this.getFlow().getSyms());
				list.add(this );
				exeList.addAll(idx+1, list);
			}
		}else if(optionGet("タイプ").equals("後判定")) {
			if((boolean)con || exe.executeOptions.get(this) == null) {
				exe.executeOptions.put(this,"前判定");
				List<FBEExecutable> exeList = exe.getExecuteList() ;
				int idx = exeList.indexOf(this);
				List<FBEExecutable> list = new ArrayList<>() ;
				list.addAll(this.getFlow().getSyms());
				list.add(this);
				exeList.addAll(idx+1,list);
			}
		}else {
		}

	}

	@Override
	public void reflectOption() {
		String lbText = String.format("ループ%d\n %s の間", this.num, this.optionGet("条件")) ;
		if(this.optionGet("タイプ").equals("後判定")) {
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
			flow.redraw();
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


	public RoundFlow getFlow() {
		return flow;
	}
	public void setFlow(RoundFlow flow) {
		this.flow = flow;

	}


	@Override
	public void onAddFlow() {
		cnt ++;
	}
	@Override
	public void onRemoveFlow() {
		cnt --;
	}

}
