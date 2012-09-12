package silbersoft.uprint.ui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBlue;
import com.typesafe.config.Config;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;
import silbersoft.uprint.domain.Printer;
import silbersoft.uprint.ui.models.PrintViewListModel;
import silbersoft.uprint.utils.R;

/**
 * Sets up the Main GUI for uprint
 *
 * @author Matt Silbernagel
 */
public class PrintViewImpl implements PrintView {

    public PrintViewImpl(Config config) {
        this.config = config;
        PlasticLookAndFeel laf = new PlasticXPLookAndFeel();
        PlasticLookAndFeel.setPlasticTheme(new DesertBlue());
        if (!System.getProperty("os.name").startsWith("Mac")) {
            try {
                UIManager.setLookAndFeel(laf);
            } catch (UnsupportedLookAndFeelException e) {
                log.debug("Unsupported Look and Feel");
            }
        }

    }

    private void buildFrame() {
        FormLayout layout = new FormLayout("pref:grow, pref:grow", "pref, 4dlu, pref, pref:grow, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(createTitlePanel(), cc.xyw(1, 1, 2));
        builder.addSeparator("", cc.xyw(1, 2, 2));
        builder.add(createListPanel(), cc.xyw(1, 3, 2));
        builder.add(createButtonPanel(), cc.xy(2, 5));
        mainFrame.add(builder.getPanel());
        mainFrame.setJMenuBar(createMainMenu());
    }

    @Override
    public void showFrame() {
        mainFrame = new JFrame();
        mainFrame.setTitle(R.getString("frame.title"));
        mainFrame.setSize(R.getInteger("frame.width"), R.getInteger("frame.height"));
        mainFrame.setIconImage(R.getImage("frame.iconimage"));
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildFrame();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame.validate();
                mainFrame.pack();
                mainFrame.setVisible(true);
            }
        });
    }

    @Override
    public void setPrintList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Create and return a panel for the Title
     *
     * @return Component
     */
    private Component createTitlePanel() {
        FormLayout layout = new FormLayout(
                "left:125px:grow, center:pref, right:25px:grow",
                "center:74px, bottom:pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setBackground(Color.WHITE);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.add(new ImagePanel(), cc.xy(1, 1));
        JLabel title = new JLabel(R.getString("frame.largeTitle"));
        title.setFont(new Font("", Font.PLAIN, 32));
        builder.add(title, cc.xy(2, 1));
        return builder.getPanel();
    }

    /**
     * Create and return a panel for the building and printer lists
     *
     * @return
     */
    private Component createListPanel() {
        JList printerList = new ZebraList(printerListModel.getListModel());
        printerList.setCellRenderer(new PrinterCellRenderer());
        printerList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        printerList.addListSelectionListener(printerListModel);
        printerList.setFont(f);
        JScrollPane printerListScroll = new JScrollPane(printerList);

        JList buildingList = new ZebraList(buildingListModel.getListModel());
        buildingList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        buildingList.addListSelectionListener(buildingListModel);
        buildingList.setFont(f);
        JScrollPane buildingListScroll = new JScrollPane(buildingList);

        FormLayout layout = new FormLayout("pref:grow, 20dlu, pref:grow", "8dlu, 4dlu, top:pref:grow");
        layout.setColumnGroups(new int[][]{{1, 3}}); // make sure that columns 1 and 3 stay the same size       
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addSeparator(R.getString("frame.locations.title"), cc.xy(1, 1));
        builder.add(buildingListScroll, cc.xy(1, 3));
        builder.addSeparator(R.getString("frame.printers.title"), cc.xy(3, 1));
        builder.add(printerListScroll, cc.xy(3, 3));

        return builder.getPanel();
    }

    /**
     * Create a return a panel for the buttons
     *
     * @return
     */
    private Component createButtonPanel() {
        FormLayout layout = new FormLayout("pref:grow,50dlu,4dlu,50dlu", "pref");
        PanelBuilder builder = new PanelBuilder(layout);
        printBtn = new JButton(printAction);
        cancelBtn = new JButton(cancelAction);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.add(printBtn, cc.xy(2, 1));
        builder.add(cancelBtn, cc.xy(4, 1));
        return builder.getPanel();
    }
    
    private JMenuBar createMainMenu(){
        JMenuBar menu = new JMenuBar();
        
        //File Menu
        JMenu fileMenu = new JMenu(R.getString("menu.file.title"));
        fileMenu.setMnemonic(KeyEvent.VK_F);
        // Print Item
        JMenuItem printItem = new JMenuItem(R.getString("menu.file.print"));
        printItem.setMnemonic(KeyEvent.VK_P);
        printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        printItem.setAction(printAction);
        fileMenu.add(printItem);
        // Exit Item
        JMenuItem exitItem = new JMenuItem(R.getString("menu.file.exit"), KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        exitItem.setAction(cancelAction);
        fileMenu.add(exitItem);
        menu.add(fileMenu);
        
        return menu;
    }

    @Override
    public void setPrintModel(Action printAction) {
        this.printAction = printAction;
    }

    @Override
    public void setCancelModel(Action cancelAction) {
        this.cancelAction = cancelAction;
    }

    @Override
    public void setBuildingListModel(PrintViewListModel buildingListModel) {
        this.buildingListModel = buildingListModel;
    }

    @Override
    public void setPrinterListModel(PrintViewListModel printListModel) {
        this.printerListModel = printListModel;
    }

    /**
     * Determines how the printers are displayed in the list
     */
    private class PrinterCellRenderer extends JLabel implements ListCellRenderer {

        private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

        public PrinterCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList jlist, Object o, int i, boolean bln, boolean bln1) {
            Printer printer = (Printer) o;
            setText(printer.getName());
            setFont(f);
            if (bln) {
                setBackground(Color.darkGray);
                setForeground(Color.white);
                setFont(f);
            } else {
                setBackground(Color.white);
                setForeground(Color.black);
                setFont(f);
            }
            return this;
        }
    }

    /**
     * Dispose of the frame
     */
    public static void tearDown() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (mainFrame.isDisplayable()) {
                    mainFrame.dispose();
                    mainFrame = null;
                }
            }
        });
    }

    /**
     * Prompt the user for their username
     *
     * @return
     */
    public static String promptForUsername() {

        String username = JOptionPane.showInputDialog(mainFrame, R.getString("frame.input.username.message"),
                R.getString("frame.input.username.title"),
                JOptionPane.QUESTION_MESSAGE, null, null, System.getProperty("user.name")).toString();
        if (username == null) {
            // user canceled 
            return null;
        } else {
            username = System.getProperty("user.name");
        }
        return username;
    }

    public static void showSuccess() {
        JOptionPane.showMessageDialog(mainFrame,
                R.getString("print.success"),
                R.getString("print.success.title"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static int showFailure(String errorMsg) {
        int retry = JOptionPane.showConfirmDialog(mainFrame,
                R.getString("print.fail") + "\nErrorMessage: " + errorMsg,
                R.getString("print.fail.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);
        log.debug("retry = " + retry);
        return retry;
    }
    private static final Logger log = Logger.getLogger(PrintViewImpl.class);
    private static JFrame mainFrame;
    private JButton printBtn;
    private JButton cancelBtn;
    private PrintViewListModel buildingListModel, printerListModel;
    private final int FONT_SIZE = 12;
    private final Font f = new Font(null, Font.PLAIN, FONT_SIZE);
    private Action printAction, cancelAction;
    private Config config;
}
