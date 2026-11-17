package core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import view.GachaView;
import view.HeroUpgradeView;
import view.InGameRenderer;
import view.MenuView;

/**
 * Bo dieu phoi Render chinh (Render Coordinator / Facade).
 * Phan phat luot ve den view tuong ung voi gameState hien tai,
 * va ve con tro chuot tuy chinh (custom cursor).
 */
public class GameRender {
    private final InGameRenderer inGameRenderer = new InGameRenderer();
    private final GachaView gachaView = new GachaView();
    private final HeroUpgradeView heroUpgradeView = new HeroUpgradeView();
    private final MenuView menuView = new MenuView();

    public void render(Graphics g, int width, int height) {
        int state = Screen.gameState;

        if (state == Constants.TITLE_SCREEN) {
            menuView.drawTitleScreen(g, width, height);
        } else if (state == Constants.IN_GAME) {
            inGameRenderer.drawInGame(g, width, height);
        } else if (state == Constants.SKILL_SELECT) {
            inGameRenderer.drawSelectSkill(g, width, height);
        } else if (state == Constants.SETTING) {
            menuView.drawSetting(g, width, height);
        } else if (state == Constants.GACHA) {
            gachaView.drawGachaScreen(g, width, height);
        } else if (state == Constants.GACHA_RATE) {
            gachaView.drawGachaRateScreen(g, width, height);
        } else if (state == Constants.SHARD_SHOP) {
            gachaView.drawShardShop(g, width, height);
        } else if (state == Constants.NOTI) {
            gachaView.drawThongBao(g, width, height);
        } else if (state == Constants.VICTORY_SCREEN) {
            menuView.drawTongKetWin(g, width, height);
        } else if (state == Constants.DEFEAT_SCREEN) {
            menuView.drawTongKetLose(g, width, height);
        } else if (state == Constants.UPDATE_HERO) {
            heroUpgradeView.drawUpdateHero(g, width, height);
        } else if (state == Constants.ITEM_SHOP) {
            inGameRenderer.drawItemShop(g, width, height);
        }

        // Ve con tro chuot tuy chinh (custom cursor)
        boolean isHover = MouseHandler.isHoveringButton(Screen.mse.x, Screen.mse.y);
        boolean overDisabledButton = MouseHandler.isOverDisabledGameButton(Screen.mse.x, Screen.mse.y);
        BufferedImage currentCursor;

        if (MouseHandler.isMousePressed && !overDisabledButton) {
            currentCursor = Screen.tiles.cursorPressed;
        } else if (isHover) {
            currentCursor = Screen.tiles.cursorHover;
        } else {
            currentCursor = Screen.tiles.cursorNormal;
        }

        if (currentCursor != null) {
            int cursorSize = 32;
            g.drawImage(currentCursor, Screen.mse.x, Screen.mse.y, cursorSize, cursorSize, null);
        }
    }
}
