package components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import main.SpaceInvadersMain;

public class Drop implements interfaces.Drawable {

	public ArrayList<int[]> drops = new ArrayList<int[]>();//[X, Y, Type, Strength] type is the same as the projectiles it represents, Strength == strength of special projectile
	private int speed = 2;
	
	
	public void addDrop(int x, int y, int type, int strength) {
		if(strength <= 0) strength = 1;
		System.out.println("Adding Drop At: [" + x + ", " + y + "] Of Type: " + type + ", And Strength: " + strength);
		int[] temp = new int[4];
		temp[0] = x;
		temp[1] = y;
		temp[2] = type;
		temp[3] = strength;
		drops.add(temp);
	}
	
	public void generateDrop(int x, int y, Random rand) {
		System.out.println("Generating Drop");
		addDrop(x, y, rand.nextInt(SpaceInvadersMain.projectiles.NUMER_OF_SPECIALS), rand.nextInt(3) + 1);
	}
	
	public void dropCollected(int id) {
		if(id < 0 || id >= drops.size()) return;
		SpaceInvadersMain.projectiles.setSpecialProjectile(drops.get(id)[2], drops.get(id)[3]);
		drops.remove(id);
	}
	
	@Override
	public void updateDraw() {
		
	}

	public void updateDropPos() {
		for(int i = 0; i < drops.size(); i++) {
			drops.get(i)[1] += speed;
			if(drops.get(i)[1] > SpaceInvadersMain.winSize[1] + 17);
		}
	}
	
	@Override
	public void draw(Graphics g) {
		for(int[] d : drops) switch(d[2]) {
		case 0:
			g.setColor(Color.orange);
			g.fillRect(d[0], d[1], 10, 16);
			g.setColor(Color.green);
			g.drawString("S", d[0] + 1, d[1] + 13);
			break;
		case 1:
			g.setColor(Color.yellow);
			g.fillRect(d[0], d[1], 10, 16);
			g.setColor(Color.black);
			g.drawString("F", d[0] + 1, d[1] + 13);
			break;
		case 2:
			g.setColor(Color.red);
			g.fillRect(d[0], d[1], 10, 16);
			g.setColor(Color.blue);
			g.drawString("R", d[0] + 1, d[1] + 13);
			break;
		default:
			System.out.println("Hit Default in components.Drop.draw");
			break;
		}
		g.setColor(Color.black);
	}

}
