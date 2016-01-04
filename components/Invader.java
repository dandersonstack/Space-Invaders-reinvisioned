package components;

import java.awt.Graphics;

import main.SpaceInvadersMain;

public class Invader implements interfaces.Living {

	private int x;
	private int y;
	private byte type;
	private static boolean model1;//use the first model? //model1 is static, and therefore all invaders share this 1 value
	private boolean isAlive;
	
	public Invader(int X, int Y, byte Type) {
		x = X;
		y = Y;
		type = Type;
		isAlive = true;
		model1 = true;
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
	public void draw(Graphics g) {
		switch(type) {
		case 0:
			if(isAlive) {
				if(model1) g.drawImage(SpaceInvadersMain.invaderModels.modles[0], x + 8, y, SpaceInvadersMain.window);//draw the first model
				else g.drawImage(SpaceInvadersMain.invaderModels.modles[1], x + 8, y, SpaceInvadersMain.window);//draw the second, same for all cases
			} else g.clearRect(x, y, SpaceInvadersMain.invaderModels.modelDims[0][0], SpaceInvadersMain.invaderModels.modelDims[0][1]);//if dead then get rid of it
			break;
		case 1:
			if(isAlive) {
				if(model1) g.drawImage(SpaceInvadersMain.invaderModels.modles[2], x + 2, y, SpaceInvadersMain.window);
				else g.drawImage(SpaceInvadersMain.invaderModels.modles[3], x + 2, y, SpaceInvadersMain.window);
			} else g.clearRect(x, y, SpaceInvadersMain.invaderModels.modelDims[2][0], SpaceInvadersMain.invaderModels.modelDims[2][1]);
			break;
		case 2:
			if(isAlive) {
				if(model1) g.drawImage(SpaceInvadersMain.invaderModels.modles[4], x, y, SpaceInvadersMain.window);
				else g.drawImage(SpaceInvadersMain.invaderModels.modles[5], x, y, SpaceInvadersMain.window);
			} else g.clearRect(x, y, SpaceInvadersMain.invaderModels.modelDims[4][0], SpaceInvadersMain.invaderModels.modelDims[4][1]);
			break;
		default:
			if(isAlive) {
				if(model1) g.fillRect(x, y, 16, 16);
				else g.drawRect(x, y, 16, 16);
			} else g.clearRect(x, y, 16, 16);
			break;
		}
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
		return type;
	}

	@Override
	public void kill() {
		isAlive = false;
		SpaceInvadersMain.score.addScore(type * 50 + 50);
		if(SpaceInvadersMain.rand.nextInt(80) == 2) SpaceInvadersMain.drops.generateDrop(x, y, SpaceInvadersMain.rand);
	}

	@Override
	public void updateDraw() {
		model1 = !model1;//update the model to use
	}

}
