import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;

public class Tiles {
    public BufferedImage tileScreen;

    

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
    public BufferedImage awakeT;
    public BufferedImage awaken;
    public BufferedImage purchaseT;


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

    public BufferedImage tileUpdateHero;
    public BufferedImage upHeroFrame;
    public BufferedImage nenExpBlack;
    public BufferedImage nenExpGreen;
    public BufferedImage expBar;
    public BufferedImage maxLevel;
    public BufferedImage levelUpT;
    public BufferedImage addPoint;
    public BufferedImage addPointS;
    public BufferedImage addPointT;
    public BufferedImage max;


    public BufferedImage tongKetWin;
    public BufferedImage tongKetLose;
    public BufferedImage tileBuy;

    public BufferedImage[] mapDanhNhau = new BufferedImage[4];

    public Font pixelFontv1;
    public Font pixelFontv2;
    public Font pixelFontvSmall;
    public Font pixelFontHeroRequire;
    public Font pixelFontMedi;


    public Color darkSilver;
    public Color royalGold;
    public Color purpleHole;
    public Color enhanceRed;
    public Color mercBlue;
    public Color timMaMi;

    public Color okOK;
    public Color notOK;
    
    




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
            // gs tileScreen
            Screen.manageNut.playgame.ima[0] = readImage("tileImage/playgame.png");
            Screen.manageNut.playgame.ima[1] = readImage("tileImage/playgameS.png");
          
            Screen.manageNut.store.ima[0] = readImage("tileImage/store.png");
            Screen.manageNut.store.ima[1] = readImage("tileImage/storeS.png");

            Screen.manageNut.updateHero.ima[0] = readImage("tileImage/updateHero.png");
            Screen.manageNut.updateHero.ima[1] = readImage("tileImage/updateHeroS.png");


            Screen.manageNut.quitgame.ima[0] = readImage("tileImage/quitgame.png");
            Screen.manageNut.quitgame.ima[1] = readImage("tileImage/quitgameS.png");

            //gs gameShop

            Screen.manageNut.backInGameShop.ima[0]= readImage("tileImage/back.png");
            Screen.manageNut.backInGameShop.ima[1]= readImage("tileImage/backS.png");

            Screen.manageNut.backInGacha.ima[0]= readImage("tileImage/back.png");
            Screen.manageNut.backInGacha.ima[1]= readImage("tileImage/backS.png");

            Screen.manageNut.gachaBH.ima[0]= readImage("tileImage/gacha.png");
            Screen.manageNut.gachaBH.ima[1]= readImage("tileImage/gachaS.png");

            Screen.manageNut.gachaEnhance.ima[0]= readImage("tileImage/gacha.png");
            Screen.manageNut.gachaEnhance.ima[1]= readImage("tileImage/gachaS.png");

            Screen.manageNut.gachaMer.ima[0]= readImage("tileImage/gacha.png");
            Screen.manageNut.gachaMer.ima[1]= readImage("tileImage/gachaS.png");

            Screen.manageNut.summon.ima[0] = readImage("tileImage/summon.png");
            Screen.manageNut.summon.ima[1] = readImage("tileImage/summonS.png");
            
            
            


            Screen.manageNut.doiBH.ima[0]= readImage("tileImage/awake.png");
            Screen.manageNut.doiBH.ima[1]= readImage("tileImage/awakeS.png");

            Screen.manageNut.doiEnhance.ima[0]= readImage("tileImage/awake.png");
            Screen.manageNut.doiEnhance.ima[1]= readImage("tileImage/awakeS.png");

            Screen.manageNut.doiMer.ima[0]= readImage("tileImage/awake.png");
            Screen.manageNut.doiMer.ima[1]= readImage("tileImage/awakeS.png");

            Screen.manageNut.doiEC.ima[0] = readImage("tileImage/purchase.png");
            Screen.manageNut.doiEC.ima[1] = readImage("tileImage/purchaseS.png");

            Screen.manageNut.levelUp.ima[0] = readImage("tileImage/levelUp.png");
            Screen.manageNut.levelUp.ima[1] = readImage("tileImage/levelUpS.png");

            Screen.manageNut.expBar.ima[0] = readImage("tileImage/expBar.png");
            Screen.manageNut.expBar.ima[1] = readImage("tileImage/expBarS.png");



           
            purchaseT = readImage("tileImage/purchaseT.png");
            awakeT = readImage("tileImage/awakeT.png");
            awaken = readImage("tileImage/awaken.png");
            maxLevel = readImage("tileImage/maxLevel.png");
            levelUpT = readImage("tileImage/levelUpT.png");

            addPoint = readImage("tileImage/addPoint.png");
            addPointS = readImage("tileImage/addPointS.png");
            addPointT = readImage("tileImage/addPointT.png");
            max = readImage("tileImage/max.png");



            


            Screen.manageNut.thongBaoFrame.ima[0] = readImage("tileImage/awake.png");








            backButton = readImage("tileImage/back.png");
            storeTile = readImage("tileImage/storeTile.png");
            storeOptions = readImage("tileImage/storeOptionVip4.png");
            gachaButton = readImage("tileImage/GachaButtonV2_3.png");
            gachaTile = readImage("tileImage/GachaScreen2.png");

            BlackHoleC = readImage("tileImage/BHC4.png");
            MercenaryC = readImage("tileImage/MercenaryC3.png");
            EnhanceC = readImage("tileImage/EnhanceC3.png");
            in4Butt = readImage("tileImage/infoButt3.png");
            gachaInfoFrame = readImage("tileImage/gachaInfoFrame3.png");
            gachaRateText = readImage("tileImage/GachaRateText2.png");
            iconShardShop = readImage("tileImage/iconShardShop3.png");
            mysteryShard = readImage("tileImage/mysteryShard3.png");
            shardShopBigFrame = readImage("tileImage/shardShopBigFrame3.png");
            awakeningText = readImage("tileImage/awakeText4.png");

            iconBH = readImage("tileImage/iconBH.png");
            iconEnhance = readImage("tileImage/iconEnhance1.png");
            iconMercenary = readImage("tileImage/iconMercenary1.png");
            dauCong = readImage("tileImage/daucong2.png");
            muiTen = readImage("tileImage/muiten2.png");
            iconCoin = readImage("tileImage/coin4.png");
            iconCore = readImage("tileImage/core1.png");
            congra = readImage("tileImage/congra3.png");
            oopss = readImage("tileImage/oopss6.png");

            tileUpdateHero = readImage("tileImage/tileUpdateHero.png");
            nenExpBlack = readImage("tileImage/nenExpBlack.png");
            nenExpGreen = readImage("tileImage/nenExpGreen.png");
            expBar = readImage("tileImage/expBar.png");



            tongKetWin = readImage("tileImage/tongKetWin4.png");
            tongKetLose = readImage("tileImage/tongKetLose2.png");
            tileBuy = readImage("tileImage/tileBuyItem1.png");

            mapDanhNhau[3] = readImage("tileImage/mapMa1.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    

        try {
            File fontFile = new File("src/font/SVN-Determination Sans.ttf");
            if (!fontFile.exists()) {
                throw new FileNotFoundException("Missing font: src/font/SVN-Determination Sans.ttf");
            }

            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.FileInputStream(fontFile));
            pixelFontv1 = baseFont.deriveFont(16f);
            pixelFontv2 = baseFont.deriveFont(22f);
            pixelFontHeroRequire = baseFont.deriveFont(20f);
            pixelFontvSmall = baseFont.deriveFont(13f);
            pixelFontMedi = baseFont.deriveFont(24f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        darkSilver = Color.decode("#BFC7D5");
        enhanceRed = Color.decode("#FF4444");
        mercBlue = Color.decode("#3399FF");
        royalGold = Color.decode("#FFBF00");
        timMaMi = Color.decode("#8A2BE2");
        okOK = Color.decode("#4ADE80");
        notOK = Color.decode("#FF0F0F");
    }
}

