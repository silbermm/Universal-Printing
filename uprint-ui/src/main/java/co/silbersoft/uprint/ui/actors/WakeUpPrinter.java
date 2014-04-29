package co.silbersoft.uprint.ui.actors;


import org.apache.log4j.Logger;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import co.silbersoft.uprint.lib.domain.PrintResult;
import co.silbersoft.uprint.lib.utils.PrintUtils;
import co.silbersoft.uprint.ui.PrintView;
import co.silbersoft.uprint.ui.models.BuildingListModel;
import co.silbersoft.uprint.ui.models.PrintButtonModel;
import co.silbersoft.uprint.ui.models.PrintViewListModel;

public class WakeUpPrinter extends UntypedActor {

	public static Props props(ActorSystem actorSystem, PrintButtonModel printButton, PrintView printView, PrintViewListModel buildingList) {
	    return Props.create(WakeUpPrinter.class, actorSystem, printButton, printView, buildingList);
	}
	
	public WakeUpPrinter(ActorSystem actorSystem, PrintButtonModel printButton, PrintView printView, BuildingListModel buildingList) {
		this.actorSystem = actorSystem;
		this.printButton = printButton;
		this.printView = printView;
		this.buildingList = buildingList;
		ActorSelection actorSelection = actorSystem.actorSelection("akka.tcp://uprint-agent@127.0.0.1:2552/user/printSettings");
		actorSelection.tell(System.getProperty("user.name"), getSelf());		
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof PrintResult) {
			PrintResult pj = (PrintResult) message; 
			log.debug("recieved Message! " + pj.getFile().getAbsolutePath());						
			printButton.setCurrentFile(pj.getFile().getAbsolutePath());
			printView.showFrame();
			buildingList.buildList("all");						
		} else if(message instanceof Integer) {
			log.debug("recieved an Integer Message");
			int msg = (Integer) message;
			if (PrintUtils.WAKE_UP == msg){
				ActorSelection actorSelection = actorSystem.actorSelection("akka.tcp://uprint-agent@127.0.0.1:2552/user/printSettings");
				actorSelection.tell(System.getProperty("user.name"), getSelf());		
			}
		} else {
			log.debug("recieved Message, but not of PrintResult :(");
		}
	}

	private final ActorSystem actorSystem;
	private PrintButtonModel printButton;
	private PrintView printView;
	private BuildingListModel buildingList;
	private final Logger log = Logger.getLogger(WakeUpPrinter.class);
}
