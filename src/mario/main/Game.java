package mario.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import mario.entity.Entity;
import mario.graphics.Sprite;
import mario.graphics.SpriteSheet;
import mario.graphics.gui.Launcher;
import mario.input.KeyInput;
import mario.input.MouseInput;

public class Game extends Canvas implements Runnable {
	public static final int WIDTH = 270 -10;
	public static final int HEIGHT = WIDTH/14*10;
	public static final int SCALE = 4;
	public static final String TITLE = "Mario";
	
	private Thread thread;
	
	private boolean running = false; 
	
	public static int level = 0;
	
	private static BufferedImage[] levels;
	public static BufferedImage background;
	
	public static int coins = 0;
	public static int lives = 5;
	public static int deathScreenTime = 0;
	public static int winScreenTime = 0;
	public static int gameOverTime = 0;
	
	
	public static boolean showDeathScreen = true;
	public static boolean gameOver = false;
	public static boolean playing = false;
	public static boolean win = false;
	public static boolean endWin = false;
	public static boolean endGameOver = false;
	
	public static Handler handler;
	public static SpriteSheet sheet;
	public static Camera cam;
	public static Launcher launcher;
	public static MouseInput mouse;
	
	public static Sprite grass;	
	public static Sprite wall;	
	public static Sprite mushroom;	
	public static Sprite powerUp;
	public static Sprite usedPowerUp;
	public static Sprite coin;
	public static Sprite lifeMushroom;
	public static Sprite star; 
	public static Sprite piranha;
	public static Sprite koopaShell;
	public static Sprite pipe;
	
	public static Sprite goomba[];
	public static Sprite koopa[];
	public static Sprite player[];
	public static Sprite[] flag;
	public static Sprite[] particle;
	public static Sprite firePlayer[];
	public static Sprite towerBoss[];
	
	
	public Game() {
		//prepares game
		Dimension size = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}
	
	private void init() {
		//initialises variables
	    handler = new Handler();
		sheet = new SpriteSheet("/spritesheet.png");
		cam = new Camera();
		launcher = new Launcher();
		mouse = new MouseInput();
		
	    addKeyListener(new KeyInput());
	    addMouseListener(mouse);
	    addMouseMotionListener(mouse);
	    
	    wall = new Sprite(sheet,1,1);
	    grass = new Sprite(sheet,2,1);
	    mushroom = new Sprite(sheet,3,1);
	    powerUp = new Sprite(sheet,4,1);
	    usedPowerUp = new Sprite(sheet,5,1);
	    coin = new Sprite(sheet,6,1);
	    lifeMushroom = new Sprite(sheet,7,1);
	    star = new Sprite(sheet, 8,1);
	    pipe = new Sprite(sheet,11,1);
	    piranha = new Sprite(sheet,12,1);
	    koopaShell = new Sprite(sheet,13,1);
	    
	    player = new Sprite[10];
	    firePlayer = new Sprite[10];
	    goomba = new Sprite[10];
	    koopa = new Sprite[10];
	    flag = new Sprite[3];
	    particle = new Sprite[6];
	    towerBoss = new Sprite[3];
	    
	    levels = new BufferedImage[3];
	    
	    for (int i=0; i<player.length;i++) {
	    	player[i] = new Sprite(sheet, i+1, 16);
	    }
	    

	    for (int i=0; i<firePlayer.length;i++) {
	    	firePlayer[i] = new Sprite(sheet, i+1, 13);
	    }
	    
	    for (int i=0; i<goomba.length;i++) {
	    	goomba[i] = new Sprite(sheet, i+1, 15);
	    }
	    
	    for (int i=0; i<koopa.length;i++) {
	    	koopa[i] = new Sprite(sheet, i+1, 4); 
	    }
	    
	    for (int i=0; i<flag.length;i++) {
	    	flag[i] = new Sprite(sheet, i+1, 2);
	    }
	    for (int i=0; i<particle.length;i++) {
	    	particle[i] = new Sprite(sheet, i+1, 14);
	    }
	    
	    for (int i=0; i<towerBoss.length;i++) {
	    	towerBoss[i] = new Sprite(sheet, i+1, 3);
	    }
	    
	    try { //loads levels and background
			levels[0]= ImageIO.read(getClass().getResource("/level.png"));
			levels[1]= ImageIO.read(getClass().getResource("/level2.png"));
			levels[2]= ImageIO.read(getClass().getResource("/level3.png"));
			background = ImageIO.read(getClass().getResource("/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}	    
	}
	
	
	private synchronized void start() { //starts the game
		if (running) return;
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}
	
	private synchronized void stop() { //stops the game
		if (!running) return;
		running = false;		
		try {
			thread.join();
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}
	
	public void run(){
		init();
		requestFocus();
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double delta = 0.0;
		double ns = 1000000000.0/60.0;
		int frames = 0;
		int ticks = 0; //updates
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			
			while (delta >= 1) { //calls for update
				try {
					tick();
				} catch (CloneNotSupportedException e) {					
					e.printStackTrace();
				}
				ticks++;
				delta--;
			}
			try {
				render();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frames++;
			
			if (System.currentTimeMillis()-timer > 1000) { //if too high resets the values
				timer+=1000;
				//System.out.println(frames + " Frames per Second " + ticks + "Updates per second");
				frames = 0;
				ticks=0;
			}
		}
		stop();
	} 
	
	public void render() throws CloneNotSupportedException {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		if(!showDeathScreen) { //if player not dead, draws background
			g.drawImage(background, getX(), getY(), getWidth(), getHeight(), null);
			
		}
		
		if(showDeathScreen) { //if player died
			
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			if (!gameOver) { //if player still has some lives
				g.setColor(Color.WHITE);
				g.setFont(new Font("Courier",Font.BOLD,50));
				g.drawImage(player[4].getBufferedImage(), 450, 300, 100, 100, null);
				g.drawString("x" + lives, 550, 360);
			}
			else if (gameOver){ //if the game is over
				handler.ClearLevel(); //clears everything
				handler.createLevel(levels[2]); //creates empty level
				try {
					background = ImageIO.read(getClass().getResource("/lostScreen.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.drawImage(background, getX(), getY(), getWidth(), getHeight(), null);
			}			
		}
		else if (win) { //if player won
			
			handler.ClearLevel(); //clears everything
			handler.createLevel(levels[2]); //creates empty level
			
			//shows screen that you won
			try {
				background = ImageIO.read(getClass().getResource("/winScreen.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(background, getX(), getY(), getWidth(), getHeight(), null); //draws the win image
		}
		
		if (endWin || endGameOver)System.exit(0); //after some time the game closes
		
		if (playing) g.translate(cam.getX(), cam.getY());
		if(!showDeathScreen && playing) handler.render(g);
		else if (!playing) launcher.render(g);
		g.dispose();
		bs.show();
	}
	
	public void tick() throws CloneNotSupportedException { //update method
		if (playing) handler.tick();
		
		for (Entity e:handler.entity) {
			if (e.getId() == Id.player) {
				cam.tick(e);
			}
		}
		
		if (win) winScreenTime++; //counts time after win
		if (gameOver) gameOverTime++; //counts time after death
		
		if (winScreenTime >= 180) { //after about 3s game ends
			endWin = true;
			playing = false;
		}		
		
		if (gameOverTime >= 180) { //after about 3s game ends
			endGameOver = true;
			playing = false;
		}
		
		if (showDeathScreen && !gameOver && playing) deathScreenTime++;
		if (deathScreenTime >= 180){ //180 ticks = 3s
			if (!gameOver) {				
				deathScreenTime = 0;
				handler.ClearLevel(); //clears level
				handler.createLevel(levels[level]);//creates new level
				showDeathScreen = false;
			}
			else if(gameOver) {
				showDeathScreen = false;
				deathScreenTime = 0;
				playing = false;
				gameOver = false;
			}
			
		}	
		
	}
	
	public static int getFrameWidth() {
		return WIDTH*SCALE;
	}
	
	public static int getFrameHeight() {
		return HEIGHT*SCALE;
	}
	
	
	public static void switchLevel() throws CloneNotSupportedException {
		Game.level++;
		
		int tempCoins = coins;
		int tempLives = lives;
		
		if (level == 1) { //moving into the second level
			handler.ClearLevel();
			handler.createLevel(levels[level]);
			coins = tempCoins;
			lives = tempLives;
		}
		else if (level == 2) {//player won
			win = true;
		}
		
		
	}
	
	public static Rectangle getVisibleArea() {//returns visible area based on players position
		for (int i = 0; i<handler.entity.size();i++) {
			Entity e = handler.entity.get(i);
			if (e.getId()==Id.player) {
				return new Rectangle(e.getX() - (getFrameWidth()/2-5), e.getY() - (getFrameHeight()/2-5),getFrameWidth()+10,getFrameHeight()+10);
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.start(); //starts the game
	}
	
}
