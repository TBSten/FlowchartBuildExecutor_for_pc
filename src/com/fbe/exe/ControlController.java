package com.fbe.exe;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ControlController implements Initializable {

	@FXML Button bt_exe ;
	@FXML Button bt_finish ;

	public FBEExecutor exe ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bt_exe.setOnAction(e->{
			exe.start();
		});
		bt_finish.setOnAction(e->{
			exe.finish();
		});
	}


}
