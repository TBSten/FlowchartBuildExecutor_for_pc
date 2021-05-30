package com.fbe.option;

import java.io.File;
import java.util.ArrayList;

import com.fbe.FBEApp;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class InputableFileChooser extends HBox implements Inputable {
	public enum Type {
		FILE,
		DIRECTORY;
	}

	ArrayList<FileChooser.ExtensionFilter> filters = new ArrayList<>();
	Type type = Type.FILE ;
	TextField tf = new TextField();
	Button bt = new Button("参照");
	public InputableFileChooser(Type t) {
		this();
		this.type = t ;
		new FileChooser.ExtensionFilter("すべてのファイル", "*.*");
	}
	public InputableFileChooser() {
		tf.prefWidthProperty().bind(this.widthProperty().subtract(bt.widthProperty()));
		tf.prefHeightProperty().bind(this.heightProperty());
		bt.prefHeightProperty().bind(this.heightProperty());

		bt.setOnAction(e->{
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().addAll(
					  this.filters
					);
			if(this.type == Type.FILE) {
				File f = fc.showOpenDialog(FBEApp.window);
				if(f != null) {
					tf.setText(f.getAbsolutePath());
				}

			}else if(this.type == Type.DIRECTORY){
				DirectoryChooser dc = new DirectoryChooser() ;
				File f = dc.showDialog(FBEApp.window);
				if(f != null) {
					tf.setText(f.getAbsolutePath());
				}

			}

		});
		this.getChildren().addAll(tf,bt);
	}

	@Override
	public void put(Object value) {
		tf.setText((String)value);
	}

	@Override
	public Object get() {
		return tf.getText() ;
	}

	@Override
	public String getString() {
		return String.valueOf(get());
	}

	@Override
	public void args(Object...obj) {
		if(obj[0] instanceof FileChooser.ExtensionFilter) {
			this.filters.add((FileChooser.ExtensionFilter)obj[0]);
		}
	}
}
