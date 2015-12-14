package components;

import java.awt.Graphics;
import java.util.ArrayList;

import main.SpaceInvadersMain;

public class Projectile implements interfaces.Drawable {
	public ArrayList<int[]> PlayerProjectiles = new ArrayList<int[]>();//[X, Y]
	public ArrayList<int[]> InvaderProjectiles = new ArrayList<int[]>();//[X, Y, Projectile Type]
	private int speed = 2;
	
	public void addProjectileToPlayer(int x, int y) {
		x += 20;
		int[] temp = new int[2];
		temp[0] = x;
		temp[1] = y;
		//System.out.println("ADDING PLAYER PROJECTILE AT: [" + x + ", " + y + "]");
		//System.out.println("CURRENT TICK COUNT: " + SpaceInvadersMain.tickCounter);
		PlayerProjectiles.add(temp);
	}
	
	public void addProjectileToInvaders(int invaderId) {
		int x = SpaceInvadersMain.window.mc.ents.get(invaderId).getX() + (SpaceInvadersMain.invaderModels.modelDims[5][0] / 2);
		int y = SpaceInvadersMain.window.mc.ents.get(invaderId).getY() + SpaceInvadersMain.invaderModels.modelDims[SpaceInvadersMain.window.mc.ents.get(invaderId).getIType() * 2][1];
		int type = SpaceInvadersMain.window.mc.ents.get(invaderId).getIType();
		int[] temp = new int[3];
		temp[0] = x;
		temp[1] = y;
		temp[2] = type;
		//System.out.println("ADDING INVADER PROJECTILE AT: [" + x + ", " + y + "]");
		//System.out.println("CURRENT TICK COUNT: " + SpaceInvadersMain.tickCounter);
		InvaderProjectiles.add(temp);
		//for(int i = 0; i < InvaderProjectiles.size(); i++) System.out.println("TYPE: " + InvaderProjectiles.get(i)[2] + "; AT: [" + InvaderProjectiles.get(i)[0] + ", " + InvaderProjectiles.get(i)[1] + "]");
	}
	
	public void updateProjectilePos(){
		if(PlayerProjectiles.size() != 0) {
			for(int i = 0; i < PlayerProjectiles.size(); i++) {
				if(PlayerProjectiles.get(i)[1] < 0) PlayerProjectiles.remove(i);
				else PlayerProjectiles.get(i)[1] -= speed;
			}
		}
		if(InvaderProjectiles.size() != 0) {
			for(int i = 0; i < InvaderProjectiles.size(); i++) {
				if(InvaderProjectiles.get(i)[1] > 1500) InvaderProjectiles.remove(i);
				else InvaderProjectiles.get(i)[1] += speed;
			}
		}
	}

	@Override
	public void updateDraw() {
		
	}

	@Override
	public void draw(Graphics g) {
		for(int i = 0; i < PlayerProjectiles.size(); i++) g.fillRect(PlayerProjectiles.get(i)[0], PlayerProjectiles.get(i)[1], 4, 16);
		for(int i = 0; i < InvaderProjectiles.size(); i++) g.fillRect(InvaderProjectiles.get(i)[0], InvaderProjectiles.get(i)[1] - 16, 2, 16);
	}
	
}
