

import java.awt.*;
import java.awt.event.*;

public class KeyHandel implements MouseMotionListener, MouseListener {

	Rectangle startGame = new Rectangle(500, 300, 144 , 72);
	Rectangle quitGame = new Rectangle(500, 450, 144 , 72);
	Rectangle settings = new Rectangle(5, 5, 35, 35);
	Rectangle store = new Rectangle(500, 375, 144 , 72);
	Rectangle backz = new Rectangle(10, 35, 50, 35);
	public void mouseClicked(MouseEvent e) {
		if(Screen.gameState == Screen.tileScreen) {
			int mouseX = e.getX();
        	int mouseY = e.getY();
			if(startGame.contains(mouseX, mouseY)) {
				Screen.gameState = Screen.playGame;
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
	
	
	public void mouseDragged(MouseEvent e) { // kuleleri sürükleme
		if(Screen.gameState == Screen.playGame) {
			Screen.mse = new Point((e.getX()) + ((Frame.size.width - Screen.myWidth)/2), (e.getY()) + ((Frame.size.height - (Screen.myHeight))-(Frame.size.width - Screen.myWidth)/2));
		}
	}
	

	public void mouseMoved(MouseEvent e) { // kuleleri ve shoptaki slotları seçme ve görme
		if (Screen.gameState == Screen.playGame) {
			Screen.mse = new Point((e.getX()) - ((Frame.size.width - Screen.myWidth)/2), (e.getY()) - ((Frame.size.height - (Screen.myHeight))-(Frame.size.width - Screen.myWidth)/2));
		}
	}
	



}
