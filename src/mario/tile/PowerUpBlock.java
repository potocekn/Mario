package mario.tile;

import java.awt.Graphics;

import mario.entity.powerup.Mushroom;
import mario.graphics.Sprite;
import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;

public class PowerUpBlock extends Tile {
	
	private Sprite powerUp;
	
	private boolean poppedUp = false;
	
	private int spriteY;
	private int type;

	public PowerUpBlock(int x, int y, int width, int height, boolean solid, Id id, Handler handler, Sprite powerUp, int type) {
		super(x, y, width, height, solid, id, handler);
		this.powerUp = powerUp;
		this.type = type;
	}

	public void render(Graphics g) {
		if(!poppedUp) g.drawImage(powerUp.getBufferedImage(), x, spriteY, width, height, null); 
		if(!activated) {
			g.drawImage(Game.powerUp.getBufferedImage(), x, y, width, height, null);
		}
		else {
			g.drawImage(Game.usedPowerUp.getBufferedImage(), x, y, width, height, null);
		}

	}

	public void tick() {
		if(activated && !poppedUp) {
			spriteY--;
			if (spriteY <= y-height) {
				if (powerUp == Game.mushroom ) handler.addEntity(new Mushroom(x, spriteY, width, height,true,Id.mushroom,handler, 0));
				else if (powerUp == Game.lifeMushroom)handler.addEntity(new Mushroom(x, spriteY, width, height,true,Id.mushroom,handler, 1));
				poppedUp = true;
			}
		}
	}

	public void setPowerUp(Sprite powerUp)
	{
		this.powerUp = powerUp;
	}
	
	public void setSpriteY(int y)
	{
		spriteY = y;
	}
	
	@Override
	public PowerUpBlock clone() throws CloneNotSupportedException {
	    return (PowerUpBlock) super.clone();
	}
}
