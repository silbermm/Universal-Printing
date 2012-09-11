/**
 * The Entry Point for Universal Printing
 */
package silbersoft.uprint.app;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.simoes.lpd.LPDServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import silbersoft.ucprint.ui.PrintView;
import silbersoft.ucprint.ui.models.BuildingListModel;
import silbersoft.ucprint.ui.models.PrintButtonModel;
import silbersoft.uprint.utils.R;

/**
 * Setup cli arguments and load the Spring Container
 * @author Matt Silbernagel  
 */
public class Main {

    public static void main(String[] args) {

        parse(args);

        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppContext.class);
        ctx.registerShutdownHook();

        Config config = ctx.getBean(Config.class);
        /*
        if (cmd.hasOption("c")) {
            log.debug("config file = " + cmd.getOptionValue("c"));
            File userSpecified = new File(cmd.getOptionValue("c"));
            if (userSpecified.exists()) {   
                config.checkValid(ConfigFactory.defaultReference(), cmd.getOptionValue("c"));                                
                config = ConfigFactory.parseFile(userSpecified).withFallback(config);
            }
            log.debug("LPD PORT IS: " + config.getString("lpd.port"));
        }               
        * 
        */
        log.debug("LPD PORT IS: " + config.getString("lpd.port"));
        
        if (cmd.hasOption("daemon")) {
            LPDServer lpd = ctx.getBean(LPDServer.class);
            lpd.start();
        } else {
            PrintButtonModel pBtn = ctx.getBean(PrintButtonModel.class);
            pBtn.setCurrentFile(cmd.getOptionValue("f"));

            PrintView printView = ctx.getBean(PrintView.class);
            printView.showFrame();

            BuildingListModel buildings = ctx.getBean(BuildingListModel.class);
            buildings.buildList("all");
        }
    }

    private static void parse(String[] args) {

        CommandLineParser parser = new PosixParser();
        options.addOption(OptionBuilder.withArgName("h").withDescription("print this help message").withLongOpt("help").create("h"));
        options.addOption(OptionBuilder.withArgName("v").withDescription("Print version").withLongOpt("version").create("v"));
        options.addOption(OptionBuilder.withArgName("d").withDescription("Prints debugging information to the screen").withLongOpt("debug").create("d"));
        options.addOption(OptionBuilder.withArgName("c").hasArg().withDescription("Specify a config file").withLongOpt("configfile").create("c"));
        options.addOption(OptionBuilder.withArgName("f").hasArg().withDescription("Specify a file to print").withLongOpt("file").create("f"));
        options.addOption(OptionBuilder.withDescription("Specify whether to run as a daemon. If this is specified it will run on port 40515"
                + "and accept jobs from a printer that prints to 40515. You can specify a different port in the config file").withLongOpt("daemon").create());


        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            log.error("Unable to parse the command line: " + ex.getMessage());
            printHelp();
        }

        if (cmd.hasOption("h")) {
            printHelp();
        }
        if (cmd.hasOption("v")) {
            System.out.println(R.getString("app.title") + " - " + R.getString("app.version"));
            System.exit(1);
        }
        if (cmd.hasOption("c")) {
            System.setProperty("config.file", cmd.getOptionValue("c"));
        }
         
        if (cmd.hasOption("d")) {
            Logger.getRootLogger().setLevel(Level.DEBUG);
        }
        
        if (cmd.hasOption("f")) {
            log.debug("file = " + cmd.getOptionValue("f"));
            System.setProperty("print.file", cmd.getOptionValue("f"));
        }

    }

    private static void printHelp() {
        HelpFormatter help = new HelpFormatter();
        help.printHelp(R.getString("app.title") + " - " + R.getString("app.version"), options);
        System.exit(1);
    }
    private static final Logger log = Logger.getLogger(Main.class);
    private static Options options = new Options();
    private static CommandLine cmd = null;
}
