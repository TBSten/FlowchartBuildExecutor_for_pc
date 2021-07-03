package com.fbe.exe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import com.fbe.FBEApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FileApp {
	public static String rootDir = "./file/" ;
	private static final FileApp instance = new FileApp() ;
	private FileApp() {}

	private Stage manageWindow ;
	private ListView<String> manageList ;

	public void openManageWindow() {
		try {
			Stage st = new Stage() ;
			manageWindow = st ;
			st.setTitle("ファイルの管理");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("FileManage.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			FileManageController cont = loader.getController() ;
			manageList = cont.list ;
			List<File> files = getFiles();
			files.forEach(e->{
				cont.list.getItems().add(e.getName());
			});
			st.setScene(new Scene(root));
			st.show();
		}catch(Exception e) {
			e.printStackTrace();
			FBEApp.msgBox("エラーが発生しました");
		}
	}
	public void openEditWindow() {
		this.openEditWindow("新しいファイル.csv","列1,列2","");
	}
	public void openEditWindow(String fileName,String columns ,String value) {
		try {
			Stage st = new Stage();
			st.setTitle("ファイルの編集");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("FileEdit.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			FileEditController cont = loader.getController() ;
			cont.window = st ;
			cont.tf_fileName.setText(fileName);
			cont.tf_columns.setText(columns);
			cont.ta_value.setText(value);
			st.setScene(new Scene(root));
			st.show();
		}catch(Exception ex) {
			ex.printStackTrace();
			FBEApp.msgBox("エラーが発生しました。");
		}
	}
	public List<File> getFiles(){
		File[] files = new File(rootDir).listFiles();
		List<File> ans = new ArrayList<File>() ;
		ans.addAll(Arrays.asList(files));
		for(int i = 0;i < ans.size();i++) {
			File e = ans.get(i);
			System.out.println(e);
			if(e.getName().equals("_files.conf")) {
				ans.remove(e);
			}
		}
		return ans ;
	}
	public File getFile(String fileName) {
		File f = new File(rootDir+fileName);
		if(f.exists()) {
			return f ;
		}else {
			return null ;
		}
	}
	public void saveFile(String fileName,String columns,String value) {
		try {
			if(!this.manageList.getItems().contains(fileName)) {
				this.manageList.getItems().add(fileName);
			}
			BufferedWriter confWriter = new BufferedWriter(new FileWriter(this.getFile("_files.conf"),true));
			confWriter.write(fileName+":"+columns);
			confWriter.newLine();
			confWriter.flush();
			confWriter.close();
			//fileNameを上書き
			BufferedWriter bw = new BufferedWriter(new FileWriter(getRootDir()+fileName)) ;
			String[] str = value.split("\n");
			for(String line:str) {
				bw.write(line);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		}catch(Exception ex) {
			ex.printStackTrace();
			FBEApp.msgBox("エラーが発生したため、保存に失敗しました。");
		}
	}
	public boolean removeFile(String fileName) {
		this.manageList.getItems().remove(fileName);
		return this.getFile(fileName).delete();
	}
	public String getColumns(String fileName) {
		String ans = null ;
		try {
			if(this.getFile(fileName).exists()) {
				BufferedReader br = new BufferedReader(new FileReader(this.getFile("_files.conf")));
				String line ;
				while((line = br.readLine()) != null) {
					Matcher m = FBEApp.matcher(
							fileName.replace("(", "\\(").replace(")", "\\)")
							+":(.*)", line);
					if(m.matches()) {
						ans = m.group(1) ;
					}
					System.out.println(line+":"+m.matches());
				}
				br.close();
			}else {
				ans = "" ;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ans ;
	}
	public String getValue(String fileName) {
		String ans = "" ;
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.getFile(fileName)));
			String line ;
			while((line = br.readLine()) != null) {
				ans += line + "\n" ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			FBEApp.msgBox("エラーが発生しました。");
		}
		return ans ;
	}

	public static String getRootDir() {
		return rootDir;
	}

	public static FileApp getInstance() {
		return instance;
	}

}
