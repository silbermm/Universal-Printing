package co.silbersoft.uprint.ui.app;

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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import co.silbersoft.uprint.lib.utils.R;
import co.silbersoft.uprint.ui.actors.WakeUpPrinter;



public class App {

	public static void main(String[] args) {
		parse(args);
		
		AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppContext.class);
		ctx.registerShutdownHook();
		
		ActorSystem actorSystem = ctx.getBean(ActorSystem.class);
		
		ActorRef actorRef = actorSystem.actorOf(Props.create(WakeUpPrinter.class), "wakeup");
		log.debug("waiting for a message!");
		
	}
	
	
    
	
	@SuppressWarnings("static-access")
	private static void parse(String[] args) {
        CommandLineParser parser = new PosixParser();
        options.addOption(OptionBuilder.withArgName("h").withDescription("print this help message").withLongOpt("help").create("h"));
        options.addOption(OptionBuilder.withArgName("v").withDescription("Print version").withLongOpt("version").create("v"));
        options.addOption(OptionBuilder.withArgName("d").withDescription("Prints debugging information to the screen").withLongOpt("debug").create("d"));
        options.addOption(OptionBuilder.withArgName("c").hasArg().withDescription("Specify a config file").withLongOpt("configfile").create("c"));
        options.addOption(OptionBuilder.withArgName("f").hasArg().withDescription("Specify a file to print").withLongOpt("file").create("f"));
        
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
            File newfile = new File(cmd.getOptionValue("c"));
            System.setProperty("config.file", newfile.getAbsolutePath());
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
    
    private static final Logger log = Logger.getLogger(App.class);    
    private static Options options = new Options();
    private static CommandLine cmd = null;
	
}
