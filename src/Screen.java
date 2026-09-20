import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.*;


public class Screen extends JPanel implements Runnable {
	Thread thread = new Thread(this);
	
	static Image[] tileset_ground = new Image[100];
	static Image[] tileset_air = new Image[100];
	static Image[] tileset_res = new Image[100];
	static Image[] tileset_mob = new Image[100];
	static Image[] tileset_mobb = new Image[100];        
	static Image[] tileset_mobbb = new Image[100];      
	
	// initilizers for animations
	static Image[] mobOrcWalk = new Image[8]; // 8 walking frames
	static Image[] mobDemonWalk = new Image[8];
	static Image[] mobSlimeWalk = new Image[8]; // cat has 10 running frames

	static Image[] mobOrcDead = new Image[4];
	static Image[] mobDemonDead = new Image[4];
	static Image[] mobSlimeDead = new Image[7];

	static int AnimFrame = 0;
	static int AnimTime = 40; // ANIMATION FRAME DELAY
	static int AnimTick = 0;

	//for Tower
	static Image[] cacherTower = new Image[8];
	static Image[] mageTower = new Image[8];
	static Image[] cannon = new Image[8];
	static Image[] goldMiner = new Image[8];
	// sẽ update tiếp sau
	
	
	static int myWidth, myHeight;
	static int coinage = 10, health = 100; //başlangıç parası, canı
	static int killed = 0, killsToWin = 0, level = 1, maxlevel = 3;
	static int winTime = 2000, winFrame = 0;
	static boolean isFirst = true;
	static boolean isDebug = false; // çerçeve modu
	static boolean isWin = false;
	
	
	static Point mse = new Point();//imlecin ekrandaki yerini belirlememize yarayacak
	
	static Room room;
	static Save save;
	static Store store;
	public static Tiles tiles;
	

	static Mob[] mobs = new Mob[100]; // gelen mob sayısı
	static Mob2[] mobss = new Mob2[100];
	static Mob3[] mobsss = new Mob3[100];
	
	Screen(Frame frame) {
		frame.addMouseListener(new KeyHandel());
		frame.addMouseMotionListener(new KeyHandel());
		
		thread.start();
	}
	
	static void hasWon() {
		if(killed >= killsToWin) {
			isWin = true;
			killed = 0;		
			coinage = 0; 
		}
	}
	
	void define() {
		room = new Room();
		save = new Save();
		store = new Store();
		Screen.tiles = new Tiles();
		
		coinage = 100; // starting coin
		health = 10; // starting health
		
		
		for(int i = 0; i < tileset_ground.length; i++) {
			tileset_ground[i] = new ImageIcon("res/tileset_ground.png").getImage();
			tileset_ground[i] = createImage(new FilteredImageSource(tileset_ground[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		for(int i = 0; i < tileset_air.length; i++) {
			tileset_air[i] = new ImageIcon("res/tileset_air.png").getImage();
			tileset_air[i] = createImage(new FilteredImageSource(tileset_air[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}

		for (int i = 0; i < cacherTower.length; i++) {
			cacherTower[i] = loadFrame("characterSprites/cacherTower/cacherTower" + i + ".png");
		}
		for (int i = 0; i < mageTower.length; i++) {
			mageTower[i] = loadFrame("characterSprites/mageTower/mageTower" + i + ".png");
		}
		for(int i = 0; i < cannon.length; i++){
			cannon[i] = loadFrame("characterSprites/cannon/cannon" + i + ".png");
		}
		for(int i = 0; i < goldMiner.length; i++){
			goldMiner[i] = loadFrame("characterSprites/goldMiner/goldMiner" + i + ".png");
		}
		
		tileset_res[0] = new ImageIcon("res/cell.png").getImage();
		tileset_res[1] = new ImageIcon("res/heart.png").getImage();
		tileset_res[2] = new ImageIcon("res/coin.png").getImage();
		
		for (int i = 0; i < mobOrcWalk.length; i++){
			mobOrcWalk[i] = loadFrame("characterSprites/orc/walk00" + i + ".png");
			mobDemonWalk[i] = loadFrame("characterSprites/demon/walk00" + i + ".png");
			mobSlimeWalk[i] = loadFrame("characterSprites/slime/walk00" + i + ".png");
		}
		for (int i = 0; i < mobOrcDead.length; i++){
			mobOrcDead[i] = loadFrame("characterSprites/orc/dead00" + i + ".png");
			mobDemonDead[i] = loadFrame("characterSprites/demon/dead00" + i + ".png");
		}
		for(int i = 0; i < mobSlimeDead.length; i++){
			mobSlimeDead[i] = loadFrame("characterSprites/slime/dead00" + i + ".png");
		}

		tileset_mob[0] = mobOrcWalk[0];
		tileset_mobb[0] = mobDemonWalk[0];
		tileset_mobbb[0] = mobSlimeWalk[0];
		
		
		save.loadSave(new File("save/map" + level )); //map ı yüklüyor
		
		
		for( int i = 0 ; i < mobs.length;i++) { // mob class ındaki özellikleri moblara atıyor
			mobs[i] = new Mob(); 
		}
		
		for( int i = 0 ; i < mobss.length;i++) { 
			mobss[i] = new Mob2();
		}
		
		for( int i = 0 ; i < mobsss.length;i++) { 
			mobsss[i] = new Mob3();
		}
	}

	public static int gameState=0;
	public static  final int tileScreen=0;
	public static final int playGame=1;
	public static final int settings=2;
	public static final int selectSkill=3;
	public static final int gameShop=4;
	public static final int buyItem=5;
	public static final int gachaHero=6;

	private GameRender gameRender = new GameRender();
	
	@Override 
	public void paintComponent(Graphics g) {  // Hàm vẽ chính --> đẩy sang gamerender.java
		if(isFirst) { 
            myWidth = getWidth();  
            myHeight = getHeight(); 
            define();
            
            isFirst = false;
        }

		super.paintComponent(g);
		gameRender.render(g, getWidth(), getHeight());
	
	}
		
	int spawnTime = 1600, spawnFrame = 0;   // oluşma aralıkları
	void mobSpawner() {
		if(spawnFrame >= spawnTime) {
			for(int i = 0; i < mobs.length; i++) {
				if(!mobs[i].inGame) {
					mobs[i].spawnMob(Value.mobMonster1);
					break;
				}
			}
			spawnFrame = 0;
		}
		else {
			spawnFrame +=1;
		}
	}

	int spawnTime2 = 1400, spawnFrame2 = 0;   
	void mobSpawner2() {
		if(spawnFrame2 >= spawnTime2) {
			for(int i = 0; i < mobss.length;i++) {
				if(!mobss[i].inGame) {
					mobss[i].spawnMob(Value.mobMonster2);
					break;
				}
			}
			spawnFrame2 = 0;
		}
		else {
			spawnFrame2 +=1;
		}
	}
	
	int spawnTime3 = 1200, spawnFrame3 = 0;    
	void mobSpawner3() {
		if(spawnFrame3 >= spawnTime3) {
			for(int i = 0; i<mobsss.length;i++) {
				if(!mobsss[i].inGame) {
					mobsss[i].spawnMob(Value.mobMonster3);
					break;
				}
			}
			spawnFrame3 = 0;
		}
		else {
			spawnFrame3 +=1;
		}	
	}

	
	public void run() {
		while(true) {
			if(!isFirst && health > 0 && !isWin) {
				room.physic(); // oyunu ekrana veriyor

				if(level == 1) { // mobu o levelda spawnlıyor
					mobSpawner();
				}
				else if(level == 2){ // mobu o levelda spawnlıyor
					mobSpawner2();
				}
				else if(level == 3){ //  mobu o levelda spawnlıyor
					mobSpawner3();
				}else { //level 3
					mobSpawner3();
				}
				// Advance animation cycle ==> ??????? sos cứu t Cường ơi éo hiểu :))))
				AnimTick++;
				if (AnimTick >= AnimTime) {
					AnimFrame++;
					if (AnimFrame >= mobOrcWalk.length){
						AnimFrame = 0;
					}
					// if (AnimFrame % 10 == 0) coinage++; 
					AnimTick = 0;
            	}

				for(int i = 0; i < mobs.length; i++) { // mobun hareketi
					if(mobs[i].inGame) {
						mobs[i].physic();
					}
					
				}
				
				for(int i = 0; i <mobss.length; i++) { //////////////*******************
					if(mobss[i].inGame) {
						mobss[i].physic();
					}
					
				}
				for(int i = 0; i < mobsss.length; i++) { ////////////////////**************************
					if(mobsss[i].inGame) {
						mobsss[i].physic();
					}
					
				}	
			}
			else {
				  if(isWin) {
					    if(winFrame>=winTime) {
							level++;
					    	if(level > maxlevel) {
					    		System.exit(0);
					    	}else {
								define();
								isWin = false;
					    	}
					    	winFrame = 0;
					    }
						else {
					    	winFrame +=1;
					    }
				  }
			}
			repaint();
			try {
				Thread.sleep(1);// acılma süresi
			} catch(Exception e)  {}
		}
	}

	// crop frame by frame
	static Image loadFrame(String path) {
		try {
			BufferedImage raw = javax.imageio.ImageIO.read(new File(path));
			if (raw == null) return null;
			return raw;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}