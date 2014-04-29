package co.silbersoft.uprint.lib.domain;

import java.io.File;
import java.io.Serializable;

public class PrintResult implements Serializable {

	public PrintResult(File file, String error) {
		this.file = file;
		this.error = error;
	}
	
	public String getError() {
		return this.error;
	}
	
	public File getFile() {
		return this.file;
	}
	
	
	private static final long serialVersionUID = -1239311350722598133L;	
	private File file = null;
	private String error = null;
	
	
	
	
}
