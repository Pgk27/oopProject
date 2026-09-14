import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.*;

public class Screen extends JPanel implements Runnable {
	public Thread thread = new Thread(this);
	
	public static Image[] tileset_ground = new Image[100];
	public static Image[] tileset_air = new Image[100];
	public static Image[] tileset_res = new Image[100];
	public static Image[] tileset_mob = new Image[100];
	public static Image[] tileset_mobb = new Image[100];        
	public static Image[] tileset_mobbb = new Image[100];      
	
	
	
	public static int myWidth, myHeight;
	public static int coinage = 10, health=100; //başlangıç parası, canı
	public static int killed = 0 , killsToWin = 0, level = 1, maxlevel = 3;
	public static int winTime =2000,winFrame =0;
	public static boolean isFirst = true;
	public static boolean isDebug = false; // çerçeve modu
	public static boolean isWin = false;
	
	
	public static Point mse = new Point();//imlecin ekrandaki yerini belirlememize yarayacak
	
	public static Room room;
	public static Save save;
	public static Store store;
	
	
	
	
	public static Mob[] mobs = new Mob[100]; // gelen mob sayısı
	 
	public static Mob2[] mobss = new Mob2[100];           
	public static Mob3[] mobsss = new Mob3[100];             
	
	
	
	public Screen(Frame frame) {
		frame.addMouseListener(new KeyHandel());
		frame.addMouseMotionListener(new KeyHandel());
		
		thread.start();
	}
	
	
	public static void hasWon() {
		if(killed == killsToWin) {
			isWin = true;
			killed = 0;		
			coinage = 0; 
		}
	}
	
	
	
	
	
	public void define() {
		room = new Room();
		save = new Save();
		store = new Store();
		
		coinage = 10; // starting coin
		health = 10; // starting health
		
		
		for(int i=0; i < tileset_ground.length; i++) {
			tileset_ground[i] = new ImageIcon("res/tileset_ground.png").getImage();
			tileset_ground[i] = createImage(new FilteredImageSource(tileset_ground[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		for(int i=0; i < tileset_air.length; i++) {
			tileset_air[i] = new ImageIcon("res/tileset_air.png").getImage();
			tileset_air[i] = createImage(new FilteredImageSource(tileset_air[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		
		tileset_res[0] = new ImageIcon("res/cell.png").getImage();
		tileset_res[1] = new ImageIcon("res/heart.png").getImage();
		tileset_res[2] = new ImageIcon("res/coin.png").getImage();
		
		tileset_mob[0] = new ImageIcon("res/mob1.png").getImage();
		tileset_mobb[0] = new ImageIcon("res/mob2.png").getImage(); 
		tileset_mobbb[0] = new ImageIcon("res/mob3.png").getImage();  
		
		
		save.loadSave(new File("save/map" + level )); //map ı yüklüyor
		
		
		for( int i = 0 ;i <mobs.length;i++) { // mob class ındaki özellikleri moblara atıyor
			mobs[i] = new Mob(); 
		}
		
		for( int i = 0 ;i <mobss.length;i++) { 
			mobss[i] = new Mob2();
		}
		
		for( int i = 0 ;i <mobsss.length;i++) { 
			mobsss[i] = new Mob3();
		}
	}
	
		
	public void paintComponent(Graphics g) {
		if(isFirst) { // oyuna ilk giriş ise, oyunu ekrana çizer
			myWidth = getWidth();
			myHeight = getHeight();
			define(); 
			
			isFirst = false;
		}
		
		g.setColor(new Color(70, 70, 70));//arka planın rengi 
		g.fillRect(0, 0, getWidth(), getHeight());
		
		
		room.draw(g); //room daki tasarımların screen de görünmesini sağlıyor
		
		
		for( int i = 0; i < mobs.length; i++) { // mobları ekrana çizdiğimiz yer burası / spawnlan mıyor!!!!!
			if(mobs[i].inGame) {
				mobs[i].draw(g);
			}
		}
		
		
		for(int i = 0; i < mobss.length; i++) { 
			if(mobss[i].inGame) {
				mobss[i].draw(g);
			}
		}
		
		for(int i = 0; i < mobsss.length; i++) { 
			if(mobsss[i].inGame) {
				mobsss[i].draw(g); 
			}
		}
		store.draw(g); //Store çizmek için constructorları store classında
		
		if(health < 1) {
			g.setColor(new Color(240,20,20));
			g.fillRect(0, 0, myWidth, myHeight);
			g.setColor(new Color(225,255,255));
			g.setFont(new Font("Courier New",Font.BOLD,14));
			g.drawString("Game Over, Unlucky...:(", 10, 20);
		}
		
		if(isWin) {
			g.setColor(new Color(255,255,255));
			g.fillRect(0, 0, getWidth(), getHeight());  
			g.setColor(new Color(0,0,0));  //yazı , siyah
			g.setFont(new Font("Courier New",Font.BOLD,14));
			if(level > maxlevel) {			
				g.drawString("You won the whole game! Please wait and the window will close...", 10, 20);
			}
			else {
				g.drawString("You won! Congratulations! Please wait for the next level...", 10, 20);
			}
		}
	}
	 
	
	public int spawnTime = 1600, spawnFrame = 0;   // oluşma aralıkları
	public void mobSpawner() {
		if(spawnFrame >= spawnTime) {
			for(int i = 0; i < mobs.length; i++) {
				if(!mobs[i].inGame) {
					mobs[i].spawnMob(Value.mobMonster);
					break;
				}
			}
			spawnFrame = 0;
		}
		else {
			spawnFrame +=1;
		}
	}

	public int spawnTime2 = 1400, spawnFrame2 = 0;   
	public void mobSpawner2() {
		if(spawnFrame2 >= spawnTime2) {
			for(int i = 0; i<mobss.length;i++) {
				if(!mobss[i].inGame) {
					mobss[i].spawnMob(Value.mobMonster);
					break;
				}
			}
			spawnFrame2 = 0;
		}
		else {
			spawnFrame2 +=1;
		}
	}
	
	public int spawnTime3 = 1200, spawnFrame3 = 0;    
	public void mobSpawner3() {
		if(spawnFrame3 >= spawnTime3) {
			for(int i = 0; i<mobsss.length;i++) {
				if(!mobsss[i].inGame) {
					mobsss[i].spawnMob(Value.mobMonster);
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
					mobSpawner();
				}
				else if(level == 3){ //  mobu o levelda spawnlıyor
					mobSpawner2();
				}else { //level 3
					mobSpawner3();
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
					    	if(level >  maxlevel) {
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
}
			
			
			
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
