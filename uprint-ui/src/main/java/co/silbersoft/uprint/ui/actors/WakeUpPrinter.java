package co.silbersoft.uprint.ui.actors;


import org.apache.log4j.Logger;

import co.silbersoft.uprint.lib.domain.PrintResult;
import co.silbersoft.uprint.lib.domain.PrintUIConfig;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class WakeUpPrinter extends UntypedActor {

	public static Props props(ActorSystem actorSystem) {
	    return Props.create(WakeUpPrinter.class, actorSystem);
	}
	
	public WakeUpPrinter(ActorSystem actorSystem) {
		this.actorSystem = actorSystem;
		ActorSelection actorSelection = actorSystem.actorSelection("akka.tcp://uprint-agent@127.0.0.1:2552/user/printSettings");
		actorSelection.tell(1, getSelf());		
	}
	
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof PrintResult) {
			PrintResult pj = (PrintResult) message; 
			log.debug("recieved Message! " + pj.getFile().getAbsolutePath());
		} else {
			log.debug("recieved Message, but not of PrintResult :(");
		}

	}

	private final ActorSystem actorSystem;
	private final Logger log = Logger.getLogger(WakeUpPrinter.class);
}
