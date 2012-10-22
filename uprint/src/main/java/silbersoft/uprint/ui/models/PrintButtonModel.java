package silbersoft.uprint.ui.models;

import com.typesafe.config.Config;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import silbersoft.uprint.domain.Printer;
import silbersoft.uprint.ui.PrintViewImpl;
import silbersoft.uprint.ui.StatusBar;
import silbersoft.uprint.utils.R;

/**
 *
 * @author Matt Silbernagel
 */
public class PrintButtonModel extends AbstractAction {

    public PrintButtonModel(Config config) {
        super(R.getString("frame.printButton"));
        this.putValue(Action.SHORT_DESCRIPTION, "Print this file");
        this.setEnabled(false);
        this.config = config;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        log.debug("printing " + currentFile + " to " + currentPrinter.getName());
        log.debug("first I need to know who is requestiong this...");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (config.getBoolean("username.prompt")) {
                    StatusBar.setStatus(R.getString("status.text.getUsername"));
                    username = PrintViewImpl.promptForUsername();
                    if (username == null) {
                        log.debug("the user hit cancel...");
                        StatusBar.setStatus(R.getString("status.text.ready"));
                        return;
                    }
                } else {
                    username = System.getProperty("user.name");
                }
                log.debug(username + " is requesting this... ");
                StatusBar.setStatus("Printng " + jobName + " to " + currentPrinter.getName() + "...");
                String printed = currentPrinter.print(currentFile, jobName, username);
                if (printed.equals("")) {
                    StatusBar.setStatus(R.getString("status.text.ready"));
                    PrintViewImpl.showSuccess();
                    PrintViewImpl.tearDown();
                } else {
                    StatusBar.setStatus("Error: " + printed);
                    int tryAgain = PrintViewImpl.showFailure(printed);
                    if (tryAgain == 1) {
                        PrintViewImpl.tearDown();
                    } else {
                        StatusBar.setStatus("status.text.ready");
                    }
                    log.error(printed);
                }
            };
    });       
    }

    public void setCurrentPrinter(Printer p) {
        currentPrinter = p;
    }

    public Printer getCurrentPrinter() {
        return currentPrinter;
    }

    public void setCurrentFile(String file) {
        this.currentFile = file;        
    }

    public String getCurrentFile() {
        return this.currentFile;
    }

    public void setJobName(String job) {
        this.jobName = job;
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                StatusBar.setStatus("Ready to print " + jobName);
            }
        
        });
    }
    private static Printer currentPrinter;
    private String currentFile;
    private String jobName;
    private String username;
    private Config config;
    private static final Logger log = Logger.getLogger(PrintButtonModel.class);
}
