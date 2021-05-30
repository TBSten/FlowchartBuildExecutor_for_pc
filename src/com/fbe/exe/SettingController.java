package com.fbe.exe;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class SettingController implements Initializable {

	@FXML HBox hb_viewType ;
	@FXML Label lb_desc ;
	@FXML ScrollPane sp_option ;

	public FBEExecutor exe ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
