package co.silbersoft.uprint.app;

import akka.actor.ActorRef;
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
        return ConfigFactory.load().withFallback(ConfigFactory.systemProperties());
    }   
    
    @Bean
    public UniversalPrinterSettings universalPrinterSettings() {
    	return new UniversalPrinterSettings(config());
    }
    
    
    
    @Bean LPDServer lpdServer(){
        LPDServer lpd = new LPDServer(universalPrinterSettings(), actorSystem());
        return lpd;
    }
    
    /*
     *  Actor System
     */
    @Bean
    public ActorSystem actorSystem() {
    	return ActorSystem.create("uprint-agent");
    }
    
    @Bean
    public ActorRef clientCommandActor() {
    	return actorSystem().actorOf(ClientCommandActor.props(universalPrinterSettings()), "printSettings");
    }
    
}
