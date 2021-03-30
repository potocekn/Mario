package mario.main;

import mario.entity.Entity;

public class Camera {

	public int x, y;
	
	public void tick(Entity player) {
		//makes rectangle around player (visible area for the game)
		setX(-player.getX() + Game.getFrameWidth()/2);
		setY(-player.getY() + Game.getFrameHeight()-350);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
