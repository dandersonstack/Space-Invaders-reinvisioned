package components;

import interfaces.Living;

import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;

import main.SpaceInvadersMain;

@SuppressWarnings("serial")
public class MainComponent extends JComponent {
	
	public ArrayList<Living> ents;//[X, Y, EntityType, aliveState(1 = Alive, 0 = Dead)]
	
	public MainComponent(ArrayList<Living> es) throws IOException {
		ents = es;
	}

	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(ents.size() > 0 && ents.get(0).isLiving()) {
			g.drawString("Level: " + SpaceInvadersMain.lvl, 5, 13);
			for(int i = 0; i < ents.size(); i++) ents.get(i).draw(g);
			if(SpaceInvadersMain.bunkers.bunkers.size() > 0)
				for(int i = 0; i < SpaceInvadersMain.bunkers.bunkers.size(); i++) SpaceInvadersMain.bunkers.bunkers.get(i).draw(g);
			SpaceInvadersMain.projectiles.draw(g);
		} else {
			g.drawString("GAME OVER", SpaceInvadersMain.winSize[0] / 2 - 45, SpaceInvadersMain.winSize[1] / 2);
			g.drawString("Score: " + SpaceInvadersMain.score.getScore(), SpaceInvadersMain.winSize[0] / 2 - 45, SpaceInvadersMain.winSize[1] / 2 + 20);
			g.drawString("Press Ctrl + Enter to restart game", SpaceInvadersMain.winSize[0] / 2 - 45, SpaceInvadersMain.winSize[1] / 2 + 40);
		}
	}

	
	
}
