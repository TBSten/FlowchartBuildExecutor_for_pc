package com.fbe.sym;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class TemplateManageController implements Initializable{
	@FXML ListView<String> list ;
	@FXML Button btn_new ;
	@FXML Button btn_edit ;
	@FXML Button btn_delete ;
	@FXML Button btn_close ;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
