package silbersoft.ucprint.ui;

import javax.swing.Action;
import silbersoft.ucprint.ui.models.PrintViewListModel;

/**
 * @author Matt Silbernagel
 */
public interface PrintView {

    public void showFrame();
    public void setPrintList();
    
    public void setPrintModel(Action printAction);
    public void setCancelModel(Action cancelAction);
    public void setPrinterListModel(PrintViewListModel printListModel);
    public void setBuildingListModel(PrintViewListModel buildingListModel);
    
}
