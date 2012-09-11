package silbersoft.uprint.dao;

import com.typesafe.config.Config;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import silbersoft.uprint.domain.Location;
import silbersoft.uprint.domain.Printer;
import silbersoft.uprint.domain.PrinterLocation;
import silbersoft.uprint.domain.UniversalPrinter;

/**
 *
 * @author Matt Silbernagel
 */
public class PrinterDaoImpl implements PrinterDao {

    public PrinterDaoImpl(Config config) {
        this.config = config;
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            log.error("Unable to initiate xml parsing engine");
        }
    }

    /**
     * Parse all available xml resources and get a list of Buildings with
     * printers
     *
     * @return
     */
    @Override
    public List<Location> getBuildings() {
        List<Location> locations = new ArrayList<Location>();
        if (xml == null) {
            xml = getXMLFromServer();
        }
        ByteArrayInputStream bs = new ByteArrayInputStream(xml.getBytes());
        try {
            Document doc = docBuilder.parse(bs);
            // normalize text representation
            doc.getDocumentElement().normalize();
            NodeList listOfPrinters = doc.getElementsByTagName("printer");
            for (int i = 0; i < listOfPrinters.getLength(); i++) {
                Node printerNode = listOfPrinters.item(i);
                if (printerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element printerElement = (Element) printerNode;
                    NodeList printerNameList = printerElement.getElementsByTagName("name");
                    Element printerNameElement = (Element) printerNameList.item(0);
                    NodeList textPNList = printerNameElement.getChildNodes();

                    NodeList printerLocationList = printerElement.getElementsByTagName("location");
                    Element printerLocationElement = (Element) printerLocationList.item(0);
                    NodeList textLocList = printerLocationElement.getChildNodes();
                    if (!locationExists(textLocList.item(0).getNodeValue().trim(), locations)) {
                        Location l = new PrinterLocation(textLocList.item(0).getNodeValue().trim());
                        locations.add(l);
                    }
                }
            }
        } catch (SAXException ex) {
        } catch (IOException ex) {
        }
        return locations;
    }

    @Override
    public List<Printer> getPrintersByBuilding(String building) {
        if (xml == null) {
            xml = getXMLFromServer();
        }
        List<Printer> printers = new ArrayList<Printer>();
        ByteArrayInputStream bs = new ByteArrayInputStream(xml.getBytes());
        try {
            Document doc = docBuilder.parse(bs);
            // normalize text representation
            doc.getDocumentElement().normalize();
            NodeList listOfPrinters = doc.getElementsByTagName("printer");
            for (int i = 0; i < listOfPrinters.getLength(); i++) {
                Node printerNode = listOfPrinters.item(i);
                if (printerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element printerElement = (Element) printerNode;
                    NodeList printerLocationList = printerElement.getElementsByTagName("location");
                    Element printerLocationElement = (Element) printerLocationList.item(0);
                    NodeList textLocList = printerLocationElement.getChildNodes();
                    if (textLocList.item(0).getNodeValue().trim().equals(building)) {
                        // Build a printer object and 
                        // Add to the list of printers
                        Printer p = new UniversalPrinter();
                        p.setLocation(building);
                        NodeList printerNameList = printerElement.getElementsByTagName("name");
                        Element printerNameElement = (Element) printerNameList.item(0);
                        NodeList textPNList = printerNameElement.getChildNodes();
                        p.setName(textPNList.item(0).getNodeValue().trim());
                        
                        NodeList PrintersServerElementList = printerElement.getElementsByTagName("server");
                        Element PrintersServerElement = (Element) PrintersServerElementList.item(0);
                        NodeList textServerList = PrintersServerElement.getChildNodes();
                        p.setServer(textServerList.item(0).getNodeValue().trim());
                        
                        NodeList printerQueueList = printerElement.getElementsByTagName("queue");
                        Element printerQueueElement = (Element) printerQueueList.item(0);
                        NodeList textQueueList = printerQueueElement.getChildNodes();
                        p.setQueue(textQueueList.item(0).getNodeValue().trim());

                        NodeList printerIDList = printerElement.getElementsByTagName("id");
                        Element printerIDElement = (Element) printerIDList.item(0);
                        NodeList textIDList = printerIDElement.getChildNodes();
                        int id = Integer.parseInt(textIDList.item(0).getNodeValue().trim());
                        p.setID(id);
                        
                        printers.add(p);
                    }
                }
            }
            return printers;
        } catch (SAXException ex) {
        } catch (IOException ex) {
        }


        return null;
    }

    /**
     * Connects to server and tries to get valid XML
     *
     * @return
     */
    private String getXMLFromServer() {
        final URLConnection connect;
        String server = config.getString("server.root");
        int port = config.getInt("server.port");
        String path = config.getString("server.path");
        String protocol = config.getString("server.protocol");
        String url = protocol + server + ":" + port + path;
        String output = "";

        try {
            log.debug("checking connection to " + url);
            URL u = new URL(url);
            connect = u.openConnection();
            connect.setConnectTimeout(2000); // 2 seconds
            connect.connect();
            log.debug("We appear to be able to connect!");
            InputStream instream = connect.getInputStream();
            Scanner in = new Scanner(instream);
            if (in.hasNext()) {
                output = "";
            }
            while (in.hasNextLine()) {
                output += in.nextLine() + "\n";
            }
            log.debug("finished fetching xml from server");
            instream.close();
            return output;
        } catch (MalformedURLException e) {
            log.error("Unable to connect to " + server + ": " + e.getMessage());
            return getBlank();
        } catch (SocketTimeoutException e) {
            log.error("Connection timed out looking for " + server);
            return getBlank();
        } catch (IOException e) {
            log.error("Unable to connect to " + server);
            return getBlank();
        }
    }

    /**
     * Return a blank xml string
     */
    private String getBlank() {
        String blank = "<?xml version='1.0' encoding='UTF-8' ?>\n"
                + "<printers />";
        return blank;
    }

    /**
     * Validate the XML TODO: Figure out how to correctly validate
     *
     * @param xml
     * @return
     */
    private boolean validateXML(String xml) {
        log.debug("Validating XML...");
        SchemaFactory factory =
                SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        URL schemaLocation = this.getClass().getResource("/printers.dtd");
        try {
            File f = new File(schemaLocation.toURI());
            log.debug(f.toString());
            Schema schema = factory.newSchema(f);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(xml);
            validator.validate(source);
        } catch (SAXException ex) {
            log.debug("SAXException: " + ex.getLocalizedMessage());
            return false;
        } catch (URISyntaxException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.debug("Invalid XML: " + ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    /**
     * Determine if the location name already exists in the location list
     *
     * @param locationName
     * @param locationList
     * @return
     */
    private boolean locationExists(String locationName, List<Location> locationList) {
        for (Location l : locationList) {
            if (l.getName().equals(locationName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reset() {
        xml = null;
    }
    private static final Logger log = Logger.getLogger(PrinterDaoImpl.class);
    private final Config config;
    private String xml = null;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder docBuilder;
}
