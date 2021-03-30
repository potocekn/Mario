package mario.entity.powerup;

import java.awt.Graphics;
import java.util.Random;

import mario.entity.Entity;
import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;
import mario.tile.Tile;
import mario.tile.Wall;

public class PowerStar extends Entity{
	
	private Random random; 

	public PowerStar(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
		
		random= new Random();
		
		//randomly choosing direction
		int dir = random.nextInt(2); 
		
		switch(dir) {
		 case 0:
			 setVelX(-4);
			 break;
		 case 1:
			 setVelX(4);
			 break;
		}
		
		falling = true;
		gravity = 0.17;
	}

	public void render(Graphics g) {
		g.drawImage(Game.star.getBufferedImage(), getX(), getY(), getWidth(), getHeight(),null);
	}

	public void tick() {
		
		x+=velX;
		y+=velY;
		
		for (int i = 0; i< handler.tile.size();i++) {
			Tile t = handler.tile.get(i);
			
			if (t.isSolid()) {
				if (getBoundsBottom().intersects(t.getBounds())) {//intersecting the bottom obstacle
					jumping = true;
					falling = false;
					gravity = 8.0;
				}
				if (getBoundsTop().intersects(t.getBounds())) {//intersecting the top obstacle
					jumping = false;
					falling = true;
					gravity = 8.0;
				}
				
				//go the other side
				if (getBoundsLeft().intersects(t.getBounds())) setVelX(4);
				if (getBoundsRight().intersects(t.getBounds())) setVelX(-4);
				
			}
		}
		
		if (jumping) {
			gravity-=0.15;
			setVelY((int)-gravity);
			if (gravity<=0.6) {
				jumping = false; 
				falling = true;
			}
		}
		
		if (falling) {
			gravity+=0.17;
			setVelY((int)gravity); //gravity pulling us down
		}
	}
	
	@Override
	public PowerStar clone() throws CloneNotSupportedException {
	    return (PowerStar) super.clone();
	}

}
