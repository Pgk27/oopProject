package main;

import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import tile.TileManager;


public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16; // 1 title = 16x16 ô vẽ
    final int scale = 3;  // 1 ô vẽ = 3 pixel chuẩn

    public final int tileSize = originalTileSize * scale; //1 title = 48x48 pixel chuẩn
    public final int maxScreenCol = 16; //ngang 16 tile
    public final int maxScreenRow = 12; // dọc 12 tile
    public final int screenWidth = tileSize * maxScreenCol; //ngang 960 pixel chuẩn  
    public final int screenHeight = tileSize * maxScreenRow;  // dọc 576 pixel chuẩn


    TileManager tileM = new TileManager(this);
    KeyHandle keyH = new KeyHandle();
    Thread gameThread;
    Player player = new Player(this, keyH);

    public int gameState=0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int tileState = 0;

    



    public GamePanel() 
    {
        this.gameState = tileState;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.addMouseListener(new MouseHandle(this));
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override 
    public void run() {
        double drawInterval = 1000000000/60; // 0.01666666667 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

    }

    public void update() {
       
        player.update();

    }














    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; //Up con g lên g2 xịn hơn

        if (gameState == playState) {
            tileM.draw(g2);
            player.draw(g2);
        } else if (gameState == tileState) {
            tileM.draw(g2);
        }

        g2.dispose();
    } 
}
