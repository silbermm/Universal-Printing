package co.silbersoft.uprint.ui.models;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import co.silbersoft.uprint.lib.utils.R;
import co.silbersoft.uprint.ui.PrintViewImpl;


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
