package components;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import main.SpaceInvadersMain;

public class Player implements interfaces.Living {

	private int x;
	private int y;
	private boolean isAlive;
	
	public Player(int X, int Y) {
		x = X;
		y = Y;
		isAlive = true;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(x + 20, y, 5, 5);
		g.fillRect(x, y + 5, 45, 25);
	}

	@Override
	public void move(int xOff, int yOff) {
		x += xOff;
		y += yOff;
	}

	@Override
	public void moveX(int off) {
		x += off;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public boolean isLiving() {
		return isAlive;
	}

	@Override
	public byte getIType() {
		return 127;//Big number so if this method is called it goes to the default option
	}
	
	@Override
	public void kill() {
		isAlive = false;
		SpaceInvadersMain.running = false;
		try {
			SpaceInvadersMain.score.writeData();
		} catch (IOException e) {
			System.out.println("ERROR: Failed to write data! IT IS ALL GONE!!!!");
			e.printStackTrace();
		}
	}

	@Override
	public void updateDraw() {}

}
