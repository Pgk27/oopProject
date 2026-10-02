import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;
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

    public Font pixelFontv1;
    public Font pixelFontv2;
    public Font pixelFontvSmall;
    public Font pixelFontMini;
    public Font pixelFontMedi;


    public Tiles() {
        try {
            tileScreen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/game_tilee.png"));
            newGameButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/playGame2.png"));
            backButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/backk3.png"));
            quitGameButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/quitgame2.png"));
            settingsButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/setting_icon2.png")); 
            storeButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/store2.png"));
            storeTile = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/storeTile.png"));
            storeOptions = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/storeOptionVip2.png"));
            gachaButton = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/GachaButtonV2_3.png"));
            gachaTile = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/GachaScreen1.png")); 



            gachaButt = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/GachaButt3.png"));
            TornadoC = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/TornadoC3.png"));
            MercenaryC = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/MercenaryC3.png"));
            EnhanceC = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/EnhanceC3.png"));
            summonHeroButt= ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/SummonH3.png"));
            in4Butt = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/infoButt3.png"));
            gachaInfoFrame = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/gachaInfoFrame3.png"));
            gachaRateText = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/GachaRateText2.png"));
            iconShardShop = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/iconShardShop3.png"));
            mysteryShard = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/mysteryShard3.png"));
            shardShopBigFrame = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/shardShopBigFrame3.png"));
            awakeningText = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/awakeText4.png"));

            iconTornado = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/iconTornado1.png"));
            iconEnhance = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/iconEnhance1.png"));
            iconMercenary = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/iconMercenary1.png"));
            iconFrame = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/iconFrame4.png"));
            dauCong = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/daucong2.png"));
            muiTen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/muiten2.png"));
            iconCoin = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/iconCoin.png"));
            iconGiantOrc = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/iconGiantOrc2.png"));
            congra = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/congra3.png"));
            oopss = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/oopss3.png"));

            awaButtV1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/awaButtV1.png"));
            awaButtV2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/awaButtV2.png"));
            awaButtV3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tileImage/awaButtV3.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }





        try (InputStream fontStream = getClass().getClassLoader().getResourceAsStream("font/SVN-Determination Sans.ttf")) {
            if (fontStream == null) {
                throw new IllegalStateException("Không tìm thấy font trong tileImage");
            }

            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
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