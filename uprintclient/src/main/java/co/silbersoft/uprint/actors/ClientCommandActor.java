package co.silbersoft.uprint.actors;

import org.apache.log4j.Logger;

import akka.actor.Props;
import akka.actor.UntypedActor;
import co.silbersoft.uprint.app.UniversalPrinterSettings;

public class ClientCommandActor extends UntypedActor {
	
	
	public static Props props(UniversalPrinterSettings printerSettings) {
	    return Props.create(ClientCommandActor.class, printerSettings);
	}
			
	public ClientCommandActor(UniversalPrinterSettings printerSettings) {
		this.printerSettings = printerSettings;	
	}
	
	
	@Override
	public void onReceive(Object message) throws Exception {		
		if(message instanceof String){
			String user = (String) message;
			printerSettings.addUserPath(user, this.context().sender().path());			
		} else {
			log.debug("message recieved but not a string");
		}
	}
	
	private UniversalPrinterSettings printerSettings;
	private static final Logger log = Logger.getLogger(ClientCommandActor.class);

}
