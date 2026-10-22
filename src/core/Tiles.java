package core;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.Buffer;

import javax.imageio.ImageIO;

public class Tiles {
    public ManageNut buttonManager = Screen.manageNut;

    public BufferedImage cursorNormal;
    public BufferedImage cursorHover;
    public BufferedImage cursorPressed;

    // SETTING MENU
    public BufferedImage volumeBarOn;
    public BufferedImage volumeBarOff;
    public BufferedImage tickButton;
    public BufferedImage tickPressedButton;
    public BufferedImage xButton;
    public BufferedImage xPressedButton;
    public BufferedImage longButton;
    public BufferedImage longPressedButton;
    public BufferedImage button;
	public BufferedImage touchedButton;
    public BufferedImage settingMenu;
    public BufferedImage settingsButton;
    public BufferedImage buttonSliderOff;
    public BufferedImage buttonSliderOn;

    // TITLE SCREEN
    public BufferedImage titleScreen;
    public BufferedImage newGameButton;
    public BufferedImage quitGameButton;
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

    public BufferedImage[] mapDanhNhau = new BufferedImage[4];

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
    public BufferedImage awakeT;
    public BufferedImage awaken;
    public BufferedImage purchaseT;

    public BufferedImage tileBuy;

    public Font pixelFontv1;
    public Font pixelFontv2;
    public Font pixelFontvSmall;
    public Font pixelFontMini;
    public Font pixelFontMedi;
    public Font pixelFontHeroRequire;
    public Font sproutLandsFont32f;

    public Color darkSilver;
    public Color royalGold;
    public Color purpleHole;
    public Color enhanceRed;
    public Color mercBlue;
    public Color gloomyPurple;
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
            
            cursorNormal = readImage("res/ui/mouse/mouse1.png");
            cursorHover = readImage("res/ui/mouse/mouse2.png");
            cursorPressed = readImage("res/ui/mouse/mouse3.png");

            volumeBarOn = readImage("res/ui/setting/volumeBarOn.png");
            volumeBarOff = readImage("res/ui/setting/volumeBarOff.png");
            tickButton = readImage("res/ui/setting/tickButton.png");
            tickPressedButton = readImage("res/ui/setting/tickPressedButton.png");
            xButton = readImage("res/ui/setting/XButton.png");
            xPressedButton = readImage("res/ui/setting/XPressedButton.png");
            longButton = readImage("res/ui/setting/longButton.png");
            longPressedButton = readImage("res/ui/setting/longPressedButton.png");

            // gs tileScreen
            buttonManager.playgame.ima[0] = readImage("res/tileImage/playgame.png");
            buttonManager.playgame.ima[1] = readImage("res/tileImage/playgameS.png");
          
            buttonManager.store.ima[0] = readImage("res/tileImage/store.png");
            buttonManager.store.ima[1] = readImage("res/tileImage/storeS.png");

            buttonManager.updateHero.ima[0] = readImage("res/tileImage/updateHero.png");
            buttonManager.updateHero.ima[1] = readImage("res/tileImage/updateHeroS.png");


            buttonManager.quitgame.ima[0] = readImage("res/tileImage/quitgame.png");
            buttonManager.quitgame.ima[1] = readImage("res/tileImage/quitgameS.png");

            //gs gameShop

            buttonManager.backInGameShop.ima[0]= readImage("res/tileImage/back.png");
            buttonManager.backInGameShop.ima[1]= readImage("res/tileImage/backS.png");

            buttonManager.backInGacha.ima[0]= readImage("res/tileImage/back.png");
            buttonManager.backInGacha.ima[1]= readImage("res/tileImage/backS.png");

            buttonManager.gachaBH.ima[0]= readImage("res/tileImage/gacha.png");
            buttonManager.gachaBH.ima[1]= readImage("res/tileImage/gachaS.png");

            buttonManager.gachaEnhance.ima[0]= readImage("res/tileImage/gacha.png");
            buttonManager.gachaEnhance.ima[1]= readImage("res/tileImage/gachaS.png");

            buttonManager.gachaMer.ima[0]= readImage("res/tileImage/gacha.png");
            buttonManager.gachaMer.ima[1]= readImage("res/tileImage/gachaS.png");

            buttonManager.summon.ima[0] = readImage("res/tileImage/summon.png");
            buttonManager.summon.ima[1] = readImage("res/tileImage/summonS.png");
            
            
            


            buttonManager.doiBH.ima[0]= readImage("res/tileImage/awake.png");
            buttonManager.doiBH.ima[1]= readImage("res/tileImage/awakeS.png");

            buttonManager.doiEnhance.ima[0]= readImage("res/tileImage/awake.png");
            buttonManager.doiEnhance.ima[1]= readImage("res/tileImage/awakeS.png");

            buttonManager.doiMer.ima[0]= readImage("res/tileImage/awake.png");
            buttonManager.doiMer.ima[1]= readImage("res/tileImage/awakeS.png");

            buttonManager.doiEC.ima[0] = readImage("res/tileImage/purchase.png");
            buttonManager.doiEC.ima[1] = readImage("res/tileImage/purchaseS.png");

            buttonManager.levelUp.ima[0] = readImage("res/tileImage/levelUp.png");
            buttonManager.levelUp.ima[1] = readImage("res/tileImage/levelUpS.png");

            buttonManager.expBar.ima[0] = readImage("res/tileImage/expBar.png");
            buttonManager.expBar.ima[1] = readImage("res/tileImage/expBarS.png");



           
            purchaseT = readImage("res/tileImage/purchaseT.png");
            awakeT = readImage("res/tileImage/awakeT.png");
            awaken = readImage("res/tileImage/awaken.png");
            maxLevel = readImage("res/tileImage/maxLevel.png");
            levelUpT = readImage("res/tileImage/levelUpT.png");

            addPoint = readImage("res/tileImage/addPoint.png");
            addPointS = readImage("res/tileImage/addPointS.png");
            addPointT = readImage("res/tileImage/addPointT.png");
            max = readImage("res/tileImage/max.png");



            


            buttonManager.thongBaoFrame.ima[0] = readImage("res/tileImage/awake.png");

            titleScreen = readImage("res/tileImage/game_tilee.png");
            newGameButton = readImage("res/tileImage/playgame2.png");
            backButton = readImage("res/tileImage/backk3.png");
            quitGameButton = readImage("res/tileImage/quitgame2.png");
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
            settingMenu = readImage("res/ui/setting/menu.png");
            settingsButton = readImage("res/ui/setting/icon.png");
            buttonSliderOff = readImage("res/ui/setting/buttonSliderOff.png");
            buttonSliderOn = readImage("res/ui/setting/buttonSliderOn.png");

            mapDanhNhau[3] = readImage("res/tileImage/mapMa1.png");
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
            pixelFontMedi = baseFont.deriveFont(32f);
            pixelFontHeroRequire = baseFont.deriveFont(20f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        darkSilver = Color.decode("#BFC7D5");
        enhanceRed = Color.decode("#FF4444");
        mercBlue = Color.decode("#3399FF");
        royalGold = Color.decode("#FFBF00");
        gloomyPurple = Color.decode("#8A2BE2");
        okOK = Color.decode("#4ADE80");
        notOK = Color.decode("#FF0F0F");
    }
}