package com.fbe.format;

import java.io.File;
import java.net.URL;
import java.util.List;

import com.fbe.item.Flow;

public abstract class FBEFormat {

	protected String ver ;
	protected String extension ;

	public FBEFormat(String ver,String extension) {
		this.ver = ver ;
		this.extension = extension ;
	}
	public abstract List<Flow> importFrom(URL url) throws Exception ;
	public abstract boolean save(String path,Flow mainFlow,List<Flow> flow) ;

	public List<Flow> importFrom(String path) throws Exception {
		return this.importFrom(new File(path));
	}
	public List<Flow> importFrom(File file) throws Exception {
		return this.importFrom(file.toURI().toURL());
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}


}
