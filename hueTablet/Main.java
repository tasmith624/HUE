package hueTablet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main implements ActionListener {
	MeasureFrictionPanel mfp;

	JFrame jfrm;
	
	public Main() {
		
		//Create the various panels.
		mfp = new MeasureFrictionPanel();
		
		//Create a new JFrame container.
		jfrm = new JFrame("HUE");
		
		//Set Layout.
		jfrm.getContentPane().setLayout(new CardLayout());
		
		jfrm.setSize(1200, 800);
		
		//Terminate the program when the user closes the application.
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Add the menu bar to the frame.
		jfrm.setJMenuBar(createMenuBar());
		
		//Add panels to the frame as cards.
		jfrm.getContentPane().add(mfp, "Measure Friction");
		
		//Display the frame.
		jfrm.setVisible(true);
		
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar jmb = new JMenuBar();
		
		jmb.add(createOperationsMenu());
		jmb.add(createWindowsMenu());
		
		return jmb;
	}
	
	private JMenu createOperationsMenu() {
		JMenu jmOperations = new JMenu("Operations");
		JMenuItem jmiSave = new JMenuItem("Save");
		JMenuItem jmiExit = new JMenuItem("Exit");
		
		jmOperations.add(jmiSave);
		jmOperations.add(jmiExit);
		
		//Add action listeners.
		jmiSave.addActionListener(this);
		jmiExit.addActionListener(this);
		
		return jmOperations;
	}
	
	private JMenu createWindowsMenu() {
		JMenu jmWindows = new JMenu("Windows");
		JMenuItem jmiUserInformation = new JMenuItem("User Information");
		JMenuItem jmiVirtualGratingTest = new JMenuItem("Virtual Grating Test");
		JMenuItem jmiMeasureFriction = new JMenuItem("Measure Friction");
		
		jmWindows.add(jmiUserInformation);
		jmWindows.add(jmiVirtualGratingTest);
		jmWindows.add(jmiMeasureFriction);
		
		//Add action listeners.
		jmiUserInformation.addActionListener(this);
		jmiVirtualGratingTest.addActionListener(this);
		jmiMeasureFriction.addActionListener(this);
		
		return jmWindows;
	}
	
	
	public static void main(String args[]) {
		//Create frame on the event dispatching thread.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
		
	}


	public void actionPerformed(ActionEvent ae) {
		String comStr = ae.getActionCommand();
		CardLayout cl = (CardLayout)(jfrm.getContentPane().getLayout());
		
		//If user chooses Exit, then exit the program.
		if(comStr.equals("Exit")) System.exit(0);
		
		if(comStr.equals("Measure Friction")) cl.show(jfrm.getContentPane(), comStr);
		
	}
}
