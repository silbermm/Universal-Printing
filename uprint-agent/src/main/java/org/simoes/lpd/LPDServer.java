/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simoes.lpd;

import akka.actor.ActorSystem;

import com.typesafe.config.Config;

import org.apache.log4j.Logger;
import org.simoes.lpd.handler.QueueMonitor;
import org.simoes.lpd.ui.PrintJobTableModel;
import org.simoes.lpd.util.PrintQueue;
import org.simoes.lpd.util.Queues;


/**
 *
 * @author silbermm
 */
public class LPDServer {

    static Logger log = Logger.getLogger(LPD.class);
        
    
    public LPDServer(Config config, ActorSystem actorSystem){
        this.config = config;
        this.actorSystem = actorSystem;
    }

    public void start() {                
        log.debug("LPDServer(): STARTED");
        final String rawQueueName = "RAW";        
        try {
            // Initialize data model and Print Queue
            PrintJobTableModel pjtm = new PrintJobTableModel();
            Queues queues = Queues.getInstance();
            // create the PrintQueue
            PrintQueue rawQueue = queues.createQueueWithTableModel(rawQueueName, pjtm);
            // initialize the TableModel by making it aware of it's data source, the PrintQueue
            pjtm.setPrintQueueDataModel(rawQueue);

            // Run the LPD spooler
            Thread lpdThread = new Thread(LPD.getInstance(config.getInt("lpd.port")));
            lpdThread.start();

            // Setup the QueueMontior to process the rawQueue
            QueueMonitor rawQueueMonitor = new QueueMonitor(rawQueueName, config, actorSystem);
            Thread rawQueueMonitorThread = new Thread(rawQueueMonitor);
            rawQueueMonitorThread.start();

        } catch (Exception e) {
            log.fatal(e.getMessage(), e);
        }
        log.debug("LPDServer(): FINSHED");
    }    
     
    private Config config;  
    private ActorSystem actorSystem;

}
