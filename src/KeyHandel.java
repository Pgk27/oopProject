

import java.awt.*;
import java.awt.event.*;

public class KeyHandel implements MouseMotionListener, MouseListener {

	private Point getLogicalPoint(MouseEvent e) {
		double scaleX = (double) Screen.LOGICAL_WIDTH / e.getComponent().getWidth();
		double scaleY = (double) Screen.LOGICAL_HEIGHT / e.getComponent().getHeight();
		return new Point((int)(e.getX() * scaleX), (int)(e.getY() * scaleY));
	}

	Rectangle startGame = new Rectangle(500, 300, 144 , 72);
	Rectangle quitGame = new Rectangle(500, 450, 144 , 72);
	Rectangle settings = new Rectangle(5, 5, 35, 35);
	Rectangle store = new Rectangle(500, 375, 144 , 72);
	Rectangle backz = new Rectangle(10, 35, 50, 35);
	public void mouseClicked(MouseEvent e) {
		Point p = getLogicalPoint(e);
		int mouseX = p.x;
		int mouseY = p.y;
		if(Screen.gameState == Screen.tileScreen) {
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
		Screen.mse = getLogicalPoint(e);
		if(Screen.gameState == Screen.playGame) {
			Screen.store.click(e.getButton());
		}
	}
	
	
	public void mouseDragged(MouseEvent e) { // kuleleri sürükleme
		if(Screen.gameState == Screen.playGame) {
			Screen.mse = getLogicalPoint(e);
		}
	}
	

	public void mouseMoved(MouseEvent e) { // kuleleri ve shoptaki slotları seçme ve görme
		if (Screen.gameState == Screen.playGame) {
			Screen.mse = getLogicalPoint(e);
		}
	}
	
}
