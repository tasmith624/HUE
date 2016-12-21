package hueTablet;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.*;
import javax.swing.event.*;

public class GratingPanel extends JPanel {
	
	
	public GratingPanel(int width, int height) {
		//Ensure the panel is opaque.
		setOpaque(true);
		
		//Set the preferred dimension as specified.
		setPreferredSize(new Dimension(width, height));
		
		//Set the background color.
		setBackground(Color.white);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
	}
}
