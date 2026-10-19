

import java.awt.*;
import java.awt.event.*;

public class KeyHandel implements MouseMotionListener, MouseListener {
	static boolean isMousePressed = false;

	static boolean isMusicMuted = false;
	static boolean isSfxMuted = false;
	static boolean isSettingCancelPressed = false;
	static boolean isSettingConfirmPressed = false;

	static long pressedTime = 0;
	static int prevMusicVolume = 0;
	static int prevSfxVolume = 0;

	static Rectangle startGame = new Rectangle(950, 450, 144, 72);
	static Rectangle quitGame = new Rectangle(950, 590, 144, 72);
	static Rectangle settings = new Rectangle(5, 5, 35, 35);
	static Rectangle store = new Rectangle(950, 520, 144, 72);
	static Rectangle backz = new Rectangle(5, 5, 50, 35);
	static Rectangle backz2 = new Rectangle (5, 700, 50, 35);
	static Rectangle toGacha = new Rectangle(680, 365, 170, 50);
	static Rectangle toBuyItem = new Rectangle(950, 365, 170, 50);
	static Rectangle in4gacha = new Rectangle(1110, 20, 40, 40);
	static Rectangle gachaRateFrame = new Rectangle(300, 5, 600, 750);
	static Rectangle iconShardShop = new Rectangle(1100, 80, 65, 65);
	static Rectangle shardShopBigFramee = new Rectangle(160, 30, 900, 680);

	static Rectangle musicSliderBox = new Rectangle();
	static Rectangle musicMuteSlider = new Rectangle(735, 240, 100, 100);
	static Rectangle sfxSliderBox = new Rectangle();
	static Rectangle sfxMuteSlider = new Rectangle(735, 340, 100, 100);
	static Rectangle fullscreenTickButton = new Rectangle(585, 465, 72, 80);
	static Rectangle fullscreenXButton = new Rectangle(685, 465, 72, 80);
	static Rectangle settingCancelButton = new Rectangle(350, 645, 240, 70);
	static Rectangle settingConfirmButton = new Rectangle(630, 645, 240, 70);

	static Rectangle gachaTornadoButt = new Rectangle(500, 345,  85, 50);
	static Rectangle gachaEnhanceButt = new Rectangle(500, 475,  85, 50);
	static Rectangle gachaMercenaryButt = new Rectangle(500, 610,  90, 53);
	static Rectangle summonButt = new Rectangle(800, 545, 125, 104);
	static Rectangle thongBaoFrame = new Rectangle(360, 140, 500, 400);

	static Rectangle doiBHButt = new Rectangle(670, 210, 95, 30);
	static Rectangle doiEnhanceButt = new Rectangle(670, 290, 95, 30);
	static Rectangle doiMercenaryButt = new Rectangle(670, 370, 95, 30);
	static Rectangle doiECButt = new Rectangle(670, 450, 95, 30);


	static Rectangle TKWtoMenu = new Rectangle(375, 495, 220, 45);
	static Rectangle TKWtoPlay = new Rectangle(625, 495, 220, 45);
	static Rectangle TKLtoMenu = new Rectangle(375, 487, 220, 45);
	static Rectangle TKLtoPlay = new Rectangle(625, 487, 220, 45);


	public void mouseClicked(MouseEvent e) {
		Point vp = Screen.toVirtualPoint(e.getX(), e.getY());
		int mouseX = vp.x;
		int mouseY = vp.y;

		if(Screen.gameState == Value.TITLE_SCREEN) {
			if(startGame.contains(mouseX, mouseY)) {
				Screen.startGame();
				e.getComponent().repaint();
			}
			else if(settings.contains(mouseX, mouseY)) {
				Screen.gameState = Value.SETTING;
			}
			else if(store.contains(mouseX, mouseY)) {
				Screen.gameState = Value.SHOP;
				e.getComponent().repaint();
			}else if(quitGame.contains(mouseX, mouseY)) {
				System.exit(0);
			}
		}
		else if(Screen.gameState == Value.IN_GAME) {
			if(backz2.contains(mouseX, mouseY)) {
				xuLyOutGame();
				Screen.gameState = Value.TITLE_SCREEN;
			}
		}
		else if(Screen.gameState == Value.SHOP) {
			if(backz.contains(mouseX, mouseY)) {
				Screen.gameState = Value.TITLE_SCREEN;
				e.getComponent().repaint();
			}
			else if(toGacha.contains(mouseX, mouseY)) {
				Screen.gameState = Value.GACHA;
			}
			else if(toBuyItem.contains(mouseX, mouseY)) {
				Screen.gameState = Value.ITEM_SHOP;
			}
		}
		else if(Screen.gameState == Value.GACHA) {
			if(backz.contains(mouseX, mouseY)) {
				Screen.gameState = Value.SHOP;
				e.getComponent().repaint();
			}
			else if (in4gacha.contains(mouseX, mouseY)) {
				Screen.gameState=Value.GACHA_RATE;
				e.getComponent().repaint();
			}
			else if( iconShardShop.contains(mouseX, mouseY)){
				Screen.gameState = Value.SHARD_SHOP;
				e.getComponent().repaint();
			}
			else if( gachaTornadoButt.contains(mouseX, mouseY)) {
				Screen.gameState = Value.NOTI;
				Screen.gachaType = Screen.gachaBH;
				logicGachaBH();
				e.getComponent().repaint();
			}
			else if( gachaEnhanceButt.contains(mouseX, mouseY)) {
				Screen.gameState = Value.NOTI;
				Screen.gachaType = Screen.gachaEnhance;
				logicGachaEnhance();
				e.getComponent().repaint();
			}
			else if( gachaMercenaryButt.contains(mouseX, mouseY)) {
				Screen.gameState = Value.NOTI;
				Screen.gachaType = Screen.gachaMercenary;
				logicGachaMercenary();
				e.getComponent().repaint();
			}
			else if( summonButt.contains(mouseX, mouseY)) {
				Screen.gameState = Value.NOTI;
				Screen.gachaType = Screen.summon;
				logicSummon();
				e.getComponent().repaint();
			}
		}
		else if(Screen.gameState == Value.GACHA_RATE) {
			if(!gachaRateFrame.contains(mouseX, mouseY)) {
				Screen.gameState = Value.GACHA;
				e.getComponent().repaint();
			}
		}
		else if(Screen.gameState == Value.SHARD_SHOP) {
			if(!shardShopBigFramee.contains(mouseX, mouseY)) {
				Screen.gameState = Value.GACHA;
				e.getComponent().repaint();
			}
			else if( doiBHButt.contains(mouseX, mouseY)) {
				if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockBH == false) {
					Screen.coinage = Screen.coinage - 100;
					Screen.shard = Screen.shard - 100;
					Screen.unlockBH = true;
				}
			}
			else if( doiEnhanceButt.contains(mouseX, mouseY)) {
				if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockEnhance == false) {
					Screen.coinage = Screen.coinage - 100;
					Screen.shard = Screen.shard - 100;
					Screen.unlockEnhance = true;
				}
			}
			else if( doiMercenaryButt.contains(mouseX, mouseY)) {
				if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockMercenary == false) {
					Screen.coinage = Screen.coinage - 100;
					Screen.shard = Screen.shard - 100;
					Screen.unlockMercenary = true;
				}
			}
			else if(doiECButt.contains(mouseX, mouseY)) {
				if(Screen.coinage >= 200 && Screen.shard >=200) {
					Screen.coinage = Screen.coinage - 200;
					Screen.shard = Screen.shard - 200;
					Screen.energyCore++;
				}
			}
		}
		else if(Screen.gameState == Value.NOTI) {
			if(!thongBaoFrame.contains(mouseX, mouseY)) {
				Screen.gameState = Value.GACHA;
				e.getComponent().repaint();
			}
		}
		else if(Screen.gameState == Value.VICTORY_SCREEN) {
			mouseX = e.getX();
			mouseY = e.getY();
			if(TKWtoMenu.contains(mouseX, mouseY)) {
				Screen.gameState = Value.TITLE_SCREEN;
				e.getComponent().repaint();
			}
			else if(TKWtoPlay.contains(mouseX, mouseY)) {
				Screen.startGame();
				e.getComponent().repaint();
			}
		}
		else if(Screen.gameState == Value.DEFEAT_SCREEN) {
			mouseX = e.getX();
			mouseY = e.getY();
			if(TKLtoMenu.contains(mouseX, mouseY)) {
				Screen.gameState = Value.TITLE_SCREEN;
				
				e.getComponent().repaint();
			}
			else if(TKLtoPlay.contains(mouseX, mouseY)) {
				Screen.startGame();
				
				e.getComponent().repaint();
			}
		}
		else if(Screen.gameState == Value.SETTING){
			if(musicMuteSlider.contains(mouseX, mouseY)){
				if (!isMusicMuted){
					prevMusicVolume = Screen.musicVolume;
					Screen.musicVolume = 0;
					isMusicMuted = true;
				}
				else{
					Screen.musicVolume = prevMusicVolume;
					isMusicMuted = false;
				}	
			}
			else if (sfxMuteSlider.contains(mouseX, mouseY)){
				if (!isSfxMuted){
					prevSfxVolume = Screen.sfxVolume;
					Screen.sfxVolume = 0;
					isSfxMuted = true;
				}
				else{
					Screen.sfxVolume = prevSfxVolume;
					isSfxMuted = false;
				}
			}
			else if (fullscreenTickButton.contains(mouseX, mouseY) && !Frame.isFullscreen){
				Frame.isFullscreen = true;
			}
			else if (fullscreenXButton.contains(mouseX, mouseY) && Frame.isFullscreen){
				Frame.isFullscreen = false;
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) isMousePressed = false;
		if (Screen.gameState == Value.SETTING){
			if (isSettingCancelPressed){
				isSettingCancelPressed = false;
				Screen.gameState = Value.TITLE_SCREEN;
			}
			else if (isSettingConfirmPressed){
				isSettingConfirmPressed = false;
				Screen.gameState = Value.TITLE_SCREEN;
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) isMousePressed = true;
		Screen.mse = Screen.toVirtualPoint(e.getX(), e.getY());
		Point mse = Screen.mse;
		if (Screen.gameState == Value.SETTING){
			if (musicSliderBox.contains(mse)){
				updateSlider(mse.x, musicSliderBox, true);
				if (Screen.musicVolume > 0) isMusicMuted = false;
			}
			else if (sfxSliderBox.contains(mse)){
				updateSlider(mse.x, sfxSliderBox, false);
				if (Screen.sfxVolume > 0) isSfxMuted = false;
			}
			else if (settingConfirmButton.contains(mse)){
				isSettingConfirmPressed = true;
				pressedTime = System.currentTimeMillis();
			}
			else if (settingCancelButton.contains(mse)){
				isSettingCancelPressed = true;
				pressedTime = System.currentTimeMillis();
			}
		}
		Screen.store.click(e.getButton());
	}
	
	//con vợ nào viết dòng này thì lúc nào gần xong nhớ xóa đi nhé:)))
	//thằng code gốc cực ngu
	//Hàm thì gọi lung tung hết file này đến file kia, phân bố như cứt
	//tính tọa độ thì lúc gọi frame, lúc gọi screen, loạn hết cụ lên, đọc chỉ khổ ng bảo trì

	// cập nhật tọa độ chuột liên tục, không chỉ ở màn hình in game
	public void mouseDragged(MouseEvent e) {
		Screen.mse = Screen.toVirtualPoint(e.getX(), e.getY());
		Point mse = Screen.mse;

		if (Screen.gameState == Value.SETTING){
			if (musicSliderBox.contains(mse) || 
				(mse.y >= musicSliderBox.y && 
					mse.y <= musicSliderBox.y + musicSliderBox.height)){
						updateSlider(mse.x, musicSliderBox, true);
				}
			if (sfxSliderBox.contains(mse) || 
				(mse.y >= sfxSliderBox.y && 
					mse.y <= sfxSliderBox.y + sfxSliderBox.height)){
						updateSlider(mse.x, sfxSliderBox, false);
				}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		Screen.mse = Screen.toVirtualPoint(e.getX(), e.getY());
	}

	public static boolean isHoveringButton(int mouseX, int mouseY) {
		if (Screen.gameState == Value.TITLE_SCREEN) 
			return startGame.contains(mouseX, mouseY) 
				|| store.contains(mouseX, mouseY) 
				|| quitGame.contains(mouseX, mouseY) 
				|| settings.contains(mouseX, mouseY);
		else if (Screen.gameState == Value.SHOP)
			return backz.contains(mouseX, mouseY) 
				|| toGacha.contains(mouseX, mouseY) 
				|| toBuyItem.contains(mouseX, mouseY);
		else if (Screen.gameState == Value.GACHA)
			return backz.contains(mouseX, mouseY) 
				|| in4gacha.contains(mouseX, mouseY) 
				|| iconShardShop.contains(mouseX, mouseY)
				|| gachaTornadoButt.contains(mouseX, mouseY) 
				|| gachaEnhanceButt.contains(mouseX, mouseY)
				|| gachaMercenaryButt.contains(mouseX, mouseY);
		else if (Screen.gameState == Value.SHARD_SHOP)
				return doiEnhanceButt.contains(mouseX, mouseY) 
					|| doiMercenaryButt.contains(mouseX, mouseY);
		else if (Screen.gameState == Value.VICTORY_SCREEN)
				return TKWtoMenu.contains(mouseX, mouseY) || TKWtoPlay.contains(mouseX, mouseY);
		else if (Screen.gameState == Value.DEFEAT_SCREEN)
				return TKLtoMenu.contains(mouseX, mouseY) || TKLtoPlay.contains(mouseX, mouseY);
		else if (Screen.gameState == Value.IN_GAME){
			if (Screen.store != null && Screen.store.button != null) {
				for (Rectangle btn : Screen.store.button) {
					if (btn != null && btn.contains(mouseX, mouseY)) return true;
				}
			}
			return false;
		}
		else 
			return false;
	}

	/**
	 * Hàm xử lí kéo thanh music & sfx trong SETTING.
	 */
	private void updateSlider(int mouseX, Rectangle sliderBox, boolean isMusic){
		if (sliderBox.width <= 0) return;

		double relativeX = mouseX - sliderBox.x + 8; // 8 là chiều ngang của thanh
		int level = (int) Math.round((relativeX / sliderBox.width) * 10); // 10 là số lượng thanh vẽ ra

		level = Math.max(0, Math.min(10, level));

		if (isMusic){
			Screen.musicVolume = level;
		}
		else{
			Screen.sfxVolume = level;
		}
	}

	/**
	 * Hàm xử lí logic cho Gacha Hố Đen (BlackHole)
	 */
	private void logicGachaBH() {
		if(Screen.coinage >=11) {
			Screen.coinage = Screen.coinage - 1;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;
					
			if (Screen.randomNum <=2) {
				Screen.unlockBH = true;
			}
			else if(Screen.randomNum <=5) {
				Screen.shard = Screen.shard + 50;
			}
			else if(Screen.randomNum <=10) {
				Screen.shard = Screen.shard + 20;
			}
			else if(Screen.randomNum <=20) {
				Screen.shard = Screen.shard + 10;
			}
			else if(Screen.randomNum <=30) {
				Screen.shard = Screen.shard + 5;
			}
			else if(Screen.randomNum <=45) {
				Screen.shard = Screen.shard + 3;
			}
			else if(Screen.randomNum <=65) {
				Screen.shard = Screen.shard + 2;
			}
			else {
				Screen.shard = Screen.shard + 1;
			}
		}
		else{
			Screen.ok=false;
		}
	}

	private void logicGachaEnhance() {
		if(Screen.coinage >=11) {
			Screen.coinage = Screen.coinage - 1;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;
					
			if (Screen.randomNum <=2) {
				Screen.unlockEnhance = true;
			}
			else if(Screen.randomNum <=5) {
				Screen.shard = Screen.shard + 50;
			}
			else if(Screen.randomNum <=10) {
				Screen.shard = Screen.shard + 20;
			}
			else if(Screen.randomNum <=20) {
				Screen.shard = Screen.shard + 10;
			}
			else if(Screen.randomNum <=30) {
				Screen.shard = Screen.shard + 5;
			}
			else if(Screen.randomNum <=45) {
				Screen.shard = Screen.shard + 3;
			}
			else if(Screen.randomNum <=65) {
				Screen.shard = Screen.shard + 2;
			}
			else {
				Screen.shard = Screen.shard + 1;
			}
		}
		else{
			Screen.ok=false;
		}
	}

	private void logicGachaMercenary() {
		if(Screen.coinage >=11) {
			Screen.coinage = Screen.coinage - 1;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;
					
			if (Screen.randomNum <=2) {
				Screen.unlockMercenary = true;
			}
			else if(Screen.randomNum <=5) {
				Screen.shard = Screen.shard + 50;
			}
			else if(Screen.randomNum <=10) {
				Screen.shard = Screen.shard + 20;
			}
			else if(Screen.randomNum <=20) {
				Screen.shard = Screen.shard + 10;
			}
			else if(Screen.randomNum <=30) {
				Screen.shard = Screen.shard + 5;
			}
			else if(Screen.randomNum <=45) {
				Screen.shard = Screen.shard + 3;
			}
			else if(Screen.randomNum <=65) {
				Screen.shard = Screen.shard + 2;
			}
			else {
				Screen.shard = Screen.shard + 1;
			}
		}
		else{
			Screen.ok=false;
		}
	}

	private void logicSummon() {
		if(Screen.coinage >=12) {
			Screen.coinage = Screen.coinage - 2;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;

			if (Screen.randomNum ==1) {
				Screen.energyCore++;
			}
			else if(Screen.randomNum ==2) {
				Screen.shard = Screen.shard + 100;
			}
			else if(Screen.randomNum <=5) {
				Screen.shard = Screen.shard + 50;
			}
			else if(Screen.randomNum <=20) {
				Screen.shard = Screen.shard + 35;
			}
			else if(Screen.randomNum <=40) {
				Screen.shard = Screen.shard + 20;
			}
			else if(Screen.randomNum <=60) {
				Screen.shard = Screen.shard + 10;
			}
			else if(Screen.randomNum <=80) {
				Screen.shard = Screen.shard + 5;
			}
			else {
				Screen.shard = Screen.shard + 3;
			}
		}
		else{
			Screen.ok=false;
		}
	}

	private void xuLyOutGame() {
		if(Screen.coinDauTran >=60) {
			Screen.coinage = Screen.coinDauTran - 50;
		}
		else {
			Screen.coinage = 50;
		}

		if(Screen.healthDauTran >=2 ) {
			Screen.health = Screen.healthDauTran - 1;
		}
		else {
			Screen.health = 1;
		}

	}


		// TKWtoMenu = new Rectangle(menuX, menuY, btnW, btnH);
		// TKWtoPlay = new Rectangle(playX, menuY, btnW, btnH);
		// TKLtoMenu = new Rectangle(menuX, menuY, btnW, btnH);
		// TKLtoPlay = new Rectangle(playX, menuY, btnW, btnH);
}
	

