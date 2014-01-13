package co.silbersoft.uprint.ui.app;

import akka.actor.ActorSystem;
import co.silbersoft.uprint.ui.*;
import co.silbersoft.uprint.ui.actors.WakeUpPrinter;
import co.silbersoft.uprint.ui.dao.*;
import co.silbersoft.uprint.ui.models.*;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.swing.Action;

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
    
    @Bean
    public PrinterDao printDao() {
        return new PrinterDaoImpl(config());
    }

    @Bean
    public PrintButtonModel printAction() {
        return new PrintButtonModel(config());
    }

    @Bean
    public Action cancelAction() {
        return new CancelButtonModel();
    }
    
    @Bean
    public Action aboutAction() {
        return new AboutUsModel();
    }
    
    @Bean
    public Action getHelpAction(){
        return new GetHelpModel();
    }

    @Bean
    public PrintViewListModel printerListModel() {
        PrintViewListModel printers = new PrinterListModel(printAction());
        printers.setPrinterDao(printDao());
        return printers;
    }
    
    @Bean
    public PrintViewListModel buildingListModel() {
        PrintViewListModel buildings = new BuildingListModel(printerListModel());
        buildings.setPrinterDao(printDao());
        return buildings;
    }

    @Bean
    public PrintView printFrame() {
        PrintView printFrame = new PrintViewImpl(config());
        printFrame.setPrintModel(printAction());
        printFrame.setCancelModel(cancelAction());
        printFrame.setAboutModel(aboutAction());
        printFrame.setGetHelpModel(getHelpAction());
        printFrame.setBuildingListModel(buildingListModel());
        printFrame.setPrinterListModel(printerListModel());
        return printFrame;
    }
    
    @Bean
    public MenuTestView menuTest() {
        return new MenuTestView();
    }
    
    /*
     *  Actor System
     */
    @Bean
    public ActorSystem actorSystem() {
    	return ActorSystem.create("uprint-ui");
    }
      

    
}

