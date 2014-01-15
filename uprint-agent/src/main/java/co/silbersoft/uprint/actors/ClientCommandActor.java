package co.silbersoft.uprint.actors;

import org.apache.log4j.Logger;

import com.typesafe.config.Config;

import co.silbersoft.uprint.app.UniversalPrinterSettings;
import co.silbersoft.uprint.lib.domain.PrintJob;
import co.silbersoft.uprint.lib.domain.PrintUIConfig;
import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.AddressFromURIString;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.remote.RemoteActorRef;

public class ClientCommandActor extends UntypedActor {
	
	
	public static Props props(UniversalPrinterSettings printerSettings) {
	    return Props.create(ClientCommandActor.class, printerSettings);
	}
			
	public ClientCommandActor(UniversalPrinterSettings printerSettings) {
		this.printerSettings = printerSettings;	
	}
	
	
	@Override
	public void onReceive(Object message) throws Exception {									
		printerSettings.setActorPath(this.context().sender().path());
	}
	
	private UniversalPrinterSettings printerSettings;
	private static final Logger log = Logger.getLogger(ClientCommandActor.class);

}
