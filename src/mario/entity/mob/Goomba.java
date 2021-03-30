package mario.entity.mob;

import java.awt.Graphics;
import java.util.Random;

import mario.entity.Entity;
import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;
import mario.tile.Tile;

public class Goomba extends Entity {

	private int frame = 0;
	private int frameDelay = 0;
	
	private Random random = new Random();
	
	public Goomba(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
		
		int dir = random.nextInt(2); 
		
		//randomly sets the direction for goomba
		switch(dir) {
		 case 0:
			 setVelX(-2); //left
			 facing = 0;
			 break;
		 case 1:
			 setVelX(2); //right
			 facing = 1;
			 break;
		}
	}

	public void render(Graphics g) {
		if (facing == 0) {
			g.drawImage(Game.goomba[frame + 5].getBufferedImage(), x, y, width, height, null); //going left animation
		}
		else 
			if (facing == 1) {
				g.drawImage(Game.goomba[frame].getBufferedImage(), x, y, width, height, null); //going right animation
			}
		
	}

	public void tick() {
		x += velX;
		y += velY;
		
		for (int i=0;i<handler.tile.size();i++) {
			Tile t = handler.tile.get(i);
			if(t.isSolid()) {
				if(getBoundsBottom().intersects(t.getBounds())) {
					setVelY(0);
					if(falling) falling = false;
				}
				else if(!falling) { //if we are up in the air we will fall
					falling = true;
					gravity = 0.8;
				}
				
				if (getBoundsLeft().intersects(t.getBounds())) {
					//if bouncing into the left obstacle go right 
					setVelX(2);
					facing = 1;
				}
				if (getBoundsRight().intersects(t.getBounds())) {
					//if bouncing into the right obstacle go left 
					setVelX(-2);
					facing = 0;
				}
			}//end of isSolid()
		}//end for
		
		if (falling) {
			gravity += 0.17;
			setVelY((int)gravity); //gravity pulling us down
		}
		
		if (velX!=0) { //if moving then animate
			frameDelay++;
			if (frameDelay>=10) {
				frame++;
				if (frame > 3) {
					frame = 0;
				}
				frameDelay = 0;
			}		
		}
	} //end tick()

	@Override
	public Goomba clone() throws CloneNotSupportedException {
	    return (Goomba) super.clone();
	}
	
}
