package mario.graphics.gui;

import java.awt.Color;
import java.awt.Graphics;

import mario.main.Game;

public class Launcher {

	public Button[] buttons;
	
	public Launcher() {
		buttons = new Button[2];
		
		buttons[0] = new Button(Game.getFrameWidth()/2-100,Game.getFrameHeight()/2 - 150,200,100,"Start Game"); //start button
		buttons[1] = new Button(Game.getFrameWidth()/2-100,Game.getFrameHeight()/2 -40,200,100,"Exit Game"); //exit button
	}
	
	public void render(Graphics g) {
		
		g.drawImage(Game.background, 0,0, Game.getFrameWidth()+15, Game.getFrameHeight()+15, null);
		
		for (int i = 0; i<buttons.length;i++) {
			buttons[i].render(g); //renders the buttons
		}
	}
}
