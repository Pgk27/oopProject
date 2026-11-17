package view;

import core.Screen;
import core.UIManager;
import entity.defender.CastleDefender;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Xu ly render cho man hinh nang cap Hero (UPDATE_HERO),
 * thanh kinh nghiem (EXP bar), va bang so sanh chi so truoc/sau nang cap.
 */
public class HeroUpgradeView {

    public void drawUpdateHero(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.tileUpdateHero, 0, 0, width, height, null);

        g.drawImage(Screen.tiles.nenExpBlack, 735, 185, 220, 36, null);
        drawExpGreen(g);
        UIManager.expBar.renderNut(g);

        g.setColor(Screen.tiles.royalGold);
        g.setFont(Screen.tiles.pixelFontv1);
        g.drawString("Level: " + Screen.heroLevel, 740, 180);

        if (Screen.heroLevel < Screen.maxHeroLevel) {
            g.setFont(Screen.tiles.pixelFontHeroRequire);
            g.setColor(Screen.tiles.royalGold);
            g.drawString("Next Level Requires: ", 710, 250);

            g.setFont(Screen.tiles.pixelFontvSmall);
            g.setColor(Screen.tiles.darkSilver);

            g.drawImage(Screen.tiles.iconCoin, 913, 230, 25, 25, null);
            g.setColor(checkCostColor(1));
            g.drawString("x" + Screen.upHeroCoin[Screen.heroLevel], 938, 250);

            g.drawImage(Screen.tiles.mysteryShard, 975, 230, 25, 25, null);
            g.setColor(checkCostColor(2));
            g.drawString("x" + Screen.upHeroShard[Screen.heroLevel], 1000, 250);

            g.drawImage(Screen.tiles.iconCore, 1037, 230, 25, 25, null);
            g.setColor(checkCostColor(3));
            g.drawString("x" + Screen.upHeroECore[Screen.heroLevel], 1062, 250);
        } else {
            g.setFont(Screen.tiles.pixelFontHeroRequire);
            g.setColor(Screen.tiles.royalGold);
            g.drawString("Legendary Status Achieved!", 708, 250);
        }

        g.setFont(Screen.tiles.pixelFontHeroRequire);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString("Points: " + Screen.remainingPoint + "/" + Screen.maxPoint, 680, 330);

        g.drawString("Health: ", 655, 360);
        g.drawString("Health Regen: ", 655, 390);
        g.drawString("Dmg Reduction: ", 655, 420);
        g.drawString("Arrow Dmg: ", 655, 450);
        g.drawString("Sword Dmg: ", 655, 480);

        int arrowX = 930;
        drawStats(g, Integer.toString(CastleDefender.getMaxHealth()), Integer.toString(CastleDefender.getMaxHealth() + 100), arrowX, 360, Screen.buffHp);
        drawStats(g, Integer.toString(Screen.buffHpRe), Integer.toString(Screen.buffHpRe + 1), arrowX, 390, Screen.buffHpRe);
        drawStats(g, Integer.toString(Screen.buffShield), Integer.toString(Screen.buffShield + 1), arrowX, 420, Screen.buffShield);
        drawStats(g, Integer.toString(CastleDefender.getArrowDmg()), Integer.toString(CastleDefender.getArrowDmg() + 20), arrowX, 450, Screen.buffADmg);
        drawStats(g, Integer.toString(CastleDefender.getSwordDmg()), Integer.toString(CastleDefender.getSwordDmg() + 25), arrowX, 480, Screen.buffSDmg);

        g.setColor(Screen.tiles.royalGold);
        g.drawString("[+" + Screen.buffHp + "]", 1040, 360);
        g.drawString("[+" + Screen.buffHpRe + "]", 1040, 390);
        g.drawString("[+" + Screen.buffShield + "]", 1040, 420);
        g.drawString("[+" + Screen.buffADmg + "]", 1040, 450);
        g.drawString("[+" + Screen.buffSDmg + "]", 1040, 480);

        g.setColor(Screen.tiles.royalGold);
        g.setFont(Screen.tiles.pixelFontHeroRequire);
        g.drawString("Remaining:", 10, 660);

        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);

        g.drawImage(Screen.tiles.iconCoin, 10, 665, 30, 30, null);
        g.drawString(": " + Screen.coinage, 45, 690);

        g.drawImage(Screen.tiles.mysteryShard, 10, 700, 30, 30, null);
        g.drawString(": " + Screen.shard, 45, 725);

        g.drawImage(Screen.tiles.iconCore, 10, 735, 30, 30, null);
        g.drawString(": " + Screen.energyCore, 45, 760);

        UIManager.levelUp.renderNut(g);
        UIManager.backInGameShop.renderNut(g);
        UIManager.add1.renderNut(g);
        UIManager.add2.renderNut(g);
        UIManager.add3.renderNut(g);
        UIManager.add4.renderNut(g);
        UIManager.add5.renderNut(g);
    }

    private void drawExpGreen(Graphics g) {
        int dx1 = 725, dy1 = 185;
        int dx2;
        int sx2;

        if (Screen.heroXp <= Screen.upHeroXp[Screen.heroLevel]) {
            dx2 = 725 + Screen.heroXp * 230 / Screen.upHeroXp[Screen.heroLevel];
            sx2 = Screen.heroXp * 1766 / Screen.upHeroXp[Screen.heroLevel];
        } else {
            dx2 = 725 + 230;
            sx2 = 1766;
        }
        int dy2 = 185 + 36;
        int sx1 = 0;
        int sy1 = 0;
        int sy2 = 62;

        g.drawImage(Screen.tiles.nenExpGreen, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    private Color checkCostColor(int resourceType) {
        if (resourceType == 1) {
            return Screen.coinage >= Screen.upHeroCoin[Screen.heroLevel] ? Screen.tiles.okOK : Screen.tiles.notOK;
        } else if (resourceType == 2) {
            return Screen.shard >= Screen.upHeroShard[Screen.heroLevel] ? Screen.tiles.okOK : Screen.tiles.notOK;
        } else {
            return Screen.energyCore >= Screen.upHeroECore[Screen.heroLevel] ? Screen.tiles.okOK : Screen.tiles.notOK;
        }
    }

    public void drawStats(Graphics g, String current, String next, int arrowX, int y, int checking) {
        g.setFont(Screen.tiles.pixelFontv1);
        FontMetrics fm = g.getFontMetrics();

        if (checking >= Screen.maxBuff) {
            g.setFont(Screen.tiles.pixelFontv1);
            g.setColor(Screen.tiles.royalGold);
            int width = fm.stringWidth(current);
            g.drawString(current, arrowX - width / 2, y);
            return;
        }

        int gap = 8;
        String arrow = "-->";
        int arrowWidth = fm.stringWidth(arrow);

        g.setColor(Color.WHITE);
        g.drawString(arrow, arrowX - arrowWidth / 2, y);

        int currentWidth = fm.stringWidth(current);
        g.setColor(new Color(214, 206, 195));
        g.drawString(current, arrowX - arrowWidth / 2 - gap - currentWidth, y);

        g.setColor(new Color(130, 216, 115));
        g.drawString(next, arrowX + arrowWidth / 2 + gap, y);
    }
}
