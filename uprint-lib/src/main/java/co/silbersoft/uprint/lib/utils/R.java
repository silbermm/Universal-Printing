package co.silbersoft.uprint.lib.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Similar to how Android handles resources we will use this class to load the
 * external resources for the app
 *
 * @author Matt Silbernagel
 */
public class R {

    public static String getString(String key) {
        Properties p = new Properties();
        String rVal = "";
        try {
            InputStream in = R.class.getResourceAsStream("/strings.properties");
            p.load(in);
            in.close();
        } catch (IOException ex) {
            log.error("Unable to load the properties, returning and empty string: " + ex.getMessage());
            return rVal;
        }
        if (p.containsKey(key)) {
            rVal = p.getProperty(key, rVal);
        }
        return rVal;

    }

    public static int getInteger(String key) {
        Properties p = new Properties();
        int rVal = 0;
        try {
            InputStream in = R.class.getResourceAsStream("/integers.properties");
            p.load(in);
            in.close();
        } catch (IOException ex) {
            log.error("Unable to load the properties, returning a 0: " + ex.getMessage());
            return rVal;
        }
        if (p.containsKey(key)) {
            rVal = Integer.parseInt(p.getProperty(key, "0"));
        }
        return rVal;
    }

    public static Image getImage(String key) {
        Properties p = new Properties();
        Image rVal = null;
        try {
            InputStream in = R.class.getResourceAsStream("/images.properties");
            p.load(in);
            in.close();
        } catch (IOException ex) {
            log.error("Unable to load the properties, returning null: " + ex.getMessage());
            return rVal;
        }
        if (p.containsKey(key)) {
            rVal = Toolkit.getDefaultToolkit().getImage(R.class.getResource(p.getProperty(key)));
        }
        return rVal;
    }
    
    private static final Logger log = Logger.getLogger(R.class);
}
