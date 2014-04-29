//
// what is the purpose of this class?
//  encapsulates the control file and all of its commands
// who constructs it?
//  LPDCommands => receive control file header ( byte array ), and control file
//  this class should then be constructed to represent the control file
// who calls its methods?
//  the gui could retrieve the info
//  "whoever pulls the jobs out of the lpqueue would care about these attributes"

package org.simoes.lpd.common;

import java.util.*;
import java.io.*;

import org.simoes.lpd.exception.*;
import org.simoes.util.StringUtil;
import org.apache.log4j.Logger;

/**
 * This encapsulates the control file and all of it's commands.
 * If you are capturing print jobs you probably care about this class
 * because it has all of the information about the print job.
 * The print job data that is sent to the printer is found at {@link DataFile}.
 * 
 * @author Jason Crowe
 *
 *
 */public class ControlFileCommands implements Cloneable {
	static Logger log = Logger.getLogger(ControlFileCommands.class);

    private String classForBannerPage;
    private String host;
    private String indentCount;
    private String jobName;
    private String userName;
    private String email;
    private String fileName;
    private String userId;
    private String symbolicLinkData;
    private String title;
    private String fileToUnlink;
    private String widthCount;
    private String troffRFontFileName;
    private String troffIFontName;
    private String troffBFontName;
    private String troffSFontName;
    private String plotCIFFileName;
    private String printDVIFileName;
    private String fileToPrintAsText;
    private String fileToPlot;
    private String fileToPrintAsTextRaw;
    private String fileToPrintAsDitroff;
    private String fileToPrintAsPostscript;
    private String fileToPrintAsPr;
    private String fileToPrintFortran;
    private String fileToPrintAsTroff;
    private String fileToPrintAsRaster;

	/**
	 * Default constructor.
	 *
	 */
    public ControlFileCommands() {
        super();
    }

	/**
	 * Fully qualified constructor.
	 * @param classForBannerPage
	 * @param host
	 * @param indentCount
	 * @param jobName the name of the print job
	 * @param userName
	 * @param email
	 * @param fileName
	 * @param userId the user that sent the command/print job
	 * @param symbolicLinkData
	 * @param title
	 * @param fileToUnlink
	 * @param widthCount
	 * @param troffRFontFileName
	 * @param troffIFontName
	 * @param troffBFontName
	 * @param troffSFontName
	 * @param plotCIFFileName
	 * @param printDVIFileName
	 * @param fileToPrintAsText
	 * @param fileToPlot
	 * @param fileToPrintAsTextRaw
	 * @param fileToPrintAsDitroff
	 * @param fileToPrintAsPostscript
	 * @param fileToPrintAsPr
	 * @param fileToPrintFortran
	 * @param fileToPrintAsTroff
	 * @param fileToPrintAsRaster
	 */
    public ControlFileCommands(String classForBannerPage,
                               String host,
                               String indentCount,
                               String jobName,
                               String userName,
                               String email,
                               String fileName,
                               String userId,
                               String symbolicLinkData,
                               String title,
                               String fileToUnlink,
                               String widthCount,
                               String troffRFontFileName,
                               String troffIFontName,
                               String troffBFontName,
                               String troffSFontName,
                               String plotCIFFileName,
                               String printDVIFileName,
                               String fileToPrintAsText,
                               String fileToPlot,
                               String fileToPrintAsTextRaw,
                               String fileToPrintAsDitroff,
                               String fileToPrintAsPostscript,
                               String fileToPrintAsPr,
                               String fileToPrintFortran,
                               String fileToPrintAsTroff,
                               String fileToPrintAsRaster)
    {
        this.classForBannerPage = classForBannerPage;
        this.host = host;
        this.indentCount = indentCount;
        this.jobName = jobName;
        this.userName = userName;
        this.email = email;
        this.fileName = fileName;
        this.userId = userId;
        this.symbolicLinkData = symbolicLinkData;
        this.title = title;
        this.fileToUnlink = fileToUnlink;
        this.widthCount = widthCount;
        this.troffRFontFileName = troffRFontFileName;
        this.troffIFontName = troffIFontName;
        this.troffBFontName = troffBFontName;
        this.troffSFontName = troffSFontName;
        this.plotCIFFileName = plotCIFFileName;
        this.printDVIFileName = printDVIFileName;
        this.fileToPrintAsText = fileToPrintAsText;
        this.fileToPlot = fileToPlot;
        this.fileToPrintAsTextRaw = fileToPrintAsTextRaw;
        this.fileToPrintAsDitroff = fileToPrintAsDitroff;
        this.fileToPrintAsPostscript = fileToPrintAsPostscript;
        this.fileToPrintAsPr = fileToPrintAsPr;
        this.fileToPrintFortran = fileToPrintFortran;
        this.fileToPrintAsTroff = fileToPrintAsTroff;
        this.fileToPrintAsRaster = fileToPrintAsRaster;
    }

    public String getClassForBannerPage() {
        return classForBannerPage;
    }
    public String getHost() {
        return host;
    }
    public String getIndentCount() {
        return indentCount;
    }
    public String getJobName() {
        return jobName;
    }
    public String getUserName() {
        return userName;
    }
    public String getEmail() {
        return email;
    }
    public String getFileName() {
        return fileName;
    }
    public String getUserId() {
        return userId;
    }
    public String getSymbolicLinkData() {
        return symbolicLinkData;
    }
    public String getTitle() {
        return title;
    }
    public String getFileToUnlink() {
        return fileToUnlink;
    }
    public String getWidthCount() {
        return widthCount;
    }
    public String getTroffRFontFileName() {
        return troffRFontFileName;
    }
    public String getTroffIFontName() {
        return troffIFontName;
    }
    public String getTroffBFontName() {
        return troffBFontName;
    }
    public String getTroffSFontName() {
        return troffSFontName;
    }
    public String getPlotCIFFileName() {
        return plotCIFFileName;
    }
    public String getPrintDVIFileName() {
        return printDVIFileName;
    }
    public String getFileToPrintAsText() {
        return fileToPrintAsText;
    }
    public String getFileToPlot() {
        return fileToPlot;
    }
    public String getFileToPrintAsTextRaw() {
        return fileToPrintAsTextRaw;
    }
    public String getFileToPrintAsDitroff() {
        return fileToPrintAsDitroff;
    }
    public String getFileToPrintAsPostscript() {
        return fileToPrintAsPostscript;
    }
    public String getFileToPrintAsPr() {
        return fileToPrintAsPr;
    }
    public String getFileToPrintFortran() {
        return fileToPrintFortran;
    }
    public String getFileToPrintAsTroff() {
        return fileToPrintAsTroff;
    }
    public String getFileToPrintAsRaster() {
        return fileToPrintAsRaster;
    }

    public void setClassForBannerPage(String a) {
        this.classForBannerPage = a;
    }
    public void setHost(String a) {
        this.host = a;
    }
    public void setIndentCount(String a) {
        this.indentCount = a;
    }
    public void setJobName(String a) {
        this.jobName = a;
    }
    public void setUserName(String a) {
        this.userName = a;
    }
    public void setEmail(String a) {
        this.email = a;
    }
    public void setFileName(String a) {
        this.fileName = a;
    }
    public void setUserId(String a) {
        this.userId = a;
    }
    public void setSymbolicLinkData(String a) {
        this.symbolicLinkData = a;
    }
    public void setTitle(String a) {
        this.title = a;
    }
    public void setFileToUnlink(String a) {
        this.fileToUnlink = a;
    }
    public void setWidthCount(String a) {
        this.widthCount = a;
    }
    public void setTroffRFontFileName(String a) {
        this.troffRFontFileName = a;
    }
    public void setTroffIFontName(String a) {
        this.troffIFontName = a;
    }
    public void setTroffBFontName(String a) {
        this.troffBFontName = a;
    }
    public void setTroffSFontName(String a) {
        this.troffSFontName = a;
    }
    public void setPlotCIFFileName(String a) {
        this.plotCIFFileName = a;
    }
    public void setPrintDVIFileName(String a) {
        this.printDVIFileName = a;
    }
    public void setFileToPrintAsText(String a) {
        this.fileToPrintAsText = a;
    }
    public void setFileToPlot(String a) {
        this.fileToPlot = a;
    }
    public void setFileToPrintAsTextRaw(String a) {
        this.fileToPrintAsTextRaw = a;
    }
    public void setFileToPrintAsDitroff(String a) {
        this.fileToPrintAsDitroff = a;
    }
    public void setFileToPrintAsPostscript(String a) {
        this.fileToPrintAsPostscript = a;
    }
    public void setFileToPrintAsPr(String a) {
        this.fileToPrintAsPr = a;
    }
    public void setFileToPrintFortran(String a) {
        this.fileToPrintFortran = a;
    }
    public void setFileToPrintAsTroff(String a) {
        this.fileToPrintAsTroff = a;
    }
    public void setFileToPrintAsRaster(String a) {
        this.fileToPrintAsRaster = a;
    }

	public Object clone() {
		final String METHOD_NAME = "clone()";
		Object result = null;
		try {
			result = super.clone();
		} catch(CloneNotSupportedException e) {
			log.error(METHOD_NAME + e.getMessage());
			throw new InternalError(METHOD_NAME + e.getMessage());
		}
		return result;
	}

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("classForBannerPage = ");
        sb.append(classForBannerPage);
        sb.append(", ");
        sb.append("host = ");
        sb.append(host);
        sb.append(", ");
        sb.append("indentCount = ");
        sb.append(indentCount);
        sb.append(", ");
        sb.append("jobName = ");
        sb.append(jobName);
        sb.append(", ");
        sb.append("userName = ");
        sb.append(userName);
        sb.append(", ");
        sb.append("email = ");
        sb.append(email);
        sb.append(", ");
        sb.append("fileName = ");
        sb.append(fileName);
        sb.append(", ");
        sb.append("userId = ");
        sb.append(userId);
        sb.append(", ");
        sb.append("symbolicLinkData = ");
        sb.append(symbolicLinkData);
        sb.append(", ");
        sb.append("title = ");
        sb.append(title);
        sb.append(", ");
        sb.append("fileToUnlink = ");
        sb.append(fileToUnlink);
        sb.append(", ");
        sb.append("widthCount = ");
        sb.append(widthCount);
        sb.append(", ");
        sb.append("troffRFontFileName = ");
        sb.append(troffRFontFileName);
        sb.append(", ");
        sb.append("troffIFontName = ");
        sb.append(troffIFontName);
        sb.append(", ");
        sb.append("troffBFontName = ");
        sb.append(troffBFontName);
        sb.append(", ");
        sb.append("troffSFontName = ");
        sb.append(troffSFontName);
        sb.append(", ");
        sb.append("plotCIFFileName = ");
        sb.append(plotCIFFileName);
        sb.append(", ");
        sb.append("printDVIFileName = ");
        sb.append(printDVIFileName);
        sb.append(", ");
        sb.append("fileToPrintAsText = ");
        sb.append(fileToPrintAsText);
        sb.append(", ");
        sb.append("fileToPlot = ");
        sb.append(fileToPlot);
        sb.append(", ");
        sb.append("fileToPrintAsTextRaw = ");
        sb.append(fileToPrintAsTextRaw);
        sb.append(", ");
        sb.append("fileToPrintAsDitroff = ");
        sb.append(fileToPrintAsDitroff);
        sb.append(", ");
        sb.append("fileToPrintAsPostscript = ");
        sb.append(fileToPrintAsPostscript);
        sb.append(", ");
        sb.append("fileToPrintAsPr = ");
        sb.append(fileToPrintAsPr);
        sb.append(", ");
        sb.append("fileToPrintFortran = ");
        sb.append(fileToPrintFortran);
        sb.append(", ");
        sb.append("fileToPrintAsTroff = ");
        sb.append(fileToPrintAsTroff);
        sb.append(", ");
        sb.append("fileToPrintAsRaster = ");
        sb.append(fileToPrintAsRaster);
        return sb.toString();
    }

	/**
	 * Constructor that is passed the raw byte array from the request.
	 * @param bytes - byte array representing a RFC1179 Control File's contents.
	 * @throws LPDException
	 */
    public ControlFileCommands(byte[] bytes) 
        throws LPDException
    {
    	final String METHOD_NAME = "ControlFileCommands(): "; 
        // read chris's object and set all these attributes if found
        // algorithm
        //  convert byte array to string
        //  use string tokenizer to split on newline
        //  call utility method parseCommand
        //  parse command returns a vector (operator,operand)
        //  switch on operator
        //    set attribute to operand
        try{
	        // read from the byte array
	        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(bytes);
	        InputStreamReader inputstreamreader =
	            new InputStreamReader(bytearrayinputstream);
	        BufferedReader bufferedReader = new BufferedReader(inputstreamreader);
	        
	        while (true) {
	            String line = bufferedReader.readLine();
	            // check for end of file and break out
	            if (line == null) { break; }
	            // process command (need to add newline back onto input line for processing)
	            processCommand(line + "\n");
	        }
		} catch(IOException e) {
			log.error(METHOD_NAME + e.getMessage());
			throw new LPDException(e);
		}
    }

	/**
	 * Processes a command (one line of input). 
	 * @param command
	 */
    protected void processCommand(String command) {
        // parse command into commandcode and operand
        Vector vector = StringUtil.parseCommand(command.getBytes());
        String commandcode = new String((byte[])vector.get(0));
        String operand = new String((byte[])vector.get(1));
        // switch on command code and set appropriate attribute
        //System.out.println("commandcode[" + commandcode + "]"+" "+"operand[" + operand + "]");
		setAttribute(commandcode,operand);
    }
    
    /** 
     * Switches on the command code and sets the appropriate attribute.
	 * 
	 * @param commandcode the flag describing what the attribute is
	 * @param operand the attribute
	 */
    protected void setAttribute(String commandcode, String operand) {
		if (commandcode == null) {
		    // do nothing
		} else if (commandcode.equals("C")) {
		    setClassForBannerPage(operand);
		} else if (commandcode.equals("H")) {
		    setHost(operand);
		} else if (commandcode.equals("I")) {
		    setIndentCount(operand);
		} else if (commandcode.equals("J")) {
		    setJobName(operand);
		} else if (commandcode.equals("L")) {
		    setUserName(operand);
		} else if (commandcode.equals("M")) {
		    setEmail(operand);
		} else if (commandcode.equals("N")) {
		    setFileName(operand);
		} else if (commandcode.equals("P")) {
		    setUserId(operand);
		} else if (commandcode.equals("S")) {
		    setSymbolicLinkData(operand);
		} else if (commandcode.equals("T")) {
		    setTitle(operand);
		} else if (commandcode.equals("U")) {
		    setFileToUnlink(operand);
		} else if (commandcode.equals("W")) {
		    setWidthCount(operand);
		} else if (commandcode.equals("1")) {
		    setTroffRFontFileName(operand);
		} else if (commandcode.equals("2")) {
		    setTroffIFontName(operand);
		} else if (commandcode.equals("3")) {
		    setTroffBFontName(operand);
		} else if (commandcode.equals("4")) {
		    setTroffSFontName(operand);
		} else if (commandcode.equals("c")) {
		    setPlotCIFFileName(operand);
		} else if (commandcode.equals("d")) {
		    setPrintDVIFileName(operand);
		} else if (commandcode.equals("f")) {
		    setFileToPrintAsText(operand);
		} else if (commandcode.equals("g")) {
		    setFileToPlot(operand);
		} else if (commandcode.equals("l")) {
		    setFileToPrintAsTextRaw(operand);
		} else if (commandcode.equals("n")) {
		    setFileToPrintAsDitroff(operand);
		} else if (commandcode.equals("o")) {
		    setFileToPrintAsPostscript(operand);
		} else if (commandcode.equals("p")) {
		    setFileToPrintAsPr(operand);
		} else if (commandcode.equals("r")) {
		    setFileToPrintFortran(operand);
		} else if (commandcode.equals("t")) {
		    setFileToPrintAsTroff(operand);
		} else if (commandcode.equals("v")) {
		    setFileToPrintAsRaster(operand);
		}
    }

    /** unit test method. */
    public static void main(String[] args)
        throws LPDException 
    {
        String controlfile = 
            new String("CClass\n"+
                       "HHost\n"+
                       "Icount\n"+
                       "JJob name\n"+
                       "LUser\n"+
                       "Muser\n"+
                       "NName\n"+
                       "PName\n"+
                       "Sdevice inode\n"+
                       "Ttitle\n"+
                       "Ufile\n"+
                       "Wwidth\n"+
                       "1file\n"+
                       "2file\n"+
                       "3file\n"+
                       "4file\n"+
                       "cfile\n"+
                       "dfile\n"+
                       "ffile\n"+
                       "gfile\n"+
                       "lfile\n"+
                       "nfile\n"+
                       "ofile\n"+
                       "pfile\n"+
                       "rfile\n"+
                       "tfile\n"+
                       "vfile\n");

        //System.out.println("controlfile[" + controlfile + "]");
        byte[] bytes = controlfile.getBytes();
		//System.out.println("bytes[" + bytes + "]");
        ControlFileCommands controlfilecommands = new ControlFileCommands(bytes);
		System.out.println("controlfilecommands[" + controlfilecommands + "]");
    }

	/**
	 * Outputs current attributes in RFC1179 control file format (byte array).  
	 * @return the controlFile in the RFC1179 format
	 */
    public byte[] toControlFileFormat() {
		StringBuffer stringbuffer = new StringBuffer();
		// go through attributes, if not null then output
		String attribute;
		attribute = getClassForBannerPage();
		if (attribute != null) {
		    stringbuffer.append("C" + attribute);
		}
		attribute = getHost();
		if (attribute != null) {
		    stringbuffer.append("H" + attribute);
		}
		attribute = getIndentCount();
		if (attribute != null) {
		    stringbuffer.append("I" + attribute);
		}
		attribute = getJobName();
		if (attribute != null) {
		    stringbuffer.append("J" + attribute);
		}
		attribute = getUserName();
		if (attribute != null) {
		    stringbuffer.append("L" + attribute);
		}
		attribute = getEmail();
		if (attribute != null) {
		    stringbuffer.append("M" + attribute);
		}
		attribute = getFileName();
		if (attribute != null) {
		    stringbuffer.append("N" + attribute);
		}
		attribute = getUserId();
		if (attribute != null) {
		    stringbuffer.append("P" + attribute);
		}
		attribute = getSymbolicLinkData();
		if (attribute != null) {
		    stringbuffer.append("S" + attribute);
		}
		attribute = getTitle();
		if (attribute != null) {
		    stringbuffer.append("T" + attribute);
		}
		attribute = getFileToUnlink();
		if (attribute != null) {
		    stringbuffer.append("U" + attribute);
		}
		attribute = getWidthCount();
		if (attribute != null) {
		    stringbuffer.append("W" + attribute);
		}
		attribute = getTroffRFontFileName();
		if (attribute != null) {
		    stringbuffer.append("1" + attribute);
		}
		attribute = getTroffIFontName();
		if (attribute != null) {
		    stringbuffer.append("2" + attribute);
		}
		attribute = getTroffBFontName();
		if (attribute != null) {
		    stringbuffer.append("3" + attribute);
		}
		attribute = getTroffSFontName();
		if (attribute != null) {
		    stringbuffer.append("4" + attribute);
		}
		attribute = getPlotCIFFileName();
		if (attribute != null) {
		    stringbuffer.append("c" + attribute);
		}
		attribute = getPrintDVIFileName();
		if (attribute != null) {
		    stringbuffer.append("d" + attribute);
		}
		attribute = getFileToPrintAsText();
		if (attribute != null) {
		    stringbuffer.append("f" + attribute);
		}
		attribute = getFileToPlot();
		if (attribute != null) {
		    stringbuffer.append("g" + attribute);
		}
		attribute = getFileToPrintAsTextRaw();
		if (attribute != null) {
		    stringbuffer.append("l" + attribute);
		}
		attribute = getFileToPrintAsDitroff();
		if (attribute != null) {
		    stringbuffer.append("n" + attribute);
		}
		attribute = getFileToPrintAsPostscript();
		if (attribute != null) {
		    stringbuffer.append("o" + attribute);
		}
		attribute = getFileToPrintAsPr();
		if (attribute != null) {
		    stringbuffer.append("p" + attribute);
		}
		attribute = getFileToPrintFortran();
		if (attribute != null) {
		    stringbuffer.append("r" + attribute);
		}
		attribute = getFileToPrintAsTroff();
		if (attribute != null) {
		    stringbuffer.append("t" + attribute);
		}
		attribute = getFileToPrintAsRaster();
		if (attribute != null) {
		    stringbuffer.append("v" + attribute);
		}
		String string = stringbuffer.toString();
		return string.getBytes();
    }
}
