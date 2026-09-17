package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandle;

public class Player extends Entity {
    GamePanel gp;
    KeyHandle keyH;

    public BufferedImage r1, r2, r3, r4, r5, r6;
    public BufferedImage l1, l2, l3, l4, l5, l6;
    public String direction;

    public int spriteCouter=0;
    public int spriteNum=1;

    public Player(GamePanel gp, KeyHandle keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues() {
        x = 100;
        y = 450;
        speed = 4;
        direction = "right";
    }

    public void getPlayerImage() {


        try {
            r1=ImageIO.read(getClass().getResourceAsStream("/res/player/k1.png"));
            r2=ImageIO.read(getClass().getResourceAsStream("/res/player/k2.png"));
            r3=ImageIO.read(getClass().getResourceAsStream("/res/player/k3.png"));
            r4=ImageIO.read(getClass().getResourceAsStream("/res/player/k4.png"));
            r5=ImageIO.read(getClass().getResourceAsStream("/res/player/k5.png"));
            r6=ImageIO.read(getClass().getResourceAsStream("/res/player/k6.png"));

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void update() {
        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {

            if (keyH.upPressed == true) {
                y -= speed;
            } else if (keyH.downPressed == true) {
                y += speed;
            } else if (keyH.leftPressed == true) {
                direction = "left";
                x -= speed;
            } else if (keyH.rightPressed == true) {
                    direction = "right";
                x += speed;
            }

            spriteCouter++;
            if (spriteCouter > 10) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                    spriteNum = 4;
                } else if (spriteNum == 4) {
                    spriteNum = 5;
                } else if (spriteNum == 5) {
                    spriteNum = 6;
                } else if (spriteNum == 6) {
                    spriteNum = 1;
                }
            spriteCouter = 0;
            }
        }
    }
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        if (spriteNum == 1) {
            image = r1;
        } else if (spriteNum == 2) {
            image = r2;
        } else if (spriteNum == 3) {
            image = r3;
        } else if (spriteNum == 4) {
            image = r4;
        } else if (spriteNum == 5) {
            image = r5;
        } else if (spriteNum == 6) {
            image = r6;
        }
        
        int size = gp.tileSize + gp.tileSize/2; 

        if (direction.equals("right")) {
            g2.drawImage(image, x, y, size, size, null);
        } else if (direction.equals("left")) {
            g2.drawImage(image, x + size, y, x, y + size, 0, 0, 24, 24, null);
        }   
    }

}
