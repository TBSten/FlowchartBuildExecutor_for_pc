package com.fbe.exe;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FileEditController implements Initializable {
	@FXML Button bt_save ;
	@FXML Button bt_close ;
	@FXML TextField tf_fileName ;
	@FXML TextField tf_columns ;
	@FXML TextArea ta_value ;

	Stage window ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bt_close.setOnAction(e->{
			window.close();
		});
		bt_save.setOnAction(e->{
			FileApp.getInstance().saveFile(tf_fileName.getText(),tf_columns.getText(),ta_value.getText());
			window.close();
		});
	}


}
