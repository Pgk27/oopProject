import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

public class Screen extends JPanel implements Runnable {
	Thread thread = new Thread(this);

	static CopyOnWriteArrayList<Projectiles> pjt = new CopyOnWriteArrayList<>();
	
	static Image[] tileset_ground = new Image[100];
	static Image[] tileset_air = new Image[100];
	static Image[] tileset_res = new Image[100];    
	
	// initilizers for animations
	static Image[] mobOrcWalkAnim = new Image[8]; // 8 walking frames
	static Image[] mobOrcDeadAnim = new Image[4];
	static Image[] mobOrcAttack1Anim = new Image[6];
	static Image[] mobOrcAttack2Anim = new Image[6];

	static Image[] mobDemonWalkAnim = new Image[8];
	static Image[] mobDemonDeadAnim = new Image[4];
	static Image[] mobDemonAttack1Anim = new Image[8];
	static Image[] mobDemonAttack2Anim = new Image[8];

	static Image[] mobSlimeWalkAnim = new Image[8]; // cat has 10 running frames
	static Image[] mobSlimeDeadAnim = new Image[7];
	static Image[] mobSlimeAttack1Anim = new Image[8];
	static Image[] mobSlimeAttack2Anim = new Image[8];

	static Image[] minionIdleAnim = new Image[6];
	static Image[] minionDeadAnim = new Image[4];
	static Image[] minionAttack1Anim = new Image[6];
	static Image[] minionAttack2Anim = new Image[6];
	static Image[] minionShootingAnim = new Image[9];

	static int AnimFrame = 0;
	static int AnimTime = 40; // ANIMATION FRAME DELAY
	static int AnimTick = 0;

	//for Tower
	static Image[] cacherTower = new Image[8];
	static Image[] mageTower = new Image[8];
	static Image[] cannon = new Image[8];
	static Image[] goldMiner = new Image[8];

	static Map<Integer, Image> projectiles = new HashMap<>(); 
	// sẽ update tiếp sau

	public static int gameState = 8;

	public static final int tileScreen=0;
	public static final int playGame=1;
	public static final int settings=2;
	public static final int selectSkill=3;
	public static final int gameShop=4;
	public static final int buyItem=5;
	public static final int gacha=6;
	public static final int gachaRate=7;
	public static final int shardShop=8;
	public static final int thongBao=9;
	public static final int tongKetWin=10;
	public static final int tongKetLose=11;


	public static Random rand = new Random();
	public static int randomNum;

	
	public static int gachaType;
	public static final int gachaBH=1;
	public static final int gachaEnhance=2;
	public static final int gachaMercenary=3;
	public static final int summon=4;


	public static boolean unlockBH = false;
	public static boolean unlockEnhance = false;
	public static boolean unlockMercenary = false;

	public static int coinage = 99999;
	public static int coinDauTran;
	public static int healthDauTran;
	public static int shard =0;
	public static int eCore =0;

	public static boolean ok; 	//Đcm tluc thông minh vclll
	//Con này fix bug đoạn coin sát mép số 10, đề phòng bọn nghẹo gacha ko có tiền mua tháp
	//Nếu cứ so sánh thì bên keyhandle trừ tiền trước, sau đó sang bên render check coin thấy =10
	//thì nó lại hiện tb cảnh cáo, bị lệch render với logic.
	
	public static final int VIRTUAL_WIDTH = 1220;
	public static final int VIRTUAL_HEIGHT = 780;

	public static double scale = 1.0;
	public static int offsetX = 0;
	public static int offsetY = 0;

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

	static int myWidth = VIRTUAL_WIDTH, myHeight = VIRTUAL_HEIGHT;
	static int health = 999999;
	static int killed = 0, killsToWin = 0, level = 1, rielLevel =1;
	static boolean isFirst = true;
	static boolean isDebug = false;
	static boolean isWin = false;
	
	
	static Point mse = new Point();
	
	static Room room;
	static Save save;
	static Store store;
	public static Tiles tiles;
	public static WaveManager waveManager;
	

	static Mob[] mobs = new Mob[100];
	static Mob2[] mobss = new Mob2[100];
	static Mob3[] mobsss = new Mob3[100];
	static Minion[] mini = new Minion[10];

	public Frame frame;
	private GameRender gameRender = new GameRender();
	
	Screen(Frame frame) {
		this.frame = frame;
		addMouseListener(new KeyHandel());
		addMouseMotionListener(new KeyHandel());
		
		thread.start();
	}
	
	static void hasWon() {
		if(waveManager != null && waveManager.isAllWavesFinished() && !waveManager.isAnyMobAlive()) {
			isWin = true;
			killed = 0;		
		}
	}

	static void startGame() {
		level = (int) ((rielLevel - 1) % 3) + 1;
		loadLevel();
		gameState = playGame;
		Screen.coinDauTran = coinage;
		Screen.healthDauTran = health;
	}
	
	public void define() {
		save = new Save();
		Screen.tiles = new Tiles();
		loadLevel();
		store = new Store();
		
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
		
		for (int i = 0; i < mobOrcWalkAnim.length; i++){
			mobOrcWalkAnim[i] = loadFrame("characterSprites/orc/walk00" + i + ".png");
			mobDemonWalkAnim[i] = loadFrame("characterSprites/demon/walk00" + i + ".png");
			mobSlimeWalkAnim[i] = loadFrame("characterSprites/slime/walk00" + i + ".png");
		}
		for (int i = 0; i < mobOrcDeadAnim.length; i++){
			mobOrcDeadAnim[i] = loadFrame("characterSprites/orc/dead00" + i + ".png");
			mobDemonDeadAnim[i] = loadFrame("characterSprites/demon/dead00" + i + ".png");
		}
		for(int i = 0; i < mobSlimeDeadAnim.length; i++){
			mobSlimeDeadAnim[i] = loadFrame("characterSprites/slime/dead00" + i + ".png");
		}

		for (int i = 0; i < minionIdleAnim.length; i++){
			minionIdleAnim[i] = loadFrame("characterSprites/minion/idle00" + i + ".png");
		}

		for (int i = 0; i < mobOrcAttack1Anim.length; i++){
			mobOrcAttack1Anim[i] = loadFrame("characterSprites/orc/attack100" + i + ".png");
			mobOrcAttack2Anim[i] = loadFrame("characterSprites/orc/attack200" + i + ".png");
		}

		for (int i = 0; i < mobDemonAttack1Anim.length; i++){
			mobDemonAttack1Anim[i] = loadFrame("characterSprites/demon/attack100" + i + ".png");
			mobDemonAttack2Anim[i] = loadFrame("characterSprites/demon/attack200" + i + ".png");
		}

		for (int i = 0; i < mobSlimeAttack1Anim.length; i++){
			mobSlimeAttack1Anim[i] = loadFrame("characterSprites/slime/attack100" + i + ".png");
			mobSlimeAttack2Anim[i] = loadFrame("characterSprites/slime/attack200" + i + ".png");
		}

		for (int i = 0; i < minionAttack1Anim.length; i++){
			minionAttack1Anim[i] = loadFrame("characterSprites/minion/attack100" + i + ".png");
			minionAttack2Anim[i] = loadFrame("characterSprites/minion/attack200" + i + ".png");
		}

		for (int i = 0; i < minionShootingAnim.length; i++){
			minionShootingAnim[i] = loadFrame("characterSprites/minion/bowAttack00" + i + ".png");
		}
		
		for (int i = 0; i < minionDeadAnim.length; i++){
			minionDeadAnim[i] = loadFrame("characterSprites/minion/dead00" + i + ".png");
		}

		projectiles.put(Value.ARCHER_TOWER, loadFrame("characterSprites/cacherTower/arrow000.png"));
		projectiles.put(Value.MAGE_TOWER, loadFrame("characterSprites/mageTower/energyBall.png"));
		projectiles.put(Value.CANNON_TOWER, loadFrame("characterSprites/cannon/cannonBall.png"));
		projectiles.put(Value.MINION_SOLDIER, loadFrame("characterSprites/cacherTower/arrow002.png"));
		
		loadLevel();
	}

	public static void loadLevel() {
		room = new Room();
		killed = 0;
		isWin = false;

		save.loadSave(new File("save/map" + level)); //map ı yüklüyor
		
		
		for( int i = 0 ; i < mobs.length;i++) { // mob class ındaki özellikleri moblara atıyor
			mobs[i] = new Mob(); 
		}
		
		for( int i = 0 ; i < mobss.length;i++) { 
			mobss[i] = new Mob2();
		}
		
		for( int i = 0 ; i < mobsss.length;i++) { 
			mobsss[i] = new Mob3();
		}

		for (int i = 0; i < mini.length; i++){
			mini[i] = new Minion();
		}
		
		waveManager = new WaveManager(level);
	}

	@Override 
	public void paintComponent(Graphics g) {  // Hàm vẽ chính --> đẩy sang gamerender.java
		if(isFirst) { 
            myWidth = VIRTUAL_WIDTH;  
            myHeight = VIRTUAL_HEIGHT; 
            define();
            
            isFirst = false;
        }

		super.paintComponent(g);

		int screenWidth = getWidth();
		int screenHeight = getHeight();

		// 1. Fill entire screen with black (letterbox/pillarbox bars)
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);

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

	public void run() {
		while(true) {
			if(!isFirst && gameState == playGame) {
				if(health > 0 && !isWin) {
					room.physic(); // oyunu ekrana veriyor

					waveManager.update();
					hasWon(); // Liên tục kiểm tra điều kiện thắng để bắt kịp lúc animation quái chết kết thúc
					
					// Advance animation cycle ==> ??????? sos cứu t Cường ơi éo hiểu :))))
					AnimTick++;
					if (AnimTick >= AnimTime) {
						AnimFrame++;
						if (AnimFrame >= mobOrcWalkAnim.length){
							AnimFrame = 0;
						}
						AnimTick = 0;
					}

					for(int i = 0; i < mobs.length; i++) { // mobun hareketi
						if(mobs[i].inGame) {
							mobs[i].physic();
						}
					}
					
					for(int i = 0; i <mobss.length; i++) { 
						if(mobss[i].inGame) {
							mobss[i].physic();
						}
					}
					
					for(int i = 0; i < mobsss.length; i++) { 
						if(mobsss[i].inGame) {
							mobsss[i].physic();
						}
					}	

					for (int i = 0; i < mini.length; i++){
						if(mini[i].inGame){
							mini[i].physic();
						}
					}

					for (Projectiles a : pjt) {
						a.physic();
						if (a.hasRemoved) {
							pjt.remove(a);
						}
					}

				}
				if (health <= 0) {
					gameState = tongKetLose;
					health = rand.nextInt(3) + 2; // reset health random ;))
				} else if (isWin) {
					rielLevel++;
					gameState = tongKetWin;
				}
			}
			repaint();
			try {
				Thread.sleep(1);// acılma süresi
			} catch(Exception e)  {}
		}
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