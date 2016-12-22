package hueTablet;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {
	int xVel, yVel, x, y;
	
	public Ball(int startX, int startY, int xV, int yV) {
		x = startX;
		y = startY;
		xVel = xV;
		yVel = yV;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.blue);
		g.fillOval((int)x-15, (int)y-15, 30, 30);
	}
	
	public void move() {
		x += xVel;
		y += yVel;
	}
	
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public void setPosition(int setX, int setY) {
		x = setX;
		y = setY;
	}
	
	public void setVelocity(int xV, int yV) {
		xVel = xV;
		yVel = yV;
	}
}
