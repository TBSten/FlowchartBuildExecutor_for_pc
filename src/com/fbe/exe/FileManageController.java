package com.fbe.exe;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

public class FileManageController implements Initializable {
	@FXML ListView<String> list ;
	@FXML Button bt_new ;
	@FXML Button bt_edit ;
	@FXML Button bt_remove ;
	@FXML Button bt_close ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bt_close.setOnAction(e->{
			bt_remove.getScene().getWindow().hide();
		});
		bt_new.setOnAction(e->{
			FileApp.getInstance().openEditWindow();
		});
		bt_edit.setOnAction(e->{
			String fileName = list.getSelectionModel().getSelectedItem() ;
			FileApp.getInstance().openEditWindow(fileName,FileApp.getInstance().getColumns(fileName),FileApp.getInstance().getValue(fileName));
		});
		bt_remove.setOnAction(e->{
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"このファイルを削除しますか？");
			alert.showAndWait()
				.filter(response -> response == ButtonType.OK)
				.ifPresent(response -> {
					FileApp.getInstance().removeFile(list.getSelectionModel().getSelectedItem());
			});
		});
	}

}
