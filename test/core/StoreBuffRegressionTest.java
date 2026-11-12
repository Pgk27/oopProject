package core;

import entity.Projectiles;
import entity.mob.Mercenary;
import entity.mob.Minion;
import entity.mob.Mob;
import entity.mob.Mob2;
import entity.mob.Mob3;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import map.Room;
import spell.CursedSkull;
import spell.Enhancement;
import spell.FireSword;
import spell.Spell;
import tower.CacherTower;
import tower.Cannon;
import tower.MageTower;
import tower.Tower;

/** Standalone regression checks; run with -Djava.awt.headless=true. */
public final class StoreBuffRegressionTest {
    private static int checks;

    public static void main(String[] args) throws Exception {
        inventoryAndExpiration();
        independentBuffsAndReset();
        purchases();
        placementCooldown(1, 90);
        placementCooldown(3, 15);
        enhancementCooldown();
        towerShots();
        monsterMovement();
        actionRowAndEmptyPlane();
        unavailableItemInteraction();
        unavailableSkillInteraction();
        enabledSelectionInteraction();
        System.out.println("StoreBuffRegressionTest: " + checks + " checks passed");
    }

    private static void setup() {
        Screen.gameState = Value.IN_GAME;
        Screen.chosenCard = 1;
        Screen.room = new Room();
        // Production map loading sets these IDs after Room construction.
        for (int y = 0; y < Screen.room.worldHeight; y++) {
            for (int x = 0; x < Screen.room.worldWidth; x++) {
                Screen.room.block[y][x].towerID = Value.PLACEHOLDER;
            }
        }
        Screen.store = new Store();
        Screen.activeSpells.clear();
        Screen.pjt.clear();
        Screen.mobs = new Mob[0];
        Screen.mobss = new Mob2[0];
        Screen.mobsss = new Mob3[0];
        Screen.mini = new Minion[0];
        Screen.merc = new Mercenary[0];
        Screen.fireSword = 0;
        Screen.cursedSkull = 0;
        Screen.goldenPickaxe = 0;
        Screen.paperPlane = 0;
        Screen.mse = new Point(-100, -100);
    }

    private static void inventoryAndExpiration() throws Exception {
        setup();
        Screen.fireSword = Screen.cursedSkull = 2;
        Screen.gameState = Value.ITEM_SHOP;
        Screen.store.useFireSword();
        Screen.store.useCursedSkull();
        check(Screen.fireSword == 2 && Screen.cursedSkull == 2
                && Screen.activeSpells.isEmpty(), "Items cannot activate before the match");

        Screen.gameState = Value.IN_GAME;
        Screen.fireSword = Screen.cursedSkull = 0;
        Screen.store.useFireSword();
        Screen.store.useCursedSkull();
        check(Screen.activeSpells.isEmpty(), "Empty inventory cannot activate buffs");

        Screen.fireSword = Screen.cursedSkull = 2;
        Screen.store.useFireSword();
        Screen.store.useCursedSkull();
        check(Screen.fireSword == 1 && Screen.cursedSkull == 1,
                "Each buff consumes one purchased item");
        check(Screen.activeSpells.size() == 2, "Both buffs register with the spell lifecycle");
        close(1.5, Screen.store.getTowerDamageMultiplier(), "Fire Sword grants 50% tower damage");
        close(0.7, Screen.store.getMonsterSpeedMultiplier(), "Cursed Skull grants 30% slow");
        check(inRange(Screen.store.getFireSwordRemainingSeconds(), 119, 120)
                && inRange(Screen.store.getCursedSkullRemainingSeconds(), 119, 120),
                "New buffs last two minutes");
        Screen.store.useFireSword();
        Screen.store.useCursedSkull();
        check(Screen.fireSword == 1 && Screen.cursedSkull == 1
                && Screen.activeSpells.size() == 2, "Repeated clicks do not waste active buffs");

        FireSword fire = active(FireSword.class);
        CursedSkull skull = active(CursedSkull.class);
        age(fire, 119_000);
        age(skull, 119_000);
        check(Screen.store.getFireSwordRemainingSeconds() == 1
                && Screen.store.getCursedSkullRemainingSeconds() == 1,
                "Timers count down before expiration");
        age(fire, 120_010);
        age(skull, 120_010);
        check(Screen.store.getFireSwordRemainingSeconds() == 0
                && Screen.store.getCursedSkullRemainingSeconds() == 0,
                "Timers reach zero when buffs expire");
        close(1.0, Screen.store.getTowerDamageMultiplier(), "Tower damage restores automatically");
        close(1.0, Screen.store.getMonsterSpeedMultiplier(), "Monster speed restores automatically");
        Screen.store.useFireSword();
        Screen.store.useCursedSkull();
        check(Screen.fireSword == 0 && Screen.cursedSkull == 0,
                "Expired buffs can be replaced using the next item");
        close(1.5, Screen.store.getTowerDamageMultiplier(), "New Fire Sword takes effect");
        close(0.7, Screen.store.getMonsterSpeedMultiplier(), "New Cursed Skull takes effect");
    }

    private static void independentBuffsAndReset() throws Exception {
        setup();
        Screen.fireSword = Screen.cursedSkull = Screen.goldenPickaxe = 1;
        Screen.store.useFireSword();
        Screen.store.useCursedSkull();
        Screen.store.useGoldenPickaxe();
        Screen.chosenCard = 2;
        Screen.store.selectChosenSkill();
        close(1.5, Screen.store.getMineGoldMultiplier(), "Pickaxe coexists with new buffs");
        close(1.5, Screen.store.getHeroDamageMultiplier(), "Enhancement coexists with new buffs");
        age(active(FireSword.class), 120_010);
        close(1.0, Screen.store.getTowerDamageMultiplier(), "One buff can expire independently");
        close(0.7, Screen.store.getMonsterSpeedMultiplier(), "Cursed Skull remains active");
        close(1.5, Screen.store.getHeroDamageMultiplier(), "Enhancement remains active");
        Screen.store.resetSkills();
        close(1.0, Screen.store.getTowerDamageMultiplier(), "Match reset clears Fire Sword");
        close(1.0, Screen.store.getMonsterSpeedMultiplier(), "Match reset clears Cursed Skull");
        close(1.0, Screen.store.getMineGoldMultiplier(), "Match reset clears Golden Pickaxe");
        close(1.0, Screen.store.getHeroDamageMultiplier(), "Match reset clears Enhancement");
        check(Screen.store.getFireSwordRemainingSeconds() == 0
                && Screen.store.getCursedSkullRemainingSeconds() == 0
                && Screen.store.isSkillReady(), "Match reset clears timers and cooldown");
    }

    private static void purchases() {
        setup();
        Screen.gameState = Value.ITEM_SHOP;
        Screen.coinage = 49;
        ManageNut.buyFireSword.onClick();
        ManageNut.buyCursedSkull.onClick();
        check(Screen.coinage == 49 && Screen.fireSword == 0 && Screen.cursedSkull == 0,
                "Insufficient gold cannot purchase either new buff");
        Screen.coinage = 100;
        ManageNut.buyFireSword.onClick();
        ManageNut.buyCursedSkull.onClick();
        check(Screen.coinage == 0 && Screen.fireSword == 1 && Screen.cursedSkull == 1,
                "Pre-match shop charges 50 gold per new item");
    }

    private static void placementCooldown(int card, int seconds) throws Exception {
        setup();
        Screen.chosenCard = card;
        for (int y = 4; y <= 5; y++) {
            for (int x = 4; x <= 5; x++) Screen.room.block[y][x].groundID = Value.groundRoad;
        }
        if (card == 3) Screen.merc = new Mercenary[] {new Mercenary()};
        Screen.store.selectChosenSkill();
        check(Screen.store.holdsItem && Screen.store.isSkillReady(),
                "Selecting skill does not begin its cooldown");
        Screen.mse = new Point(Screen.room.block[5][5].x + 10, Screen.room.block[5][5].y + 10);
        long before = System.currentTimeMillis();
        Screen.store.click(1);
        long after = System.currentTimeMillis();
        long readyAt = (long) read(Store.class, Screen.store, "skillReadyAt");
        check(readyAt >= before + seconds * 1000L && readyAt <= after + seconds * 1000L,
                "Placed card " + card + " starts the requested " + seconds + "s cooldown");
        check(!Screen.store.holdsItem && !Screen.store.isSkillReady(),
                "Successful placement clears selection and locks skill");
        if (card == 1) check(Screen.activeSpells.size() == 1, "Black Hole is placed");
        else check(Screen.merc[0].inGame, "Mercenary is placed");
        Screen.store.selectChosenSkill();
        check(!Screen.store.holdsItem, "Skill cannot be selected again during cooldown");
        write(Store.class, Screen.store, "skillReadyAt", System.currentTimeMillis() - 1);
        check(Screen.store.isSkillReady() && Screen.store.getSkillCooldownSeconds() == 0,
                "Skill becomes available at the end of cooldown");
    }

    private static void enhancementCooldown() throws Exception {
        setup();
        Screen.chosenCard = 2;
        Screen.store.selectChosenSkill();
        Enhancement buff = active(Enhancement.class);
        check(inRange(Screen.store.getEnhancementRemainingSeconds(), 29, 30),
                "Enhancement retains its 30-second effect");
        long readyAt = (long) read(Store.class, Screen.store, "skillReadyAt");
        check(readyAt - buff.getEndTime() == 45_000,
                "Enhancement cooldown is 45 seconds after its effect ends");
        check(!Screen.store.isSkillReady() && Screen.store.getSkillCooldownSeconds() == 0,
                "Active Enhancement displays its effect timer before cooldown");
        Screen.store.selectChosenSkill();
        check(Screen.activeSpells.size() == 1, "Enhancement cannot stack while active");
        age(buff, 30_010);
        write(Store.class, Screen.store, "skillReadyAt", buff.getEndTime() + 45_000);
        close(1.0, Screen.store.getHeroDamageMultiplier(), "Hero damage restores after 30 seconds");
        check(Screen.store.getEnhancementRemainingSeconds() == 0
                && Screen.store.getSkillCooldownSeconds() == 45 && !Screen.store.isSkillReady(),
                "A full 45-second cooldown follows Enhancement");
    }

    private static void towerShots() throws Exception {
        setup();
        List<Supplier<Tower>> towers = Arrays.asList(
            () -> new CacherTower(100, 100, 60, 60),
            () -> new MageTower(100, 100, 60, 60),
            () -> new Cannon(100, 100, 60, 60));
        double[] armor = {0, 0.25, 0.5};
        double[][] baseline = new double[towers.size()][armor.length];
        for (int i = 0; i < towers.size(); i++) {
            for (int j = 0; j < armor.length; j++) {
                baseline[i][j] = shotDamage(towers.get(i).get(), armor[j]);
            }
        }
        Screen.fireSword = 1;
        Screen.store.useFireSword();
        for (int i = 0; i < towers.size(); i++) {
            for (int j = 0; j < armor.length; j++) {
                close(baseline[i][j] * 1.5, shotDamage(towers.get(i).get(), armor[j]),
                        "Fire Sword buffs actual health lost from tower " + i + ", armor " + armor[j]);
            }
        }
        age(active(FireSword.class), 120_010);
        for (int i = 0; i < towers.size(); i++) {
            for (int j = 0; j < armor.length; j++) {
                close(baseline[i][j], shotDamage(towers.get(i).get(), armor[j]),
                        "Actual health loss restores for tower " + i + ", armor " + armor[j]);
            }
        }
    }

    private static double shotDamage(Tower tower, double armor) throws Exception {
        Mob target = new Mob();
        target.setBounds(120, 120, 32, 32);
        target.health = 1000;
        target.inGame = true;
        target.dmgReduction = armor;
        Screen.mobs = new Mob[] {target};
        Screen.pjt.clear();
        write(Tower.class, tower, "shootSound", null);
        write(Tower.class, tower, "towerAnimFrame", read(Tower.class, tower, "shootFrame"));
        tower.physic();
        check(Screen.pjt.size() == 1, "Tower emits one projectile at its firing frame");
        Projectiles projectile = Screen.pjt.get(0);
        for (int i = 0; i < 100 && !projectile.hasRemoved; i++) projectile.physic();
        check(projectile.hasRemoved, "Projectile reaches its target");
        return 1000.0 - target.health;
    }

    private static void monsterMovement() throws Exception {
        setup();
        for (int x = 0; x < Screen.room.worldWidth; x++) {
            Screen.room.block[5][x].groundID = Value.groundRoad;
        }
        List<Supplier<Mob>> types = Arrays.asList(Mob::new, Mob2::new, Mob3::new);
        int[] baseline = new int[types.size()];
        Mob[] existing = new Mob[types.size()];
        for (int i = 0; i < types.size(); i++) {
            existing[i] = types.get(i).get();
            baseline[i] = distanceAfterTicks(existing[i], 600);
        }
        Screen.cursedSkull = 1;
        Screen.store.useCursedSkull();
        for (int i = 0; i < types.size(); i++) {
            int slowed = distanceAfterTicks(types.get(i).get(), 600);
            check(Math.abs(slowed - baseline[i] * 0.7) <= 1,
                    "Cursed Skull slows newly spawned monster type " + i + " by approximately 30%");
            check(Math.abs(advanceDistance(existing[i], 600) - baseline[i] * 0.7) <= 1,
                    "Cursed Skull also slows existing monster type " + i);
        }
        age(active(CursedSkull.class), 120_010);
        for (int i = 0; i < types.size(); i++) {
            check(distanceAfterTicks(types.get(i).get(), 600) == baseline[i],
                    "New monsters move normally after expiration for type " + i);
            check(Math.abs(advanceDistance(existing[i], 600) - baseline[i]) <= 1,
                    "Existing monster speed restores after expiration for type " + i);
        }
    }

    private static int distanceAfterTicks(Mob mob, int ticks) {
        mob.spawnMob(Value.ORC_MONSTER);
        return advanceDistance(mob, ticks);
    }

    private static int advanceDistance(Mob mob, int ticks) {
        int start = mob.x;
        for (int i = 0; i < ticks; i++) mob.physic();
        return mob.x - start;
    }

    private static void actionRowAndEmptyPlane() {
        setup();
        NutBamVip[] buttons = {ManageNut.GoldenPickaxeButton, ManageNut.fireSwordButton,
            ManageNut.cursedSkullButton, ManageNut.bomberButton, ManageNut.skillButton};
        for (int i = 0; i < buttons.length; i++) {
            Rectangle bounds = Screen.store.getItemButtonBounds(i);
            Rectangle trash = Screen.store.button[Screen.store.button.length - 1];
            check(bounds.x == trash.x + (i + 1) * (trash.width + Store.cellSpace)
                    && bounds.width == trash.width && bounds.height == trash.height && bounds.y == trash.y,
                    "Item column " + i + " follows the trash button in the same row");
            check(bounds.y + bounds.height <= Screen.VIRTUAL_HEIGHT,
                    "Item column " + i + " fits in the game viewport");
            check(buttons[i].contains(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2),
                    "Visible item column " + i + " accepts clicks");
            check(!buttons[i].contains(bounds.x, bounds.y - 1),
                    "Item column " + i + " does not accept clicks above its bounds");
        }
        Screen.tiles = new Tiles();
        BufferedImage white = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
        Graphics2D whiteGraphics = white.createGraphics();
        whiteGraphics.setColor(Color.WHITE);
        whiteGraphics.fillRect(0, 0, 8, 8);
        whiteGraphics.dispose();
        Screen.tiles.button = Screen.tiles.touchedButton = Screen.tiles.iconGoldenPickaxe = white;
        Screen.airplane = white;
        int emptyPlane = centerPixel(ManageNut.bomberButton, 3);
        int emptyPickaxe = centerPixel(ManageNut.GoldenPickaxeButton, 0);
        Screen.paperPlane = 1;
        int availablePlane = centerPixel(ManageNut.bomberButton, 3);
        check(emptyPlane == emptyPickaxe && emptyPlane < availablePlane,
                "An empty Paper Plane is darkened like an empty Golden Pickaxe");
    }

    private static int centerPixel(NutBamVip button, int column) {
        BufferedImage canvas = new BufferedImage(Screen.VIRTUAL_WIDTH, Screen.VIRTUAL_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = canvas.createGraphics();
        button.state = 0;
        button.renderNut(graphics);
        graphics.dispose();
        Rectangle bounds = Screen.store.getItemButtonBounds(column);
        return canvas.getRGB(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2) & 0xFFFFFF;
    }

    private static void unavailableItemInteraction() throws Exception {
        configureInteractionImages();
        new ManageNut(null);
        NutBamVip[] buffs = {ManageNut.GoldenPickaxeButton, ManageNut.fireSwordButton,
            ManageNut.cursedSkullButton};
        for (int i = 0; i < buffs.length; i++) {
            setup();
            NutBamVip button = buffs[i];
            assertUnavailable(button, i, "Empty buff " + i);
            setInventory(i, 2);
            clickEnabled(button, i, "Available buff " + i);
            check(getInventory(i) == 1 && Screen.activeSpells.size() == 1,
                    "Available buff " + i + " activates exactly once");
            check(button.state == 0, "Buff " + i + " loses hover immediately after activation");
            assertUnavailable(button, i, "Already active buff " + i);

            setup();
            setInventory(i, 1);
            clickEnabled(button, i, "Last available buff " + i);
            check(getInventory(i) == 0 && button.state == 0,
                    "Last buff " + i + " is consumed without leaving a hover effect");
        }
        setup();
        assertUnavailable(ManageNut.bomberButton, 3, "Empty Paper Plane");
        Rectangle towerButton = Screen.store.button[0];
        check(KeyHandel.isHoveringButton(towerButton.x + 10, towerButton.y + 10)
                && !KeyHandel.isOverDisabledGameButton(towerButton.x + 10, towerButton.y + 10),
                "Tower store buttons keep their cursor feedback");

        setup();
        Screen.fireSword = 1;
        Rectangle bounds = Screen.store.getItemButtonBounds(1);
        int mouseX = bounds.x + bounds.width / 2;
        int mouseY = bounds.y + bounds.height / 2;
        ManageNut.fireSwordButton.mousePressed(mouseX, mouseY);
        Screen.fireSword = 0;
        ManageNut.fireSwordButton.mouseReleased(mouseX, mouseY);
        check(ManageNut.fireSwordButton.state == 0 && Screen.activeSpells.isEmpty(),
                "Losing availability between press and release cancels interaction");
    }

    private static void unavailableSkillInteraction() throws Exception {
        setup();
        Screen.chosenCard = 0;
        assertUnavailable(ManageNut.skillButton, 4, "No selected skill");
        for (int card = 1; card <= 3; card++) {
            setup();
            Screen.chosenCard = card;
            write(Store.class, Screen.store, "skillReadyAt", System.currentTimeMillis() + 90_000);
            assertUnavailable(ManageNut.skillButton, 4, "Cooling skill " + card);
        }
        setup();
        Screen.chosenCard = 2;
        clickEnabled(ManageNut.skillButton, 4, "Ready Enhancement");
        check(Screen.store.getEnhancementRemainingSeconds() > 0 && ManageNut.skillButton.state == 0,
                "Enhancement activation immediately clears its hover effect");
        assertUnavailable(ManageNut.skillButton, 4, "Active Enhancement");
    }

    private static void enabledSelectionInteraction() throws Exception {
        setup();
        Screen.paperPlane = 1;
        clickEnabled(ManageNut.bomberButton, 3, "Available Paper Plane");
        check(Screen.store.holdsItem && Store.heldID == Value.SPELL_BOMBER_PLANE,
                "An available Paper Plane can be selected");
        clickEnabled(ManageNut.bomberButton, 3, "Selected Paper Plane");
        check(!Screen.store.holdsItem && Screen.paperPlane == 1,
                "Clicking a selected Paper Plane cancels selection without spending it");

        for (int card : new int[] {1, 3}) {
            setup();
            Screen.chosenCard = card;
            clickEnabled(ManageNut.skillButton, 4, "Ready placement skill " + card);
            check(Screen.store.holdsItem, "Ready skill " + card + " can be selected");
            clickEnabled(ManageNut.skillButton, 4, "Selected placement skill " + card);
            check(!Screen.store.holdsItem, "Selected skill " + card + " can be deselected");
        }

        setup();
        Screen.fireSword = Screen.cursedSkull = Screen.goldenPickaxe = Screen.paperPlane = 1;
        Screen.gameState = Value.TITLE_SCREEN;
        NutBamVip[] buttons = {ManageNut.GoldenPickaxeButton, ManageNut.fireSwordButton,
            ManageNut.cursedSkullButton, ManageNut.bomberButton, ManageNut.skillButton};
        for (int i = 0; i < buttons.length; i++) {
            assertUnavailable(buttons[i], i, "Control outside a match " + i);
        }

        final int[] ordinaryClicks = {0};
        NutBamVip ordinary = new NutBamVip(10, 10, 20, 20) {
            @Override
            public void onClick() { ordinaryClicks[0]++; }
        };
        check(ordinary.isEnabled(), "Ordinary menu controls remain enabled by default");
        ordinary.updateMouse(15, 15);
        check(ordinary.state == 1, "Ordinary menu controls retain hover feedback");
        ordinary.mousePressed(15, 15);
        check(ordinary.state == 2, "Ordinary menu controls retain press feedback");
        ordinary.mouseReleased(15, 15);
        check(ordinaryClicks[0] == 1 && ordinary.state == 1,
                "Ordinary menu controls still click normally");
    }

    private static void assertUnavailable(NutBamVip button, int column, String label) throws Exception {
        Rectangle bounds = Screen.store.getItemButtonBounds(column);
        int mouseX = bounds.x + bounds.width / 2;
        int mouseY = bounds.y + bounds.height / 2;
        check(button.contains(mouseX, mouseY), label + " keeps its geometric hitbox");
        check(!button.isEnabled(), label + " is unavailable");
        if (Screen.gameState == Value.IN_GAME) {
            check(!KeyHandel.isHoveringButton(mouseX, mouseY)
                    && KeyHandel.isOverDisabledGameButton(mouseX, mouseY),
                    label + " uses the normal cursor even during a click");
        }
        button.updateMouse(mouseX, mouseY);
        check(button.state == 0, label + " has no hover feedback");
        button.mousePressed(mouseX, mouseY);
        check(button.state == 0 && !button.startPressInNut, label + " has no press feedback");
        int[] inventory = {Screen.goldenPickaxe, Screen.fireSword, Screen.cursedSkull, Screen.paperPlane};
        int spells = Screen.activeSpells.size();
        long readyAt = (long) read(Store.class, Screen.store, "skillReadyAt");
        int heldID = Store.heldID;
        boolean holding = Screen.store.holdsItem;
        button.mouseReleased(mouseX, mouseY);
        check(Arrays.equals(inventory, new int[] {Screen.goldenPickaxe, Screen.fireSword,
                Screen.cursedSkull, Screen.paperPlane}) && spells == Screen.activeSpells.size()
                && readyAt == (long) read(Store.class, Screen.store, "skillReadyAt")
                && heldID == Store.heldID && holding == Screen.store.holdsItem,
                label + " does not trigger an action");
        button.state = 0;
        int[] normal = edgePixels(button, bounds);
        for (int staleState : new int[] {1, 2}) {
            button.state = staleState;
            button.startPressInNut = true;
            int[] actual = edgePixels(button, bounds);
            check(button.state == 0 && !button.startPressInNut,
                    label + " clears stale interaction state " + staleState + " on draw");
            check(Arrays.equals(normal, actual),
                    label + " keeps its normal background and position for stale state " + staleState);
        }
    }

    private static void clickEnabled(NutBamVip button, int column, String label) {
        Rectangle bounds = Screen.store.getItemButtonBounds(column);
        int mouseX = bounds.x + bounds.width / 2;
        int mouseY = bounds.y + bounds.height / 2;
        check(button.isEnabled(), label + " permits interaction");
        check(KeyHandel.isHoveringButton(mouseX, mouseY)
                && !KeyHandel.isOverDisabledGameButton(mouseX, mouseY),
                label + " permits cursor feedback");
        button.updateMouse(mouseX, mouseY);
        check(button.state == 1, label + " has hover feedback");
        button.mousePressed(mouseX, mouseY);
        check(button.state == 2 && button.startPressInNut, label + " has press feedback");
        button.mouseReleased(mouseX, mouseY);
    }

    private static int[] edgePixels(NutBamVip button, Rectangle bounds) {
        BufferedImage canvas = new BufferedImage(Screen.VIRTUAL_WIDTH, Screen.VIRTUAL_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = canvas.createGraphics();
        button.renderNut(graphics);
        graphics.dispose();
        return canvas.getRGB(bounds.x + 1, bounds.y - 4, 2, bounds.height + 8, null, 0, 2);
    }

    private static void configureInteractionImages() {
        BufferedImage normal = solidImage(Color.WHITE);
        Screen.tiles.button = normal;
        Screen.tiles.touchedButton = solidImage(Color.RED);
        Screen.tiles.iconGoldenPickaxe = Screen.tiles.iconFireSword = Screen.tiles.iconCursedSkull = normal;
        Screen.tiles.iconBH = Screen.tiles.iconEnhance = Screen.tiles.iconMercenary = normal;
        Screen.airplane = normal;
    }

    private static BufferedImage solidImage(Color color) {
        BufferedImage image = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();
        return image;
    }

    private static void setInventory(int column, int quantity) {
        if (column == 0) Screen.goldenPickaxe = quantity;
        else if (column == 1) Screen.fireSword = quantity;
        else Screen.cursedSkull = quantity;
    }

    private static int getInventory(int column) {
        return column == 0 ? Screen.goldenPickaxe : column == 1 ? Screen.fireSword : Screen.cursedSkull;
    }

    private static <T extends Spell> T active(Class<T> type) {
        for (Spell spell : Screen.activeSpells) if (type.isInstance(spell)) return type.cast(spell);
        throw new AssertionError("Missing active spell " + type.getSimpleName());
    }

    private static void age(Spell spell, long ageMs) throws Exception {
        write(Spell.class, spell, "spawnTime", System.currentTimeMillis() - ageMs);
    }

    private static Object read(Class<?> type, Object instance, String name) throws Exception {
        Field field = type.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(instance);
    }

    private static void write(Class<?> type, Object instance, String name, Object value) throws Exception {
        Field field = type.getDeclaredField(name);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static void close(double expected, double actual, String message) {
        check(Math.abs(expected - actual) < 0.000001, message + " (actual " + actual + ")");
    }

    private static boolean inRange(int value, int low, int high) {
        return value >= low && value <= high;
    }

    private static void check(boolean condition, String message) {
        checks++;
        if (!condition) throw new AssertionError(message);
    }
}
