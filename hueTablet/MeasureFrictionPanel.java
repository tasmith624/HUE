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
	
	JLabel jlabSpatialPeriod;
	
	JSlider jsldrSpatialPeriod;
	
	GratingPanel gp;
	
	int spatialPeriod_pixels = 50;
	
	public MeasureFrictionPanel() {
		//Create components
		gp = new GratingPanel(1200,600);
		
		
		setOpaque(true);
		
		setLayout(new BorderLayout());
		
		//Add sub panels.
		add(createControlPanel(), BorderLayout.SOUTH);
		add(gp, BorderLayout.CENTER);
		
	}
	
	//Creates the Start and Stop buttons and the spatial period slider.
	private JPanel createControlPanel() {
		JPanel jpnl = new JPanel();
		
		jbtnStart = new JButton("Start");
		jbtnStop = new JButton("Stop");
		
		//Add button action listeners.
		jbtnStart.addActionListener(this);
		jbtnStop.addActionListener(this);
		
		jsldrSpatialPeriod = new JSlider(10,100);
		
		//Set the slider tick spacing.
		jsldrSpatialPeriod.setMajorTickSpacing(10);
		jsldrSpatialPeriod.setMinorTickSpacing(5);
		
		//Create standard numeric labels
		jsldrSpatialPeriod.setLabelTable(jsldrSpatialPeriod.createStandardLabels(10));
		
		//Cause the tick marks to be displayed.
		jsldrSpatialPeriod.setPaintTicks(true);
		
		//Cause the labels to be displayed.
		jsldrSpatialPeriod.setPaintLabels(true);
		
		//This label displays the current slider value in mm.
		
		double spatialPeriod_mm = jsldrSpatialPeriod.getValue() / PIXELS_PER_MM;
		spatialPeriod_mm = BigDecimal.valueOf(spatialPeriod_mm).setScale(1, RoundingMode.HALF_UP).doubleValue();
		jlabSpatialPeriod = new JLabel("Spatial Period: " + spatialPeriod_mm + " mm");
		
		
		//Add change listener for the slider.
		jsldrSpatialPeriod.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				//Do nothing if the slider is being adjusted;
				if(jsldrSpatialPeriod.getValueIsAdjusting()) return;
				
				//Display the new value and save the spatial period setting.
				double spatialPeriod_mm = jsldrSpatialPeriod.getValue() / PIXELS_PER_MM;
				spatialPeriod_mm = BigDecimal.valueOf(spatialPeriod_mm).setScale(1, RoundingMode.HALF_UP).doubleValue();
				
				jlabSpatialPeriod.setText("Spatial Period: " + spatialPeriod_mm + " mm");
				spatialPeriod_pixels = jsldrSpatialPeriod.getValue();
			}
		});
		
		//Add components to the panel.
		jpnl.add(jbtnStart);
		jpnl.add(jbtnStop);
		jpnl.add(jsldrSpatialPeriod);
		jpnl.add(jlabSpatialPeriod);
		
		return jpnl;
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		
		
	}

}
