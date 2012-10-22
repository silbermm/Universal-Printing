package silbersoft.uprint.ui.models;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import javax.swing.AbstractAction;
import org.apache.log4j.Logger;
import silbersoft.uprint.utils.R;

/**
 *
 * @author silbermm
 */
public class GetHelpModel extends AbstractAction {

    public GetHelpModel(){
        super(R.getString("menu.about.gethelp"));
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(Desktop.isDesktopSupported()){
            URI helpURI = URI.create(R.getString("menu.about.uri"));
            try {
                Desktop.getDesktop().browse(helpURI);
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
    }
    
    private static final Logger log = Logger.getLogger(GetHelpModel.class);

}
