package com.fbe.item;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public abstract class Item extends AnchorPane {

	public static final Map<Long,Item> items = new LinkedHashMap<>();
	public static long itemCnt = 0;

//	public static DoubleProperty baseWidthProperty = new SimpleDoubleProperty(180);
//	public static DoubleProperty baseHeightProperty = new SimpleDoubleProperty(40);
	public static DoubleProperty baseWidthProperty = new SimpleDoubleProperty(160);
	public static DoubleProperty baseHeightProperty = new SimpleDoubleProperty(42);
	public static DoubleProperty baseLineWidthProperty = new SimpleDoubleProperty(3);
	public static double fontSize = 10.5 ;

	public static ObjectProperty<Color> baseLineColorProperty = new SimpleObjectProperty<>(Color.BLACK) ;
	public static ObjectProperty<Color> baseFillColorProperty = new SimpleObjectProperty<>(Color.WHITE) ;

	public static ObjectProperty<Color> selectLineColorProperty = new SimpleObjectProperty<>(Color.BLUE) ;
	public static ObjectProperty<Color> selectFillColorProperty = new SimpleObjectProperty<>(Color.WHITE) ;

	public static ObjectProperty<Color> exeLineColorProperty = new SimpleObjectProperty<>(Color.RED) ;
	public static ObjectProperty<Color> exeFillColorProperty = new SimpleObjectProperty<>(Color.WHITE) ;


//	protected Label symLabel = new Label("[DEFAULT]");
//	protected Canvas symCanvas = new Canvas();
	public Label symLabel = new Label("[DEFAULT]");
	public Canvas symCanvas = new Canvas();
	protected Flow parentFlow = null ;
	public double itemLineWidth = baseLineWidthProperty.get() ;
	public Color itemLineColor = baseLineColorProperty.get() ;
	public Color itemFillColor = baseFillColorProperty.get() ;
	public long itemId ;


	public Item() {
		items.put(itemCnt, this);
		this.itemId = itemCnt ;
		itemCnt ++;

//		symLabel.setStyle("-fx-background-color:red;");

		//????????????
		this.symLabel.setLineSpacing(-5);
		this.symLabel.setWrapText(true);
		this.symLabel.setAlignment(Pos.CENTER);
		this.symLabel.setTextAlignment(TextAlignment.CENTER);

		this.symLabel.setFont(Font.font(fontSize));

		//???????????????
		this.widthProperty().addListener( e -> {
			redraw();
		} );
		this.heightProperty().addListener( e -> {
			redraw();
		} );
/*
		this.heightProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				redraw();
			}
		});
*/
		this.widthProperty().addListener(new ChangeListener<Number>(){
			@Override public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				redraw();
			}
		});
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				redraw();
			}
		});
		this.symLabel.prefWidthProperty().bind(this.widthProperty());
		this.symLabel.prefHeightProperty().bind(this.heightProperty());
		this.symLabel.minWidthProperty().bind(this.widthProperty());
		this.symLabel.minHeightProperty().bind(this.heightProperty());
		this.symLabel.maxWidthProperty().bind(this.widthProperty());
		this.symLabel.maxHeightProperty().bind(this.heightProperty());
		this.symCanvas.widthProperty().bind(this.widthProperty());
		this.symCanvas.heightProperty().bind(this.heightProperty());
//		this.widthProperty().addListener( e ->{redraw();});
//		this.heightProperty().addListener( e ->{redraw();});

//		this.setWidth(baseWidth);
//		this.setHeight(baseHeight);

		this.prefWidthProperty().bind(baseWidthProperty);
		this.prefHeightProperty().bind(baseHeightProperty);
		this.maxWidthProperty().bind(baseWidthProperty);
		this.maxHeightProperty().bind(baseHeightProperty);
		this.minWidthProperty().bind(baseWidthProperty);
		this.minHeightProperty().bind(baseHeightProperty);

		baseWidthProperty.addListener(e->{ redraw(); });
		baseHeightProperty.addListener(e->{ redraw(); });


		this.getChildren().add(this.symCanvas);
		this.getChildren().add(this.symLabel);

		/*
		this.focusedProperty().addListener(e->{
			if(isFocused() && this.isFocusTraversable() ) {
				changeFocusedDesign();
				System.out.println("focused:"+this);
			}else {
				changeUnfocusedDesign();
				System.out.println("unfocused:"+this);
			}
			redraw();
		});
		*/

	//	redraw();

	}
/*
	protected void changeFocusedDesign() {
		this.itemLineColor = focusedLineColor;
		this.itemFillColor = focusedFillColor;
	}

	protected void changeUnfocusedDesign() {
		this.itemLineColor = baseLineColor;
		this.itemFillColor = baseFillColor;

	}
*/
	public void redraw() {
/*
		this.setWidth(this.getPrefWidth());
		this.setHeight(this.getPrefHeight());
*/
		this.autosize();
		for(Node child:getChildren()) {
			child.autosize();
		}
		if(this.getParent() != null) {
			this.getParent().requestLayout();
			this.requestParentLayout();
		}
		if(this.parentFlow != null) {
			this.parentFlow.requestLayout();
		}
		this.requestLayout();

		GraphicsContext gc = symCanvas.getGraphicsContext2D() ;
		gc.clearRect(0, 0, getWidth(), getHeight());
		draw();


	}

	protected void setText(String text) {
		this.symLabel.setText(text);
	}
	protected String getText() {
		return this.symLabel.getText();
	}



	public abstract void draw() ;

	public void toBaseLook() {
		this.itemLineColor = baseLineColorProperty.get();
		this.itemFillColor = baseFillColorProperty.get();
	}
	public void toSelectLook() {
		this.itemLineColor = selectLineColorProperty.get();
		this.itemFillColor = selectFillColorProperty.get();
	}
	public void toExeLook() {
		this.itemLineColor = exeLineColorProperty.get();
		this.itemFillColor = exeFillColorProperty.get();
	}

	@Override public String toString() {
		return this.getClass().getSimpleName()+"["+symLabel.getText()+"]" ;
	}

	public void setParentFlow(Flow f) {
		this.parentFlow = f ;
	}
	public Flow getParentFlow() {
		return this.parentFlow ;
	}

	public Node getExportView() {
//		changeUnfocusedDesign();
		this.toBaseLook();
		redraw();
		WritableImage wi = this.snapshot();
		ImageView iv = new ImageView(wi);
		return iv ;
	}

	@Override public WritableImage snapshot(SnapshotParameters sp,WritableImage wi) {
		this.setManaged(true);
		redraw();
		WritableImage ans = super.snapshot(sp, wi) ;
		/*
		System.out.println("snapshot :"+ans.getWidth()+"*"+ans.getHeight());
		System.out.println("         :"+ans.getRequestedWidth()+"*"+ans.getRequestedHeight());
		System.out.println("  symLabel :"+symLabel.getWidth()+"*"+symLabel.getHeight());
		System.out.println("           :"+symLabel.getText());
		System.out.println("  sp.depth :"+sp.isDepthBuffer());
		for(Node n :getChildren()) {
			System.out.println("  childs   :"+n);
		}
		*/
		return ans ;
	}
	public WritableImage snapshot() {
		SnapshotParameters sp = new SnapshotParameters() ;
		sp.setDepthBuffer(true);
		sp.setFill(new Color(1,1,1,0));
		return this.snapshot(sp, null);
	}


}
