package view;

import core.Screen;
import core.UIManager;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Xu ly render cho he thong Gacha, xem ty le roi do,
 * Shard Shop (doi manh) va khung thong bao ket qua gacha.
 */
public class GachaView {

    public void drawGachaScreen(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        UIManager.backInGacha.renderNut(g);

        g.drawImage(Screen.tiles.BlackHoleC, 470, 310, 135, 30, null);
        g.drawImage(Screen.tiles.EnhanceC, 470, 430, 150, 35, null);
        g.drawImage(Screen.tiles.MercenaryC, 470, 565, 140, 38, null);

        UIManager.gachaBH.renderNut(g);
        UIManager.gachaEnhance.renderNut(g);
        UIManager.gachaMer.renderNut(g);
        UIManager.summon.renderNut(g);

        UIManager.info.renderNut(g);
        UIManager.iconShardShop.renderNut(g);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);
    }

    public void drawGachaRateScreen(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);

        g.drawImage(Screen.tiles.BlackHoleC, 470, 310, 135, 30, null);
        g.drawImage(Screen.tiles.EnhanceC, 470, 430, 150, 35, null);
        g.drawImage(Screen.tiles.MercenaryC, 470, 565, 140, 38, null);

        UIManager.gachaBH.renderNut(g);
        UIManager.gachaEnhance.renderNut(g);
        UIManager.gachaMer.renderNut(g);
        UIManager.summon.renderNut(g);

        UIManager.info.renderNut(g);
        UIManager.iconShardShop.renderNut(g);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);

        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, width, height);
        g.drawImage(Screen.tiles.gachaInfoFrame, 300, 5, 600, 750, null);
        g.drawImage(Screen.tiles.gachaRateText, 475, 110, 250, 50, null);

        g.setColor(Screen.tiles.royalGold);
        g.setFont(Screen.tiles.pixelFontv2);
        g.drawString("1. Skill Chest", 415, 185);

        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString("Skill: 2%", 430, 207);
        g.drawString("x1 Mystery Shard: 35%", 430, 225);
        g.drawString("x2 Mystery Shard: 20%", 430, 243);
        g.drawString("x3 Mystery Shard: 15%", 430, 261);
        g.drawString("x5 Mystery Shard: 10%", 430, 279);
        g.drawString("x10 Mystery Shard: 10%", 430, 297);
        g.drawString("x20 Mystery Shard: 5%", 430, 315);
        g.drawString("x50 Mystery Shard: 3%", 430, 333);

        g.setFont(Screen.tiles.pixelFontv2);
        g.setColor(Screen.tiles.royalGold);
        g.drawString("2. Summon Hero", 415, 360);

        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString("Energy Core: 1%", 430, 382);
        g.drawString("x3 Mystery Shard: 20%", 430, 400);
        g.drawString("x5 Mystery Shard: 20%", 430, 418);
        g.drawString("x10 Mystery Shard: 20%", 430, 436);
        g.drawString("x20 Mystery Shard: 20%", 430, 454);
        g.drawString("x35 Mystery Shard: 15%", 430, 472);
        g.drawString("x50 Mystery Shard: 3%", 430, 490);
        g.drawString("x100 Mystery Shard: 1%", 430, 508);
    }

    public void drawShardShop(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);

        g.drawImage(Screen.tiles.BlackHoleC, 470, 310, 135, 30, null);
        g.drawImage(Screen.tiles.EnhanceC, 470, 430, 150, 35, null);
        g.drawImage(Screen.tiles.MercenaryC, 470, 565, 140, 38, null);

        UIManager.doiBH.renderNut(g);
        UIManager.doiEnhance.renderNut(g);
        UIManager.doiMer.renderNut(g);
        UIManager.doiEC.renderNut(g);

        UIManager.info.renderNut(g);
        UIManager.iconShardShop.renderNut(g);
        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);

        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, width, height);

        g.drawImage(Screen.tiles.shardShopBigFrame, 160, 30, 900, 680, null);
        g.drawImage(Screen.tiles.awakeningText, 435, 130, 350, 75, null);

        g.setColor(Screen.tiles.darkSilver);
        g.setFont(Screen.tiles.pixelFontvSmall);

        for (int i = 0; i < 4; i++) {
            int oy = i * 80;
            g.drawImage(Screen.tiles.mysteryShard, 310, 200 + oy, 50, 50, null);
            g.drawImage(Screen.tiles.dauCong, 395, 210 + oy, 30, 30, null);
            g.drawImage(Screen.tiles.iconCoin, 440, 200 + oy, 50, 50, null);
            g.drawImage(Screen.tiles.muiTen, 525, 210 + oy, 50, 30, null);

            g.drawString("Mystery Shard", 295, 260 + oy);
            if (i == 3) {
                g.drawString("x200", 360, 248 + oy);
                g.drawString("x200", 485, 248 + oy);
            } else {
                g.drawString("x100", 360, 248 + oy);
                g.drawString("x100", 485, 248 + oy);
            }
            g.drawString("Coin", 452, 260 + oy);
        }

        g.drawImage(Screen.tiles.iconBH, 590, 200, 50, 50, null);
        g.drawImage(Screen.tiles.iconEnhance, 590, 280, 50, 50, null);
        g.drawImage(Screen.tiles.iconMercenary, 590, 360, 50, 50, null);
        g.drawImage(Screen.tiles.iconCore, 590, 440, 50, 50, null);

        g.setColor(Screen.tiles.gloomyPurple);
        g.drawString("Black Hole", 585, 260);
        g.setColor(Screen.tiles.enhanceRed);
        g.drawString("Enhancement", 580, 340);
        g.setColor(Screen.tiles.mercBlue);
        g.drawString("Mercenary", 585, 420);
        g.setColor(Screen.tiles.gloomyPurple);
        g.drawString("Energy Core", 580, 500);

        UIManager.doiBH.renderNut(g);
        UIManager.doiEnhance.renderNut(g);
        UIManager.doiMer.renderNut(g);
        UIManager.doiEC.renderNut(g);

        g.setFont(Screen.tiles.pixelFontv2);
        g.setColor(Screen.tiles.royalGold);
        g.drawImage(Screen.tiles.mysteryShard, 300, 580, 35, 35, null);
        g.setColor(Screen.tiles.darkSilver);
        g.setFont(Screen.tiles.pixelFontvSmall);
        g.drawString("x" + Screen.shard + " remaining", 335, 613);
    }

    public void drawThongBao(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);

        g.drawImage(Screen.tiles.BlackHoleC, 470, 310, 135, 30, null);
        g.drawImage(Screen.tiles.EnhanceC, 470, 430, 150, 35, null);
        g.drawImage(Screen.tiles.MercenaryC, 470, 565, 140, 38, null);

        UIManager.gachaBH.renderNut(g);
        UIManager.gachaEnhance.renderNut(g);
        UIManager.gachaMer.renderNut(g);
        UIManager.summon.renderNut(g);

        UIManager.info.renderNut(g);
        UIManager.iconShardShop.renderNut(g);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);

        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, width, height);

        g.drawImage(Screen.tiles.shardShopBigFrame, 360, 140, 500, 400, null);

        if (!Screen.ok) {
            g.drawImage(Screen.tiles.oopss, 460, 220, 300, 70, null);
            g.setColor(Screen.tiles.darkSilver);
            g.setFont(Screen.tiles.pixelFontMedi);
            g.drawString("Save your coins!", 440, 320);
            g.drawString("You need at least 10 coins", 440, 350);
            g.drawString("to buy an archer tower for the", 440, 380);
            g.drawString("next level.", 440, 410);
            g.drawString("Spend wisely!", 440, 450);
        } else {
            g.drawImage(Screen.tiles.congra, 440, 200, 340, 120, null);
            g.drawImage(Screen.tiles.mysteryShard, 560, 330, 100, 100, null);
            g.setFont(Screen.tiles.pixelFontv1);
            g.setColor(Screen.tiles.darkSilver);

            if (Screen.gachaType == Screen.summon) {
                if (Screen.randomNum == 1) {
                    g.drawImage(Screen.tiles.iconCore, 559, 329, 102, 102, null);
                    g.setColor(Screen.tiles.gloomyPurple);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Energy Core", 542, 455);
                } else if (Screen.randomNum == 2) {
                    drawShardReward(g, "x100");
                } else if (Screen.randomNum <= 5) {
                    drawShardReward(g, "x50");
                } else if (Screen.randomNum <= 20) {
                    drawShardReward(g, "x35");
                } else if (Screen.randomNum <= 40) {
                    drawShardReward(g, "x20");
                } else if (Screen.randomNum <= 60) {
                    drawShardReward(g, "x10");
                } else if (Screen.randomNum <= 80) {
                    drawShardReward(g, "x5");
                } else {
                    drawShardReward(g, "x3");
                }
            } else {
                if (Screen.randomNum <= 2) {
                    if (Screen.gachaType == Screen.gachaBH) {
                        g.drawImage(Screen.tiles.iconBH, 559, 329, 102, 102, null);
                        g.setColor(Screen.tiles.gloomyPurple);
                        g.setFont(Screen.tiles.pixelFontMedi);
                        g.drawString("Black Hole", 550, 455);
                    } else if (Screen.gachaType == Screen.gachaEnhance) {
                        g.drawImage(Screen.tiles.iconEnhance, 559, 329, 102, 102, null);
                        g.setColor(Screen.tiles.enhanceRed);
                        g.setFont(Screen.tiles.pixelFontMedi);
                        g.drawString("Enhancement", 540, 455);
                    } else if (Screen.gachaType == Screen.gachaMercenary) {
                        g.drawImage(Screen.tiles.iconMercenary, 559, 329, 102, 102, null);
                        g.setColor(Screen.tiles.mercBlue);
                        g.setFont(Screen.tiles.pixelFontMedi);
                        g.drawString("Mercenary", 555, 455);
                    }
                } else if (Screen.randomNum <= 5) {
                    drawShardReward(g, "x50");
                } else if (Screen.randomNum <= 10) {
                    drawShardReward(g, "x20");
                } else if (Screen.randomNum <= 20) {
                    drawShardReward(g, "x10");
                } else if (Screen.randomNum <= 30) {
                    drawShardReward(g, "x5");
                } else if (Screen.randomNum <= 45) {
                    drawShardReward(g, "x3");
                } else if (Screen.randomNum <= 65) {
                    drawShardReward(g, "x2");
                } else {
                    drawShardReward(g, "x1");
                }
            }
        }
    }

    private void drawShardReward(Graphics g, String count) {
        g.setColor(Screen.tiles.darkSilver);
        g.setFont(Screen.tiles.pixelFontMedi);
        g.drawString("Mystery Shard", 540, 453);
        g.setFont(Screen.tiles.pixelFontv1);
        g.drawString(count, 660, 428);
    }
}
