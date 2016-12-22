package hueTablet;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.*;
import javax.swing.event.*;

public class GratingPanel extends JPanel {
	
	boolean running = false;
	
	int spatialPeriod;
	int width;
	int height;
	
	int x = 0;
	int y = 0;
	
	Thread thrd;
	
	public GratingPanel(int w, int h, int spatP) {
		
		width = w;
		height = h;
		spatialPeriod = spatP;
		
		//Ensure the panel is opaque.
		setOpaque(true);
		
		//Set the preferred dimension as specified.
		setPreferredSize(new Dimension(width, height));
		
		//Set the background color.
		setBackground(Color.white);
		
		//Create the runnable object that will run in a separate thread and update the friction.
		Runnable myThread = new Runnable() {
			//This method will run in the separate thread.
			public void run() {
				try {
					//Update the friction and repaint the frame
					for(;;) {
						//Pause for 10 milliseconds.
						Thread.sleep(10);
						
						//Update friction
						
						//Invoke repaint() on the event dispatching thread.
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								updateGraphics();
							}
						});
					}
				} catch(InterruptedException exc) {
					System.out.println("Call to sleep was interrupted.");
					System.exit(1);
				}
			}
		};
		
		//Create a new thread.
		thrd = new Thread(myThread);
		
		//Start the thread.
		thrd.start();
	}
	
	public void setSpatialPeriod(int spatP) {
		spatialPeriod = spatP;
	}
	
	public void start() {
		running = true;
	}
	
	public void stop() {
		running = false;
	}
	
	//This method should be overwritten by the children of of GratingPanel for graphical manipulations.
	protected void updateGraphics() {
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
	}
}
