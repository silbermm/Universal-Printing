package co.silbersoft.uprint.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.simoes.lpd.common.PrintJob;
import org.simoes.lpd.handler.HandlerInterface;
import org.simoes.util.FileUtil;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import co.silbersoft.uprint.app.UniversalPrinterSettings;
import co.silbersoft.uprint.lib.domain.PrintResult;

import com.typesafe.config.Config;

/**
 * @author Matt Silbernagel
 */
public class UCITHandler implements HandlerInterface {

	static Logger log = Logger.getLogger(UCITHandler.class);

	public UCITHandler(UniversalPrinterSettings printerSettings,
			ActorSystem actorSystem) {
		super();
		this.printerSettings = printerSettings;
		this.actorSystem = actorSystem;
		this.config = printerSettings.getConfig();
	}

	/**
	 * Writes the printJob to disk using the specified jobName
	 * 
	 * @param printJob
	 *            the PrintJob we are processing
	 * @return the result of our work, true for success or false for non-success
	 */
	@Override
	public boolean process(PrintJob printJob) {
		final String METHOD_NAME = "process(): ";
		final File CREATE_DIR = new File(getTempDir());
		boolean result = false;
		if (null != printJob && null != printJob.getControlFile()
				&& null != printJob.getDataFile()) {
			if (!CREATE_DIR.exists()) {
				if (!CREATE_DIR.mkdirs()) {
					log.error("unable to create the tmp directory "
							+ CREATE_DIR);
					return result;
				}
			}
			String user = printJob.getOwner();

			// create file name, pjb == print job
			String unique = UUID.randomUUID().toString();
			String name = unique + "."
					+ printJob.getControlFile().getJobNumber() + ".pjb";
			File fileName = new File(CREATE_DIR.getAbsolutePath()
					+ File.separator + name);
			if (printerSettings.getUserPath(user) == null) {
				log.fatal("There is no daemon running for user " + user);
			} else {
				ActorSelection aSelection = actorSystem
						.actorSelection(printerSettings.getActorPath());
				try {
					FileUtil.writeFile(printJob.getDataFile().getContents(),
							fileName.getAbsolutePath());
					result = true;
					PrintResult pResult = new PrintResult(fileName, null);
					aSelection.tell(pResult, ActorRef.noSender());
				} catch (IOException e) {
					log.error(METHOD_NAME + e.getMessage());
					PrintResult pResult = new PrintResult(null, e.getMessage());
					aSelection.tell(pResult, ActorRef.noSender());
				}
			}
		} else {
			log.error(METHOD_NAME
					+ "The printJob or printJob.getControlFile() or printJob.getDataFile() were empty");
		}
		return result;
	}

	private String getTempDir() {
		String tmpDir = config.getString("java.io.tmpdir");
		return tmpDir + File.separator + "uprint" + File.separator + "jobs"
				+ File.separator;
	}

	private Config config;
	private final ActorSystem actorSystem;
	private UniversalPrinterSettings printerSettings;
	final String JOB_DIR = "/jobs/";
}
