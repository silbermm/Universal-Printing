package silbersoft.ucprint.ui.models;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import silbersoft.ucprint.ui.PrintViewImpl;
import silbersoft.uprint.utils.R;

/**
 *
 * @author Matt Silbernagel
 */
public class CancelButtonModel extends AbstractAction {

    public CancelButtonModel(){
        super(R.getString("frame.cancelButton"));
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        // tell the PrintFrame to close... 
        PrintViewImpl.tearDown();
    }
    
}
