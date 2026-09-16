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
	static Image[] mobCatRun = new Image[10]; // cat has 10 running frames

	static int AnimFrame = 0;
	static int AnimTime = 50; // ANIMATION FRAME DELAY
	static int AnimTick = 0;
	static int spawnFrame = 0;
	
	
	static int myWidth, myHeight;
	static int coinage = 10, health = 100; //başlangıç parası, canı
	static int killed = 0 , killsToWin = 0, level = 1, maxlevel = 3;
	static int winTime = 2000, winFrame = 0;
	static boolean isFirst = true;
	static boolean isDebug = false; // çerçeve modu
	static boolean isWin = false;
	
	
	static Point mse = new Point();//imlecin ekrandaki yerini belirlememize yarayacak
	
	static Room room;
	static Save save;
	static Store store;
	

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
		
		coinage = 10; // starting coin
		health = 10; // starting health
		
		
		for(int i = 0; i < tileset_ground.length; i++) {
			tileset_ground[i] = new ImageIcon("res/tileset_ground.png").getImage();
			tileset_ground[i] = createImage(new FilteredImageSource(tileset_ground[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		for(int i = 0; i < tileset_air.length; i++) {
			tileset_air[i] = new ImageIcon("res/tileset_air.png").getImage();
			tileset_air[i] = createImage(new FilteredImageSource(tileset_air[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		
		tileset_res[0] = new ImageIcon("res/cell.png").getImage();
		tileset_res[1] = new ImageIcon("res/heart.png").getImage();
		tileset_res[2] = new ImageIcon("res/coin.png").getImage();
		
		for (int i = 0; i < mobOrcWalk.length; i++){
			mobOrcWalk[i] = loadAndCropSingleFrame("characterSprites/orcWalk00" + i + ".png");
		}
		for (int i = 0; i < mobDemonWalk.length; i++){
			mobDemonWalk[i] = loadAndCropSingleFrame("characterSprites/Demon/tile00" + i + ".png");
		}
		for (int i = 0; i < mobCatRun.length; i++){
			mobCatRun[i] = loadAndCropSingleFrame("characterSprites/Cat/tile00" + i + ".png");
		}

		tileset_mob[0] = mobOrcWalk[0];
		tileset_mobb[0] = mobDemonWalk[0];
		tileset_mobbb[0] = mobCatRun[0];
		
		
		save.loadSave(new File("save/map" + level )); //map ı yüklüyor
		
		
		for( int i = 0 ; i <mobs.length;i++) { // mob class ındaki özellikleri moblara atıyor
			mobs[i] = new Mob(); 
		}
		
		for( int i = 0 ; i < mobss.length;i++) { 
			mobss[i] = new Mob2();
		}
		
		for( int i = 0 ; i < mobsss.length;i++) { 
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
	 
	
	int spawnTime = 1600;   // oluşma aralıkları
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
					mobSpawner();
				}
				else if(level == 3){ //  mobu o levelda spawnlıyor
					mobSpawner2();
				}else { //level 3
					mobSpawner3();
				}
				// Advance animation cycle
				AnimTick++;
				if (AnimTick >= AnimTime) {
					AnimFrame++;
					if (AnimFrame >= mobOrcWalk.length){
						AnimFrame = 0;
					}
					if (AnimFrame % 10 == 0) coinage++; 
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

	// crop frame by frame
	static Image loadAndCropSingleFrame(String path) {
		try {
			BufferedImage raw = javax.imageio.ImageIO.read(new File(path));
			if (raw == null) return null;

			int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
			int maxX = 0, maxY = 0;
			boolean hasVisiblePixel = false;

			// 1. Quét tìm viền bounding box của các pixel thực sự có hình
			for (int y = 0; y < raw.getHeight(); y++) {
				for (int x = 0; x < raw.getWidth(); x++) {
					int rgb = raw.getRGB(x, y);
					Color c = new Color(rgb, true);

					// Loại trừ pixel trong suốt (alpha thấp) HOẶC màu nền trắng/xám sáng
					boolean isTransparent = (c.getAlpha() < 20);
					boolean isWhiteBg = (c.getRed() > 230 && c.getGreen() > 230 && c.getBlue() > 230);

					if (!isTransparent && !isWhiteBg) {
						hasVisiblePixel = true;
						if (x < minX) minX = x;
						if (x > maxX) maxX = x;
						if (y < minY) minY = y;
						if (y > maxY) maxY = y;
					}
				}
			}

			// Nếu ảnh trống, trả về nguyên bản
			if (!hasVisiblePixel) return raw;

			// Thêm 2 pixel đệm ở rìa để không cấn mép vũ khí/bóng
			minX = Math.max(0, minX - 2);
			minY = Math.max(0, minY - 2);
			maxX = Math.min(raw.getWidth() - 1, maxX + 2);
			maxY = Math.min(raw.getHeight() - 1, maxY + 2);

			int width = maxX - minX + 1;
			int height = maxY - minY + 1;

			// 2. Cắt ảnh con và lọc trong suốt các pixel nền thừa
			BufferedImage cropped = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int rgb = raw.getRGB(minX + x, minY + y);
					Color c = new Color(rgb, true);

					if (c.getAlpha() >= 20 && !(c.getRed() > 230 && c.getGreen() > 230 && c.getBlue() > 230)) {
						cropped.setRGB(x, y, rgb);
					} else {
						cropped.setRGB(x, y, 0x00000000); // Đặt thành trong suốt
					}
				}
			}

			return cropped;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}