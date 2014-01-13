package co.silbersoft.uprint.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import co.silbersoft.uprint.lib.utils.R;

/**
 *
 * @author Matt Silbernagel
 */
public class ImagePanel extends JPanel {

    public ImagePanel() {
        img = R.getImage("frame.titleimage");
        setPreferredSize(new Dimension(100, 74));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
    
    private static Logger log = Logger.getLogger(ImagePanel.class);
    private Image img;
}
