package silbersoft.uprint.dao;

import java.util.List;
import silbersoft.uprint.domain.Location;
import silbersoft.uprint.domain.Printer;

/**
 *
 * @author Matt Silbernagel
 */
public interface PrinterDao {
    
    public List<Location> getBuildings();
    public List<Printer> getPrintersByBuilding(String building);
    
    public void reset();
    
}
