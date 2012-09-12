package silbersoft.uprint.ui.models;

import com.typesafe.config.Config;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import silbersoft.uprint.domain.Printer;
import silbersoft.uprint.ui.PrintViewImpl;
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
                if(config.getBoolean("username.prompt")){
                username = PrintViewImpl.promptForUsername();
                } else {
                    username = System.getProperty("user.name");
                }
                log.debug(username + " is requesting this... ");
                print = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        String result = currentPrinter.print(currentFile, jobName, username);
                        log.debug(result);
                        return result;
                    }
                };
                print.execute ();
            };
        });

        if (print.isDone()) {
            try {
                String result = (String) print.get();
                if (result.equals("")) {
                    // yeah
                } else {
                    // boo
                    log.error(result);
                }
            } catch (InterruptedException ex) {
            } catch (ExecutionException ex) {
            }
        }
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
    
    public void setJobName(String jobName){
        this.jobName = jobName;
    }
    
    private static Printer currentPrinter;
    private String currentFile;
    private String jobName;
    private String username;
    private Config config;
    private SwingWorker print;
    private static final Logger log = Logger.getLogger(PrintButtonModel.class);
}
