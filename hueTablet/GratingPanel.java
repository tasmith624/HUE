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
    
    //Specifies the current friction state as LOW (0), MID (1), or HIGH (2). Default (off) is 1. 
    private int frictionState = 1;
    
    int x = 0;
    int y = 0;
    
    Thread thrd;
    FrictionDriver driver;
    
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
        
        //Create a friction driver object with the specified haptic mode.
        //0 -> EV
        //1 -> UFM
        //2 -> Hybrid
        driver = new FrictionDriver(2); //May need to add the mode selection to constructor.
        
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
                        updateFriction();
                        
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
    
    private void updateFriction() {
        //Store the previous friction state, and find the new one.
        int prevFrictionState = frictionState;
        updateFrictionState();
        
       applyTransitionEffect(prevFrictionState);
        
        
    }
    
    private void applyTransitionEffect(int prevFrictionState) {
		if (prevFrictionState != frictionState) {
			try{
			if (prevFrictionState == 0) {
				if (frictionState == 2) {
					//Low to High
					driver.setFrictionHigh();
				} else {
					//Low to Middle
					driver.disableAll();
				}
			}
			else if (prevFrictionState == 1) {
				if (frictionState == 0) {
					//middle to low
					driver.setFrictionLow();
				} else {
					//middle to high
					driver.setFrictionHigh();
				}
			}
			else if (prevFrictionState == 2) {
				if (frictionState == 0) {
					//high to low
					driver.setFrictionLow();
				} else {
					//high to middle
					driver.disableAll();
				}
			}} catch (Exception e) {}
			
		}
	}
    
    //This method should be overwritten by the children of of GratingPanel for graphical manipulations.
    protected void updateGraphics() {
        
    }
    
    private void updateFrictionState() {
        //If not running, set the friction to the default state and return;
        if(!running) {
            frictionState = 1;
            return;
        }
        
        //Set the friction HIGH on even intervals, LOW on odd intervals.
        if ( ( (x/(spatialPeriod/2)) & 1 ) == 0) {
				//even
				frictionState = 2;
			} else {
				//odd
				frictionState = 0;
			}
        
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
    }
}
