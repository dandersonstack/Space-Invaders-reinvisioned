package components.bunkers;

import java.awt.Graphics;

import main.SpaceInvadersMain;

public class BasicIndistructible implements interfaces.Bunker {

	private int x;
	private int y;
	private int width;
	private int height;
	public int speed = 5;//if the bunker is to move, this is the speed at which it does
	public boolean goingRight = true;
	
	public BasicIndistructible(int X, int Y, int w, int h) {
		x = X;
		y = Y;
		width = w;
		height = h;
	}
	
	@Override
	public void updateDraw() {
		if(x > SpaceInvadersMain.winSize[0]) move((SpaceInvadersMain.winSize[0] + width) * -1, 0);
		else move(3, 0);
	}

	@Override
	public void draw(Graphics g) {
		g.fillRect(x, y, width, height);//draw to the specified dimensions
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
	public void move(int xOff, int yOff) {
		x += xOff;
		y += yOff;
	}

	@Override
	public void moveX(int off) {
		if(SpaceInvadersMain.lvl > 9) {//bunkers only can move After lvl 9
			if(x > SpaceInvadersMain.winSize[0] + 100 && goingRight) {//this might change in the future...
				x += speed;//but for now the bunkers move in the same was as the invaders
			} else if(x > -100 - width && !goingRight) {
				x -= speed;
			} else goingRight = !goingRight;
		}
	}

	@Override
	public boolean hit(int projIndex, boolean playerProj) {//second part of the bunkerCollisoin method, is different for every type of bunker
		if(playerProj) {//playerProj is a boolean that dictates if it is a player projectile, and thus how to treat it.
			if(SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[0] < x + width)//if it is to the left of the right edge, move on(already confirmed to be to the right of the left edge)
				if(SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[1] < y + height) {//if above the bottom (already confirmed to be bellow the top) then the projectile has hit the bunker
					SpaceInvadersMain.projectiles.PlayerProjectiles.remove(projIndex);//because the bunker is invincible, the projectile is absorbed and removed
					return true;
				}
		} else {
			if(SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[0] < x + width)//same as above, except with the invader projectile instead of the player
				if(SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[1] < y + height) {
					SpaceInvadersMain.projectiles.InvaderProjectiles .remove(projIndex);
					return true;
				}
		}
		return false;
	}
}
