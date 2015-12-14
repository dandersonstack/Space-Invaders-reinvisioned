package main;

import components.Bunkers;
import components.Invader;
import components.Player;
import components.Projectile;
import data.Score;
import interfaces.Living;
import loaders.InvaderModelLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvadersMain {
	
	public static GameFrame window;
	public static int[] winSize = new int[2];
	public static ArrayList<Living> ents;
	public static Projectile projectiles;
	private static int ticksSinceLastShot = 0;
	public static int tickCounter;
	private static boolean movingRight = true;
	private static final int INVADERSPEED = 10;
	private static int lastMinY = 0;
	public static Random rand;
	public static InvaderModelLoader invaderModels;//hold the models for all the invaders to conserver memory
	public static boolean running = true;
	public static int lvl = 1;
	public static Score score;
	public static Bunkers bunkers;

	public static void main(String[] args) {
		
		winSize[0] = 1080;//Width
		winSize[1] = 720;//Height of window
		
		try {
			invaderModels = new InvaderModelLoader();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bunkers = new Bunkers();
		
		rand = new Random();
		
		projectiles = new Projectile();
		
		ents = new ArrayList<Living>();
		window = new GameFrame("Space Invaders!");
		window.mc.ents.add(new Player(10, 650));
		createInvaders();
		bunkers.addBunkersForLvl(lvl);
		
		window.pack();
		window.setSize(winSize[0], winSize[1]);
		
		score = new Score();
		
		run();
	}
	
	public static void run() {
		double delta = 0;//Handles ticking of game <Variable decloration starts here>
		long oldTime = System.nanoTime();//nanoTimeis the most consistent clock on the computer, though it may not be accurate to real time.
		long newTime = 0;
		double NanoSecondsPerTick = 1000000000L / 90D;// Ticks per second //<Ends Here>
		tickCounter = 0;//initialize this variable
		byte invaderTicker = 0;//stores amount of ticks passed since last move
		int invaderShotTicker = 0;//stores ticks since last invader shot
		int nextShot = 10;//stores amount of ticks till the next invader shoots.
		int ticklimiter = 0;
		updateInvadersPos();//moves invaders for first time and initializes some variables
		while(true) {
			while(running) {//implemented for the pause function and for ending the game
				newTime = System.nanoTime();
				delta += (newTime - oldTime) / NanoSecondsPerTick;
				while(delta >= 1) {
					delta -= 1;//deals with the ticking
					if(ticklimiter > 6) {
						ticklimiter = 0;
						tickCounter++;//update all the counters (I probably could have done this better... but oh well, I am not redoing it)
						ticksSinceLastShot++;
						invaderTicker++;
						invaderShotTicker++;
						if(invaderTicker > 4) {//if it has been 4 ticks, or approximately 1/4 (4/15) of a second, move the invaders
							updateInvadersPos();
							invaderTicker = 0;//resets the tick counter
						}
						if(invaderShotTicker >= nextShot) {//handles making the invaders shoot
							nextShot = (int) Math.abs(rand.nextGaussian() * 100) / ((lvl > 2) ? lvl / 3 : 1);//randomly generate a double between -1 and 1, make it positive, increase it's size, decrease it as the levels increase. <ticks till next shot>
							invaderShotTicker = 0;//reset the tick counter
							int nextInvaderToFire = rand.nextInt(window.mc.ents.size() - ((window.mc.ents.size() > 2) ? 2 : 1)) + 1;//randomly select an invader(the odd dealings inside of the function are to insure the player doesnt shoot it's self.
							projectiles.addProjectileToInvaders(nextInvaderToFire);//make the chosen one shoot a projectile.
						}
						
					} ticklimiter++;
					playerMvr();//checks user input and acts accordingly
					if(lvl > 9) for(int b = 0; b < bunkers.bunkers.size(); b++) bunkers.bunkers.get(b).updateDraw();
					projectiles.updateProjectilePos();//makes the invader projectiles head down, and the player ones head up.
					invaderCollision();//see if the player projectiles hit any of the invaders
					playerCollision();//see if any of the invader projectiles hit the player
					bunkerCollision();//see if any projectiles hit the bunkers
					window.repaint();//update the window to any changes.
				}
				oldTime = newTime;//dont know why I didnt place this earlier... but this is part of the ticking process
			}
			newTime = System.nanoTime();//more ticking stuff, this makes it so the game can be unpaused.
			delta += (newTime - oldTime) / (NanoSecondsPerTick);//tick 2 times as fast
			while(delta >= 1) {
				delta -= 1;
				playerMvr();//check to see if player has unpaused the game yet
				window.repaint();
			}
			oldTime = newTime;
		}
	}
	
	private static void playerMvr() {
		if(running && !window.keyToggles[0] && (window.mc.ents.get(0).getX() > 0)) window.mc.ents.get(0).moveX(-1);//if left key is pressed, move left
		if(running && !window.keyToggles[1] && (window.mc.ents.get(0).getX() < winSize[0] - 45)) window.mc.ents.get(0).moveX(1);//if right key is pressed, move right
		if(running && !window.keyToggles[2] && (ticksSinceLastShot > 12)) {//if space bar is pressed, shoot projectile
			projectiles.addProjectileToPlayer(window.mc.ents.get(0).getX(), window.mc.ents.get(0).getY());
			ticksSinceLastShot = 0;//limits the speed in which the player can shoot
		}
		if(!window.keyToggles[3]) running = !running;//pause/unpause the game when escape is pressed
		if(!running && !window.keyToggles[4] && !window.keyToggles[5]) restartGame();//Restart the game (Ctrl + Enter)
	}
	
	private static void restartGame() {
		score.setScore(0L);
		lvl = 1;
		window.mc.ents = new ArrayList<Living>();
		window.mc.ents.add(new Player(10, 650));
		createInvaders();
		bunkers.addBunkersForLvl(lvl);
		projectiles = new Projectile();
		running = true;
	}
	
	private static void createInvaders() {//TODO fix model declaration to be correctly distributed
		if(lvl > 9) lvl = 9;//limit the number of invaders that can spawn
		int width = (int) (lvl * 1.5 + 3);//how many invaders wide
		int height = (((int) (lvl / 3 + 1)) * 3);//how many invaders tall
		for(int i = 0; i < height; i++) {//loop through the rows
			byte mId;//model/invader Id
			if(i <= (int) (height / 3.0) - 1) mId = 0;// 3/3 == 1//if in the lower 3rd make type 0 (the ones that look a little like a skull)
			else if(i <= (int) ((height / 3.0) * 2) - 1) mId = 1;// 3/3*2 == 2//if in middle 3rd make type 1 (the most "alien like" ones)
			else mId = 2;//if in the top 3rd, make type 2 (the jelly fish like ones)
			for(int j = 0; j < width; j++) {
				window.mc.ents.add(new Invader(10 + (invaderModels.modelDims[5][0] + 10) * j,//define the X coordinate
						10 + (invaderModels.modelDims[5][1] + 10) * i, //define the Y coordinate
						mId));//define the type of invader
			}
		}
		//sortInvaderY();//this really isnt needed, but if I do some funky things in the future then I might need it
	}
	
	public static void sortInvaderY() {
		for(int i = 2; i < window.mc.ents.size(); i++) {
			if(window.mc.ents.get(1).getY() < window.mc.ents.get(i).getY()) {//index 0 is the player, index 1 is the invader with the largest Y value
				window.mc.ents.add(1, window.mc.ents.get(i));//if a invader is found with a larger Y value, it is moved to position 1
				window.mc.ents.remove(i + 1);//then the old invader is removed. //if the + 1 is removed, a very odd bug pops up
			}
		}
	}
	
	private static void updateInvadersPos() {
		int maxX = 0;//max as in closest to the right of the screen
		int minX = 0x0FFFFFFF;//min as in closest to the left of the screen (it was causing problems to have it as 0, so I set it to 268435455 in decimal)
		int minY = 0;//min as in closest to the bottom of the screen (and therefore the largest Y value)
		for(int i = 1; i < window.mc.ents.size(); i++) {
			if((window.mc.ents.get(i).getX() > maxX) && (window.mc.ents.get(i).isLiving())) maxX = window.mc.ents.get(i).getX();
			if((window.mc.ents.get(i).getX() < minX) && (window.mc.ents.get(i).isLiving())) minX = window.mc.ents.get(i).getX();
			if((window.mc.ents.get(i).getY() > minY) && (window.mc.ents.get(i).isLiving())) minY = window.mc.ents.get(i).getY();
		}
		
		if((maxX < winSize[0] - (20 + invaderModels.modelDims[5][0])) && movingRight) {//if the invaders are moving right and there is room to move, then move.
			for(int i = 1; i < window.mc.ents.size(); i++) {
				window.mc.ents.get(i).moveX(INVADERSPEED);//move the invader at index i right
			}
			window.mc.ents.get(1).updateDraw();//change the model to draw
		} else if((minX > 10) && !movingRight) {//if moving left and space is left, continue moving left
			for(int i = 1; i < window.mc.ents.size(); i++) {
				window.mc.ents.get(i).moveX(-1 * INVADERSPEED);//move the invader at index i left
			}
			window.mc.ents.get(1).updateDraw();
		} else movingRight = !movingRight;//if there is no more room to move then go the other way
		lastMinY = minY;//helps speed up the invader collision function
	}
	
	private static void invaderCollision() {
		if(projectiles.PlayerProjectiles.size() != 0) {//Skips the block of code if there are no projectiles
			boolean IndexFlag = false;//If a projectile is removed then this flag is turned to true so the game doesnt crash
			for(int i = 0; i < projectiles.PlayerProjectiles.size(); i++) {//Loop through all projectiles
				if(projectiles.PlayerProjectiles.get(i)[1] < (lastMinY + 72)) for(int j = 1; j < window.mc.ents.size(); j++) {//Reduce operations needed to search as if a projectiles is lower than the index it is useless to search
					if(window.mc.ents.get(j).isLiving())//Check if the current invader is alive
						if((projectiles.PlayerProjectiles.get(i)[0] > (window.mc.ents.get(j).getX() + offsetOfInvaderModel(window.mc.ents.get(j).getIType()))) && //The X-Coordinate of the upper left corner + the offset of the model
							(projectiles.PlayerProjectiles.get(i)[0] < ((window.mc.ents.get(j).getX() + invaderModels.modelDims[window.mc.ents.get(j).getIType() * 2][0]) + offsetOfInvaderModel(window.mc.ents.get(j).getIType())))) {//That ^ + The width of the model + The offset of the model
								if((projectiles.PlayerProjectiles.get(i)[1] < (window.mc.ents.get(j).getY() + invaderModels.modelDims[window.mc.ents.get(j).getIType()][1])) && (projectiles.PlayerProjectiles.get(i)[1] > window.mc.ents.get(j).getY())) {//Checks to see if the projectile is within the bounds of the Y coordinate
									window.mc.ents.get(j).kill();//if it is to die, then kill it and take care of what needs to be done
									window.mc.ents.remove(j);//remove it from the list to release some of the load on the game
									projectiles.PlayerProjectiles.remove(i);//the projectiles are not invincible and therefore need to stop and go away
									isTimeForNextLevel();//if all the invaders are dead, then move on to the next level
									invaderCollision();//refresh the function, if this is not called, then the game will crash
									IndexFlag = true;//stops the current instance of the function
									break;//stop the loop
								}
					}
				}
				if(IndexFlag) break;//break the loop to stop the function to avoid a crash
			}
		}
	}
	
	private static void playerCollision() {
		for(int i = 0; i < projectiles.InvaderProjectiles.size(); i++) {
			if(projectiles.InvaderProjectiles.get(i)[1] >= window.mc.ents.get(0).getY() && projectiles.InvaderProjectiles.get(i)[1] <= window.mc.ents.get(0).getY() + 30) //if the projectile is in the same row as the player, check the X
				if(projectiles.InvaderProjectiles.get(i)[0] >= window.mc.ents.get(0).getX() && projectiles.InvaderProjectiles.get(i)[0] <= window.mc.ents.get(0).getX() + 45) {//if the projectile is within the same collumn, then it was a hit
					window.mc.ents.get(0).kill();//kill the player
					projectiles.InvaderProjectiles.remove(i);//remove the projectile that killed the player, even though this is not neccessary
				}
		}
	}
	//TODO figure out the bug with the basicBlockDestructible hit method (left most bunker is invincible, and the right 2 have entity specific rows)
	private static void bunkerCollision() {//every bunker is a little different, so this method is split into 2, this is the first part
		if(bunkers.bunkers.size() <= 0) return;//if there are no bunkers, then stop looking at this function!
		if(projectiles.InvaderProjectiles.size() > 0)//if there are no invader projectiles, then move on
			for(int i = 0; i < projectiles.InvaderProjectiles.size(); i++)
				for(int j = bunkers.bunkers.size() - 1; j >= 0; j--) {//start from the right and move on to the left
					if(bunkers.bunkers.get(j).getY() < projectiles.InvaderProjectiles.get(i)[1])//if the projectile is below the upper left hand corner, check the X
						if(projectiles.InvaderProjectiles.get(i)[0] > bunkers.bunkers.get(j).getX())//if the projectile is to the right of the upper left hand corner then move on to the next part of the method
							if(bunkers.bunkers.get(j).hit(i, false)) break;//pass the projectile onto the bunker specific method //if the projectile hit the bunker then move on to the next one
				}
		if(projectiles.PlayerProjectiles.size() > 0)//if there are no player projectiles, then move on
			for(int i = 0; i < projectiles.PlayerProjectiles.size(); i++)
				for(int j = bunkers.bunkers.size() - 1; j >= 0; j--) {
					if(bunkers.bunkers.get(j).getY() < projectiles.PlayerProjectiles.get(i)[1])
						if(projectiles.PlayerProjectiles.get(i)[0] > bunkers.bunkers.get(j).getX()) 
							if(bunkers.bunkers.get(j).hit(i, true)) break;//the only differences from above are, 1) the projectile is coming from below, 2) it is from the player
				}
	}
	
	public static int offsetOfInvaderModel(int type) {//models are different in width, and therefore need to be offset
		switch(type) {//this is just a way to get the needed data
		case 0:
			return 8;
		case 1:
			return 2;
		default:
			return 0;
		}
	}
	
	private static void isTimeForNextLevel() {//updates everything for the next level
		if(window.mc.ents.size() == 1) {//if only the player is alive, then proceed
			lvl++;//increment the level
			System.out.println("Current level: " + lvl);//log to the console
			bunkers.addBunkersForLvl(lvl);//set up the bunker for this level
			createInvaders();//set up the new invaders
		}
	}
	
}
