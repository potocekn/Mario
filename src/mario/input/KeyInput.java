package mario.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import mario.entity.Entity;
import mario.main.Game;
import mario.main.Id;
import mario.tile.Tile;

public class KeyInput implements KeyListener {

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		for (int i = 0;i<Game.handler.entity.size();i++) {
		  Entity en = Game.handler.entity.get(i);	
		  if(en.id == Id.player){
			  
			if (en.goingDownPipe) return;  
			switch(key) {
			case KeyEvent.VK_W: //if W is pressed
				for (int j = 0; j<Game.handler.tile.size();j++) {
					Tile t = Game.handler.tile.get(j);
					if (t.getId() == Id.pipe && t.getFacing() == 0) { //if current tile is upwards pipe 
						if (en.getBoundsTop().intersects(t.getBounds())) { //if players top intersects bottom of pipe we enable going trough the pipe
							if(!en.goingDownPipe) {
								en.goingDownPipe = true;
								break;
							}
						}
					}
				}//end of for
				
				if (!en.jumping) {
					en.jumping = true;
					en.gravity = 10.0;
				}
				break;
			case KeyEvent.VK_S: //if S is pressed
				for (int j = 0; j<Game.handler.tile.size();j++) {
					Tile t = Game.handler.tile.get(j);
					if (t.getId() == Id.pipe && t.getFacing() == 2) {//if current tile is downward pipe 
						if (en.getBoundsBottom().intersects(t.getBounds())) {//if players bottom intersects bottom of pipe we enable going trough the pipe
							if(!en.goingDownPipe) {
								en.goingDownPipe = true;
								break;
							}
						}
					}
				}
				break;
			case KeyEvent.VK_A: //if A is pressed
				
				boolean touchesLeft = false;
				
				for (int j = 0; j<Game.handler.tile.size();j++) {
					Tile t = Game.handler.tile.get(j);
					if (t.getId() == Id.wall) {//if current tile is pipe 
						if (en.getBoundsLeft().intersects(t.getBounds())) {//if players left intersects wall
							en.setVelX(0);
							touchesLeft = true;
							break;
						}
					}
				}
				if (!touchesLeft)en.setVelX(-5); //going left				
				en.facing = 0;
				break;
				
			case KeyEvent.VK_D: //id D is pressed
				
				boolean touchesRight = false;						
				
				for (int j = 0; j<Game.handler.tile.size();j++) {
					Tile t = Game.handler.tile.get(j);
					if (t.getId() == Id.wall) {//if current tile is pipe 
						if (en.getBoundsRight().intersects(t.getBounds())) {//if players bottom intersects bottom of pipe we enable going trough the pipe
							en.setVelX(0);
							touchesRight = true;
							break;
						}
					}
				}
				
				if (!touchesRight)en.setVelX(5); //going right
				en.facing = 1;
				break;				
			}
		  }	
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		for (Entity en:Game.handler.entity) {
			if(en.id == Id.player){
			switch(key) {
			case KeyEvent.VK_W: 
				en.setVelY(0);
				break;
			case KeyEvent.VK_S: 
				en.setVelY(0);
				break;
			case KeyEvent.VK_A: 
				en.setVelX(0);
				break;
			case KeyEvent.VK_D: 
				en.setVelX(0);
				break;
			}
		  }	
		}
	}

	public void keyTyped(KeyEvent e) {
		//not using
	}

}
