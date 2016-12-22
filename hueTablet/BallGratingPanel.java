package hueTablet;


import java.awt.Graphics;

public class BallGratingPanel extends GratingPanel {
	
	Ball b1;
	
	//Initialize ball properties.
	int startX = 0;
	int startY = 0;
	
	int velX = 2;
	int velY = 0;
	
	public BallGratingPanel(int w, int h, int spatP) {
		//Call Grating Panel constructor.
		super(w, h, spatP);
		
		//Default starting position for the ball.
		startY = h/2;
		
		//Create the ball object.
		b1 = new Ball(startX, startY, velX, velY);
			
	}
	
	protected void updateGraphics() {
		//If the animation isn't running, return immediately.
		if(!running) return;
		
		//If the ball is out of bounds, stop running, and return.
		if(!inBounds()) {
			stop();
			repaint();
			return;
		}
		
		//Save the current position of the ball.
		x = b1.getX();
		y = b1.getY();
		
		//Move the ball.
		b1.move();
		repaint();
	}
	
	public void reset() {
		running = false;
		b1.setPosition(startX, startY);
		x = startX;
		y = startY;
		repaint();
	}
	
	public boolean inBounds() {
		return(b1.getX() >= 0 && b1.getY() >=0 && b1.getX() <= width && b1.getY() <= height);
	}
	
	public void setVelocity(int xV, int yV) {
		b1.setVelocity(xV, yV);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		b1.draw(g);
	}

}
