package silbersoft.uprint.ui.models;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import silbersoft.uprint.ui.PrintViewImpl;
import silbersoft.uprint.utils.R;

/**
 *
 * @author Matt Silbernagel
 */
public class AboutUsModel extends AbstractAction {
    
    public AboutUsModel(){
        super(R.getString("menu.about.about"));
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        PrintViewImpl.showAbout();
    }   
}
