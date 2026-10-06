

import java.awt.*;
import java.awt.event.*;

public class KeyHandel implements MouseMotionListener, MouseListener {

	Rectangle startGame = new Rectangle(500, 300, 144 , 72);
	Rectangle quitGame = new Rectangle(500, 450, 144 , 72);
	Rectangle settings = new Rectangle(5, 5, 35, 35);
	Rectangle store = new Rectangle(500, 375, 144 , 72);
	Rectangle backz = new Rectangle(5, 5, 50, 35);
	Rectangle toGacha = new Rectangle(340, 275, 100, 40);
	Rectangle toBuyItem = new Rectangle(515, 275, 100, 40);
	Rectangle in4gacha = new Rectangle(640, 20, 30, 30);
	Rectangle gachaRateFrame = new Rectangle(140, 5, 400, 555);
	Rectangle iconShardShop = new Rectangle(620, 60, 50, 50);
	Rectangle shardShopBigFramee = new Rectangle(90, 5, 500, 500);


	Rectangle gachaTornadoButt = new Rectangle(270, 250, 75, 40);
	Rectangle gachaEnhanceButt = new Rectangle(270, 345, 75, 40);
	Rectangle gachaMercenaryButt = new Rectangle(265, 450, 75, 40);
	Rectangle summonHeroButt = new Rectangle(438, 410, 96, 80);
	Rectangle thongBaoFrame = new Rectangle(168, 130, 350, 250);

	Rectangle doiTornadoButt = new Rectangle(450, 145, 70, 25);
	Rectangle doiEnhanceButt = new Rectangle(450, 210, 70, 25);
	Rectangle doiMercenaryButt = new Rectangle(450, 275, 70, 25);
	Rectangle doiGiantOrcButt = new Rectangle(450, 340, 70, 25);


	Rectangle TKWtoMenu = new Rectangle(210, 357, 125, 30);
	Rectangle TKWtoPlay = new Rectangle(352, 357, 125, 30);
	Rectangle TKLtoMenu = new Rectangle(210, 352, 125, 30);
	Rectangle TKLtoPlay = new Rectangle(352, 352, 125, 30);


	public void mouseClicked(MouseEvent e) {
		if(Screen.gameState == Screen.tileScreen) {
			int mouseX = e.getX();
        	int mouseY = e.getY();
			if(startGame.contains(mouseX, mouseY)) {
				Screen.startGame();
				e.getComponent().repaint();
			}
			else if(settings.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.settings;
			}
			else if(store.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.gameShop;
				e.getComponent().repaint();
			}else if(quitGame.contains(mouseX, mouseY)) {
				System.exit(0);
			}
		}




		else if(Screen.gameState == Screen.gameShop) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			if(backz.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.tileScreen;
				e.getComponent().repaint();
			}
			else if(toGacha.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.gacha;
			}
			else if(toBuyItem.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.buyItem;
			}
		}




		else if(Screen.gameState == Screen.gacha) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			if(backz.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.gameShop;
				e.getComponent().repaint();
			}
			else if (in4gacha.contains(mouseX, mouseY)) {
				Screen.gameState=Screen.gachaRate;
				e.getComponent().repaint();
			}
			else if( iconShardShop.contains(mouseX, mouseY)){
				Screen.gameState = Screen.shardShop;
				e.getComponent().repaint();
			}
			else if( gachaTornadoButt.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.thongBao;
				Screen.gachaType = Screen.gachaTornado;
				
				logicGachaTornado();
				
				e.getComponent().repaint();
			}
			else if( gachaEnhanceButt.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.thongBao;
				Screen.gachaType = Screen.gachaEnhance;
				logicGachaEnhance();
				e.getComponent().repaint();
			}
			else if( gachaMercenaryButt.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.thongBao;
				Screen.gachaType = Screen.gachaMercenary;
				logicGachaMercenary();
				e.getComponent().repaint();
			}
			else if( summonHeroButt.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.thongBao;
				Screen.gachaType = Screen.summonHero;
				logicSummonHero();
				e.getComponent().repaint();
			}




		}
		else if(Screen.gameState == Screen.gachaRate) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			if(!gachaRateFrame.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.gacha;
				e.getComponent().repaint();
			}
		}



		else if  (Screen.gameState ==  Screen.shardShop) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			if(!shardShopBigFramee.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.gacha;
				e.getComponent().repaint();
			}
			else if( doiTornadoButt.contains(mouseX, mouseY)) {
				if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockTornado == false) {
					Screen.coinage = Screen.coinage - 100;
					Screen.shard = Screen.shard - 100;
					Screen.unlockTornado = true;
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
			else if( doiGiantOrcButt.contains(mouseX, mouseY)) {
				if(Screen.coinage >= 250 && Screen.shard >=100  && Screen.unlockGiantOrc == false) {
					Screen.coinage = Screen.coinage - 250;
					Screen.shard = Screen.shard - 100;
					Screen.unlockGiantOrc = true;
				}
			}
			
		}




		else if(Screen.gameState == Screen.thongBao) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			if(!thongBaoFrame.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.gacha;
				e.getComponent().repaint();
			}
		}
		else if(Screen.gameState == Screen.tongKetWin) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			if(TKWtoMenu.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.tileScreen;
				e.getComponent().repaint();
			}
			else if(TKWtoPlay.contains(mouseX, mouseY)) {
				Screen.startGame();
				e.getComponent().repaint();
			}
		}
		else if(Screen.gameState == Screen.tongKetLose) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			if(TKLtoMenu.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.tileScreen;
				
				e.getComponent().repaint();
			}
			else if(TKLtoPlay.contains(mouseX, mouseY)) {
				Screen.startGame();
				
				e.getComponent().repaint();
			}
		}



		
	}

	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if(Screen.gameState == Screen.playGame) {
			Screen.store.click(e.getButton());
		}
	}
	
	
	//thằng code gốc cực ngu
	//Hàm thì gọi lung tung hết file này đến file kia, phân bố như cứt
	//tính tọa độ thì lúc gọi frame, lúc gọi screen, loạn hết cụ lên, đọc chỉ khổ ng bảo trì


	public void mouseDragged(MouseEvent e) { // kuleleri sürükleme
		if(Screen.gameState == Screen.playGame) {
			Screen.mse = new Point((e.getX()) + ((Frame.size.width - Screen.myWidth)/2), (e.getY()) + ((Frame.size.height - (Screen.myHeight))-(Frame.size.width - Screen.myWidth)/2));
		}
	}
	
	public void mouseMoved(MouseEvent e) { // kuleleri ve shoptaki slotları seçme ve görme
		if (Screen.gameState == Screen.playGame) {
			Screen.mse = new Point((e.getX()) - ((Frame.size.width - Screen.myWidth)/2), (e.getY()) - ((Frame.size.height - (Screen.myHeight))-(Frame.size.width - Screen.myWidth)/2)+50);
		}
	}





	//Hàm phụ xử lí logic, thằng nào đụng vào bố chặt tay

	private void logicGachaTornado() {
		if(Screen.coinage >=11) {
			Screen.coinage = Screen.coinage - 1;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;
					
			if (Screen.randomNum <=2) {
				Screen.unlockTornado = true;
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

	private void logicSummonHero() {
		if(Screen.coinage >=12) {
			Screen.coinage = Screen.coinage - 2;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;
					
			if (Screen.randomNum ==1) {
				Screen.unlockGiantOrc = true;
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


	
}
