package components.bunkers;

import java.awt.Graphics;
import java.util.ArrayList;

import main.SpaceInvadersMain;

public class BasicBlockDistructible implements interfaces.Bunker {

	private int x;
	private int y;
	private int boundX = 0;
	private int boundY = 0;
	private int[] blockDims;
	private ArrayList<int[]> parts = new ArrayList<int[]>();//holds x and y offsets: [xOffSet, yOffSet]
	
	public BasicBlockDistructible(int X, int Y, int[] BkDims, int[] BlDims) {//BkDims == how many blocks the array will hold(wxh), BlDims == dimensions of each block 
		x = X;
		y = Y;
		blockDims = BlDims;
		for(int i = 0; i < BkDims[0]; i++)
			for(int j = 0; j < BkDims[1]; j++) {
				int[] temp = new int[2];
				temp[0] = (i * (BlDims[0] + 1));
				temp[1] = (j * (BlDims[1] + 1));
				parts.add(temp);
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
	public void move(int xOff, int yOff) {
		x += xOff;
		y += yOff;
	}

	@Override
	public void moveX(int off) {
		x += off;
	}

	@Override
	public void updateDraw() {
		if(x > SpaceInvadersMain.winSize[0]) {
			getBounds();
			moveX((SpaceInvadersMain.winSize[0] + (boundX + blockDims[0] - x)) * -1);
		} else moveX(3);
	}

	@Override
	public void draw(Graphics g) {
		for(int i = 0; i < parts.size(); i++) g.fillRect(parts.get(i)[0] + x, parts.get(i)[1] + y, blockDims[0], blockDims[1]);
	}
	
	private void getBounds() {
		if(parts.size() <= 0) {
			boundX = x;
			boundY = y;
		}
		for(int i = 0; i < parts.size(); i++){
			if(boundX < parts.get(i)[0] + x) boundX = parts.get(i)[0] + x;
			if(boundY < parts.get(i)[1] + y) boundY = parts.get(i)[1] + y;
		}
	}

	@Override//TODO findout why top, 4rd and 7th layers can only be hit by invaders, and the others only by the player
	public boolean hit(int projIndex, int projType) {//second part of bunkerCollision method
		if(parts.size() <= 0) return false;//if the bunker is already gone, then nothing is going to hit this bunker
		getBounds();
		if(projType == 0) {
			if(SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[0] < boundX + blockDims[0])//if projectile is left of right edge, continue
				if(SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[1] < boundY + blockDims[1])//if projectile is above the bottom, continue
					for(int i = 0; i < parts.size(); i++)//start looking for what part the projectile will hit
						if(parts.get(i)[0] + x < SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[0] &&//test x bounds
								SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[0] < parts.get(i)[0] + blockDims[0] + x)
							if(parts.get(i)[1] + y < SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[1] &&//test y bounds
									SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[1] < parts.get(i)[1] + blockDims[1] + y) {
								SpaceInvadersMain.projectiles.PlayerProjectiles.remove(projIndex);
								parts.remove(i);
								return true;
							}
		} else if(projType == 1) {
			if(SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[0] < boundX + blockDims[0])
				if(SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[1] < boundY + blockDims[1])//same as above
					for(int i = parts.size() - 1; i >= 0; i--)//starts from the top instead of the bottom to improve efficiency
						if(parts.get(i)[0] + x < SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[0] &&// x bounds
								SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[0] < parts.get(i)[0] + blockDims[0] + x)
							if(parts.get(i)[1] + y < SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[1] &&// y bounds
									SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[1] < parts.get(i)[1] + blockDims[1] + y) {
								SpaceInvadersMain.projectiles.InvaderProjectiles.remove(projIndex);
								parts.remove(i);
								return true;
							}
		} else {
			if(SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[0] < boundX + blockDims[0])
				if(SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[1] < boundY + blockDims[1])//same as above
					for(int i = parts.size() - 1; i >= 0; i--)//starts from the top instead of the bottom to improve efficiency
						if(parts.get(i)[0] + x < SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[0] &&// x bounds
								SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[0] < parts.get(i)[0] + blockDims[0] + x)
							if(parts.get(i)[1] + y < SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[1] &&// y bounds
									SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[1] < parts.get(i)[1] + blockDims[1] + y) {
								SpaceInvadersMain.projectiles.SpecialProjectiles.remove(projIndex);
								parts.remove(i);
								return true;
							}
		}
		return false;
	}

}
