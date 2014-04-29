package org.simoes.lpd.ui;

import org.simoes.lpd.common.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import org.apache.log4j.Logger;

/**
 * The GUI window that shows all print jobs that have been
 * sent to the default queue.  The GUI is not required for
 * the lpdspooler.  This has been added just to show initial 
 * users what is happening.  An actual implementation might not
 * use this GUI at all.  Maybe in time it will be slicker.
 * 
 * @author Chris Simoes
 *
 */
public class PrintJobJFrame extends javax.swing.JFrame {
	static Logger log = Logger.getLogger(PrintJobJFrame.class);

	SymMouse aSymMouse = new SymMouse();
	// Used by addNotify
	boolean frameSizeAdjusted = false;
	JMenuBar menuBar = new JMenuBar();

	JMenu editMenu = new JMenu(Constants.MENU_EDIT);
	JMenu aboutMenu = new JMenu(Constants.MENU_ABOUT);
	
	JMenuItem itemDeleteAll = new JMenuItem(Constants.MENU_ITEM_DELETE_ALL);
	JMenuItem itemAbout = new JMenuItem(Constants.MENU_ITEM_ABOUT);
	
	PrintJobTableModel printJobTableModel = null;
	
	/**
	 * Constructor requiring the TableModel the View will show.
	 * @param tableModel the TableModel our View will show
	 */
	public PrintJobJFrame(TableModel tableModel)
	{
		BorderLayout layout = new BorderLayout();
		//setResizable(false);
		setTitle("Print Job JFrame");
		setJMenuBar(menuBar);
		setSize(600,400);
		setVisible(true);
		// initialize the menu bar
		menuBar.add(editMenu);
		menuBar.add(aboutMenu);
		editMenu.add(itemDeleteAll);
		aboutMenu.add(itemAbout);
		
		// initialize the table
		printJobTableModel = (PrintJobTableModel) tableModel;
		JTable queueTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane();//queueTable);
		
		JViewport viewport = new JViewport();
		viewport.add(queueTable);
		scrollPane.setOpaque(false);
		scrollPane.setViewport(viewport);
		
		getContentPane().setLayout(layout);
		getContentPane().add(scrollPane, BorderLayout.NORTH);
		
	
		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		//aSymMouse = new SymMouse();
		
		LpdMenuListener aLpdMenuListener = new LpdMenuListener();
		itemDeleteAll.addActionListener(aLpdMenuListener);
		itemAbout.addActionListener(aLpdMenuListener);
		//}}
	}

	/**
	 * Constructor requiring the TableModel the View will show and the
	 * title of the GUI.
	 * @param sTitle the title of the window 
	 * @param tableModel the TableModel our View will show
	 */
	public PrintJobJFrame(String sTitle, TableModel tableModel)
	{
		this(tableModel);
		setTitle(sTitle);
	}

	public void run()  {
        final String METHOD_NAME = "run(): ";
		setVisible(true);
	}

	public void setVisible(boolean b)
	{
		if (b)
			setLocation(50, 50);
		super.setVisible(b);
	}

	static public void main(String args[])
	{
/*
		PrintJobTableModel pjtm = new PrintJobTableModel();
		PrintJobJFrame printJobJFrame = new PrintJobJFrame(pjtm); 
		printJobJFrame.setVisible(true);
		try {
			Thread.sleep(2000);
			Vector row1 = new Vector();
			row1.add("Test");
			row1.add("Test2");
			row1.add("Test3");
			pjtm.addRow(row1);

			Thread.sleep(2000);
			Vector row2 = new Vector();
			row2.add("TestA");
			row2.add("TestB");
			row2.add("TestC");
			pjtm.addRow(row2);
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
*/		
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension size = getSize();

		super.addNotify();

		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;

		// Adjust size of frame according to the insets and menu bar
		Insets insets = getInsets();
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null)
			menuBarHeight = menuBar.getPreferredSize().height;
		setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == PrintJobJFrame.this) {
				PrintJobJFrame_windowClosing(event);
			}
		}
	}

	void PrintJobJFrame_windowClosing(java.awt.event.WindowEvent event)
	{
		PrintJobJFrame.this.dispose();
	}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseClicked(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			//System.out.println("Mouse clicked: object=" + object);
/*
			if (object == JButtonUpdateQueue) {
				JButtonUpdateQueue_mouseClicked(event);
			} 
*/
				}
	}

	class LpdMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			final String METHOD_NAME = "LpdMenuListener.actionPerformed(): ";
			log.debug(METHOD_NAME + "Action command: " + e.getActionCommand());
			if(Constants.MENU_ITEM_DELETE_ALL == e.getActionCommand()) {
				JMenuItem sourceItem = (JMenuItem) e.getSource();
				deleteAll();
			} else if(Constants.MENU_ITEM_ABOUT == e.getActionCommand()) {
				JMenuItem sourceItem = (JMenuItem) e.getSource();
				showDialog("LPDspooler", "Thanks for trying lpdspooler.");
			} else {
				log.warn(METHOD_NAME + "Action Command:" + e.getActionCommand() + ", is not supported.");
			}
		 }
	}

	void deleteAll() {
		printJobTableModel.removeAllRows();
		
	}
	
	void showDialog(String title, String text) {
		final JDialog d = new JDialog(this, title, true);
		d.setSize(200,100);
		JLabel l = new JLabel(text, JLabel.CENTER);
		d.getContentPane().setLayout(new BorderLayout());
		d.getContentPane().add(l, BorderLayout.CENTER);
		JButton b = new JButton("OK");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.setVisible(false);
				d.dispose();
			}
		});
		JPanel p = new JPanel();
		p.add(b);
		d.getContentPane().add(p, BorderLayout.SOUTH);
		d.setLocationRelativeTo(this);
		d.setVisible(true);
	}
	
/*
	void JButtonUpdateQueue_mouseClicked(java.awt.event.MouseEvent event)
	{
        final String METHOD_NAME = "JButtonUpdateQueue_mouseClicked(): ";
	    SoapClient soapClient = new SoapClient();
	    try {
	        Vector capturedLabels = soapClient.getReprintQueue();
	        Iterator iter = capturedLabels.iterator();
	        while(iter.hasNext()) {
	            PrintLabel cl = (PrintLabel) iter.next();
                reprintTableModel.addRow(cl);
	        }
            reprintTableModel.fireTableDataChanged();
        } catch(NomadSoapException e) {
            cat.error(METHOD_NAME + e.getMessage(), e);
            JTextAreaStatus.setText(e.getMessage());
        }
	}
*/
}