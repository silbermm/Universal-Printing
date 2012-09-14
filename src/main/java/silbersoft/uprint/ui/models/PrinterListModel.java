package silbersoft.uprint.ui.models;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import org.apache.log4j.Logger;
import silbersoft.uprint.dao.PrinterDao;
import silbersoft.uprint.domain.Printer;

/**
 *
 * @author silbermm
 */
public class PrinterListModel implements PrintViewListModel {

    public PrinterListModel(PrintButtonModel printButtonModel) {
        super();
        this.printButtonModel = printButtonModel;
        printerListModel = new DefaultListModel();
    }

    @Override
    public void buildList(final String l) {        
        final List<Printer> printers = printerDao.getPrintersByBuilding(l);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (Printer p : printers) {
                    printerListModel.addElement(p);
                }
            }
        });
    }

    /**
     * Clear the printer list and disable the print button
     */
    @Override
    public void clearList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                printButtonModel.setEnabled(false);
                printerListModel.clear();
            }
        });
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        if (!lse.getValueIsAdjusting()) {
            log.debug("Value Changed in Printer list");
            JList list = (JList) lse.getSource();
            Printer printer = (Printer) list.getSelectedValue();
            printButtonModel.setCurrentPrinter(printer);
            if (printer != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        printButtonModel.setEnabled(true);
                    }
                });
            }
        }
    }

    @Override
    public DefaultListModel getListModel() {
        return printerListModel;
    }

    @Override
    public void setPrinterDao(PrinterDao printerDao) {
        this.printerDao = printerDao;
    }
    private static Logger log = Logger.getLogger(PrinterListModel.class);
    private static DefaultListModel printerListModel;
    private PrinterDao printerDao;
    private PrintButtonModel printButtonModel;
}
