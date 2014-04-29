package co.silbersoft.uprint.lib.domain;

import java.io.Serializable;

public class PrintJob implements Serializable {
	
	public PrintJob(String username) {
		this.username = username;
	}
		
	public String getUsername() {
		return this.username;
	}	
	
	private static final long serialVersionUID = -6671228483767518772L;

	private String username;
	
	
}
