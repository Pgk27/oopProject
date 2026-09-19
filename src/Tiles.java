import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Tiles {
    public BufferedImage tileScreen;
    public BufferedImage newGameButton;
    public BufferedImage quitGameButton;
    public BufferedImage settingsButton;
    public BufferedImage backButton;
    public BufferedImage storeButton;
    public BufferedImage storeTile;

    public Tiles() {
        try {
            tileScreen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/game_tilee.png"));
            newGameButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/playGame2.png"));
            backButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/backk3.png"));
            quitGameButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/quitgame2.png"));
            settingsButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/setting_icon2.png")); 
            storeButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/store2.png"));
            storeTile = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/storeTile.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}