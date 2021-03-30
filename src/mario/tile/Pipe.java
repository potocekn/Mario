package mario.tile;

import java.awt.Color;
import java.awt.Graphics;

import mario.entity.mob.Piranha;
import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;

public class Pipe extends Tile {
	
	
	public Pipe(int x, int y, int width, int height, boolean solid, Id id, Handler handler, int facing, boolean plant) {
		super(x, y, width, height, solid, id, handler);
		this.facing = facing;		
	}

	public void render(Graphics g) {
		g.drawImage(Game.pipe.getBufferedImage(), x, y, width, height, null);
		//g.setColor(new Color(0,128,0));
		//g.fillRect(x, y, width, height);
		
	}

	public void tick() {
		
	}
	
	public void setFacing(int facing)
	{
		this.facing = facing;
	}

	
	
	@Override
	public Pipe clone() throws CloneNotSupportedException {
	    return (Pipe)super.clone();
	}
}
