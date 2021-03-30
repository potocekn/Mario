package mario.entity;

import java.awt.Graphics;

import mario.main.Game;
import mario.main.Handler;
import mario.main.Id;
import mario.tile.Wall;

public class Coin extends Entity{

	public Coin(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
	}

	public void render(Graphics g) {
		g.drawImage(Game.coin.getBufferedImage(), x, y, width, height, null);
	}


	public void tick() {
		
	}

	@Override
	public Coin clone() throws CloneNotSupportedException {
	    return (Coin) super.clone();
	}
}
