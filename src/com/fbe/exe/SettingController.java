package com.fbe.exe;

import java.net.URL;
import java.util.ResourceBundle;

import com.fbe.exe.factory.ExecutorFactory;
import com.fbe.option.OptionTable;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SettingController implements Initializable {

	@FXML public HBox hb_viewType ;
	@FXML Label lb_desc ;
	@FXML ScrollPane sp_option ;
	@FXML Button bt_ok ;
	@FXML Button bt_can ;

	public FBEExecutor exe ;
	public ExecutorFactory<?> fact ;
	public Stage stage ;
	public boolean status = false ;
	private OptionTable optionTable ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bt_ok.setOnAction(e->{
			status = true ;
			if(this.optionTable != null && this.fact != null) {
				this.fact.reflectOptionTable(optionTable);
			}
			if(stage != null) {
				stage.close();
			}
		});
		bt_can.setOnAction(e->{
			status = false ;
			if(stage != null) {
				stage.close();
			}
		});

	}

	public void setFactory(ExecutorFactory<?> fact) {
		if(this.fact != null) {
			this.fact.toUnSelectMode();
		}
		this.fact = fact ;
		fact.toSelectMode();
		//lb_descやsp_optionに値などを設定
		lb_desc.setText(fact.getDescription());
		OptionTable table = fact.createOptionTable();
		sp_option.viewportBoundsProperty().addListener(e->{
			table.setPrefWidth(sp_option.getViewportBounds().getWidth());
			table.setMaxWidth(sp_option.getViewportBounds().getWidth());
			table.setMinWidth(sp_option.getViewportBounds().getWidth());
		});
		sp_option.setContent(table);
		this.optionTable = table ;
	}
	public ExecutorFactory<?> getFactory(){
		return this.fact ;
	}

}
