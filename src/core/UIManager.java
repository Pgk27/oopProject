package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import service.GachaService;
import service.HeroUpgradeService;

/**
 * Quan ly toan bo he thong nut bam UI (UIButton), danh sach nut theo tung gameState,
 * va cac lop nut tai su dung (Reusable Buttons).
 */
public class UIManager {
    public static int[] countNut = new int[35];
    public static UIButton[][] heThongNut = new UIButton[35][20];
    public Screen screen;

    public UIManager(Screen screen) {
        this.screen = screen;
        for (int i = 0; i < countNut.length; i++) {
            countNut[i] = 0;
        }

        // Title Screen
        addNut(playgame, Constants.TITLE_SCREEN);
        addNut(store, Constants.TITLE_SCREEN);
        addNut(updateHero, Constants.TITLE_SCREEN);
        addNut(quitgame, Constants.TITLE_SCREEN);
        addNut(setting, Constants.TITLE_SCREEN);
        addNut(toGacha, Constants.TITLE_SCREEN);

        // In-game
        addNut(backToMenu, Constants.IN_GAME);
        addNut(GoldenPickaxeButton, Constants.IN_GAME);
        addNut(fireSwordButton, Constants.IN_GAME);
        addNut(cursedSkullButton, Constants.IN_GAME);
        addNut(bomberButton, Constants.IN_GAME);
        addNut(skillButton, Constants.IN_GAME);

        // Select Skill
        addNut(selectBH, Constants.SKILL_SELECT);
        addNut(selectEnh, Constants.SKILL_SELECT);
        addNut(selectMer, Constants.SKILL_SELECT);
        addNut(selectSkillFrame, Constants.SKILL_SELECT);

        // Buy Item
        addNut(backInGacha, Constants.ITEM_SHOP);
        addNut(buyGoldenPickaxe, Constants.ITEM_SHOP);
        addNut(buyFireSword, Constants.ITEM_SHOP);
        addNut(buyCursedSkull, Constants.ITEM_SHOP);
        addNut(buyPaperPlane, Constants.ITEM_SHOP);
        addNut(buyHeart, Constants.ITEM_SHOP);

        // Gacha
        addNut(in4gacha, Constants.GACHA);
        addNut(shardShop, Constants.GACHA);
        addNut(backInGacha, Constants.GACHA);
        addNut(gachaBH, Constants.GACHA);
        addNut(gachaEnhance, Constants.GACHA);
        addNut(gachaMer, Constants.GACHA);
        addNut(summon, Constants.GACHA);
        addNut(iconShardShop, Constants.GACHA);
        addNut(info, Constants.GACHA);

        // Shard Shop
        addNut(shardShopBigFramee, Constants.SHARD_SHOP);
        addNut(doiBH, Constants.SHARD_SHOP);
        addNut(doiEnhance, Constants.SHARD_SHOP);
        addNut(doiMer, Constants.SHARD_SHOP);
        addNut(doiEC, Constants.SHARD_SHOP);

        // Thong bao & Gacha Rate
        addNut(thongBaoFrame, Constants.NOTI);
        addNut(gachaRateFrame, Constants.GACHA_RATE);

        // Update Hero
        addNut(backInGameShop, Constants.UPDATE_HERO);
        addNut(levelUp, Constants.UPDATE_HERO);
        addNut(expBar, Constants.UPDATE_HERO);
        addNut(add1, Constants.UPDATE_HERO);
        addNut(add2, Constants.UPDATE_HERO);
        addNut(add3, Constants.UPDATE_HERO);
        addNut(add4, Constants.UPDATE_HERO);
        addNut(add5, Constants.UPDATE_HERO);

        // Tong Ket Thua & Win
        addNut(menuLose, Constants.DEFEAT_SCREEN);
        addNut(again, Constants.DEFEAT_SCREEN);
        addNut(menuWin, Constants.VICTORY_SCREEN);
        addNut(next, Constants.VICTORY_SCREEN);
    }

    public static void addNut(UIButton x, int gState) {
        heThongNut[gState][countNut[gState]] = x;
        countNut[gState]++;
    }

    public static UIButton createButton(int x, int y, int dx, int dy, Runnable onClick) {
        return new UIButton(x, y, dx, dy) {
            @Override
            public void onClick() {
                if (onClick != null) onClick.run();
            }
        };
    }

    // =========================================================================
    // 1. REUSABLE BUTTON CLASSES
    // =========================================================================

    /** Nut bam mua vat pham (dong goi logic tru tien va ve hieu ung 3 trang thai) */
    public static class BuyItemButton extends UIButton {
        private final int price;
        private final Runnable onBuy;

        public BuyItemButton(int x, int y, int dx, int dy, int price, Runnable onBuy) {
            super(x, y, dx, dy);
            this.price = price;
            this.onBuy = onBuy;
        }

        @Override
        public void onClick() {
            if (Screen.coinage >= price) {
                Screen.coinage -= price;
                onBuy.run();
            }
        }

        @Override
        public void renderNut(Graphics g) {
            if (Screen.coinage >= price) {
                int drawY = (state == 2) ? y + 3 : y;
                Image img = (state == 0) ? Screen.tiles.buyItem : Screen.tiles.buyItemS;
                g.drawImage(img, x, drawY, dx, dy, null);
            } else {
                g.drawImage(Screen.tiles.buyItemT, x, y, dx, dy, null);
            }
        }
    }

    /** Nut bam cong diem chi so Hero (HP, Giap, Sat thuong...) */
    public static class StatUpgradeButton extends UIButton {
        private final IntSupplier currentLevel;
        private final Runnable onUpgrade;

        public StatUpgradeButton(int x, int y, IntSupplier currentLevel, Runnable onUpgrade) {
            super(x, y, 25, 25);
            this.currentLevel = currentLevel;
            this.onUpgrade = onUpgrade;
        }

        @Override
        public void onClick() {
            onUpgrade.run();
        }

        @Override
        public void renderNut(Graphics g) {
            if (currentLevel.getAsInt() >= Screen.maxBuff) {
                g.drawImage(Screen.tiles.max, x - 7, y, dx + 14, dy, null);
            } else if (Screen.remainingPoint > 0) {
                int drawY = (state == 1) ? y - 2 : (state == 2) ? y + 1 : y;
                Image img = (state == 0) ? Screen.tiles.addPoint : Screen.tiles.addPointS;
                g.drawImage(img, x, drawY, dx, dy, null);
            } else {
                g.drawImage(Screen.tiles.addPointT, x, y, dy, dx, null);
            }
        }
    }

    /** Nut bam doi manh Shard / Energy Core */
    public static class ShardExchangeButton extends UIButton {
        private final int requiredCost;
        private final BooleanSupplier isUnlocked;
        private final Runnable onExchange;

        public ShardExchangeButton(int x, int y, int dx, int dy, int cost, BooleanSupplier isUnlocked, Runnable onExchange) {
            super(x, y, dx, dy);
            this.requiredCost = cost;
            this.isUnlocked = isUnlocked;
            this.onExchange = onExchange;
        }

        @Override
        public void onClick() {
            onExchange.run();
        }

        @Override
        public void renderNut(Graphics g) {
            if (isUnlocked != null && isUnlocked.getAsBoolean()) {
                g.drawImage(Screen.tiles.awaken, x, y, dx, dy, null);
                return;
            }
            if (Screen.coinage >= requiredCost && Screen.shard >= requiredCost) {
                int drawY = (state == 2) ? y + 3 : y;
                Image img = (state == 0) ? ima[0] : ima[1];
                if (img != null) g.drawImage(img, x, drawY, dx, dy, null);
            } else {
                Image disabled = (isUnlocked != null) ? Screen.tiles.awakeT : Screen.tiles.purchaseT;
                g.drawImage(disabled, x, y, dx, dy, null);
            }
        }
    }

    /** Nut chon ky nang xuat tran (Black Hole, Enhance, Mercenary) */
    public static class CardSelectButton extends UIButton {
        private final int cardId;
        private final String cardName;
        private final BooleanSupplier isUnlocked;

        public CardSelectButton(int x, int y, int dx, int dy, int cardId, String cardName, BooleanSupplier isUnlocked) {
            super(x, y, dx, dy);
            this.cardId = cardId;
            this.cardName = cardName;
            this.isUnlocked = isUnlocked;
        }

        @Override
        public void onClick() {
            if (isUnlocked.getAsBoolean()) {
                Screen.chosenCard = cardId;
                Screen.strChosenCard = cardName;
                Screen.startGame();
            }
        }

        @Override
        public void renderNut(Graphics g) {
            if (isUnlocked.getAsBoolean()) {
                int drawY = (state == 1) ? y - 3 : (state == 2) ? y + 3 : y;
                Image img = (state == 0) ? Screen.tiles.selectCard : Screen.tiles.selectCardS;
                g.drawImage(img, x, drawY, dx, dy, null);
            } else {
                g.drawImage(Screen.tiles.lockedCard, x, y, dx, dy, null);
            }
        }
    }

    /** Khung phu click ra ngoai de dong modal */
    public static class ModalBackdropButton extends UIButton {
        private final Runnable onClose;

        public ModalBackdropButton(int x, int y, int dx, int dy, Runnable onClose) {
            super(x, y, dx, dy);
            this.onClose = onClose;
        }

        @Override
        public boolean contains(int mouseX, int mouseY) {
            return !(mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy);
        }

        @Override
        public void onClick() {
            if (onClose != null) onClose.run();
        }

        @Override
        public void renderNut(Graphics g) {}
    }

    // =========================================================================
    // 2. IN-GAME HUD BUTTONS (COOLDOWN, DYNAMIC POSITIONS)
    // =========================================================================

    public abstract static class InGameItemButton extends UIButton {
        protected final int column;

        public InGameItemButton(int column) {
            super(0, 0, 0, 0);
            this.column = column;
        }

        protected abstract boolean canUse();

        @Override
        public boolean isEnabled() {
            return Screen.gameState == Constants.IN_GAME && canUse();
        }

        private boolean updateBounds() {
            if (Screen.store == null) return false;
            Rectangle bounds = Screen.store.getItemButtonBounds(column);
            x = bounds.x;
            y = bounds.y;
            dx = bounds.width;
            dy = bounds.height;
            return true;
        }

        @Override
        public boolean contains(int mouseX, int mouseY) {
            return updateBounds() && mouseX >= x && mouseX < x + dx && mouseY >= y && mouseY < y + dy;
        }

        @Override
        public final void renderNut(Graphics g) {
            if (!isEnabled()) resetInteraction();
            if (updateBounds()) renderItem(g);
        }

        protected abstract void renderItem(Graphics g);
    }

    private static void drawBuffItem(Graphics g, UIButton button, Image icon, int quantity, int remaining) {
        int drawY = button.y + (button.state == 1 ? -3 : button.state == 2 ? 3 : 0);
        g.drawImage(button.state == 0 ? Screen.tiles.button : Screen.tiles.touchedButton, button.x, drawY, button.dx, button.dy, null);
        g.drawImage(icon, button.x + Store.itemIn, drawY + Store.itemIn, button.dx - Store.itemIn * 2, button.dy - Store.itemIn * 2, null);

        if (remaining > 0 || quantity <= 0) {
            g.setColor(remaining > 0 ? new Color(255, 180, 0, 110) : new Color(0, 0, 0, 160));
            g.fillRect(button.x, drawY, button.dx, button.dy);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier New", Font.BOLD, 12));
        g.drawString("x" + quantity, button.x + Store.itemIn, drawY + Store.itemIn + 10);
        if (remaining > 0) {
            g.setFont(new Font("Courier New", Font.BOLD, 14));
            String timer = remaining + "s";
            g.drawString(timer, button.x + (button.dx - g.getFontMetrics().stringWidth(timer)) / 2, drawY + button.dy / 2 + 8);
        }
    }

    public static class BuffItemButton extends InGameItemButton {
        private final BooleanSupplier canUse;
        private final Runnable onUse;
        private final Supplier<Image> icon;
        private final IntSupplier quantity;
        private final IntSupplier remaining;

        public BuffItemButton(int column, BooleanSupplier canUse, Runnable onUse, Supplier<Image> icon, IntSupplier quantity, IntSupplier remaining) {
            super(column);
            this.canUse = canUse;
            this.onUse = onUse;
            this.icon = icon;
            this.quantity = quantity;
            this.remaining = remaining;
        }

        @Override
        protected boolean canUse() {
            return Screen.store != null && canUse.getAsBoolean();
        }

        @Override
        public void onClick() {
            if (Screen.store != null) onUse.run();
        }

        @Override
        protected void renderItem(Graphics g) {
            drawBuffItem(g, this, icon.get(), quantity.getAsInt(), remaining.getAsInt());
        }
    }

    public static UIButton GoldenPickaxeButton = new BuffItemButton(0,
            () -> Screen.store.canUseGoldenPickaxe(), () -> Screen.store.useGoldenPickaxe(),
            () -> Screen.tiles.iconGoldenPickaxe, () -> Screen.goldenPickaxe, () -> Screen.store.getGoldenPickaxeRemainingSeconds());

    public static UIButton fireSwordButton = new BuffItemButton(1,
            () -> Screen.store.canUseFireSword(), () -> Screen.store.useFireSword(),
            () -> Screen.tiles.iconFireSword, () -> Screen.fireSword, () -> Screen.store.getFireSwordRemainingSeconds());

    public static UIButton cursedSkullButton = new BuffItemButton(2,
            () -> Screen.store.canUseCursedSkull(), () -> Screen.store.useCursedSkull(),
            () -> Screen.tiles.iconCursedSkull, () -> Screen.cursedSkull, () -> Screen.store.getCursedSkullRemainingSeconds());

    public static UIButton bomberButton = new InGameItemButton(3) {
        @Override
        protected boolean canUse() {
            return Screen.store != null && Screen.store.canSelectBomberPlane();
        }

        @Override
        public void onClick() {
            if (Screen.store != null) Screen.store.selectBomberPlane();
        }

        @Override
        protected void renderItem(Graphics g) {
            int drawY = (state == 1) ? y - 3 : (state == 2) ? y + 3 : y;
            g.drawImage(state == 0 ? Screen.tiles.button : Screen.tiles.touchedButton, x, drawY, dx, dy, null);
            g.drawImage(Screen.airplane, x + Store.itemIn, drawY + Store.itemIn, dx - Store.itemIn * 2, dy - Store.itemIn * 2, null);

            if (Screen.paperPlane <= 0) {
                g.setColor(new Color(0, 0, 0, 160));
                g.fillRect(x, drawY, dx, dy);
            } else if (Screen.store != null && Screen.store.holdsItem && Store.heldID == Constants.SPELL_BOMBER_PLANE) {
                g.setColor(new Color(255, 215, 0, 70));
                g.fillRect(x, drawY, dx, dy);
                g.setColor(Color.YELLOW);
                g.drawRect(x, drawY, dx, dy);
                g.drawRect(x + 1, drawY + 1, dx - 2, dy - 2);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 12));
            g.drawString("x" + Screen.paperPlane, x + Store.itemIn, drawY + Store.itemIn + 10);
        }
    };

    public static UIButton skillButton = new InGameItemButton(4) {
        @Override
        protected boolean canUse() {
            return Screen.store != null && Screen.store.canSelectChosenSkill();
        }

        @Override
        public void onClick() {
            if (Screen.store != null) Screen.store.selectChosenSkill();
        }

        @Override
        protected void renderItem(Graphics g) {
            Image icon;
            int skillID = Constants.PLACEHOLDER;
            switch (Screen.chosenCard) {
                case 1:
                    icon = Screen.tiles.iconBH;
                    skillID = Constants.SPELL_BLACK_HOLE;
                    break;
                case 2:
                    icon = Screen.tiles.iconEnhance;
                    break;
                case 3:
                    icon = Screen.tiles.iconMercenary;
                    skillID = Constants.SPELL_MERCENARY;
                    break;
                default:
                    return;
            }

            int drawY = (state == 1) ? y - 3 : (state == 2) ? y + 3 : y;
            g.drawImage(state == 0 ? Screen.tiles.button : Screen.tiles.touchedButton, x, drawY, dx, dy, null);
            g.drawImage(icon, x + Store.itemIn, drawY + Store.itemIn, dx - Store.itemIn * 2, dy - Store.itemIn * 2, null);

            if (Screen.store == null) return;
            int buffSeconds = Screen.store.getEnhancementRemainingSeconds();
            int cooldownSeconds = Screen.store.getSkillCooldownSeconds();
            if (buffSeconds > 0 || cooldownSeconds > 0) {
                boolean buffActive = buffSeconds > 0;
                g.setColor(buffActive ? new Color(255, 180, 0, 100) : new Color(0, 0, 0, 160));
                g.fillRect(x, drawY, dx, dy);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Courier New", Font.BOLD, 16));
                String timer = (buffActive ? buffSeconds : cooldownSeconds) + "s";
                g.drawString(timer, x + (dx - g.getFontMetrics().stringWidth(timer)) / 2, drawY + dy / 2 + 6);
            } else if (Screen.store.holdsItem && Store.heldID == skillID) {
                g.setColor(new Color(255, 215, 0, 70));
                g.fillRect(x, drawY, dx, dy);
                g.setColor(Color.YELLOW);
                g.drawRect(x, drawY, dx, dy);
                g.drawRect(x + 1, drawY + 1, dx - 2, dy - 2);
            }
        }
    };

    // =========================================================================
    // 3. TITLE & MENU BUTTONS
    // =========================================================================

    public static UIButton playgame = createButton(880, 495, 144, 72, () -> {
        if (!Screen.unlockBH && !Screen.unlockEnhance && !Screen.unlockMercenary) {
            Screen.gameState = Constants.IN_GAME;
            Screen.startGame();
        } else {
            Screen.gameState = Constants.SKILL_SELECT;
        }
    });

    public static UIButton store = createButton(180, 590, 144, 72, () -> Screen.gameState = Constants.ITEM_SHOP);
    public static UIButton backToMenu = createButton(120, 672, 55, 37, () -> {
        Screen.gameState = Constants.TITLE_SCREEN;
        xuLyOutGame();
    });
    public static UIButton updateHero = createButton(180, 440, 144, 72, () -> {
        Screen.gameState = Constants.UPDATE_HERO;
        HeroUpgradeService.initUpgradeCosts();
    });
    public static UIButton quitgame = createButton(880, 570, 144, 72, () -> System.exit(0));
    public static UIButton toGacha = createButton(180, 515, 144, 72, () -> Screen.gameState = Constants.GACHA);
    public static UIButton setting = createButton(5, 5, 40, 45, () -> Screen.gameState = Constants.SETTING);

    // =========================================================================
    // 4. SKILL SELECT BUTTONS
    // =========================================================================

    public static UIButton selectSkillFrame = new ModalBackdropButton(50, 50, 1120, 680, () -> Screen.gameState = Constants.TITLE_SCREEN) {
        @Override
        public void renderNut(Graphics g) {
            g.drawImage(Screen.tiles.selectSkill, x, y, dx, dy, null);
            g.drawImage(Screen.tiles.cardBH, 140, 200, 300, 415, null);
            g.drawImage(Screen.tiles.cardEnh, 460, 200, 300, 415, null);
            g.drawImage(Screen.tiles.cardMer, 780, 200, 300, 415, null);
        }
    };

    public static UIButton selectBH = new CardSelectButton(210, 542, 160, 35, 1, "Black Hole", () -> Screen.unlockBH);
    public static UIButton selectEnh = new CardSelectButton(530, 542, 160, 35, 2, "Enhancement", () -> Screen.unlockEnhance);
    public static UIButton selectMer = new CardSelectButton(850, 542, 160, 35, 3, "Mercenary", () -> Screen.unlockMercenary);

    // =========================================================================
    // 5. ITEM SHOP BUTTONS
    // =========================================================================

    public static UIButton backInGameShop = createButton(5, 5, 50, 35, () -> Screen.gameState = Constants.TITLE_SCREEN);
    public static UIButton backInGacha = createButton(5, 5, 50, 35, () -> Screen.gameState = Constants.TITLE_SCREEN);

    public static UIButton buyGoldenPickaxe = new BuyItemButton(360, 465, 80, 30, 50, () -> Screen.goldenPickaxe++);
    public static UIButton buyFireSword = new BuyItemButton(632, 465, 80, 30, 50, () -> Screen.fireSword++);
    public static UIButton buyCursedSkull = new BuyItemButton(360, 648, 80, 30, 50, () -> Screen.cursedSkull++);
    public static UIButton buyPaperPlane = new BuyItemButton(632, 648, 80, 30, 50, () -> Screen.paperPlane++);

    public static UIButton buyHeart = new UIButton(938, 646, 56, 35) {
        @Override
        public void onClick() {
            if (Screen.coinage >= 35 && Screen.health < Screen.maxHealth) {
                Screen.coinage -= 35;
                Screen.health++;
            }
        }

        @Override
        public void renderNut(Graphics g) {
            if (Screen.health == Screen.maxHealth) {
                g.drawImage(Screen.tiles.fullHeart, x, y, dx, dy, null);
            } else if (Screen.coinage >= 50) {
                int drawY = (state == 2) ? y + 3 : y;
                Image img = (state == 0) ? Screen.tiles.buyHeart : Screen.tiles.buyHeartS;
                g.drawImage(img, x, drawY, dx, dy, null);
            } else {
                g.drawImage(Screen.tiles.buyHeartT, x, y, dx, dy, null);
            }
        }
    };

    // =========================================================================
    // 6. GACHA & SHARD SHOP BUTTONS
    // =========================================================================

    public static UIButton in4gacha = createButton(1110, 20, 40, 40, () -> Screen.gameState = Constants.GACHA_RATE);
    public static UIButton shardShop = createButton(1100, 80, 65, 65, () -> Screen.gameState = Constants.SHARD_SHOP);

    public static UIButton gachaBH = createButton(500, 345, 85, 50, () -> {
        Screen.gameState = Constants.NOTI;
        Screen.gachaType = Screen.gachaBH;
        GachaService.rollSkillGacha(GachaService.TARGET_BLACK_HOLE);
    });

    public static UIButton gachaEnhance = createButton(500, 475, 85, 50, () -> {
        Screen.gameState = Constants.NOTI;
        Screen.gachaType = Screen.gachaEnhance;
        GachaService.rollSkillGacha(GachaService.TARGET_ENHANCE);
    });

    public static UIButton gachaMer = createButton(500, 610, 90, 53, () -> {
        Screen.gameState = Constants.NOTI;
        Screen.gachaType = Screen.gachaMercenary;
        GachaService.rollSkillGacha(GachaService.TARGET_MERCENARY);
    });

    public static UIButton summon = createButton(800, 545, 125, 104, () -> {
        Screen.gameState = Constants.NOTI;
        Screen.gachaType = Screen.summon;
        GachaService.rollSummon();
    });

    public static UIButton iconShardShop = createButton(1110, 75, 50, 60, () -> Screen.gameState = Constants.SHARD_SHOP);
    public static UIButton info = createButton(1110, 20, 50, 50, () -> Screen.gameState = Constants.GACHA_RATE);

    public static UIButton gachaRateFrame = new ModalBackdropButton(300, 5, 600, 750, () -> Screen.gameState = Constants.GACHA);
    public static UIButton shardShopBigFramee = new ModalBackdropButton(60, 30, 900, 680, () -> Screen.gameState = Constants.GACHA);

    public static UIButton doiBH = new ShardExchangeButton(670, 210, 95, 30, 100, () -> Screen.unlockBH, GachaService::exchangeBlackHole);
    public static UIButton doiEnhance = new ShardExchangeButton(670, 290, 95, 30, 100, () -> Screen.unlockEnhance, GachaService::exchangeEnhance);
    public static UIButton doiMer = new ShardExchangeButton(670, 370, 95, 30, 100, () -> Screen.unlockMercenary, GachaService::exchangeMercenary);
    public static UIButton doiEC = new ShardExchangeButton(670, 450, 95, 30, 200, null, GachaService::exchangeEnergyCore);

    public static UIButton thongBaoFrame = new ModalBackdropButton(360, 140, 500, 400, () -> {
        Screen.gameState = Constants.GACHA;
        if (Screen.manageNut != null && Screen.manageNut.screen != null) {
            Screen.manageNut.screen.paintImmediately(0, 0, Screen.WIDTH, Screen.HEIGHT);
        }
    });

    // =========================================================================
    // 7. HERO UPGRADE BUTTONS
    // =========================================================================

    public static UIButton levelUp = new UIButton(980, 180, 110, 40) {
        @Override
        public void onClick() {
            HeroUpgradeService.levelUp();
        }

        @Override
        public void renderNut(Graphics g) {
            if (Screen.heroLevel >= 25) {
                g.drawImage(Screen.tiles.maxLevel, x, y, dx, dy, null);
            } else if (!HeroUpgradeService.canLevelUp()) {
                g.drawImage(Screen.tiles.levelUpT, x, y, dx, dy, null);
            } else {
                int drawY = (state == 1) ? y - 3 : (state == 2) ? y + 3 : y;
                Image img = (state == 0) ? ima[0] : ima[1];
                if (img != null) g.drawImage(img, x, drawY, dx, dy, null);
            }
        }
    };

    public static UIButton expBar = new UIButton(700, 170, 280, 60) {
        @Override
        public void renderNut(Graphics g) {
            if (state == 0) {
                g.drawImage(ima[0], x, y, dx, dy, null);
            } else {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(730, 185, 220, 36);
                g.drawImage(ima[1], x, y, dx, dy, null);
                g.setFont(Screen.tiles.pixelFontv1);
                g.setColor(Color.white);
                if (Screen.heroLevel < Screen.maxHeroLevel) {
                    g.drawString("Exp: " + Screen.heroXp + "/" + Screen.upHeroXp[Screen.heroLevel], 745, 208);
                } else {
                    g.drawString("Master of Battle!", 745, 208);
                }
            }
        }
    };

    public static UIButton add1 = new StatUpgradeButton(1090, 340, () -> Screen.buffHp, HeroUpgradeService::upgradeHp);
    public static UIButton add2 = new StatUpgradeButton(1090, 370, () -> Screen.buffHpRe, HeroUpgradeService::upgradeHpRegen);
    public static UIButton add3 = new StatUpgradeButton(1090, 400, () -> Screen.buffShield, HeroUpgradeService::upgradeShield);
    public static UIButton add4 = new StatUpgradeButton(1090, 430, () -> Screen.buffADmg, HeroUpgradeService::upgradeAttackDamage);
    public static UIButton add5 = new StatUpgradeButton(1090, 460, () -> Screen.buffSDmg, HeroUpgradeService::upgradeSkillDamage);

    // =========================================================================
    // 8. VICTORY & DEFEAT SUMMARY BUTTONS
    // =========================================================================

    public static UIButton menuLose = createButton(280, 580, 220, 55, () -> Screen.gameState = Constants.TITLE_SCREEN);
    public static UIButton menuWin = createButton(280, 580, 220, 55, () -> {
        Screen.rielLevel++;
        Screen.gameState = Constants.TITLE_SCREEN; 
    });

    public static UIButton again = createButton(720, 580, 220, 55, () -> {
        Screen.gameState = Constants.IN_GAME;
        Screen.startGame();
    });

    public static UIButton next = createButton(720, 580, 220, 55, () -> {
        Screen.rielLevel++;
        Screen.startGame();
    });

    public static void xuLyOutGame() {
        HeroUpgradeService.resetOutGame();
    }
}
