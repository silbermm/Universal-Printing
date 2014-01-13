package co.silbersoft.uprint.actors;

import org.apache.log4j.Logger;

import co.silbersoft.uprint.lib.domain.PrintJob;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.AddressFromURIString;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class ClientCommandActor extends UntypedActor {
	
	public ClientCommandActor() {
		
	}
	
	
	@Override
	public void onReceive(Object message) throws Exception {
		log.debug("message received...");
		if(message instanceof String) {
			log.debug("it's a string!");
			String strMessage = (String) message; 
			if(strMessage.equals("wakeup")){
				log.debug("now trying to send a remote message!");
				PrintJob pj = new PrintJob("test");
				
				selection.tell(pj, getSelf());
				getContext().stop(getSelf());
			}
		}

	}
	
	ActorSelection selection = getContext().actorSelection("akka.tcp://uprint-ui@127.0.0.1:5150/user/wakeup");
	private static final Logger log = Logger.getLogger(ClientCommandActor.class);

}
