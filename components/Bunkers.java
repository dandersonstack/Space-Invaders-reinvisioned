package components;

import interfaces.Bunker;

import java.awt.Graphics;
import java.util.ArrayList;

import main.SpaceInvadersMain;
import components.bunkers.*;

public class Bunkers implements interfaces.Drawable {
	
	private int[] bunkerBlockDims = new int[2];
	private int[] bunkerDims = new int[2];//declared earlier on to avoid problems in addBunkersForLvl
	private int oneFithWinX;
	private int oneTwentythWinY;
	public ArrayList<Bunker> bunkers;
	private int[] winSize;

	public Bunkers() {
		winSize = new int[2];
		winSize[0] = SpaceInvadersMain.winSize[0];
		winSize[1] = SpaceInvadersMain.winSize[1];
		bunkers = new ArrayList<Bunker>();
		oneFithWinX = winSize[0] / 5;
		oneTwentythWinY = winSize[1] / 20;
	}
	
	public void addBunkersForLvl(int lvl) {
		if(lvl == 10) winSize[0] = winSize[0] + oneFithWinX;//adds spread to bunkers after lvl 9
		bunkers = new ArrayList<Bunker>();
		if(lvl % 4 == 0) {
			bunkerBlockDims[0] = ((lvl > 25) ? 25 : lvl);//make the blocks bigger as time goes on, this has the effect of making the bunker weaker
			bunkerBlockDims[1] = bunkerBlockDims[0];//saves vertical space to have the blocks half as tall as they are wide//set the dimensions for the individual blocks
			bunkerDims[0] = (oneFithWinX / bunkerBlockDims[0] / 2);//set the amount of blocks wide
			bunkerDims[1] = (int) (oneTwentythWinY / ((bunkerBlockDims[1] > oneTwentythWinY) ? oneTwentythWinY : bunkerBlockDims[1]));//set amount of blocks tall
			addBunker((winSize[0] / 4) - (oneFithWinX / 3) * 2, 500, 2);
			bunkerBlockDims[0] = lvl;//make the blocks bigger as time goes on, this has the effect of making the bunker weaker
			bunkerBlockDims[1] = bunkerBlockDims[0];// / 2;//saves vertical space to have the blocks half as tall as they are wide//set the dimensions for the individual blocks
			bunkerDims[0] = oneFithWinX / (bunkerBlockDims[0]);//set the amount of blocks wide
			bunkerDims[1] = (int) (oneTwentythWinY / ((bunkerBlockDims[1] > oneTwentythWinY) ? oneTwentythWinY : bunkerBlockDims[1]));//set amount of blocks tall
			addBunker((winSize[0] / 4) * 3 - (oneFithWinX / 3), 500, 1);
			bunkerDims[0] = oneFithWinX / ((lvl > 2) ? ((lvl < 75) ? lvl / 3 : 25) : 1);//as time goes on make the barriers smaller, but has a limit to how small it can get
			if(bunkerDims[0] < oneTwentythWinY) bunkerDims[0] = oneTwentythWinY;//Sets a limit so the bunker doesn't totally disappear
			bunkerDims[1] = oneTwentythWinY;//set the standard size to 1/20th of the window thick
			addBunker((winSize[0] / 4) * 2 - (oneFithWinX / 2), 500, 0);
		} else if(lvl % 3 == 0) {//every 3 levels, only use the basic block distructible bunkers
			bunkerBlockDims[0] = ((lvl > 20) ? 20 : lvl);//make the blocks bigger as time goes on, this has the effect of making the bunker weaker
			bunkerBlockDims[1] = bunkerBlockDims[0];// / 2;//saves vertical space to have the blocks half as tall as they are wide//set the dimensions for the individual blocks
			bunkerDims[0] = oneFithWinX / (bunkerBlockDims[0]);//set the amount of blocks wide
			bunkerDims[1] = (int) (oneTwentythWinY / ((bunkerBlockDims[1] > oneTwentythWinY) ? oneTwentythWinY : bunkerBlockDims[1]));//set amount of blocks tall
			//System.out.println(winSize[0]);
			addBunker((winSize[0] / 4) - (oneFithWinX / 3) * 2, 500, 1);
			addBunker((winSize[0] / 4) * 2 - (oneFithWinX / 2), 500, 1);
			addBunker((winSize[0] / 4) * 3 - (oneFithWinX / 3), 500, 1);
		} else if(lvl % 2 == 0) {
			bunkerBlockDims[0] = ((lvl > 20) ? 20 : lvl);//make the blocks bigger as time goes on, this has the effect of making the bunker weaker
			bunkerBlockDims[1] = bunkerBlockDims[0];// / 2;//saves vertical space to have the blocks half as tall as they are wide//set the dimensions for the individual blocks
			bunkerDims[0] = (oneFithWinX / (bunkerBlockDims[0]) / 2);//set the amount of blocks wide
			bunkerDims[1] = (int) (oneTwentythWinY / ((bunkerBlockDims[1] > oneTwentythWinY) ? oneTwentythWinY : bunkerBlockDims[1]));//set amount of blocks tall
			addBunker((winSize[0] / 4) - (oneFithWinX / 3) * 2, 500, 2);
			addBunker((winSize[0] / 4) * 2 - (oneFithWinX / 2), 500, 2);
			addBunker((winSize[0] / 4) * 3 - (oneFithWinX / 3), 500, 2);
		} else {//when the above 2 dont fill out use indestructible barriers
			bunkerDims[0] = oneFithWinX / ((lvl > 2) ? (lvl / 3) : 1);//as time goes on make the barriers smaller
			if(bunkerDims[0] < oneTwentythWinY) bunkerDims[0] = oneTwentythWinY;//Set limit so this doesn't totally disappear
			bunkerDims[1] = oneTwentythWinY;//set the standard size to 50 pixels thick
			addBunker((winSize[0] / 4) - (oneFithWinX / 2), 500, 0);
			addBunker((winSize[0] / 4) * 2 - (oneFithWinX / 2), 500, 0);
			addBunker((winSize[0] / 4) * 3 - (oneFithWinX / 2), 500, 0);
		}
	}
	
	public void addBunker(int x, int y, int type) {//I am using instance variables in this class because each type has different requirements
		switch(type) {
		case 0://type 0 == Basic invincible bunker
			bunkers.add(new BasicIndistructible(x, y, bunkerDims[0], bunkerDims[1]));//this needs width and height
			break;
		case 1://type 1 == basic block destructible bunker
			bunkers.add(new BasicBlockDistructible(x, y, bunkerDims, bunkerBlockDims));//this needs width, height and the dimensions for each block
			break;
		case 2:
			bunkers.add(new BrickDestructible(x, y, bunkerBlockDims, bunkerDims));
		default://if no cases fill out, do nothing
			break;
		}
	}
	
	@Override
	public void updateDraw() {
		for(int i = 0; i < bunkers.size(); i++) bunkers.get(i).updateDraw();
		//System.out.println("Updating Bunker Draw");
	}

	@Override
	public void draw(Graphics g) {
		for(int i = 0; i < bunkers.size(); i++) bunkers.get(i).draw(g);
	}

	

}
