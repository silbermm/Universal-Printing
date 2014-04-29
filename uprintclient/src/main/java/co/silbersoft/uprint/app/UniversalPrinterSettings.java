package co.silbersoft.uprint.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import akka.actor.ActorPath;

import com.typesafe.config.Config;

public class UniversalPrinterSettings {

	public UniversalPrinterSettings(Config config) {
		this.config = config;
	}
	
	public void setActorPath(ActorPath actorPath) {
		logger.debug("setting actorPath to: " + actorPath);
		this.actorPath = actorPath;
	}
	
	public void addUserPath(String user, ActorPath path){
		if(userPathMap.containsKey(user)){
			logger.debug("there is already a key for that user, updating with the new path");
			userPathMap.remove(user);
			userPathMap.put(user, path);
		} else {
			logger.debug("creating a new key");
			userPathMap.put(user, path);
		}
	}
	
	public ActorPath getUserPath(String user){
		if(userPathMap.containsKey(user)){
			return userPathMap.get(user);
		} else {
			return null;
		}
	}
	
	public ActorPath getActorPath() {
		return actorPath;
	}
	
	public Config getConfig() {
		return this.config;
	}
	
	
	private Logger logger = Logger.getLogger(UniversalPrinterSettings.class);
	private Map<String,ActorPath> userPathMap = new HashMap<String,ActorPath>();
	private ActorPath actorPath;
	private Config config;
	
}
