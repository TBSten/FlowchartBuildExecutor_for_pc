package com.fbe.sym;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class TemplateNewController implements Initializable {
	@FXML ComboBox<String> cb ;
	@FXML TextField tf ;
	@FXML Button okB ;
	@FXML Button canB ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
