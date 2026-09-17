package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandle implements MouseListener {

    GamePanel gp;

    public MouseHandle(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (gp.gameState == gp.tileState) {
            if (mouseX >= 11 * gp.tileSize && mouseX <= 15 * gp.tileSize &&
                mouseY >= 7 * gp.tileSize && mouseY <= 9 * gp.tileSize) {
                gp.gameState = gp.playState;
            }
            else if (mouseX >= 11 * gp.tileSize && mouseX <= 15 * gp.tileSize &&
                     mouseY >= 9 * gp.tileSize && mouseY <= 11 * gp.tileSize) {
                System.exit(0);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}

   
