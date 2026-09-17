package main;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class main {
    public static void main (String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("tluc is sleeping");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();
        BufferedImage taskBarIcon = null;
        try {
            taskBarIcon = ImageIO.read(main.class.getResourceAsStream("/res/tiles/iconTaskbar4.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        window.setIconImage(taskBarIcon);


    }

}
