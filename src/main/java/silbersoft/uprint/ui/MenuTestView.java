/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package silbersoft.uprint.ui;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import silbersoft.uprint.utils.R;

/**
 *
 * @author silbermm
 */
public class MenuTestView {
    
    private JFrame mainFrame;
    
    public MenuTestView(){
        mainFrame = new JFrame();
        mainFrame.setTitle(R.getString("frame.title"));
        mainFrame.setSize(R.getInteger("frame.width"), R.getInteger("frame.height"));
        mainFrame.setIconImage(R.getImage("frame.iconimage"));
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setAlwaysOnTop(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        mainFrame.setJMenuBar(createMainMenu());mainFrame.setJMenuBar(createMainMenu());
    }
    
    public void showFrame(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame.validate();
                mainFrame.pack();
                mainFrame.setVisible(true);
            }
        });
    }
    
    
    private JMenuBar createMainMenu(){
      JMenuBar menu = new JMenuBar();
        //File Menu
        JMenu fileMenu = new JMenu(R.getString("menu.file.title"));
        fileMenu.setMnemonic(KeyEvent.VK_F);
        // Print Item
        JMenuItem printItem = new JMenuItem(R.getString("menu.file.print"));
        printItem.setMnemonic(KeyEvent.VK_R);
        printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        fileMenu.add(printItem);
        // Exit Item
        JMenuItem exitItem = new JMenuItem(R.getString("menu.file.exit"), KeyEvent.VK_E);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        fileMenu.add(exitItem);

        //Help Menu
        JMenu aboutMenu = new JMenu(R.getString("menu.about.title"));
        aboutMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem aboutUsItem = new JMenuItem(R.getString("menu.about.about"));
        //aboutUsItem.setMnemonic(KeyEvent.VK_A);
        aboutMenu.add(aboutUsItem);
        menu.add(fileMenu);
        menu.add(aboutMenu);
        menu.putClientProperty(Options.HEADER_STYLE_KEY,
                HeaderStyle.BOTH);
        return menu;   
    }
}
