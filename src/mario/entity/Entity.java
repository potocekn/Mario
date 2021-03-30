package mario.entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import mario.main.Handler;
import mario.main.Id;
import mario.main.states.KoopaState;
import mario.states.BossState;

public abstract class Entity implements Cloneable{

	public int x,y;
	public int width, height;
	public int facing = 0; //0 - left, 1 - right
	public int hp;
	public int phaseTime;
	public int type;
	
	public boolean solid; 
	public boolean jumping = false;
	public boolean falling = true;
	public boolean goingDownPipe = false;
	public boolean attackable = false;
	
	public int velX, velY; //velocity X and Y
	
	public Id id; 
	public BossState bossState;
	public KoopaState koopaState;
	
	public double gravity = 0.0;
	
	public Handler handler;
	
	public Entity(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.solid = solid;
		this.id = id;
		this.handler = handler;
	}
	
	public abstract void render(Graphics g);	
	public abstract void tick() throws CloneNotSupportedException;

	public void die() {
		handler.removeEntity(this);
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getType() {
		return type;
	}
	
	public Id getId() {
		return id;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void setSolid(boolean solid){
		this.solid = solid;
	}
	
	public void setVelX(int velX) {
		this.velX = velX;
	}


	public void setVelY(int velY) {
		this.velY = velY;
	}
	
	public int getVelX() {
		return velX;
	}

	public int getVelY() {
		return velY;
	}
	
	
	//for collision
	public Rectangle getBounds() {
		return new Rectangle(getX(),getY(),width, height);
	}
	
	public Rectangle getBoundsTop() {
		return new Rectangle(getX()+10,getY(),width-20, 5);
	}
	
	public Rectangle getBoundsBottom() {
		return new Rectangle(getX()+10,getY()+height-5,width-20, 5);
	}
	
	public Rectangle getBoundsLeft() {
		return new Rectangle(getX(),getY()+10,5,height-20);
	}
	
	public Rectangle getBoundsRight() {
		return new Rectangle(getX()+width,getY()+10,5,height-20);
	}
	public Rectangle getBoundsRightPlayer() { //for special cases
		return new Rectangle(getX()+width+10,getY()+10,5,height-20);
	}
	
	public Rectangle getBoundsRightBoss() { //for special cases
		return new Rectangle(getX()+width-10,getY()+10,5,height-20);
	}
}
