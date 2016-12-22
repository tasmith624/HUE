package hueTablet;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.*;
import javax.swing.event.*;

public class MeasureFrictionPanel extends JPanel implements ActionListener {
	final int HEIGHT = 700, WIDTH = 1200;
	
	final double PIXELS_PER_MM = 5.53;
	
	JButton jbtnStart;
	JButton jbtnStop;
	JButton jbtnReset;
	
	JLabel jlabSpatialPeriod;
	JLabel jlabBallVelocity;
	
	
	JSlider jsldrSpatialPeriod;
	JSlider jsldrBallVelocity;
	
	BallGratingPanel gp;
	
	int spatialPeriod_pixels = 50;
	//Ball velocity in units of pixels per cycle.
	int ballVelocity_ppc = 2;
	
	public MeasureFrictionPanel() {
		//Create the ball grating panel.
		gp = new BallGratingPanel(1200,600, spatialPeriod_pixels);
		
		
		setOpaque(true);
		
		setLayout(new BorderLayout());
		
		//Add sub panels.
		add(createControlPanel(), BorderLayout.SOUTH);
		add(gp, BorderLayout.CENTER);
		
		
		//start with the "Stop" and "Reset" buttons disabled.
		jbtnStop.setEnabled(false);
		jbtnReset.setEnabled(false);
		
	}
	
	//Creates the Start and Stop buttons and the spatial period slider.
	private JPanel createControlPanel() {
		JPanel jpnl = new JPanel();
		
		jbtnStart = new JButton("Start");
		jbtnStop = new JButton("Stop");
		jbtnReset = new JButton("Reset");
		
		//Add button action listeners.
		jbtnStart.addActionListener(this);
		jbtnStop.addActionListener(this);
		jbtnReset.addActionListener(this);
		
		jsldrSpatialPeriod = new JSlider(10,100);
		jsldrBallVelocity = new JSlider(1,10);
		
		//Set the slider tick spacing.
		jsldrSpatialPeriod.setMajorTickSpacing(10);
		jsldrSpatialPeriod.setMinorTickSpacing(5);
		jsldrBallVelocity.setMajorTickSpacing(1);
		
		//Create standard numeric labels
		jsldrSpatialPeriod.setLabelTable(jsldrSpatialPeriod.createStandardLabels(10));
		jsldrBallVelocity.setLabelTable(jsldrBallVelocity.createStandardLabels(1));
		
		
		//Cause the tick marks to be displayed.
		jsldrSpatialPeriod.setPaintTicks(true);
		jsldrBallVelocity.setPaintTicks(true);
		
		//Cause the labels to be displayed.
		jsldrSpatialPeriod.setPaintLabels(true);
		jsldrBallVelocity.setPaintLabels(true);
		
		//Synchronize the spatial period and ball velocity with initial slider values.
		spatialPeriod_pixels = jsldrSpatialPeriod.getValue();
		ballVelocity_ppc = jsldrSpatialPeriod.getValue();
		
		//This label displays the current slider value in mm.
		double spatialPeriod_mm = spatialPeriod_pixels / PIXELS_PER_MM;
		spatialPeriod_mm = BigDecimal.valueOf(spatialPeriod_mm).setScale(1, RoundingMode.HALF_UP).doubleValue();
		jlabSpatialPeriod = new JLabel("Spatial Period: " + spatialPeriod_mm + " mm");
		
		//This label displays the ball velocity slider value in pixels per second.
		double ballVelocity_pps = ballVelocity_ppc / 0.01;
		ballVelocity_pps = BigDecimal.valueOf(ballVelocity_pps).setScale(1, RoundingMode.HALF_UP).doubleValue();
		jlabBallVelocity = new JLabel("Ball Velocity: " + ballVelocity_pps + "pix/s");
			
		//Set the spatial period and ball velocity to the initial slider values.
		gp.setSpatialPeriod(spatialPeriod_pixels);
		gp.setVelocity(ballVelocity_ppc, 0);
		
		//Add change listener for the spatial period slider.
		jsldrSpatialPeriod.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				//Do nothing if the slider is being adjusted;
				if(jsldrSpatialPeriod.getValueIsAdjusting()) return;
				
				//Display the new value and save the spatial period setting.
				double spatialPeriod_mm = jsldrSpatialPeriod.getValue() / PIXELS_PER_MM;
				spatialPeriod_mm = BigDecimal.valueOf(spatialPeriod_mm).setScale(1, RoundingMode.HALF_UP).doubleValue();
				
				jlabSpatialPeriod.setText("Spatial Period: " + spatialPeriod_mm + " mm");
				spatialPeriod_pixels = jsldrSpatialPeriod.getValue();
				gp.setSpatialPeriod(spatialPeriod_pixels);
			}
		});
		
		jsldrBallVelocity.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				//Set the ball velocity to the slider value.
				ballVelocity_ppc = jsldrBallVelocity.getValue();
				
				//Update velocity label.
				double ballVelocity_pps = ballVelocity_ppc / 0.01;
				ballVelocity_pps = BigDecimal.valueOf(ballVelocity_pps).setScale(1, RoundingMode.HALF_UP).doubleValue();
				jlabBallVelocity.setText("Ball Velocity: " + ballVelocity_pps + "pix/s");
				
				//Set the new ball velocity.
				gp.setVelocity(ballVelocity_ppc, 0);
			}
		});
		
	
	
		//Add components to the panel.
		jpnl.add(jbtnStart);
		jpnl.add(jbtnStop);
		jpnl.add(jbtnReset);
		jpnl.add(jsldrSpatialPeriod);
		jpnl.add(jlabSpatialPeriod);
		jpnl.add(jsldrBallVelocity);
		jpnl.add(jlabBallVelocity);
		
		return jpnl;
		
	}
	
	
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == jbtnStart) {
			//Disable the "Start" button while the trial is running.
			jbtnStart.setEnabled(false);
			
			//Enable the "Stop" and "Reset" buttons.
			jbtnReset.setEnabled(true);
			jbtnStop.setEnabled(true);
			
			//Start the Ball animation. 
			gp.start();
		} else if(ae.getSource() == jbtnStop) {
			//Enable the "Start" button and disable "Stop" button.
			jbtnStart.setEnabled(true);
			jbtnStop.setEnabled(false);
			
			//Stop the Ball animation.
			gp.stop();
			
		} else if(ae.getSource() == jbtnReset) {
			jbtnStop.setEnabled(false);
			jbtnStart.setEnabled(true);
			
			//Reset the ball to original position.
			gp.reset();
		}
		
	}

}
