package components.bunkers;

import java.awt.Graphics;
import java.util.ArrayList;

import main.SpaceInvadersMain;

public class BrickDestructible implements interfaces.Bunker {

	private int x;
	private int y;
	private int[] brickDims;
	private int[] bunkerDims;
	private ArrayList<int[]> parts;
	
	public BrickDestructible(int X, int Y, int[] brDims, int[] bDims) {//brDims = Dimentions of individual bricks; bDims = [bricks wide, layers tall]
		x = X;
		y = Y;
		brickDims = brDims;
		bunkerDims = bDims;
		parts = new ArrayList<int[]>();
		for(int i = 0; i < bunkerDims[1]; i++) {
			for(int j = 0; j < bunkerDims[0]; j++) {
				int[] temp = new int[2];
				temp[1] = (brickDims[1] + 1) * i;
				temp[0] = (brickDims[0] * 2 + 1) * j;
				if(i % 2 == 0) {
					parts.add(temp);
				} else {
					temp[0] += brickDims[0];
					parts.add(temp);
				}
			}
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
			int[] bounds = getBounds();
			moveX((SpaceInvadersMain.winSize[0] + (bounds[0] - x)) * -1);
		} else moveX(3);
	}

	@Override
	public void draw(Graphics g) {
		for(int[] p : parts) g.fillRect(x + p[0], y + p[1], brickDims[0] * 2, brickDims[1]);
	}
	
	private int[] getBounds() {
		int[] temp = new int[2];
		temp[0] = 0;
		temp[1] = 0;
		for(int[] p : parts) {
			if(p[0] > temp[0]) temp[0] = p[0];
			if(p[1] > temp[1]) temp[1] = p[1];
		}
		temp[0] += x + (brickDims[0] * 2);
		temp[1] += y + brickDims[1];
		return temp;
	}

	@Override
	public boolean hit(int projIndex, int projType) {
		int[] bounds = getBounds();
		if(projType == 0) {
			if(SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[0] < bounds[0] &&
					SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[1] < bounds[1]) for(int i = 0; i < parts.size(); i++)
						if(parts.get(i)[1] + y < SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[1] &&
								SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[1] < parts.get(i)[1] + brickDims[1] + y)
							if(parts.get(i)[0] + x < SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[0] &&
									SpaceInvadersMain.projectiles.PlayerProjectiles.get(projIndex)[0] < parts.get(i)[0] + x + (brickDims[0] * 2)) {
								parts.remove(i);
								SpaceInvadersMain.projectiles.PlayerProjectiles.remove(projIndex);
								return true;
							}
		} else if(projType == 1) {
			if(SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[0] < bounds[0] &&
					SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[1] < bounds[1]) for(int i = 0; i < parts.size(); i++)
						if(parts.get(i)[1] + y < SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[1] &&
								SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[1] < parts.get(i)[1] + brickDims[1] + y)
							if(parts.get(i)[0] + x < SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[0] &&
									SpaceInvadersMain.projectiles.InvaderProjectiles.get(projIndex)[0] < parts.get(i)[0] + x + (brickDims[0] * 2)) {
								parts.remove(i);
								SpaceInvadersMain.projectiles.InvaderProjectiles.remove(projIndex);
								return true;
							}
		} else {
			if(SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[0] < bounds[0] &&
					SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[1] < bounds[1]) for(int i = 0; i < parts.size(); i++)
						if(parts.get(i)[1] + y < SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[1] &&
								SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[1] < parts.get(i)[1] + brickDims[1] + y)
							if(parts.get(i)[0] + x < SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[0] &&
									SpaceInvadersMain.projectiles.SpecialProjectiles.get(projIndex)[0] < parts.get(i)[0] + x + (brickDims[0] * 2)) {
								parts.remove(i);
								SpaceInvadersMain.projectiles.SpecialProjectiles.remove(projIndex);
								return true;
							}
		}
		return false;
	}

}
