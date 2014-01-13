package co.silbersoft.uprint.ui.actors;


import org.apache.log4j.Logger;

import co.silbersoft.uprint.lib.domain.PrintJob;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;

public class WakeUpPrinter extends UntypedActor {

	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof PrintJob) {
			PrintJob pj = (PrintJob) message; 
			log.debug("recieved Message! " + pj.getUsername());
		} else {
			log.debug("recieved Message, but not of PrintJob :(");
		}

	}

	private final Logger log = Logger.getLogger(WakeUpPrinter.class);
}
