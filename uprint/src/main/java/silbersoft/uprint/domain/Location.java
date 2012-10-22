package silbersoft.uprint.domain;

import java.util.List;

/**
 *
 * @author silbermm
 */
public interface Location extends Comparable{
    
    public void setName(String name);
    public String getName();
    
    public void setPrinters(List<Printer> printers);
    public List<Printer> getPrinters();
    
    public void addPrinter(Printer p);
    
}
