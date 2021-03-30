package mario.entity.mob;

import java.awt.Graphics;

import mario.entity.Entity;
import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;
import mario.tile.PowerUpBlock;

public class Piranha extends Entity{
	private int wait;
	private int pixelsTravelled = 0;
	
	private boolean moving;
	public boolean insidePipe;

	public Piranha(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
		moving = false;
		insidePipe = true;
	}

	public void render(Graphics g) {
		//g.setColor(Color.RED);
		//g.fillRect(getX(), getY()-64, getWidth(), getHeight());
		g.drawImage(Game.piranha.getBufferedImage(), getX(), getY()-64, getWidth(), getHeight(), null);
	}

	public void tick() {
		y += velY;
		
		if (!moving) wait++;
		
		if (wait>=180) {//after about 3s 
			if (insidePipe) {
				insidePipe = false;
			} //so that you could go out
			else {
				insidePipe = true;
			}
				
			moving = true;
			wait = 0;
			
		}
		
		if (moving) {
			if (insidePipe) setVelY(-3);//go up
			else setVelY(3);	//go down
			
			pixelsTravelled += velY;
			
			if ((pixelsTravelled>= getHeight()) || (pixelsTravelled<=- getHeight())) {
				pixelsTravelled = 0;
				moving = false;
				setVelY(0);
			}
			
		}
	}
	@Override
	public Piranha clone() throws CloneNotSupportedException {
	    return (Piranha) super.clone();
	}	
}
