package silbersoft.uprint.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author silbermm
 */
public class PrinterLocation implements Location {

    public PrinterLocation(){
        
    }
    
    public PrinterLocation(String name){
        this.name = name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addPrinter(Printer p){
        this.printers.add(p);
    }
    
    @Override
    public void setPrinters(List<Printer> printers) {
        this.printers = printers;
    }

    @Override
    public List<Printer> getPrinters() {
        return printers;
    }

    @Override
    public int compareTo(Object t) {
        Location l = (Location) t;
        return(getName().compareTo(l.getName()));
    }
    
    private String name;
    private List<Printer> printers = new ArrayList<Printer>();
    
}
