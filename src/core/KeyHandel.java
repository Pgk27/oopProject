package core;

import java.awt.*;
import java.awt.event.*;

public class KeyHandel implements MouseMotionListener, MouseListener {
	static boolean isMousePressed = false;

	static boolean isMusicMuted = false;
	static boolean isSfxMuted = false;
	static boolean isSettingCancelPressed = false;
	static boolean isSettingConfirmPressed = false;
	static boolean isUpdated = false;

	static boolean prevMusicMuteState = isMusicMuted;
	static boolean prevSfxMuteState = isSfxMuted;
	static int prevMusicVolume = Screen.musicVolume;
	static int prevSfxVolume = Screen.sfxVolume;
	
	static int currMusicVolume = Screen.musicVolume;
	static int currSfxVolume = Screen.sfxVolume;

	static Rectangle backz = new Rectangle(5, 5, 50, 35);
	static Rectangle backz2 = new Rectangle (5, 700, 50, 35);
	static Rectangle toGacha = new Rectangle(680, 365, 170, 50);
	static Rectangle toBuyItem = new Rectangle(950, 365, 170, 50);

	static Rectangle TKWtoMenu = new Rectangle(375, 495, 220, 45);
	static Rectangle TKWtoPlay = new Rectangle(625, 495, 220, 45);
	static Rectangle TKLtoMenu = new Rectangle(375, 487, 220, 45);
	static Rectangle TKLtoPlay = new Rectangle(625, 487, 220, 45);

	static Rectangle gachaTornadoButt = new Rectangle(500, 345,  85, 50);
	static Rectangle gachaEnhanceButt = new Rectangle(500, 475,  85, 50);
	static Rectangle gachaMercenaryButt = new Rectangle(500, 610,  90, 53);
	static Rectangle summonButt = new Rectangle(800, 545, 125, 104);
	static Rectangle thongBaoFrame = new Rectangle(360, 140, 500, 400);

	static Rectangle startGame = new Rectangle(950, 450, 144, 72);
	static Rectangle quitGame = new Rectangle(950, 590, 144, 72);
	static Rectangle settings = new Rectangle(5, 5, 35, 35);
	static Rectangle store = new Rectangle(950, 520, 144, 72);
	static Rectangle in4gacha = new Rectangle(1110, 20, 40, 40);
	static Rectangle gachaRateFrame = new Rectangle(300, 5, 600, 750);
	static Rectangle iconShardShop = new Rectangle(1100, 80, 65, 65);
	static Rectangle shardShopBigFramee = new Rectangle(160, 30, 900, 680);

	static Rectangle doiBHButt = new Rectangle(670, 210, 95, 30);
	static Rectangle doiEnhanceButt = new Rectangle(670, 290, 95, 30);
	static Rectangle doiMercenaryButt = new Rectangle(670, 370, 95, 30);
	static Rectangle doiECButt = new Rectangle(670, 450, 95, 30);

	static Rectangle musicSliderBox = new Rectangle();
	static Rectangle musicMuteSlider = new Rectangle(735, 240, 100, 100);
	static Rectangle sfxSliderBox = new Rectangle();
	static Rectangle sfxMuteSlider = new Rectangle(735, 340, 100, 100);
	static Rectangle fullscreenTickButton = new Rectangle(585, 465, 72, 80);
	static Rectangle fullscreenXButton = new Rectangle(685, 465, 72, 80);
	static Rectangle settingCancelButton = new Rectangle(350, 645, 240, 70);
	static Rectangle settingConfirmButton = new Rectangle(630, 645, 240, 70);


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
		}
		else if(Screen.gameState == Value.GACHA_RATE) {
		}
		else if(Screen.gameState == Value.SHARD_SHOP) {

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
					currMusicVolume = Screen.musicVolume;
					Screen.musicVolume = 0;
					isMusicMuted = true;
				}
				else{
					Screen.musicVolume = currMusicVolume;
					isMusicMuted = false;
				}	
				Screen.sound.setMusicVolume(Screen.musicVolume, true);
			}
			else if (sfxMuteSlider.contains(mouseX, mouseY)){
				if (!isSfxMuted){
					currSfxVolume = Screen.sfxVolume;
					Screen.sfxVolume = 0;
					isSfxMuted = true;
				}
				else{
					Screen.sfxVolume = currSfxVolume;
					isSfxMuted = false;
				}
				Screen.sound.setSfxVolume(Screen.sfxVolume);
			}
			else if (fullscreenTickButton.contains(mouseX, mouseY) && !Frame.isFullscreen) {
				Frame.setFullscreen(true);
				e.getComponent().repaint();
			}
			else if (fullscreenXButton.contains(mouseX, mouseY) && Frame.isFullscreen) {
				Frame.setFullscreen(false);
				e.getComponent().repaint();
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
		Point p = Screen.toVirtualPoint(e.getX(), e.getY());

    	if(e.getButton() != MouseEvent.BUTTON1) {
    	    return;
    	}

    	// Chốt state tại thời điểm event bắt đầu
    	int currentState = Screen.gameState;

    	for(int i = 0;
    	    i < Screen.manageNut.countNut[currentState]; i++) {
        	Screen.manageNut.heThongNut[currentState][i].mouseReleased(p.x, p.y);
		}
		if (e.getButton() == MouseEvent.BUTTON1) isMousePressed = false;
		if (Screen.gameState == Value.SETTING){
			// // instant = true --> nhạc to nhỏ ngay tức thì, không bị trễ một giây mới update như trước
			// if (musicSliderBox.contains(mse)){
			// 	Screen.sound.setMusicVolume(Screen.musicVolume, true);
			// }
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
		Point p = Screen.toVirtualPoint(e.getX(), e.getY());
		int currentState = Screen.gameState;
		Screen.mse = p;
		Screen.store.click(e.getButton());
		
		for(int i = 0; i < Screen.manageNut.countNut[currentState]; i++) {
    	    Screen.manageNut.heThongNut[currentState][i].mousePressed(p.x, p.y);
    	}
		if (currentState == Value.SETTING){
			if (musicSliderBox.contains(p.x, p.y)){
				updateSlider(p.x, musicSliderBox, true);
				if (Screen.musicVolume > 0) isMusicMuted = false;
			}
			else if (sfxSliderBox.contains(p.x, p.y)){
				updateSlider(p.x, sfxSliderBox, false);
				if (Screen.sfxVolume > 0) isSfxMuted = false;
			}
			else if (settingConfirmButton.contains(p)){
				// update giá trị trước (prev) nếu confirm
				prevMusicMuteState = isMusicMuted;
				prevSfxMuteState = isSfxMuted;

				prevMusicVolume = Screen.musicVolume;
				prevSfxVolume = Screen.sfxVolume;
				isSettingConfirmPressed = true;
			}
			else if (settingCancelButton.contains(p.x, p.y)){
				// trao trả các giá trị về giá trị ban đầu
				isMusicMuted = (isMusicMuted == prevMusicMuteState) ? isMusicMuted : !isMusicMuted;
				isSfxMuted = (isSfxMuted == prevSfxMuteState) ? isSfxMuted : !isSfxMuted;

				Screen.musicVolume = prevMusicVolume;
				Screen.sfxVolume = prevSfxVolume;
				Screen.sound.setMusicVolume(prevMusicVolume);
				Screen.sound.setSfxVolume(prevSfxVolume);
				isSettingCancelPressed = true;
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
		Point p = Screen.toVirtualPoint(e.getX(), e.getY());
		int currentState = Screen.gameState;
		for(int i = 0; i < Screen.manageNut.countNut[currentState]; i++) {
			Screen.manageNut.heThongNut[currentState][i].updateMouse(p.x, p.y);
		}

		Screen.mse = p;

		if (currentState == Value.SETTING){
			if (musicSliderBox.contains(p.x, p.y) || 
				(p.y >= musicSliderBox.y && 
					p.y <= musicSliderBox.y + musicSliderBox.height)){
						updateSlider(p.x, musicSliderBox, true);
				}
			if (sfxSliderBox.contains(p.x, p.y) || 
				(p.y >= sfxSliderBox.y && 
					p.y <= sfxSliderBox.y + sfxSliderBox.height)){
						updateSlider(p.x, sfxSliderBox, false);
				}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		Point p = Screen.toVirtualPoint(e.getX(), e.getY());

		int currentState = Screen.gameState;

		Screen.mse = p;

		for(int i = 0; i < Screen.manageNut.countNut[currentState]; i++) {
			Screen.manageNut.heThongNut[currentState][i].updateMouse(p.x, p.y);
		}
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
			Screen.sound.setMusicVolume(level, false);
		}
		else{
			Screen.sfxVolume = level;
			Screen.sound.setSfxVolume(level);
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
}
