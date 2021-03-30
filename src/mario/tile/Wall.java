package mario.tile;

import java.awt.Graphics;

import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;

public class Wall extends Tile{
	private int type;

	public Wall(int x, int y, int width, int height, boolean solid, Id id, Handler handler, int type) {
		super(x, y, width, height, solid, id, handler);
		
		this.type = type;
	}

	public void render(Graphics g) {
		if (type == 0) g.drawImage(Game.grass.getBufferedImage(), x, y, width, height, null);
		else if (type == 1)g.drawImage(Game.wall.getBufferedImage(), x, y, width, height, null);
	}

	public void tick() {
		
	}
	
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	@Override
	public Wall clone() throws CloneNotSupportedException {
	    return (Wall) super.clone();
	}
}
