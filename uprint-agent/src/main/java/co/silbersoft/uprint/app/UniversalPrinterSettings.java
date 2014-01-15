package co.silbersoft.uprint.app;

import org.apache.log4j.Logger;

import akka.actor.ActorPath;
import co.silbersoft.uprint.lib.domain.PrintUIConfig;

import com.typesafe.config.Config;

public class UniversalPrinterSettings {

	public UniversalPrinterSettings(Config config) {
		this.config = config;
	}
	
	public void setActorPath(ActorPath actorPath) {
		logger.debug("setting actorPath to: " + actorPath);
		this.actorPath = actorPath;
	}
	
	public ActorPath getActorPath() {
		return actorPath;
	}
		
	
	public Config getConfig() {
		return this.config;
	}
	
	
	private Logger logger = Logger.getLogger(UniversalPrinterSettings.class);
	private ActorPath actorPath;
	private Config config;
	
}
