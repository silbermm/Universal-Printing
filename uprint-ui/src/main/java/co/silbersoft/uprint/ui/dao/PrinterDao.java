package co.silbersoft.uprint.ui.dao;

import java.util.List;

import co.silbersoft.uprint.ui.domain.Location;
import co.silbersoft.uprint.ui.domain.Printer;

/**
 *
 * @author Matt Silbernagel
 */
public interface PrinterDao {
    
    public List<Location> getBuildings();
    public List<Printer> getPrintersByBuilding(String building);
    
    public void reset();
    
}
