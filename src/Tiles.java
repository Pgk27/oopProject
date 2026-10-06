import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
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
    public BufferedImage TornadoC;
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

    public BufferedImage iconTornado;
    public BufferedImage iconEnhance;
    public BufferedImage iconMercenary;
    public BufferedImage iconFrame;
    public BufferedImage dauCong;
    public BufferedImage muiTen;
    public BufferedImage iconCoin;
    public BufferedImage iconGiantOrc;

    public BufferedImage congra;
    public BufferedImage oopss;
    public BufferedImage awaButtV1;
    public BufferedImage awaButtV2;
    public BufferedImage awaButtV3;

    public BufferedImage tongKetWin;
    public BufferedImage tongKetLose;

    public Font pixelFontv1;
    public Font pixelFontv2;
    public Font pixelFontvSmall;
    public Font pixelFontMini;
    public Font pixelFontMedi;


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
            tileScreen = readImage("tileImage/game_tilee.png");
            newGameButton = readImage("tileImage/playGame2.png");
            backButton = readImage("tileImage/backk3.png");
            quitGameButton = readImage("tileImage/quitgame2.png");
            settingsButton = readImage("tileImage/setting_icon2.png");
            storeButton = readImage("tileImage/store2.png");
            storeTile = readImage("tileImage/storeTile.png");
            storeOptions = readImage("tileImage/storeOptionVip2.png");
            gachaButton = readImage("tileImage/GachaButtonV2_3.png");
            gachaTile = readImage("tileImage/GachaScreen1.png");

            gachaButt = readImage("tileImage/GachaButt3.png");
            TornadoC = readImage("tileImage/TornadoC3.png");
            MercenaryC = readImage("tileImage/MercenaryC3.png");
            EnhanceC = readImage("tileImage/EnhanceC3.png");
            summonHeroButt = readImage("tileImage/SummonH3.png");
            in4Butt = readImage("tileImage/infoButt3.png");
            gachaInfoFrame = readImage("tileImage/gachaInfoFrame3.png");
            gachaRateText = readImage("tileImage/GachaRateText2.png");
            iconShardShop = readImage("tileImage/iconShardShop3.png");
            mysteryShard = readImage("tileImage/mysteryShard3.png");
            shardShopBigFrame = readImage("tileImage/shardShopBigFrame3.png");
            awakeningText = readImage("tileImage/awakeText4.png");

            iconTornado = readImage("tileImage/iconTornado1.png");
            iconEnhance = readImage("tileImage/iconEnhance1.png");
            iconMercenary = readImage("tileImage/iconMercenary1.png");
            iconFrame = readImage("tileImage/iconFrame4.png");
            dauCong = readImage("tileImage/daucong2.png");
            muiTen = readImage("tileImage/muiten2.png");
            iconCoin = readImage("tileImage/iconCoin.png");
            iconGiantOrc = readImage("tileImage/iconGiantOrc2.png");
            congra = readImage("tileImage/congra3.png");
            oopss = readImage("tileImage/oopss3.png");

            awaButtV1 = readImage("tileImage/awaButtV1.png");
            awaButtV2 = readImage("tileImage/awaButtV2.png");
            awaButtV3 = readImage("tileImage/awaButtV3.png");

            tongKetWin = readImage("tileImage/tongKetWin4.png");
            tongKetLose = readImage("tileImage/tongKetLose2.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File fontFile = new File("src/font/SVN-Determination Sans.ttf");
            if (!fontFile.exists()) {
                throw new FileNotFoundException("Missing font: src/font/SVN-Determination Sans.ttf");
            }

            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.FileInputStream(fontFile));
            pixelFontv1 = baseFont.deriveFont(13f);
            pixelFontv2 = baseFont.deriveFont(20f);
            pixelFontvSmall = baseFont.deriveFont(10f);
            pixelFontMini = baseFont.deriveFont(6f);
            pixelFontMedi = baseFont.deriveFont(16f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}