package core;
import entity.Projectiles;
import entity.defender.Mercenary;
import entity.defender.CastleDefender;
import entity.enemy.Mob;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;
import map.Room;
import map.Save;
import spell.Spell;
import wave.WaveManager;

public class Screen extends JPanel implements Runnable {
	Thread thread = new Thread(this);

	public static CopyOnWriteArrayList<Projectiles> pjt = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<Mob> activeMobs = new CopyOnWriteArrayList<>();

	public static Image[] tileset_ground = new Image[100];
	public static Image[] tileset_air = new Image[100];
	public static Image[] tileset_res = new Image[100];    

	public static CastleDefender[] mini = new CastleDefender[1];
	public static Mercenary[] merc = new Mercenary[100];
	
	// khởi tạo chu kì animation
	// Orc
	public static Image[] mobOrcWalkAnim = new Image[8]; // 8 walking frames
	public static Image[] mobOrcDeadAnim = new Image[4];
	public static Image[] mobOrcIdleAnim = new Image[6];
	public static Image[] mobOrcAttack1Anim = new Image[6];
	public static Image[] mobOrcAttack2Anim = new Image[6];

	// Demon
	public static Image[] mobDemonWalkAnim = new Image[8];
	public static Image[] mobDemonDeadAnim = new Image[4];
	public static Image[] mobDemonIdleAnim = new Image[6];
	public static Image[] mobDemonAttack1Anim = new Image[8];
	public static Image[] mobDemonAttack2Anim = new Image[8];

	// Slime
	public static Image[] mobSlimeWalkAnim = new Image[8];
	public static Image[] mobSlimeDeadAnim = new Image[7];
	public static Image[] mobSlimeIdleAnim = new Image[6];
	public static Image[] mobSlimeAttack1Anim = new Image[8];
	public static Image[] mobSlimeAttack2Anim = new Image[8];

	// Minion
	public static Image[] minionIdleAnim = new Image[6];
	public static Image[] minionDeadAnim = new Image[4];
	public static Image[] minionAttack1Anim = new Image[6];
	public static Image[] minionAttack2Anim = new Image[6];
	public static Image[] minionShootingAnim = new Image[9];

	// Mercenary
	public static Image mercenaryIcon;
	public static Image[] mercenaryIdleAnim = new Image[4];
	public static Image[] mercenaryDeadAnim = new Image[5];
	public static Image[] mercenaryAttackAnim = new Image[7];

	//for Tower
	public static Image[] archerTower = new Image[8];
	public static Image[] mageTower = new Image[8];
	public static Image[] cannon = new Image[8];
	public static Image[] goldMiner = new Image[8];
	public static Image[] blackHole = new Image[8];
	public static Image[] explosion = new Image[8];
	public static Image airplane;

	public static Random rand = new Random();
	public static int randomNum;

	public static int gameState = Constants.TITLE_SCREEN; // cho biết mình đang ở màn hình nào

	public static int gachaType;
	public static final int gachaBH=1;
	public static final int gachaEnhance=2;
	public static final int gachaMercenary=3;
	public static final int summon=4;

	public static int heroXp = 100;
	public static int heroLevel = 1;
	public static int[] upHeroXp = new int[26];
	public static int[] upHeroCoin = new int[26];
	public static int[] upHeroShard = new int[26];
	public static int[] upHeroECore = new int[26];

	public static double startGameTime;
	public static double endGameTime;
	public static int gameTime;

	public static boolean unlockBH = true;
	public static boolean unlockEnhance = true;
	public static boolean unlockMercenary = false;

	public static int chosenCard = 0; // 1=BH, 2=Enhance, 3=Mercenary
	public static String strChosenCard = "None";

	public static int coinage = 100;
	public static int heroXpDauTran = 0;
	public static int coinDauTran;
	public static int healthDauTran;
	public static int shard = 100;
	public static int energyCore = 10;
	public static int remainingPoint = 5 * heroLevel;
	public static int maxPoint = 5 * heroLevel;

	public static int buffHp=0;
	public static int buffHpRe=0;
	public static int buffShield=0;
	public static int buffADmg=0;
	public static int buffSDmg=0;
	public static int buffAtkSp=0;
	public static int maxBuff = 25;
	public static int maxHeroLevel = 25;

	public static int goldenPickaxe = 0;
	public static int fireSword = 0;
	public static int cursedSkull = 0;
	public static int paperPlane = 0;

	public static boolean ok; 	//Đcm tluc thông minh vclll
	//Con này fix bug đoạn coin sát mép số 10, đề phòng bọn nghẹo gacha ko có tiền mua tháp
	//Nếu cứ so sánh thì bên keyhandle trừ tiền trước, sau đó sang bên render check coin thấy =10
	//thì nó lại hiện tb cảnh cáo, bị lệch render với logic.

	public static final int VIRTUAL_WIDTH = 1220;
	public static final int VIRTUAL_HEIGHT = 780;
	public static int myWidth = VIRTUAL_WIDTH;  
    public static int myHeight = VIRTUAL_HEIGHT; 

	public static double scale = 1.0;
	public static int offsetX = 0;
	public static int offsetY = 0;

	public static int health = 25;
	public static int maxHealth = 25;
	public static int killed = 0;
    public static int killsToWin = 0;

    public static int level = 1;
    public static int rielLevel = 2;
	public static volatile boolean isLoading = true;
	public static volatile int loadProgress = 0;
	public static volatile String loadStatus = "Initializing Final Kingdom...";
	public static volatile boolean isFirst = true;
	public static boolean isWin = false;


	public static CopyOnWriteArrayList<Spell> activeSpells = new CopyOnWriteArrayList<>();

	public static Map<Integer, Image> projectiles = new HashMap<>(); 
	
	// sẽ update tiếp sau
	static Point mse = new Point();
	
	static BufferedImage blankCursorImg;
	static Cursor blankCursor;

	public static int musicVolume = 2;
	public static int sfxVolume = 5;

	// SYSTEM
	public static Room room;
	public static Save save;
	public static Store store;
	public static Tiles tiles;
	public static WaveManager waveManager;
	public Frame frame;
	public static Sound sound = new Sound();
	private GameRender gameRender = new GameRender();

	public static UIManager manageNut;
	
	public static int ScreenCoNut=2;

	// Đôi hàm chuyển tọa độ chuột từ bên Screen... sang tọa độ theo hệ quy chiếu gốc
	// Cái này chuyển ngược so với hàm vẽ, hay phết :))
	public static Point toVirtualPoint(Point p) {
		return toVirtualPoint(p.x, p.y);
	}

	public static Point toVirtualPoint(int screenX, int screenY) {
		if (scale <= 0) return new Point(screenX, screenY);
		int vx = (int) Math.round((screenX - offsetX) / scale);
		int vy = (int) Math.round((screenY - offsetY) / scale);
		return new Point(vx, vy);
	}
	
	Screen(Frame frame) {
		this.frame = frame;
		manageNut = new UIManager(this);
		addMouseListener(new MouseHandler());
		addMouseMotionListener(new MouseHandler());
		sound.playMusic(sound.themeSong);
		thread.start();

		// Background asset loading thread
		new Thread(() -> {
			try {
				define();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isLoading = false;
				isFirst = false;
				SwingUtilities.invokeLater(() -> repaint());
			}
		}, "AssetLoaderThread").start();
	}
	
	public static void hasWon() {
		if(waveManager != null && waveManager.isAllWavesFinished() && !waveManager.isAnyMobAlive()) {
			isWin = true;
			// killed = 0;		
		}
	}

	static void startGame() {
		level = (int) ((rielLevel - 1) % 3) + 1;
		if (health <= 0) {
			health = maxHealth;
		}
		loadLevel();
		Screen.coinDauTran = coinage;
		Screen.healthDauTran = health;
		Screen.heroXpDauTran = Screen.heroXp;
		Screen.startGameTime = System.currentTimeMillis();
		gameState = Constants.IN_GAME;
	}

	public static void loadLevel() {
		if (store != null) store.resetSkills();
		room = new Room();
		killed = 0;
		isWin = false;
		activeSpells.clear();

		save.loadSave(new File("res/map/map" + level));
		
		activeMobs.clear();

		for (int i = 0; i < mini.length; i++){
			mini[i] = new CastleDefender();
		}

		if (mini.length > 0 && mini[0] != null) {
			mini[0].spawnMob(Constants.MINION_SOLDIER);
		}

		for (int i = 0; i < merc.length; i++){
			merc[i] = new Mercenary();
		}
		
		waveManager = new WaveManager(level);
	}

	public void run() {
		final int TARGET_UPS = 420;
		final double NS_PER_TICK = 1_000_000_000.0 / TARGET_UPS;

		long lastTime = System.nanoTime();
		double delta = 0;

		while(true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / NS_PER_TICK;
			lastTime = now;

			// Update physics at a guaranteed 60 times per second
			if (delta > 5) delta = 5;

			if(!isFirst && gameState == Constants.IN_GAME && room != null && waveManager != null) {
				while (delta >= 1){
					updateGamePhysics();
					delta--;
				}
			} else delta = 0;

			repaint();
			
			try {
				Thread.sleep(2); // tính toán của Gehihi để được 60fps
			} catch(Exception e)  {}
		}
	}

	private void updateGamePhysics(){
		if(health > 0 && !isWin) {
			room.physic();

			waveManager.update();
			hasWon(); // Liên tục kiểm tra điều kiện thắng để bắt kịp lúc animation quái chết kết thúc

			for (Mob mob : activeMobs){
				if (mob.inGame){
					mob.physic();
				}
				else{
					activeMobs.remove(mob);
				}
			}

			for (int i = 0; i < mini.length; i++){
				if(mini[i].inGame){
					mini[i].physic();
				}
			}

			for (int i = 0; i < merc.length; i++){
				if(merc[i].inGame){
					merc[i].physic();
				}
			}

			for (Projectiles a : pjt) {
				a.physic();
				if (a.hasRemoved) {
					pjt.remove(a);
				}
			}

			for (Spell s : activeSpells) {
				s.physic();
				if (s.isFinished()) {
					s.destroy();
					activeSpells.remove(s);
				}
			}

		}
		if (health <= 0) {
			endGameTime = System.currentTimeMillis();
			gameTime = (int)((endGameTime - startGameTime)/1000);

			gameState = Constants.DEFEAT_SCREEN;
			health = rand.nextInt(3) + 2; // reset health random ;)) // chịu em lực
			int luckyNumber = rand.nextInt(3)+2;
			//Số kia vừa là hp radom reset, vừa là % heroXp bị trừ :))
			health = luckyNumber; // reset health random ;))
			heroXp = (int)(heroXp *(100-3*luckyNumber)/100); // trừ heroXp theo % :)))))))))))))))))
		} else if (isWin) {
			// rielLevel++;
			gameState = Constants.VICTORY_SCREEN;
			heroXp = heroXp + killed;
		}
	}

	// đẩy define xuống dưới vì nó kém quan trọng, phải lướt nhiều >:(
	public void define() {
		loadStatus = "Loading configurations...";
		loadProgress = 10;
		save = new Save();
		
		loadStatus = "Loading textures...";
		loadProgress = 25;
		Screen.tiles = new Tiles();

		loadStatus = "Loading game systems...";
		loadProgress = 45;
		store = new Store();
		
		// tạo con trỏ trong suốt
		// mục đích để chèn "skin" con trỏ khác lên
		blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0,0), "blank cursor");
		SwingUtilities.invokeLater(() -> this.setCursor(blankCursor));
		
		loadStatus = "Loading map tiles...";
		loadProgress = 55;
		try {
			BufferedImage groundSheet = javax.imageio.ImageIO.read(new File("res/tileset_ground.png"));
			int count = Math.min(tileset_ground.length, groundSheet.getHeight() / 26);
			for(int i = 0; i < count; i++) {
				tileset_ground[i] = groundSheet.getSubimage(0, 26 * i, 26, 26);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedImage airSheet = javax.imageio.ImageIO.read(new File("res/tileset_air.png"));
			int count = Math.min(tileset_air.length, airSheet.getHeight() / 26);
			for(int i = 0; i < count; i++) {
				tileset_air[i] = airSheet.getSubimage(0, 26 * i, 26, 26);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		loadStatus = "Loading tower frames...";
		loadProgress = 70;
		for (int i = 0; i < archerTower.length; i++) {
			archerTower[i] = loadFrame("res/archerTower/archerTower" + i + ".png");
		}
		for (int i = 0; i < mageTower.length; i++) {
			mageTower[i] = loadFrame("res/mageTower/mageTower" + i + ".png");
		}
		for(int i = 0; i < cannon.length; i++){
			cannon[i] = loadFrame("res/cannon/cannon" + i + ".png");
		}
		for(int i = 0; i < goldMiner.length; i++){
			goldMiner[i] = loadFrame("res/goldMiner/goldMiner" + i + ".png");
		}
		for(int i = 0; i < blackHole.length; i++){
			blackHole[i] = loadFrame("res/blackHole/blackHole" + i + ".png");
		}
		airplane = loadFrame("res/airplane/airplane.png");
		for(int i = 0; i < explosion.length; i++){
			explosion[i] = loadFrame("res/airplane/explosion" + i + ".png");
		}

		tileset_res[0] = loadFrame("res/cell.png");
		tileset_res[1] = loadFrame("res/heart.png");
		tileset_res[2] = loadFrame("res/coin.png");
		
		loadStatus = "Loading unit animations...";
		loadProgress = 85;
		for (int i = 0; i < mobOrcWalkAnim.length; i++){
			mobOrcWalkAnim[i] = loadFrame("res/orc/walk00" + i + ".png");
			mobDemonWalkAnim[i] = loadFrame("res/demon/walk00" + i + ".png");
			mobSlimeWalkAnim[i] = loadFrame("res/slime/walk00" + i + ".png");
		}
		for (int i = 0; i < mobOrcDeadAnim.length; i++){
			mobOrcDeadAnim[i] = loadFrame("res/orc/dead00" + i + ".png");
			mobDemonDeadAnim[i] = loadFrame("res/demon/dead00" + i + ".png");
		}
		for (int i = 0; i < mobOrcIdleAnim.length; i++){
			mobOrcIdleAnim[i] = loadFrame("res/orc/idle00" + i + ".png");
		}
		for (int i = 0; i < mobDemonIdleAnim.length; i++){
			mobDemonIdleAnim[i] = loadFrame("res/demon/idle00" + i + ".png");
		}
		for (int i = 0; i < mobSlimeIdleAnim.length; i++){
			mobSlimeIdleAnim[i] = loadFrame("res/slime/idle00" + i + ".png");
		}
		for(int i = 0; i < mobSlimeDeadAnim.length; i++){
			mobSlimeDeadAnim[i] = loadFrame("res/slime/dead00" + i + ".png");
		}


		for (int i = 0; i < minionIdleAnim.length; i++){
			minionIdleAnim[i] = loadFrame("res/minion/idle00" + i + ".png");
		}

		mercenaryIcon = loadFrame("res/mercenary/icon.png");
		for (int i = 0; i < mercenaryIdleAnim.length; i++){
			mercenaryIdleAnim[i] = loadFrame("res/mercenary/idle00" + i + ".png");
		}
		for (int i = 0; i < mercenaryDeadAnim.length; i++){
			mercenaryDeadAnim[i] = loadFrame("res/mercenary/dead00" + i + ".png");
		}
		for (int i = 0; i < mercenaryAttackAnim.length; i++){
			mercenaryAttackAnim[i] = loadFrame("res/mercenary/attack00" + i + ".png");
		}

		for (int i = 0; i < mobOrcAttack1Anim.length; i++){
			mobOrcAttack1Anim[i] = loadFrame("res/orc/attack100" + i + ".png");
			mobOrcAttack2Anim[i] = loadFrame("res/orc/attack200" + i + ".png");
		}

		for (int i = 0; i < mobDemonAttack1Anim.length; i++){
			mobDemonAttack1Anim[i] = loadFrame("res/demon/attack100" + i + ".png");
			mobDemonAttack2Anim[i] = loadFrame("res/demon/attack200" + i + ".png");
		}

		for (int i = 0; i < mobSlimeAttack1Anim.length; i++){
			mobSlimeAttack1Anim[i] = loadFrame("res/slime/attack100" + i + ".png");
			mobSlimeAttack2Anim[i] = loadFrame("res/slime/attack200" + i + ".png");
		}

		for (int i = 0; i < minionAttack1Anim.length; i++){
			minionAttack1Anim[i] = loadFrame("res/minion/attack100" + i + ".png");
			minionAttack2Anim[i] = loadFrame("res/minion/attack200" + i + ".png");
		}

		for (int i = 0; i < minionShootingAnim.length; i++){
			minionShootingAnim[i] = loadFrame("res/minion/bowAttack00" + i + ".png");
		}
		
		for (int i = 0; i < minionDeadAnim.length; i++){
			minionDeadAnim[i] = loadFrame("res/minion/dead00" + i + ".png");
		}

		projectiles.put(Constants.ARCHER_TOWER, loadFrame("res/archerTower/arrow000.png")); // bắn tên thường
		projectiles.put(Constants.MAGE_TOWER, loadFrame("res/mageTower/energyBall.png"));
		projectiles.put(Constants.CANNON_TOWER, loadFrame("res/cannon/cannonBall.png"));
		projectiles.put(Constants.MINION_SOLDIER, loadFrame("res/archerTower/arrow002.png")); // bắn tên đỏ
		
		loadStatus = "Ready to play!";
		loadProgress = 100;
	}

	@Override 
	public void paintComponent(Graphics g) {  // Hàm vẽ chính --> đẩy sang gamerender.java
		super.paintComponent(g);

		int screenWidth = getWidth();
		int screenHeight = getHeight();

		// 1. Fill entire screen with black (letterbox/pillarbox bars)
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);

		if (isLoading) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Title
			g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
			FontMetrics fmTitle = g2d.getFontMetrics();
			String title = "FINAL KINGDOM";
			int tx = (screenWidth - fmTitle.stringWidth(title)) / 2;
			int ty = screenHeight / 2 - 40;
			g2d.setColor(new Color(245, 215, 110));
			g2d.drawString(title, tx, ty);

			// Status text
			g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
			FontMetrics fmStatus = g2d.getFontMetrics();
			String status = loadStatus;
			int sx = (screenWidth - fmStatus.stringWidth(status)) / 2;
			int sy = ty + 40;
			g2d.setColor(new Color(200, 200, 200));
			g2d.drawString(status, sx, sy);

			// Progress bar outline
			int barWidth = 360;
			int barHeight = 12;
			int bx = (screenWidth - barWidth) / 2;
			int by = sy + 25;
			g2d.setColor(new Color(40, 40, 40));
			g2d.fillRoundRect(bx, by, barWidth, barHeight, 6, 6);

			// Progress fill
			int progress = Math.min(100, Math.max(0, loadProgress));
			int fillWidth = (int) (barWidth * (progress / 100.0));
			if (fillWidth > 0) {
				g2d.setColor(new Color(60, 160, 240));
				g2d.fillRoundRect(bx, by, fillWidth, barHeight, 6, 6);
			}

			// Border
			g2d.setColor(new Color(100, 100, 100));
			g2d.drawRoundRect(bx, by, barWidth, barHeight, 6, 6);

			g2d.dispose();
			Toolkit.getDefaultToolkit().sync();
			return;
		}

		// 2. Calculate aspect-ratio-preserving scale and center offsets
		// Đcm hay vcl
		// Screen... là cái độ dài thực tế của cái Screen(tính cả phần đen)
		// VIRTUAL... là con độ dài được khai báo bên Frame, là cái size default để tao căn nút với vẽ ấy
		// ScaleX/Y tính tỉ lệ, xem set theo con X hay Y
		// draw... là cái độ dài thực tế mình vẽ lên theo tỉ lệ ok nhất(ko tính phần rìa đen)
		// con offset là độ dài rìa đen nếu có, ko có thì nó bằng 0 nhá :D

		double scaleX = (double) screenWidth / VIRTUAL_WIDTH;
		double scaleY = (double) screenHeight / VIRTUAL_HEIGHT;
		scale = Math.min(scaleX, scaleY);

		int drawWidth = (int) Math.round(VIRTUAL_WIDTH * scale);
		int drawHeight = (int) Math.round(VIRTUAL_HEIGHT * scale);
		offsetX = (screenWidth - drawWidth) / 2;
		offsetY = (screenHeight - drawHeight) / 2;

		// 3. Fast scaling without heavy CPU antialiasing on 800+ tiles
		Graphics2D g2d = (Graphics2D) g.create(); // Tạo ra 1 cái cọ khác để vẽ
		g2d.clipRect(offsetX, offsetY, drawWidth, drawHeight); //Giới hạn vùng vẽ(x, y, dx, dy), ra ngoài = xóa
		g2d.translate(offsetX, offsetY); // thay đổi gốc tọa độ(đến đoạn hết viền đen)
		g2d.scale(scale, scale); //phóng to, ví dụ truyền tọa độ x, y thì auto phóng lên thành x.scale, y.scale
		//dòng dưới này cực quan trọng
		//Khi phóng to ảnh, java sẽ làm mờ viền
		//Game ae mình làm pixel, phải có dòng này thì từng ô nó mới vuông đc
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		// cái này là gọi sang hàm vẽ chính này, sau khi đã dịch gốc tọa độ và thêm thu phóng scale
		// thì mình cứ phang cái tọa độ gốc của mình là ok thôi :>
		gameRender.render(g2d, VIRTUAL_WIDTH, VIRTUAL_HEIGHT); 
		
		g2d.dispose();

		// Flushes graphics buffer to eliminate display queue stutter in fullscreen
		Toolkit.getDefaultToolkit().sync(); //Cái này để đỡ lag, cụ thể ntn t đ.. rõ nữa :))
	}

	// crop frame by frame
	public Image loadFrame(String path) {
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
