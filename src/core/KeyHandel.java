

import java.awt.*;
import java.awt.event.*;

public class KeyHandel implements MouseMotionListener, MouseListener {

	Rectangle backz = new Rectangle(5, 5, 50, 35);
	Rectangle backz2 = new Rectangle (5, 700, 50, 35);
	Rectangle toGacha = new Rectangle(680, 365, 170, 50);
	Rectangle toBuyItem = new Rectangle(950, 365, 170, 50);




	Rectangle TKWtoMenu = new Rectangle(375, 495, 220, 45);
	Rectangle TKWtoPlay = new Rectangle(625, 495, 220, 45);
	Rectangle TKLtoMenu = new Rectangle(375, 487, 220, 45);
	Rectangle TKLtoPlay = new Rectangle(625, 487, 220, 45);


	public void mouseClicked(MouseEvent e) {
		Point vp = Screen.toVirtualPoint(e.getX(), e.getY());
		int mouseX = vp.x;
		int mouseY = vp.y;
		if(Screen.gameState == Screen.playGame) {
			if(backz2.contains(mouseX, mouseY)) {
				xuLyOutGame();
				Screen.gameState = Screen.tileScreen;
			}
		}

		else if(Screen.gameState == Screen.gameShop) {
			if(backz.contains(mouseX, mouseY)) {
				// Screen.gameState = Screen.tileScreen;
				// e.getComponent().repaint();
			}
			else if(toGacha.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.gacha;
			}
			else if(toBuyItem.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.buyItem;
			}
		}


		else if(Screen.gameState == Screen.gacha) {
			if(backz.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.gameShop;
				e.getComponent().repaint();
			}
			// else if (in4gacha.contains(mouseX, mouseY)) {
			// 	Screen.gameState=Screen.gachaRate;
			// 	e.getComponent().repaint();
			// }
			// else if( iconShardShop.contains(mouseX, mouseY)){
			// 	Screen.gameState = Screen.shardShop;
			// 	e.getComponent().repaint();
			// }
			// else if( gachaTornadoButt.contains(mouseX, mouseY)) {
			// 	Screen.gameState = Screen.thongBao;
			// 	Screen.gachaType = Screen.gachaBH;
			// 	logicGachaBH();
			// 	e.getComponent().repaint();
			// }
			// else if( gachaEnhanceButt.contains(mouseX, mouseY)) {
			// 	Screen.gameState = Screen.thongBao;
			// 	Screen.gachaType = Screen.gachaEnhance;
			// 	logicGachaEnhance();
			// 	e.getComponent().repaint();
			// }
			// else if( gachaMercenaryButt.contains(mouseX, mouseY)) {
			// 	Screen.gameState = Screen.thongBao;
			// 	Screen.gachaType = Screen.gachaMercenary;
			// 	logicGachaMercenary();
			// 	e.getComponent().repaint();
			// }
			// else if( summonButt.contains(mouseX, mouseY)) {
			// 	Screen.gameState = Screen.thongBao;
			// 	Screen.gachaType = Screen.summon;
			// 	logicSummon();
			// 	e.getComponent().repaint();
			// }
			




		}
		else if(Screen.gameState == Screen.gachaRate) {
			// if(!gachaRateFrame.contains(mouseX, mouseY)) {
			// 	Screen.gameState = Screen.gacha;
			// 	e.getComponent().repaint();
			// }
		}



		else if  (Screen.gameState ==  Screen.shardShop) {
			// if(!shardShopBigFramee.contains(mouseX, mouseY)) {
			// 	Screen.gameState = Screen.gacha;
			// 	e.getComponent().repaint();
			// }
			// else if( doiBHButt.contains(mouseX, mouseY)) {
			// 	if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockBH == false) {
			// 		Screen.coinage = Screen.coinage - 100;
			// 		Screen.shard = Screen.shard - 100;
			// 		Screen.unlockBH = true;
			// 	}
			// }
			// else if( doiEnhanceButt.contains(mouseX, mouseY)) {
			// 	if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockEnhance == false) {
			// 		Screen.coinage = Screen.coinage - 100;
			// 		Screen.shard = Screen.shard - 100;
			// 		Screen.unlockEnhance = true;
			// 	}
			// }
			// else if( doiMercenaryButt.contains(mouseX, mouseY)) {
			// 	if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockMercenary == false) {
			// 		Screen.coinage = Screen.coinage - 100;
			// 		Screen.shard = Screen.shard - 100;
			// 		Screen.unlockMercenary = true;
			// 	}
			// }
			// else if(doiECButt.contains(mouseX, mouseY)) {
			// 	if(Screen.coinage >= 200 && Screen.shard >=200) {
			// 		Screen.coinage = Screen.coinage - 200;
			// 		Screen.shard = Screen.shard - 200;
			// 		Screen.eCore++;
			// 	}
			// }
			
			
		}




		else if(Screen.gameState == Screen.thongBao) {
			// if(!thongBaoFrame.contains(mouseX, mouseY)) {
			// 	Screen.gameState = Screen.gacha;
			// 	e.getComponent().repaint();
			// }
		}
		else if(Screen.gameState == Screen.tongKetWin) {
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
	}

	public void mousePressed(MouseEvent e) {
		Point p = Screen.toVirtualPoint(e.getX(), e.getY());

    	if(e.getButton() != MouseEvent.BUTTON1) {
    	    return;
    	}

    	// Chốt state tại thời điểm event bắt đầu
    	int currentState = Screen.gameState;
		if(currentState == Screen.playGame) {
        	Screen.mse = p;
			Screen.store.click(e.getButton());
    	}
    	for(int i = 0; i < Screen.manageNut.countNut[currentState]; i++) {
    	    Screen.manageNut.heThongNut[currentState][i].mousePressed(p.x, p.y);
    	}
		
		
	}
	


	public void mouseDragged(MouseEvent e) {
    Point p = Screen.toVirtualPoint(e.getX(), e.getY());

    int currentState = Screen.gameState;

    // Logic riêng của playGame
    

    for(int i = 0; i < Screen.manageNut.countNut[currentState]; i++) {
        Screen.manageNut.heThongNut[currentState][i].updateMouse(p.x, p.y);
    }
}
	
	public void mouseMoved(MouseEvent e) {

    Point p = Screen.toVirtualPoint(e.getX(), e.getY());

    int currentState = Screen.gameState;

    if(currentState == Screen.playGame) {
        Screen.mse = p;
    }

    for(int i = 0;
        i < Screen.manageNut.countNut[currentState];
        i++) {

        Screen.manageNut.heThongNut[currentState][i]
            .updateMouse(p.x, p.y);
    }
}





	//Hàm phụ xử lí logic, thằng nào đụng vào bố chặt tay

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
				Screen.eCore++;
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



	
	
}
	
