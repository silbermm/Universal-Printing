package silbersoft.uprint.ui.models;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import silbersoft.uprint.ui.PrintViewImpl;

/**
 *
 * @author Matt Silbernagel
 */
public class AboutUsModel extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent ae) {
        PrintViewImpl.showAbout();
    }   
}
