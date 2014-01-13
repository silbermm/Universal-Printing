package co.silbersoft.uprint.ui.domain;

import java.io.IOException;

import org.apache.log4j.Logger;

import co.silbersoft.uprint.lib.utils.jLpr;

/**
 *
 * @author Matt Silbernagel
 */
public class UniversalPrinter implements Printer {

    public UniversalPrinter(){
        this.jLpr = new jLpr();
        jLpr.setPrintRaw(true);
        jLpr.setUseOutOfBoundPorts(true);
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getServer() {
        return this.server;
    }

    @Override
    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String getQueue() {
       return this.queue;
    }

    @Override
    public void setQueue(String queue) {
        this.queue = queue;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public String getZone() {
        return this.zone;
    }

    @Override
    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public int compareTo(Object t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String print(String filename, String jobname, String username) {
        String returnVal = "";
        jLpr.setUsername(username);
        try {
            log.debug("printing " + filename + " to " + this.server + " at " + this.queue);
            jLpr.printFile(filename, this.server, this.queue, jobname);
        } catch (IOException ex) {
            returnVal = ex.getMessage();
        } catch (InterruptedException ex) {
            returnVal = ex.getMessage();
        }
        return returnVal;
    }
    
    private static final Logger log = Logger.getLogger(UniversalPrinter.class);
    private String name;
    private String server;
    private String queue;
    private String location;
    private int id;
    private String zone;
    private jLpr jLpr;
    
}
