package silbersoft.uprint.domain;

//import edu.uc.labs.wirelessprinting.app.utils.jLpr;

/**
 *
 * @author silbermm
 */
public interface Printer extends Comparable{
     
    String getName();
    void setName(String name);
    
    String getLocation();
    void setLocation(String location);
    
    String getServer();
    void setServer(String server);
    
    String getQueue();
    void setQueue(String queue);
    
    int getID();
    void setID(int id);
    
    String getZone();
    void setZone(String zone);
    
    @Override
    public int compareTo(Object t);
    
    public String print(String filename, String jobname, String username);
}
