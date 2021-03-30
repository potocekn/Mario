package mario.entity.mob;

import java.awt.Graphics;
import java.util.Random;

import mario.entity.Entity;
import mario.entity.Particle;
import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;
import mario.main.states.KoopaState;
import mario.main.states.PlayerState;
import mario.states.BossState;
import mario.tile.Tile;
import mario.tile.Trial;

public class Player extends Entity {
	
	private PlayerState state = PlayerState.small;
	
	private int frame = 0;
	private int frameDelay = 0;
	private int particleDelay=0;
	private int pixelsTravelled = 5;
	private int invincibilityTime = 0;
	
	private boolean invincible = false;
	
	private Random random;
	
	private boolean animate = false;

	public Player(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);	
		state = PlayerState.small;
		random = new Random();
	}

	@Override
	public void die() {
		handler.removeEntity(this);		
		// check lives
			Game.lives--; 
			Game.showDeathScreen = true;
			
			if (Game.lives<=0) {
				Game.gameOver = true;
			}
     }
	
	public void render(Graphics g) {
		
			if (facing == 0) {
				g.drawImage(Game.player[frame + 5].getBufferedImage(), x, y, width, height, null);
			}
			else 
				if (facing == 1) {
					g.drawImage(Game.player[frame].getBufferedImage(), x, y, width, height, null);
				}
		}

	public void tick() throws CloneNotSupportedException {
		x+=velX;
		y+=velY;		
		
		// time for invicibility
		if(invincible) {
			
			if (facing ==0) {
				handler.addTile(new Trial(getX(), getY(), getWidth(), getHeight(), false, Id.trial, handler, Game.player[5+frame].getBufferedImage()));
			}
			else if (facing == 1) {
				handler.addTile(new Trial(getX(), getY(), getWidth(), getHeight(), false, Id.trial, handler, Game.player[frame].getBufferedImage()));
			}
			
			
			particleDelay++;
			if (particleDelay>=3) {
				 handler.addEntity(new Particle(getX() + random.nextInt(getWidth()), getY() + random.nextInt(getHeight()), 10, 10, false, Id.particle, handler));
				 particleDelay = 0;
			}
			
			invincibilityTime++;
			if(invincibilityTime >= 600) { // after cca 6 seconds goes into normal state
				invincible = false;
				invincibilityTime = 0;
			}
			
			// get faster
			if (velX == 5) setVelX(8);
			else if (velX == -5)setVelX(-8);
		}
		else {
			// go to normal
			if (velX == 8) setVelX(5);
			else if (velX == -8)setVelX(-5);
		}
		
		//animation 
		if (velX != 0) {
			animate = true;
		}else animate = false;
		
		//collision detection
		for(int i=0;i<handler.tile.size();i++) {
			Tile t = handler.tile.get(i);
			if (t.isSolid() && !goingDownPipe) { //if not in pipe
				if (getBoundsTop().intersects(t.getBounds())) {
					setVelY(0);
					if (jumping && !goingDownPipe) { //if jumping, then start falling
						jumping = false; 
						gravity = 0.8;
						falling = true;
					}
					if(t.getId()==Id.powerUp) { //activates powerUpBlock
						if(getBoundsTop().intersects(t.getBounds())) {
							t.activated = true;
						}
					}
				}
				
				if (getBoundsBottom().intersects(t.getBounds())) {
					setVelY(0);
					if (falling) { //if falling stop falling
						falling = false;
					}
				}
				else
					if(!jumping && !falling) {
						gravity = 0.8;
						falling = true; //starts falling again
					}
				
				if (getBoundsLeft().intersects(t.getBounds()) ) {
					setVelX(0);
					if (falling) x = getX();
					else x = t.getX() + t.width;					
				}
				
				if (getBoundsRight().intersects(t.getBounds())) {
					setVelX(0);		
					x = t.getX()- t.width;
				}
				
				if (getBoundsRightPlayer().intersects(t.getBounds())) {
					if (t.getId()==Id.flag) {
						Game.switchLevel();
					}
				}
			}
		}
		
		//when bumped into mushroom player gets bigger
		for (int i=0; i<handler.entity.size();i++) {		
			Entity e = handler.entity.get(i);
			
			if (e.getId()==Id.mushroom) {
				switch(e.getType()) {
				case 0:
					if (getBounds().intersects(e.getBounds())) {
						if (state==PlayerState.small) { //gets bigger
							state = PlayerState.big;
							int tpx = getX();
							int tpy = getY();
							width+=(width/3);
							height+=(height/3);
							setX(tpx-width);
							setY(tpy-height);
						}
						e.die(); //destroy mushroom						
					}
					break;
				case 1:
					if (getBounds().intersects(e.getBounds())) { //plus life
						Game.lives++;
						e.die();
					}
				}
			}
			else 
				if ((e.getId()==Id.goomba) || (e.getId()==Id.towerBoss) || (e.getId()==Id.piranha)) {
					
					if (invincible) {
						if (getBounds().intersects(e.getBounds()))e.die();
					}
					else {
						if (getBoundsBottom().intersects(e.getBoundsTop())) {
							if (e.getId() == Id.goomba) e.die();
							else if (e.getId() == Id.piranha) {
								if (e.getBounds().intersects(getBoundsBottom())) die();
							}
							else if (e.attackable){
								e.hp--;
								e.falling = true;
								e.gravity = 3.0;
								e.bossState = BossState.recovering;
								e.attackable = false;
								e.phaseTime = 0;
								jumping = true;
								falling = false; 
								gravity = 3.5;
							}
						}
						else if (getBounds().intersects(e.getBounds())) {
							if (state == PlayerState.big) {
								state = PlayerState.small;
								width = 48;
								height = 48;
							}
							else if (state == PlayerState.small) {
								die();	
							}
						}
					}
				}
				else if (e.getId()==Id.coin) {
					if (getBounds().intersects(e.getBounds()) && e.getId() == Id.coin) {
						Game.coins++;
						e.die(); //destroy coin
					}
				}
				else if (e.getId()==Id.koopa) {
					
					if (invincible) {
						if (getBounds().intersects(e.getBounds()))
							e.die();
					}
					else {
						if (e.koopaState==KoopaState.walking) {
							if (getBoundsBottom().intersects(e.getBoundsTop())) {
								e.koopaState = KoopaState.shell;
								
								jumping = true;
								falling = false; 
								gravity = 3.5;
							}
							else if (getBounds().intersects(e.getBounds())) {
								die();
							}
						}
						else if (e.koopaState==KoopaState.shell) {
							if (getBoundsBottom().intersects(e.getBoundsTop())) {
								e.koopaState = KoopaState.spinning;
								int dir = random.nextInt(2); 
								switch(dir) {
								 case 0:
									 e.setVelX(-10);
									 facing = 0;
									 break;
								 case 1:
									 e.setVelX(10);
									 facing = 1;
									 break;
								}
								jumping = true;
								falling = false; 
								gravity = 3.5;
								
							} else if (getBoundsLeft().intersects(e.getBoundsRight())) {
								e.koopaState = KoopaState.spinning;
								e.setVelX(-10);
							} else if (getBoundsRight().intersects(e.getBoundsLeft())) {
								e.koopaState = KoopaState.spinning;
								e.setVelX(10);
							}
						}
						else if (e.koopaState==KoopaState.spinning) {
							if (getBoundsBottom().intersects(e.getBoundsTop())) {
								e.koopaState = KoopaState.shell;
								
								jumping = true;
								falling = false; 
								gravity = 3.5;
							}
							else if (getBounds().intersects(e.getBounds())) {
								die(); // destroy koopa
							}
						}
					}
				}
				else if (e.getId()==Id.star) {
						if (getBounds().intersects(e.getBounds())) {
							invincible = true;
							e.die(); 
						}
				}
				
		}
		
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
			if (getVelY()<=10)setVelY((int)gravity);
		}
		
		//player moving
		if (velX!=0) {
			frameDelay++;
			if (frameDelay>=10) {
				frame++;
				if (frame > 3) {
					frame = 0;
				}
				frameDelay = 0;
			}		
		}
		
		if (goingDownPipe) {
			for (int i = 0; i<Game.handler.tile.size(); i++) {
				Tile t = Game.handler.tile.get(i);
				
				if (t.getId()==Id.pipe) {
					boolean up = false;
					boolean down = false;
					if (getBounds().intersects(t.getBounds())) {
						switch(t.facing) {
						case 0: //upwards pipe
							setVelY(-5);
							setVelX(0);
							pixelsTravelled += -velY;
							 up = true;
							 down = false;
							break;
						case 2: //downwards pipe
							setVelY(5);
							setVelX(0);
							pixelsTravelled += velY;
							down = true;
							up = false;
							break;
						}
						if (pixelsTravelled >= t.height) { //getting out of pipe
							goingDownPipe = false;	
							pixelsTravelled = 0;
							
							
							if (down) {
								if (state == PlayerState.small) {
									x-=20;
									y+=100;
								}
								if (state == PlayerState.big) {
									x-=25;
									y+=100;
								}								
							}
							
							if (up) {
								if (state == PlayerState.small) {
									x+=1;
									y-=20;
								}
								if (state == PlayerState.big) {
									x+=2;
									y-=20;
								}			
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Player clone() throws CloneNotSupportedException {
	    return (Player) super.clone();
	}
}
