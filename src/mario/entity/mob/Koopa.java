package mario.entity.mob;

import java.awt.Graphics;
import java.util.Random;

import mario.entity.Entity;
import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;
import mario.main.states.KoopaState;
import mario.tile.Tile;
import mario.tile.Wall;

public class Koopa extends Entity {
	private Random random;
	private int shellCount;
	private int frame = 0;
	private int frameDelay = 0;

	public Koopa(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
		random = new Random();
		int dir = random.nextInt(2); 
		//randomly sets the direction for koopa
			switch(dir) {
			 case 0:
				 setVelX(-1);//left
				 facing = 0;
				 break;
			 case 1:
				 setVelX(1);//right
				 facing = 1;
				 break;
			}
			
		koopaState = KoopaState.walking;	
	}

	public void render(Graphics g) {
		
		if (koopaState ==  KoopaState.walking) { //if koopa is walking then animate movement
			if (facing == 0) {
				g.drawImage(Game.koopa[frame + 5].getBufferedImage(), x, y, width, height, null);
			}
			else 
				if (facing == 1) {
					g.drawImage(Game.koopa[frame].getBufferedImage(), x, y, width, height, null);
				}
		}
		else //if koopa is in shell then load shell image
		{
			g.drawImage(Game.koopaShell.getBufferedImage(), x, y, width, height, null);
		}
		
	
	}

	public void tick() {
		x += velX;
		y += velY;
		
		if (koopaState == KoopaState.shell) {
			setVelX(0); //doesn't move 
			shellCount++;
			
			if (shellCount>=300) { //after about 5s go into walking state 
				shellCount = 0;
				koopaState = KoopaState.walking;
			}
		}
		
		if (koopaState==KoopaState.walking || koopaState==KoopaState.spinning) {
			shellCount = 0;
			
			if (velX == 0) { //if not moving choose random direction
				int dir = random.nextInt(2); 
				
				switch(dir) {
				 case 0:
					 setVelX(-1);
					 facing = 0;
					 break;
				 case 1:
					 setVelX(1);
					 facing = 1;
					 break;
				}
			}
		}
		
		for (int i=0;i<handler.tile.size();i++) {
			Tile t = handler.tile.get(i);
			if(t.isSolid()) {
				if(getBoundsBottom().intersects(t.getBounds())) {
					setVelY(0);
					if(falling) falling = false;
				}
				else if(!falling) {//if we are up in the air we will fall
					falling = true;
					gravity = 0.8;
				}
				
				if (getBoundsLeft().intersects(t.getBounds())) {
					if (koopaState == KoopaState.spinning)setVelX(10); //if spinning go faster
					else setVelX(2);
					facing = 1;
				}
				if (getBoundsRight().intersects(t.getBounds())) {
					if (koopaState == KoopaState.spinning)setVelX(-10);//if spinning go faster
					else setVelX(-2);
					facing = 0;
				}
			}
		}
		
		if (falling) {
			gravity += 0.17;
			setVelY((int)gravity); //gravity pulling us down
		}
		
		if (velX!=0) {//if moving then animate
			frameDelay++;
			if (frameDelay>=10) {
				frame++;
				if (frame > 3) {
					frame = 0;
				}
				frameDelay = 0;
			}
		
		}
		
	}//end tick()

	@Override
	public Koopa clone() throws CloneNotSupportedException {
	    return (Koopa) super.clone();
	}
	
}
