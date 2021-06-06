package com.fbe.sym;

import java.util.ArrayList;
import java.util.List;

import com.fbe.FBEApp;
import com.fbe.exe.FBEExecutor;
import com.fbe.item.Item;
import com.fbe.item.RoundFlow;
import com.fbe.option.OptionTable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public abstract class BranchSym extends Sym {

	protected List<RoundFlow> flows = new ArrayList<>() ;
	public HBox flowHb = new HBox() ;
	protected double betweenFlow = 5 ;

	public BranchSym(String condition) {
		super() ;
		this.optionPut("条件", "分岐処理の条件を指定します。YesNo分岐の時は条件式、多分岐の時は変数を指定します。", OptionTable.Type.TEXTFIELD, condition);

		MenuItem i = new MenuItem("フローを追加");
		i.setOnAction(e ->{
			RoundFlow flow = new RoundFlow() ;
			flow.setTag(flows.size()+1+"");
			this.addFlow(flow);
		});
		menu.getItems().add(i);


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

		this.prefWidthProperty().unbind();
		this.prefHeightProperty().unbind();
		this.prefWidthProperty().bind(flowHb.widthProperty());
		this.maxWidthProperty().bind(flowHb.widthProperty());
		this.minWidthProperty().bind(Item.baseWidthProperty);
		this.prefHeightProperty().bind(flowHb.heightProperty().add(baseHeightProperty.multiply(2)));
		this.maxHeightProperty().bind(flowHb.heightProperty().add(baseHeightProperty.multiply(2)));
		this.minHeightProperty().bind(flowHb.heightProperty().add(baseHeightProperty.multiply(2)));
		this.flowHb.widthProperty().addListener(e->{
			redraw() ;
		});
		this.flowHb.heightProperty().addListener(e->{
			redraw() ;
		});

		flowHb.setLayoutX(0);
		flowHb.translateYProperty().bind(baseHeightProperty);
		this.getChildren().add(flowHb);
		this.setFocusTraversable(false);

	}

	@Override
	public void execute(FBEExecutor exe) {
		RoundFlow flow = this.decideFlow(exe);
		exe.getExecuteList().addAll(exe.getExecuteList().indexOf(this),flow.getSyms());
	}

	//"条件"オプションをもとに実行するRoundFlowをflowsの中から返します。
	public abstract RoundFlow decideFlow(FBEExecutor exe) ;

	@Override
	public void redraw() {
		for(RoundFlow f:flows) {
			f.redraw();
		}
		this.flowHb.setSpacing(this.betweenFlow);
		this.flowHb.requestLayout();
		super.redraw() ;
	}

	@Override
	public void reflectOption() {
		this.symLabel.setText(this.optionGet("条件"));
	}

	@Override
	public void draw() {
		GraphicsContext gc = this.symCanvas.getGraphicsContext2D() ;
		/*
		gc.setFill(Item.baseFillColorProperty.get());
		gc.setStroke(Item.baseLineColorProperty.get());
		gc.setLineWidth(Item.baseWidthProperty.get());
		*/
		gc.setFill(itemFillColor);
		gc.setStroke(itemLineColor);
		gc.setLineWidth(itemLineWidth);


		double bw = baseWidthProperty.get() ;
		double bh = baseHeightProperty.get() ;
		double blw = baseLineWidthProperty.get() ;
		double w = getWidth() ;
		double h = getHeight() ;
		double bf = betweenFlow ;
		double work = 0 ;

		for(int i = 0;i < Math.max(flows.size(), 1);i++) {
			double sx = work + bw/2 ;
			double sy = bh/2 ;
			if(i == 0) {
				sy = bh ;
			}
			gc.strokeLine(sx, sy, sx, sy+h-bh);
			double fontSize = 18 ;
			gc.setFill(itemLineColor);
			gc.setFont(Font.font(fontSize));
			if(flows.size() > i) {
				String text = flows.get(i).getTag() ;
				gc.fillText(text, work+3, bh+fontSize);
			}


			if(i < flows.size()-1) {
				work += flows.get(i).getWidth() + bf ;
			}

		}
		gc.strokeLine(bw,bh/2,work+bw/2,bh/2);
		gc.strokeLine(bw/2,h-bh/2,work+bw/2,h-bh/2);		//矢印つけるならこの直後

		gc.setFill(itemFillColor);
		gc.setStroke(itemLineColor);
		gc.setLineWidth(itemLineWidth);
		double[] x = {bw/2,bw-blw/2,bw/2,0+blw/2} ;
		double[] y = {0+blw/2,bh/2,bh-blw/2,bh/2} ;
		gc.fillPolygon(
				x ,
				y ,x.length);
		gc.strokePolygon(
				x ,
				y ,x.length);

	}

	public void addFlow(RoundFlow flow) {
		flow.setOnDisabled(()->{
			this.removeFlow(flow);
		});
		this.flows.add(flow);
		this.flowHb.getChildren().add(flow);
		this.redraw();
	}
	public void removeFlow(RoundFlow flow) {
		if(flows.size() >= 2) {
			this.flows.remove(flow);
			this.flowHb.getChildren().remove(flow);
			this.redraw();
		}else {
			FBEApp.msgBox("分岐処理は常に1つ以上でなければいけません。");
		}
	}

	public List<RoundFlow> getFlowsForReference() {
		List<RoundFlow> ans = new ArrayList<>();
		for(RoundFlow f:this.flows) {
			ans.add(f);
		}
		return ans;
	}

}
