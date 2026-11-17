package view;

import core.Frame;
import core.MouseHandler;
import core.Screen;
import core.UIManager;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Xu ly render cho man hinh Title (TITLE_SCREEN),
 * Cai dat am thanh / man hinh (SETTING),
 * va Tong ket tran thang/thua (VICTORY_SCREEN, DEFEAT_SCREEN).
 */
public class MenuView {

    public void drawTitleScreen(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.titleScreen, 0, 0, width, height, null);

        UIManager.playgame.renderNut(g);
        UIManager.store.renderNut(g);
        UIManager.updateHero.renderNut(g);
        UIManager.quitgame.renderNut(g);
        UIManager.setting.renderNut(g);
        UIManager.toGacha.renderNut(g);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);
    }

    public void drawSetting(Graphics g, int width, int height) {
        int offsetFromCenterX = 300;
        int offsetFromCenterY = 375;
        int buttonSize = 100;
        int buttonWidth = 72;
        int buttonHeight = 80;
        int longButtonHeight = 70;
        int longButtonWidth = 240;
        int awayFromLeftEdge = 75;
        int awayFromTopEdge = 250;
        int count = 0;
        int space = 100;

        int menuX = width / 2 - offsetFromCenterX;
        int menuY = height / 2 - offsetFromCenterY;
        int menuWidth = 2 * offsetFromCenterX;
        int menuHeight = 2 * offsetFromCenterY;

        g.drawImage(Screen.tiles.titleScreen, 0, 0, width, height, null);

        UIManager.playgame.renderNut(g);
        UIManager.store.renderNut(g);
        UIManager.updateHero.renderNut(g);
        UIManager.quitgame.renderNut(g);
        UIManager.setting.renderNut(g);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, width, height);

        g.drawImage(Screen.tiles.settingMenu, menuX, menuY, menuWidth, menuHeight, null);

        g.setFont(Screen.tiles.pixelFont32f);
        g.drawString("MUSIC", menuX + awayFromLeftEdge, menuY + awayFromTopEdge + 35);
        if (!MouseHandler.isMusicMuted)
            g.drawImage(Screen.tiles.buttonSliderOn, menuX + awayFromLeftEdge + 350, menuY + awayFromTopEdge - 25 + space * count, buttonSize, buttonSize, null);
        else
            g.drawImage(Screen.tiles.buttonSliderOff, menuX + awayFromLeftEdge + 350, menuY + awayFromTopEdge - 25 + space * count, buttonSize, buttonSize, null);
        drawSlider(g, menuX + awayFromLeftEdge + 125, menuY + awayFromTopEdge + space * (count++), Screen.musicVolume, MouseHandler.musicSliderBox);

        g.drawString("SFX", menuX + awayFromLeftEdge, menuY + awayFromTopEdge + space * count + 35);
        if (!MouseHandler.isSfxMuted)
            g.drawImage(Screen.tiles.buttonSliderOn, menuX + awayFromLeftEdge + 350, menuY + awayFromTopEdge - 25 + space * count, buttonSize, buttonSize, null);
        else
            g.drawImage(Screen.tiles.buttonSliderOff, menuX + awayFromLeftEdge + 350, menuY + awayFromTopEdge - 25 + space * count, buttonSize, buttonSize, null);
        drawSlider(g, menuX + awayFromLeftEdge + 125, menuY + awayFromTopEdge + space * (count++), Screen.sfxVolume, MouseHandler.sfxSliderBox);

        g.drawString("FULLSCREEN", menuX + awayFromLeftEdge, menuY + awayFromTopEdge + space * count + 35);
        if (Frame.isFullscreen) {
            g.drawImage(Screen.tiles.tickPressedButton, menuX + awayFromLeftEdge + 200, menuY + awayFromTopEdge - 15 + space * count, buttonWidth, buttonHeight, null);
            g.drawImage(Screen.tiles.xButton, menuX + awayFromLeftEdge + 300, menuY + awayFromTopEdge - 15 + space * count, buttonWidth, buttonHeight, null);
        } else {
            g.drawImage(Screen.tiles.tickButton, menuX + awayFromLeftEdge + 200, menuY + awayFromTopEdge - 15 + space * count, buttonWidth, buttonHeight, null);
            g.drawImage(Screen.tiles.xPressedButton, menuX + awayFromLeftEdge + 300, menuY + awayFromTopEdge - 15 + space * count, buttonWidth, buttonHeight, null);
        }

        if (!MouseHandler.isSettingCancelPressed) {
            g.drawImage(Screen.tiles.longButton, menuX + 40, menuY + 630, longButtonWidth, longButtonHeight, null);
            g.drawString("CANCEL", menuX + 115, menuY + 638 + longButtonHeight / 2);
        } else {
            g.drawImage(Screen.tiles.longPressedButton, menuX + 40, menuY + 630, longButtonWidth, longButtonHeight, null);
            g.drawString("CANCEL", menuX + 115, menuY + 640 + longButtonHeight / 2);
        }

        if (!MouseHandler.isSettingConfirmPressed) {
            g.drawImage(Screen.tiles.longButton, menuX + 40 + longButtonWidth + 40, menuY + 630, longButtonWidth, longButtonHeight, null);
            g.drawString("CONFIRM", menuX + longButtonWidth + 140, menuY + 638 + longButtonHeight / 2);
        } else {
            g.drawImage(Screen.tiles.longPressedButton, menuX + 40 + longButtonWidth + 40, menuY + 630, longButtonWidth, longButtonHeight, null);
            g.drawString("CONFIRM", menuX + longButtonWidth + 140, menuY + 640 + longButtonHeight / 2);
        }
    }

    public void drawTongKetWin(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.tongKetWin, 0, 0, width, height, null);
        g.setFont(Screen.tiles.pixelFontv2);
        g.setColor(Screen.tiles.darkSilver);

        g.drawString("Chosen Skill: " + Screen.strChosenCard, 225, 410);
        g.drawString("Wave survived: " + Screen.waveManager.getTotalWaves() + "/" + Screen.waveManager.getTotalWaves(), 225, 435);
        g.drawString("Battle Duration: " + Screen.gameTime + " seconds", 225, 460);
        g.drawString("Enermy Killed: " + Screen.killed, 225, 485);
        g.drawString("Enermy Esscaped: " + (-Screen.health + Screen.healthDauTran), 225, 510);
        g.drawString("Remaining Health: " + Screen.health, 225, 535);

        g.drawImage(Screen.tiles.iconCoin, 630, 390, 100, 100, null);
        g.drawImage(Screen.tiles.iconEXP, 880, 390, 100, 100, null);

        drawRssTK(g, Screen.coinDauTran, Screen.coinage, 80, 8, 695, 530);
        drawRssTK(g, Screen.heroXpDauTran, Screen.heroXp, 80, 8, 940, 530);

        UIManager.menuWin.renderNut(g);
        UIManager.next.renderNut(g);
    }

    public void drawTongKetLose(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.tongKetLose, 0, 0, width, height, null);
        g.setFont(Screen.tiles.pixelFontv2);
        g.setColor(Screen.tiles.darkSilver);

        g.drawString("Chosen Skill: " + Screen.strChosenCard, 225, 410);
        g.drawString("Wave survived: " + (Screen.waveManager.getCurrentWave() - 1) + "/" + Screen.waveManager.getTotalWaves(), 225, 435);
        g.drawString("Battle Duration: " + Screen.gameTime + " seconds", 225, 460);
        g.drawString("Enermy Killed: " + Screen.killed, 225, 485);
        g.drawString("Enermy Esscaped: " + Screen.healthDauTran, 225, 510);
        g.drawString("Remaining Health: " + Screen.health, 225, 535);

        g.drawImage(Screen.tiles.iconCoin, 630, 390, 100, 100, null);
        g.drawImage(Screen.tiles.iconEXP, 880, 390, 100, 100, null);

        drawRssTK(g, Screen.coinDauTran, Screen.coinage, 80, 8, 695, 530);
        drawRssTK(g, Screen.heroXpDauTran, Screen.heroXp, 80, 8, 940, 530);

        UIManager.menuLose.renderNut(g);
        UIManager.again.renderNut(g);
    }

    private void drawSlider(Graphics g, int startX, int startY, int currentVolume, Rectangle outHitBox) {
        int barW = 16;
        int barH = 48;
        int spacing = 5;
        int totalBar = 10;

        int totalWidth = totalBar * barW + (totalBar - 1) * spacing;
        outHitBox.setBounds(startX, startY, totalWidth, barH);

        for (int i = 0; i < totalBar; i++) {
            int x = startX + i * (barW + spacing);
            if (i < currentVolume) {
                g.drawImage(Screen.tiles.volumeBarOn, x, startY, barW, barH, null);
            } else {
                g.drawImage(Screen.tiles.volumeBarOff, x, startY, barW, barH, null);
            }
        }
    }

    private void drawRssTK(Graphics g, int dau, int cuoi, int widthMuiTen, int offSetX, int x, int y) {
        g.setFont(Screen.tiles.pixelFontv2);
        g.setColor(Screen.tiles.darkSilver);
        FontMetrics fm = g.getFontMetrics();
        int widthDau = fm.stringWidth(Integer.toString(dau));
        g.drawString("" + dau, x - widthMuiTen / 2 - offSetX - widthDau, y);
        int heightMuiTen = (int) (widthMuiTen * 0.5);
        g.drawImage(Screen.tiles.muiTen, x - widthMuiTen / 2, y - 30, widthMuiTen, heightMuiTen, null);

        g.setColor(checkDeltaColor(dau, cuoi));
        g.drawString("" + cuoi, x + widthMuiTen / 2 + offSetX, y);
        int change = cuoi - dau;
        String strChange = (change >= 0) ? ("+" + change) : ("" + change);
        int widthChange = fm.stringWidth(strChange);
        g.setFont(Screen.tiles.pixelFontv1);
        g.drawString(strChange, x - widthMuiTen / 3 - widthChange / 2 + 10, y - 22);
    }

    private Color checkDeltaColor(int x, int y) {
        if (x == y) return Screen.tiles.darkSilver;
        return (x > y) ? Screen.tiles.notOK : Screen.tiles.okOK;
    }
}
