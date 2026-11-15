package view;

import core.Constants;
import core.Screen;
import core.Store;
import core.UIManager;
import entity.Projectiles;
import entity.defender.CastleDefender;
import entity.defender.Mercenary;
import entity.enemy.Mob;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import spell.BomberPlane;
import spell.Spell;

/**
 * Xu ly render cho che do choi trong tran (IN_GAME),
 * chon ky nang (SKILL_SELECT), va cua hang truoc tran (ITEM_SHOP).
 */
public class InGameRenderer {

    public void drawInGame(Graphics g, int width, int height) {
        g.setColor(new Color(70, 70, 70));
        g.fillRect(0, 0, width, height);

        if (Screen.room != null) {
            Screen.room.draw(g);
        }

        Rectangle optionFrame = Store.OPTION_FRAME_BOUNDS;
        g.drawImage(Screen.tiles.khungOption, optionFrame.x, optionFrame.y,
                optionFrame.width, optionFrame.height, null);

        // Ve Spell mat dat (nhu BlackHole) truoc quai
        for (Spell s : Screen.activeSpells) {
            if (!(s instanceof BomberPlane)) {
                s.draw(g);
            }
        }

        // Danh sach tat ca entity can ve (ke thu + hau ve)
        ArrayList<Mob> renderMobs = new ArrayList<>(Screen.activeMobs);

        for (CastleDefender m : Screen.mini) {
            if (m != null && m.inGame) {
                renderMobs.add(m);
            }
        }

        for (Mercenary m : Screen.merc) {
            if (m != null && m.inGame) {
                renderMobs.add(m);
            }
        }

        // Sap xep theo truc Y de con o tren khong de len con o duoi
        renderMobs.sort(Comparator.comparingInt(Mob::getY));
        for (Mob m : renderMobs) {
            m.draw(g);
        }

        // Ve Spell tren khong / bom no phia tren quai
        for (Spell s : Screen.activeSpells) {
            if (s instanceof BomberPlane) {
                s.draw(g);
            }
        }

        Screen.store.draw(g);

        for (Projectiles a : Screen.pjt) {
            a.draw(g);
        }

        UIManager.GoldenPickaxeButton.renderNut(g);
        UIManager.fireSwordButton.renderNut(g);
        UIManager.cursedSkullButton.renderNut(g);
        UIManager.bomberButton.renderNut(g);
        UIManager.skillButton.renderNut(g);
        UIManager.backToMenu.renderNut(g);

        g.setColor(Screen.tiles.darkSilver);
        g.setFont(Screen.tiles.pixelFontv1);
        g.drawImage(Screen.tiles.iconHeart, 185, 663, 25, 25, null);
        g.drawString(": " + Screen.health, 210, 683);
        g.drawImage(Screen.tiles.iconCoin, 185, 693, 25, 25, null);
        g.drawString(": " + Screen.coinage, 210, 713);
    }

    public void drawSelectSkill(Graphics g, int width, int height) {
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

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, width, height);

        UIManager.selectSkillFrame.renderNut(g);
        UIManager.selectBH.renderNut(g);
        UIManager.selectEnh.renderNut(g);
        UIManager.selectMer.renderNut(g);
    }

    public void drawItemShop(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.tileBuy, 0, 0, width, height, null);
        g.setColor(Screen.tiles.darkSilver);
        g.setFont(Screen.tiles.pixelFontvSmall);
        g.drawString("gold mine +50%", 240, 479);
        g.drawString("in 2 minutes", 240, 491);

        g.drawString("tower dps +50%", 512, 479);
        g.drawString("in 2 minutes", 512, 491);

        g.drawString("monster speed ", 240, 662);
        g.drawString("-30% in 2 minutes", 240, 674);

        g.drawString("Boommmm", 512, 662);
        g.drawString("Destroy all !!!", 512, 674);

        UIManager.backInGacha.renderNut(g);
        UIManager.buyGoldenPickaxe.renderNut(g);
        UIManager.buyFireSword.renderNut(g);
        UIManager.buyCursedSkull.renderNut(g);
        UIManager.buyPaperPlane.renderNut(g);
        UIManager.buyHeart.renderNut(g);

        g.setColor(Screen.tiles.royalGold);
        g.setFont(Screen.tiles.pixelFontHeroRequire);
        g.drawString("Remaining:", 10, 590);

        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);

        g.drawImage(Screen.tiles.iconGoldenPickaxe, 10, 595, 30, 30, null);
        g.drawString(": " + Screen.goldenPickaxe, 45, 620);

        g.drawImage(Screen.tiles.iconFireSword, 10, 630, 30, 30, null);
        g.drawString(": " + Screen.fireSword, 45, 655);

        g.drawImage(Screen.tiles.iconCursedSkull, 10, 665, 30, 30, null);
        g.drawString(": " + Screen.cursedSkull, 45, 690);

        g.drawImage(Screen.tiles.iconPaperPlane, 10, 700, 30, 30, null);
        g.drawString(": " + Screen.paperPlane, 45, 725);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.drawString(": " + Screen.coinage, 45, 760);

        g.drawImage(Screen.tiles.iconHeart, 1130, 735, 30, 30, null);
        g.drawString(": " + Screen.health + "/" + Screen.maxHealth, 1162, 760);
    }
}
