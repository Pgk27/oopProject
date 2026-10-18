import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.Buffer;

import javax.imageio.ImageIO;

public class Tiles {
    public BufferedImage tileScreen;
    public BufferedImage newGameButton;
    public BufferedImage quitGameButton;
    public BufferedImage settingsButton;
    public BufferedImage backButton;
    public BufferedImage storeButton;
    public BufferedImage storeTile;
    public BufferedImage storeOptions;
    public BufferedImage gachaButton;
    public BufferedImage gachaTile;
    public BufferedImage gachaButt;
    public BufferedImage BlackHoleC;
    public BufferedImage MercenaryC;
    public BufferedImage EnhanceC;
    public BufferedImage summonHeroButt;
    public BufferedImage in4Butt;
    public BufferedImage gachaInfoFrame;
    public BufferedImage gachaRateText;
    public BufferedImage iconShardShop;
    public BufferedImage mysteryShard;
    public BufferedImage shardShopBigFrame;
    public BufferedImage awakeningText;

    public BufferedImage iconBH;
    public BufferedImage iconEnhance;
    public BufferedImage iconMercenary;
    public BufferedImage iconFrame;
    public BufferedImage dauCong;
    public BufferedImage muiTen;
    public BufferedImage iconCoin;
    public BufferedImage iconCore;

    public BufferedImage congra;
    public BufferedImage oopss;
    public BufferedImage awaButtV1;
    public BufferedImage awaButtV2;
    public BufferedImage awaButtV3;
    public BufferedImage purchaseSang;
    public BufferedImage purchaseToi;

    public BufferedImage tongKetWin;
    public BufferedImage tongKetLose;

    static BufferedImage button;
	static BufferedImage touchedButton;

    public Font pixelFontv1;
    public Font pixelFontv2;
    public Font pixelFontvSmall;
    public Font pixelFontMini;
    public Font pixelFontMedi;


    public Color darkSilver;
    public Color royalGold;
    public Color purpleHole;
    public Color enhanceRed;
    public Color mercBlue;
    public Color gloomyPurple;
    




    private BufferedImage readImage(String relativePath) throws Exception {
        File file = new File("src/" + relativePath);
        if (!file.exists()) {
            file = new File(relativePath);
        }
        if (!file.exists()) {
            throw new FileNotFoundException("Missing asset: " + relativePath);
        }
        return ImageIO.read(file);
    }

    public Tiles() {
        try {
            tileScreen = readImage("res/tileImage/game_tilee.png");
            newGameButton = readImage("res/tileImage/playGame2.png");
            backButton = readImage("res/tileImage/backk3.png");
            quitGameButton = readImage("res/tileImage/quitgame2.png");
            settingsButton = readImage("res/tileImage/setting_icon2.png");
            storeButton = readImage("res/tileImage/store2.png");
            storeTile = readImage("res/tileImage/storeTile.png");
            storeOptions = readImage("res/tileImage/storeOptionVip4.png");
            gachaButton = readImage("res/tileImage/GachaButtonV2_3.png");
            gachaTile = readImage("res/tileImage/GachaScreen2.png");

            gachaButt = readImage("res/tileImage/GachaButt3.png");
            BlackHoleC = readImage("res/tileImage/BHC4.png");
            MercenaryC = readImage("res/tileImage/MercenaryC3.png");
            EnhanceC = readImage("res/tileImage/EnhanceC3.png");
            summonHeroButt = readImage("res/tileImage/SummonH3.png");
            in4Butt = readImage("res/tileImage/infoButt3.png");
            gachaInfoFrame = readImage("res/tileImage/gachaInfoFrame3.png");
            gachaRateText = readImage("res/tileImage/GachaRateText2.png");
            iconShardShop = readImage("res/tileImage/iconShardShop3.png");
            mysteryShard = readImage("res/tileImage/mysteryShard3.png");
            shardShopBigFrame = readImage("res/tileImage/shardShopBigFrame3.png");
            awakeningText = readImage("res/tileImage/awakeText4.png");

            iconBH = readImage("res/tileImage/iconBH.png");
            iconEnhance = readImage("res/tileImage/iconEnhance1.png");
            iconMercenary = readImage("res/tileImage/iconMercenary1.png");
            dauCong = readImage("res/tileImage/daucong2.png");
            muiTen = readImage("res/tileImage/muiten2.png");
            iconCoin = readImage("res/tileImage/coin4.png");
            iconCore = readImage("res/tileImage/core1.png");
            congra = readImage("res/tileImage/congra3.png");
            oopss = readImage("res/tileImage/oopss6.png");

            awaButtV1 = readImage("res/tileImage/awaken.png");
            awaButtV2 = readImage("res/tileImage/awaButtV2.png");
            awaButtV3 = readImage("res/tileImage/awaButtV3.png");
            purchaseSang = readImage("res/tileImage/purchaseSang.png");
            purchaseToi = readImage("res/tileImage/purchaseToi.png");

            tongKetWin = readImage("res/tileImage/tongKetWin4.png");
            tongKetLose = readImage("res/tileImage/tongKetLose2.png");

            button = readImage("res/ui/button.png");
		    touchedButton = readImage("res/ui/touchedButton.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    

        try {
            File fontFile = new File("res/font/SVN-Determination Sans.ttf");
            if (!fontFile.exists()) {
                throw new FileNotFoundException("Missing font: res/font/SVN-Determination Sans.ttf");
            }

            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.FileInputStream(fontFile));
            pixelFontv1 = baseFont.deriveFont(16f);
            pixelFontv2 = baseFont.deriveFont(22f);
            pixelFontvSmall = baseFont.deriveFont(13f);
            pixelFontMini = baseFont.deriveFont(11f);
            pixelFontMedi = baseFont.deriveFont(24f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        darkSilver = Color.decode("#BFC7D5");
        enhanceRed = Color.decode("#FF4444");
        mercBlue = Color.decode("#3399FF");
        royalGold = Color.decode("#FFBF00");
        gloomyPurple = Color.decode("#8A2BE2");
    }
}