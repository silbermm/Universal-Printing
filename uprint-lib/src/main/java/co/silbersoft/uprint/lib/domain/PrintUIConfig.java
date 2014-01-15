package co.silbersoft.uprint.lib.domain;

import java.io.Serializable;

public class PrintUIConfig implements Serializable {

	private static final long serialVersionUID = -7440143701401237705L;

	public PrintUIConfig(String host, String port, String actorPath) {
		this.host = host;
		this.port = port;
		this.actorPath = actorPath;
	}
	
	public String getHost() {
		return this.host;
		
	}
	
	public String getPort() {
		return this.port;
	}
	
	public String getActorPath() {
		return this.actorPath;
	}
		
	@Override
	public String toString() {
		return new StringBuilder("HOST: ")
			.append(this.getHost())
			.append("PORT: ")
			.append(this.getPort())
			.append("ActorPath: ")
			.append(this.getActorPath())
			.toString();		
	}
	
	private String port;
	private String host;
	private String actorPath;
	
}
