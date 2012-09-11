/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simoes.lpd;

import com.typesafe.config.Config;
import org.apache.log4j.Logger;
import org.simoes.lpd.handler.QueueMonitor;
import org.simoes.lpd.ui.PrintJobTableModel;
import org.simoes.lpd.util.PrintQueue;
import org.simoes.lpd.util.Queues;
import silbersoft.ucprint.ui.PrintView;
import silbersoft.ucprint.ui.models.PrintButtonModel;
import silbersoft.ucprint.ui.models.PrintViewListModel;


/**
 *
 * @author silbermm
 */
public class LPDServer {

    static Logger log = Logger.getLogger(LPD.class);
        
    
    public LPDServer(Config config){
        this.config = config;
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
            QueueMonitor rawQueueMonitor = new QueueMonitor(rawQueueName, config, printButton, buildingList, printerView);
            Thread rawQueueMonitorThread = new Thread(rawQueueMonitor);
            rawQueueMonitorThread.start();

            //TODO: implement a way to end gracefully
        } catch (Exception e) {
            log.fatal(e.getMessage(), e);
        }
        log.debug("LPDServer(): FINSHED");
    }    
    
    public void setPrintButtonModel(PrintButtonModel printButton){
        this.printButton = printButton;
    }
    
    public void setBuildingList(PrintViewListModel buildingList){
        this.buildingList = buildingList;
    }
    
    public void setPrintView(PrintView printView){
        this.printerView = printView;
    }
    
    private Config config;
    private PrintButtonModel printButton;
    private PrintViewListModel buildingList;
    private PrintView printerView;

}
