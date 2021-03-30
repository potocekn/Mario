package mario.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import mario.entity.Coin;
import mario.entity.Entity;
import mario.entity.mob.Goomba;
import mario.entity.mob.Koopa;
import mario.entity.mob.Piranha;
import mario.entity.mob.Player;
import mario.entity.mob.TowerBoss;
import mario.entity.powerup.PowerStar;
import mario.tile.Flag;
import mario.tile.Pipe;
import mario.tile.PowerUpBlock;
import mario.tile.Tile;
import mario.tile.Wall;

public class Handler {
	
	public LinkedList<Entity> entity = new LinkedList<Entity>();
	public LinkedList<Tile> tile = new LinkedList<Tile>();
	
	//prototypes
	Wall wallPrototype = new Wall(0, 0, 64, 64,true, Id.wall,this,0);
	Flag flagPrototype = new Flag(0, 0, 64, 64*5, true, Id.flag, this);
	Pipe pipePrototype = new Pipe(0,0,64,1110,true, Id.pipe, this, 0,false);
	Pipe plantPipePrototype = new Pipe(0,0,64,1088,true, Id.pipe, this, 0,true);
	PowerUpBlock powerUpBlockPrototype = new PowerUpBlock(0, 0, 64, 64, true, Id.powerUp, this,Game.mushroom,0);
	Coin coinPrototype = new Coin(0,0,64,64,true, Id.coin, this);
	PowerStar powerStarPrototype = new PowerStar(0, 0, 64, 64, true, Id.star, this);
	Koopa koopaPrototype = new Koopa(0, 0, 64, 64, true, Id.koopa, this);
	Goomba goombaPrototype = new Goomba(0, 0, 64, 64, true, Id.goomba, this);
	TowerBoss towerBossPrototype = new TowerBoss(0,0,64,64,true, Id.towerBoss, this,3);
	Player playerPrototype = new Player(0, 0, 48, 48,false, Id.player,this);
	Piranha piranhaPrototype = new Piranha(0, 0, 0, 64, true, Id.piranha, this);
	
	public void render(Graphics g) {
		for(int i=0;i<entity.size();i++) {
			Entity e = entity.get(i);
			//does render() for each entity in visible area
			if (Game.getVisibleArea()!=null && e.getBounds().intersects(Game.getVisibleArea()) && e.getId() != Id.particle) e.render(g);
		}
		
		for(int i=0;i<tile.size();i++) {
			Tile t = tile.get(i);
			//does render() for each tile in visible area
			if (Game.getVisibleArea()!=null && t.getBounds().intersects(Game.getVisibleArea())) t.render(g);
		}
		
		for(int i=0;i<entity.size();i++) {
			Entity e = entity.get(i);
			//does render() for particles in visible area (we want particles to be on top, so they have to be rendered as last)
			if (Game.getVisibleArea()!=null && e.getBounds().intersects(Game.getVisibleArea())&& e.getId() == Id.particle) e.render(g);
		}
		
		if (Game.level < 2) {
			//in visible area shows the current state of coins
			g.drawImage(Game.coin.getBufferedImage(), Game.getVisibleArea().x + 20, Game.getVisibleArea().y + 20, 75, 75, null); 
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier",Font.BOLD,20));
			g.drawString("x" + Game.coins, Game.getVisibleArea().x+100, Game.getVisibleArea().y+95);
		}
		
		
	}
	
	public void tick() throws CloneNotSupportedException {
		for(int i=0;i<entity.size();i++) {
			Entity e = entity.get(i);
			e.tick(); //does tick() for each entity
		}
		
		for(int i=0;i<tile.size();i++) {
			Tile t = tile.get(i);
			if (Game.getVisibleArea()!=null && t.getBounds().intersects(Game.getVisibleArea())) t.tick(); //does tick() for each tile in visible area
		}
	}
	
	public void addEntity(Entity en) {
		entity.add(en);
	}
	
	public void removeEntity(Entity en) {
		entity.remove(en);
	}
	
	public void addTile(Tile ti) {
		tile.add(ti);
	}
	
	public void removeTile(Tile ti) {
		tile.remove(ti);
	}
	
	public void createLevel(BufferedImage level) throws CloneNotSupportedException {
		int width = level.getWidth();
		int height = level.getHeight();
		
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				
				//loading current pixel
				int pixel = level.getRGB(x, y);
				
				//division into RGB parts
				int red = (pixel >> 16) & 0xff; //bit shift
				int green = (pixel >> 8) & 0xff; 
				int blue = (pixel) & 0xff; 
				
				
				//adds entity or tile based on RGB values
				
				if ((red == 0)&&(green == 0)&&(blue == 0)) { // adds a wall
					Wall wall = wallPrototype.clone();
					wall.setX(x*64);
					wall.setY(y*64);
					wall.setType(1);
					addTile(wall);
				}
				if ((red == 0)&&(green == 255)&&(blue == 0)) { // adds grass
					Wall grass = wallPrototype.clone();
					grass.setX(x*64);
					grass.setY(y*64);
					addTile(grass);
				}
				
				if ((red == 0)&&(green == 0)&&(blue == 255)) { //adds player
					Player player = playerPrototype.clone();
					player.setX(x*64);
					player.setY(y*64);
					addEntity(player);
				}
				
				if ((red == 255)&&(green == 0)&&(blue == 0)) {//adds mushroom
					PowerUpBlock block = powerUpBlockPrototype.clone();
					block.setX(x*64);
					block.setY(y*64);
					block.setSpriteY(y*64);
					block.setPowerUp(Game.mushroom);
					addTile(block);
				}
				
				if ((red == 255)&&(green == 255)&&(blue == 0)) {//adds life mushroom
					PowerUpBlock block = powerUpBlockPrototype.clone();
					block.setX(x*64);
					block.setY(y*64);
					block.setSpriteY(y*64);
					block.setPowerUp(Game.lifeMushroom);
					addTile(block);
				}
				
				if ((red == 255)&&(green == 119)&&(blue == 0)) {//adds goomba
					Goomba goomba = goombaPrototype.clone();
					goomba.setX(x*64);
					goomba.setY(y*64);
					addEntity(goomba);
				}
				
				if ((red == 255)&&(green == 200)&&(blue == 0)) {//adds koopa
					Koopa koopa = koopaPrototype.clone();
					koopa.setX(x*64);
					koopa.setY(y*64);
					addEntity(koopa);
				}
				
				if ((red == 255)&&(green == 0)&&(blue == 115)) {//adds star
					PowerStar star = powerStarPrototype.clone();
					star.setX(x*64);
					star.setY(y*64);
					addEntity(star);
				}
				
				if ((red == 255) && (green >123 && green < 129) && (blue == 0)) {//adds pipe
					Pipe pipe = pipePrototype.clone();
					pipe.setX(x*64);
					pipe.setY(y*64);
					pipe.setFacing(128 - green);
					addTile(pipe); 
				}
				
				if ((red == 0) && (green >123 && green < 129) && (blue == 0)) {//adds pipe + piranha
					Pipe pipe = plantPipePrototype.clone();
					pipe.setX(x*64);
					pipe.setY(y*64);
					pipe.setFacing(128 - green);					
					addTile(pipe);
					Piranha piranha = piranhaPrototype.clone();
					piranha.setX(pipe.getX());
					piranha.setY(pipe.getY());
					piranha.setWidth(pipe.getWidth());
					piranha.setSolid(pipe.isSolid());
					addEntity(piranha);
				}
				
				if ((red == 255) && (green == 250) && (blue == 0)) {//adds coin
					Coin coin = coinPrototype.clone();
					coin.setX(x*64);
					coin.setY(y*64);
					addEntity(coin);
				}
				
				if ((red == 255) && (green == 0) && (blue == 255)) {//adds towerBossa
					TowerBoss towerBoss = towerBossPrototype.clone();
					towerBoss.setX(x*64);
					towerBoss.setY(y*64);
					addEntity(towerBoss); 
				}
				
				if ((red == 0)&&(green == 255)&&(blue == 150)) {//adds flag
					Flag flag = flagPrototype.clone();
					flag.setX(x*64);
					flag.setY(y*64);
					addTile(flag);
				}
			}//end for y
		}//end for x
		
	}//end of CreateLevel()
	
	public void ClearLevel() {
		entity.clear();
		tile.clear();
		Game.coins = 0;
	}
	
}
