package mario.entity.mob;

import java.awt.Graphics;
import java.util.Random;

import mario.entity.Entity;
import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;
import mario.states.BossState;
import mario.tile.Tile;

public class TowerBoss extends Entity {
	
	public int jumpTime = 0;
	
	public boolean addJumpTime = false;
	
	private Random random;

	public TowerBoss(int x, int y, int width, int height, boolean solid, Id id, Handler handler, int hp) {
		super(x, y, width, height, solid, id, handler);
		this.hp = hp;
		
		bossState = BossState.idle;
		random = new Random();
	}

	public void render(Graphics g) {
		if(bossState == BossState.idle || bossState == BossState.spinning) { // if in idle or spinning
			g.drawImage(Game.towerBoss[0].getBufferedImage(), x, y, width, height, null);
		}
		else if(bossState == BossState.recovering) { //if recovering
			g.drawImage(Game.towerBoss[1].getBufferedImage(), x, y, width, height, null);
		}
		else {//if running or jumping
			g.drawImage(Game.towerBoss[2].getBufferedImage(), x, y, width, height, null);
		}		
	}//end of render()

	public void tick() {
		x+=velX;
		y+=velY;
		
		if (hp<=0) die();
		
		phaseTime++;
		
		if ((phaseTime>=180 && bossState == BossState.idle ) || (phaseTime>=600 && bossState!=BossState.spinning)) ChooseState();
		
		if (bossState == BossState.recovering && phaseTime>=180) { //after 3s go into spinning mode
			bossState = BossState.spinning;
			phaseTime = 0;
		}
		
		if (phaseTime>=360 && bossState==BossState.spinning) { //after about 6s go to idle
			phaseTime = 0;
			bossState = BossState.idle;
		}
		
		if ((bossState == BossState.idle) || (bossState == BossState.recovering)) { //not moving in idle or recovering
			setVelX(0);
			setVelY(0);
		}
		
		//if jumping or running player can attack the boss
		if ((bossState == BossState.jumping) || (bossState == BossState.running) ) attackable = true; 
		else attackable = false;
		
		if (bossState!=BossState.jumping) {
			addJumpTime = false;
			jumpTime = 0;
		}
		
		if (addJumpTime){
			jumpTime++;
			if (jumpTime>=30) {
				addJumpTime = false;
				jumpTime = 0;
			}
			
			if (!jumping && !falling) {
				jumping = true;
				gravity = 8.0;
			}
		}
		
		//collision detection
				for(int i=0;i<handler.tile.size();i++) {
					Tile t = handler.tile.get(i);
					
					if (t.isSolid() && !goingDownPipe) {
						if (getBoundsTop().intersects(t.getBounds())) { //if bouncing into the top obstacle
							setVelY(0);
							if (jumping) {
								jumping = false;
								gravity = 0.8; //gravity pulling us down
								falling = true;
							}												
						}
						
						if (getBoundsBottom().intersects(t.getBounds())) {//if bouncing into the bottom obstacle
							setVelY(0);
							if (falling) {
								falling = false;
								addJumpTime = true;
							}
						}						
						
						if (getBoundsLeft().intersects(t.getBounds()) ) { //if bouncing left turn right
							setVelX(0);
							if (bossState == BossState.running || bossState == BossState.spinning) setVelX(4);
							x = t.getX() + t.width;
						}
						
						if (getBoundsRightBoss().intersects(t.getBounds())) { //if bouncing right turn left
							setVelX(0);
							if (bossState == BossState.running || bossState == BossState.spinning) setVelX(-4);							
							x = t.getX()- t.width;
							
						}											
					}//end of ifSolid
				}//end of for
				
				for (int i = 0; i<handler.entity.size();i++) {
					Entity e = handler.entity.get(i);
					if (e.getId()==Id.player) { //if our entity is player
						if (bossState == BossState.jumping) {
							if (jumping || falling) {
								
								//following the player
								if ((getX() >= e.getX()-4) && (getX() <= e.getX()+4) )setVelX(0);
								else if (e.getX()<getX()) setVelX(-3);
								else if (e.getX()>getX()) setVelX(3);
							}
							else setVelX(0);
						}
						else if (bossState == BossState.spinning) {
							if (e.getX()<getX()) setVelX(-3);
							else if (e.getX()>getX()) setVelX(3);
						}
					}
				}//end of for
				
				if (jumping && !goingDownPipe) {
					gravity-=0.15;
					setVelY((int)-gravity);
					if (gravity<=0.6) {
						jumping = false; 
						falling = true;
					}
				}
				
				if (falling && !goingDownPipe) {
					gravity+=0.17;
					setVelY((int)gravity);
				}
		
	}//end tick

	public void ChooseState() { //randomly choosing state 
		int nextPhase = random.nextInt(2);
		
		if (nextPhase == 0) {
			bossState = BossState.running;
			int dir = random.nextInt(2);
			if (dir == 0) setVelX(-4);
			else setVelX(4);
		}
		else if(nextPhase == 1) {
			bossState = BossState.jumping;
			
			jumping = true;
			gravity = 8.0;
		}
		
		phaseTime = 0;
	}
	
	@Override
	public TowerBoss clone() throws CloneNotSupportedException {
	    return (TowerBoss) super.clone();
	}
}
