package mario.entity.powerup;

import java.awt.Graphics;
import java.util.Random;

import mario.entity.Entity;
import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;
import mario.tile.Tile;

public class Mushroom extends Entity {
	
	private Random random = new Random();
	

	public Mushroom(int x, int y, int width, int height, boolean solid, Id id, Handler handler, int type) {
		super(x, y, width, height, solid, id, handler);
		this.type = type;
		
		//randomly choosing direction
		 int dir = random.nextInt(2); 
			
			switch(dir) {
			 case 0:
				 setVelX(-1);
				 break;
			 case 1:
				 setVelX(1);
				 break;
			}
	}
	
   

	public void render(Graphics g) {
		
		switch(getType()) {
		case 0: //mushroom that makes you bigger
			g.drawImage(Game.mushroom.getBufferedImage(), x, y, width, height, null);
			break;	
		case 1://mushroom that gives you +1 life
			g.drawImage(Game.lifeMushroom.getBufferedImage(), x, y, width, height, null);
			break;
		}			
	}

	public void tick() {
		x += velX;
		y += velY;
		
		//collision detection
		for (Tile t:handler.tile) {
			if(!t.solid) break;
			if (t.getId() == Id.wall) {				
				
				if (getBoundsBottom().intersects(t.getBounds())) { //intersecting the bottom obstacle
					setVelY(0);
					if (falling) falling = false;
				}
				else
					if(!falling) {
						gravity = 0.8; 
						falling = true;
					}
				
				if (getBoundsLeft().intersects(t.getBounds())) { //go to the other side
					setVelX(1);
				}
				
				if (getBoundsRight().intersects(t.getBounds())) {//go to the other side
					setVelX(-1);
				}
			}
		}
		
		if (falling) {
			gravity+=0.17;
			setVelY((int)gravity);//gravity pulling us down
		}
	}

}
