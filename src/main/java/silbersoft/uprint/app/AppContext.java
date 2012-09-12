package silbersoft.uprint.app;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import javax.swing.Action;
import org.simoes.lpd.LPDServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import silbersoft.uprint.ui.PrintView;
import silbersoft.uprint.ui.PrintViewImpl;
import silbersoft.uprint.ui.models.BuildingListModel;
import silbersoft.uprint.ui.models.CancelButtonModel;
import silbersoft.uprint.ui.models.PrintButtonModel;
import silbersoft.uprint.ui.models.PrintViewListModel;
import silbersoft.uprint.ui.models.PrinterListModel;
import silbersoft.uprint.dao.PrinterDao;
import silbersoft.uprint.dao.PrinterDaoImpl;

/**
 *
 * Setup the Spring IOC Container
 *
 * @author Matt Silberangel
 */
@Configuration
public class AppContext {

    @Bean
    public Config config() {
        return ConfigFactory.load();
    }

    @Bean
    public PrinterDao printDao() {
        return new PrinterDaoImpl(config());
    }

    @Bean
    public PrintButtonModel printAction() {
        return new PrintButtonModel(config());
    }

    @Bean
    public Action cancelAction() {
        return new CancelButtonModel();
    }

    @Bean
    public PrintViewListModel printerListModel() {
        PrintViewListModel printers = new PrinterListModel(printAction());
        printers.setPrinterDao(printDao());
        return printers;
    }
    
    @Bean
    public PrintViewListModel buildingListModel() {
        PrintViewListModel buildings = new BuildingListModel(printerListModel());
        buildings.setPrinterDao(printDao());
        return buildings;
    }

    @Bean
    public PrintView printFrame() {
        PrintView printFrame = new PrintViewImpl(config());
        printFrame.setPrintModel(printAction());
        printFrame.setCancelModel(cancelAction());
        printFrame.setBuildingListModel(buildingListModel());
        printFrame.setPrinterListModel(printerListModel());
        return printFrame;
    }
    
    @Bean LPDServer lpdServer(){
        LPDServer lpd = new LPDServer(config());
        lpd.setBuildingList(buildingListModel());
        lpd.setPrintButtonModel(printAction());
        lpd.setPrintView(printFrame());
        return lpd;
    }
}
