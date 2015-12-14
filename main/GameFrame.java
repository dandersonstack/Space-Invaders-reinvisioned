package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;

import components.MainComponent;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements KeyListener {
	
	public boolean[] keyToggles = new boolean[6];
	public MainComponent mc;
	
	public GameFrame(String Title) {
		super(Title);
		for(int i = 0; i < keyToggles.length; i++) keyToggles[i] = true;
		startWindow();
	}
	
	private void startWindow() {
		setSize(1080, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			mc = new MainComponent(SpaceInvadersMain.ents);
			add(mc);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to load Modles!!!");
			mc = null;
		}
		addKeyListener(this);
		this.setResizable(false);
		setVisible(true);
		//System.out.println("GameFrame: Window Visible = true");
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@SuppressWarnings("static-access")
	@Override
	public void keyPressed(KeyEvent e) {
		int kc = e.getKeyCode();
		if(e.VK_LEFT == kc){
			if(keyToggles[0]) {
				keyToggles[0] = false;
			}
		} else if(e.VK_RIGHT == kc) {
			if(keyToggles[1]) {
				keyToggles[1] = false;
			}
		} else if(e.VK_SPACE == kc) {
			if(keyToggles[2]) {
				keyToggles[2] = false;
			}
		} else if(e.VK_ESCAPE == kc) {
			if(keyToggles[3]) {
				keyToggles[3] = false;
			}
		} else if(e.VK_ENTER == kc) {
			if(keyToggles[4]) {
				keyToggles[4] = false;
			}
		} else if(e.VK_CONTROL == kc) {
			if(keyToggles[5]) {
				keyToggles[5] = false;
			}
		} /*else
			System.out.println("KeyCode: " + kc);*/
	}

	@SuppressWarnings("static-access")
	@Override
	public void keyReleased(KeyEvent e) {
		int kc = e.getKeyCode();
		if(e.VK_LEFT == kc){
			if(!keyToggles[0]) {
				keyToggles[0] = true;
			}
		} else if(e.VK_RIGHT == kc) {
			if(!keyToggles[1]) {
				keyToggles[1] = true;
			}
		} else if(e.VK_SPACE == kc) {
			if(!keyToggles[2]) {
				keyToggles[2] = true;
			}
		} else if(e.VK_ESCAPE == kc) {
			if(!keyToggles[3]) {
				keyToggles[3] = true;
			}
		} else if(e.VK_ENTER == kc) {
			if(!keyToggles[4]) {
				keyToggles[4] = true;
			}
		} else if(e.VK_CONTROL == kc) {
			if(!keyToggles[5]) {
				keyToggles[5] = true;
			}
		} else
			System.out.println("KeyCode: " + e.getKeyCode());
	}

}
