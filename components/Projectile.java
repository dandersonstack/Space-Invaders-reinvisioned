package components;

import java.awt.Graphics;
import java.util.ArrayList;

import main.SpaceInvadersMain;

public class Projectile implements interfaces.Drawable {
	public ArrayList<int[]> PlayerProjectiles = new ArrayList<int[]>();//[X, Y]
	public int playerProjectileID = -1;//used to switch between standard projectiles and special ones. -1 == standard, others == special projectile type
	public int ppStrength = 0;//playerProjectileStrength, I needed an external variable to hold this.
	public int projTimerDefault = 90 * 45;//time in ticks (1 / 90 of a second == 1)
	public int projectileTimer = -1;//-1 == done processing and back to default projectiles.
	public final int NUMER_OF_SPECIALS = 3;//number that needs to be changed when new projectiles are added
	public ArrayList<int[]> InvaderProjectiles = new ArrayList<int[]>();//[X, Y, Type]
	public ArrayList<int[]> SpecialProjectiles = new ArrayList<int[]>();//[X, Y, Direction, Type, Strength]
	//Directions: 0 == up; 1 == right; 2 == down; 3 == left
	private int speed = 2;
	
	public void addSpecialProjectiles(int originX, int originY, int type, int strength, int direction) {
		//types: 0 == scatter shot base; 1 == armor piercing rounds; 2 == Spiral armor piercing rounds;
		int[] temp = new int[5];
		temp[0] = originX;
		temp[1] = originY;
		temp[2] = direction;
		temp[3] = type;
		temp[4] = strength;
		SpecialProjectiles.add(temp);
	}
	
	public void setSpecialProjectile(int type, int strength) {
		playerProjectileID = type;
		projectileTimer = projTimerDefault / strength;
		if(projectileTimer < 90 * 10) projectileTimer = 90 * 10;
		ppStrength = strength;
	}
	
	public void tickSpecialTimer() {
		if(projectileTimer > 0) projectileTimer -= 1;
		else if(projectileTimer == 0) {
			projectileTimer = -1;
			playerProjectileID = -1;
		}
	}
	
	public void updateHitSpecialRound(int id) {
		switch(SpecialProjectiles.get(id)[3]) {
		case 0://ScatterShot
			if(SpecialProjectiles.get(id)[4] > 0) {
				addSpecialProjectiles(SpecialProjectiles.get(id)[0], SpecialProjectiles.get(id)[1],
					0, SpecialProjectiles.get(id)[4] - 1, 0);
				addSpecialProjectiles(SpecialProjectiles.get(id)[0], SpecialProjectiles.get(id)[1],
						0, SpecialProjectiles.get(id)[4] - 1, 1);
				addSpecialProjectiles(SpecialProjectiles.get(id)[0], SpecialProjectiles.get(id)[1],
						0, SpecialProjectiles.get(id)[4] - 1, 2);
				addSpecialProjectiles(SpecialProjectiles.get(id)[0], SpecialProjectiles.get(id)[1],
						0, SpecialProjectiles.get(id)[4] - 1, 3);
				SpecialProjectiles.remove(id);
			}
			if(SpecialProjectiles.get(id)[4] <= 0) SpecialProjectiles.remove(id);
			else SpecialProjectiles.get(id)[4] -= 1;
			break;
		case 1://armor piercing
			if(SpecialProjectiles.get(id)[4] > 0) SpecialProjectiles.get(id)[4] -= 1;
			else SpecialProjectiles.remove(id);
			break;
		case 2://Spiraling armor piercing
			if(SpecialProjectiles.get(id)[4] > 0) {
				SpecialProjectiles.get(id)[4] -= 1;
				if(SpecialProjectiles.get(id)[2] > 3) SpecialProjectiles.get(id)[2] = 0;
				else SpecialProjectiles.get(id)[2] += 1;
			} else SpecialProjectiles.remove(id);
			break;
		default:
			System.out.println("Default case called at projectiles.updateHitSpecialRound");
			SpecialProjectiles.remove(id);
			break;
		}
	}
	
	public void addProjectileToPlayer(int x, int y) {
		x += 20;
		if(playerProjectileID == -1) {
			int[] temp = new int[2];
			temp[0] = x;
			temp[1] = y;
			//System.out.println("ADDING PLAYER PROJECTILE AT: [" + x + ", " + y + "]");
			//System.out.println("CURRENT TICK COUNT: " + SpaceInvadersMain.tickCounter);
			PlayerProjectiles.add(temp);
		} else addSpecialProjectiles(x, y, playerProjectileID, ppStrength, 0);
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
				if(InvaderProjectiles.get(i)[1] > SpaceInvadersMain.winSize[1]) InvaderProjectiles.remove(i);
				else InvaderProjectiles.get(i)[1] += speed;
			}
		}
		if(SpecialProjectiles.size() != 0) {
			for(int i = 0; i < SpecialProjectiles.size(); i++) {
				if(SpecialProjectiles.get(i)[1] < -50) SpecialProjectiles.remove(i);
				else if(SpecialProjectiles.get(i)[1] > SpaceInvadersMain.winSize[1] + 50) SpecialProjectiles.remove(i);
				else if(SpecialProjectiles.get(i)[0] < -50) SpecialProjectiles.remove(i);
				else if(SpecialProjectiles.get(i)[0] > SpaceInvadersMain.winSize[0] + 50) SpecialProjectiles.remove(i);
				else switch(SpecialProjectiles.get(i)[2]) {
				case 0:
					SpecialProjectiles.get(i)[1] -= speed;
					break;
				case 1:
					SpecialProjectiles.get(i)[0] += speed;
					break;
				case 2:
					SpecialProjectiles.get(i)[1] += speed;
					break;
				case 3:
					SpecialProjectiles.get(i)[0] -= speed;
					break;
				default:
					SpecialProjectiles.get(i)[2] = 0;
					break;
				}
			}
		}
	}

	@Override
	public void updateDraw() {
		
	}
	
	public void increaseProjectileSpeed(int s) {
		speed += s;
	}

	@Override
	public void draw(Graphics g) {
		for(int i = 0; i < PlayerProjectiles.size(); i++) g.fillRect(PlayerProjectiles.get(i)[0], PlayerProjectiles.get(i)[1], 4, 16);
		for(int i = 0; i < InvaderProjectiles.size(); i++) g.fillRect(InvaderProjectiles.get(i)[0], InvaderProjectiles.get(i)[1] - 16, 2, 16);
		for(int i = 0; i < SpecialProjectiles.size(); i++) switch(SpecialProjectiles.get(i)[3]) {
		case 0:
			g.fillRect(SpecialProjectiles.get(i)[0] + 2, SpecialProjectiles.get(i)[1], 2, 2);
			g.fillRect(SpecialProjectiles.get(i)[0], SpecialProjectiles.get(i)[1] + 2, 2, 2);
			g.fillRect(SpecialProjectiles.get(i)[0] + 4, SpecialProjectiles.get(i)[1] + 2, 2, 2);
			g.fillRect(SpecialProjectiles.get(i)[0] + 2, SpecialProjectiles.get(i)[1] + 4, 2, 2);
			break;
		case 1:
			g.fillRect(SpecialProjectiles.get(i)[0] + 1, SpecialProjectiles.get(i)[1], 2, 2);
			g.fillRect(SpecialProjectiles.get(i)[0], SpecialProjectiles.get(i)[1] + 2, 4, 10);
			break;
		case 2:
			g.fillRect(SpecialProjectiles.get(i)[0] + 3, SpecialProjectiles.get(i)[1], 3, 9);
			g.fillRect(SpecialProjectiles.get(i)[0], SpecialProjectiles.get(i)[1] + 3, 9, 3);
			break;
		default:
			break;
		}
	}

	public void tickProjectiles() {
		updateProjectilePos();
		tickSpecialTimer();
	}
	
}
