package co.silbersoft.uprint.ui.models;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import co.silbersoft.uprint.lib.utils.R;
import co.silbersoft.uprint.ui.PrintViewImpl;


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
