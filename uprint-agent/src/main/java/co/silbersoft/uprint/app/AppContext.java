package co.silbersoft.uprint.app;

import akka.actor.ActorSystem;
import co.silbersoft.uprint.actors.ClientCommandActor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.swing.Action;

import org.simoes.lpd.LPDServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * Setup the Spring IOC Container
 *
 * @author Matt Silberangel
 */
@Configuration
public class AppContext {
	
    @Bean
    public Config config() {
        return ConfigFactory.load();
    }           
    
    @Bean LPDServer lpdServer(){
        LPDServer lpd = new LPDServer(config(), actorSystem());
        //lpd.setBuildingList(buildingListModel());
        //lpd.setPrintButtonModel(printAction());
        //lpd.setPrintView(printFrame());
        return lpd;
    }
    
    /*
     *  Actor System
     */
    @Bean
    public ActorSystem actorSystem() {
    	return ActorSystem.create("uprint-agent");
    }
    
}
