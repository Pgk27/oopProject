package core;

import spell.CursedSkull;
import spell.Enhancement;
import spell.FireSword;
import spell.GoldenPickaxe;

/**
 * Quan ly toan bo trang thai cac buff tam thoi (Item Buffs)
 * va thoi gian hoi chieu (Cooldown) cua ky nang chon (Hero / Placement Skills).
 */
public class BuffManager {
    public static final long BLACK_HOLE_COOLDOWN_MS = 90_000;
    public static final long MERCENARY_COOLDOWN_MS = 15_000;
    public static final long ENHANCEMENT_COOLDOWN_MS = 45_000;

    private volatile long skillReadyAt;
    private volatile Enhancement enhancement;
    private volatile GoldenPickaxe goldenPickaxeBuff;
    private volatile FireSword fireSwordBuff;
    private volatile CursedSkull cursedSkullBuff;

    public boolean isSkillReady() {
        return System.currentTimeMillis() >= skillReadyAt;
    }

    public int getSkillCooldownSeconds() {
        if (getEnhancementRemainingSeconds() > 0) return 0;
        long remaining = Math.max(0, skillReadyAt - System.currentTimeMillis());
        return (int) ((remaining + 999) / 1000);
    }

    public long getSkillReadyAt() {
        return skillReadyAt;
    }

    public void setSkillReadyAt(long readyAt) {
        this.skillReadyAt = readyAt;
    }

    // --- ENHANCEMENT ---
    public int getEnhancementRemainingSeconds() {
        Enhancement buff = enhancement;
        return buff == null ? 0 : buff.getRemainingSeconds();
    }

    public double getHeroDamageMultiplier() {
        Enhancement buff = enhancement;
        return buff == null ? 1.0 : buff.getDamageMultiplier();
    }

    public void activateEnhancement() {
        Enhancement buff = new Enhancement();
        this.enhancement = buff;
        this.skillReadyAt = buff.getEndTime() + ENHANCEMENT_COOLDOWN_MS;
        Screen.activeSpells.add(buff);
    }

    // --- GOLDEN PICKAXE ---
    public int getGoldenPickaxeRemainingSeconds() {
        GoldenPickaxe buff = goldenPickaxeBuff;
        return buff == null ? 0 : buff.getRemainingSeconds();
    }

    public double getMineGoldMultiplier() {
        GoldenPickaxe buff = goldenPickaxeBuff;
        return buff == null ? 1.0 : buff.getGoldMultiplier();
    }

    public boolean canUseGoldenPickaxe() {
        GoldenPickaxe currentBuff = goldenPickaxeBuff;
        return Screen.gameState == Constants.IN_GAME && Screen.goldenPickaxe > 0
                && (currentBuff == null || currentBuff.isFinished());
    }

    public boolean useGoldenPickaxe() {
        if (!canUseGoldenPickaxe()) return false;
        GoldenPickaxe buff = new GoldenPickaxe();
        this.goldenPickaxeBuff = buff;
        Screen.activeSpells.add(buff);
        Screen.goldenPickaxe--;
        return true;
    }

    // --- FIRE SWORD ---
    public int getFireSwordRemainingSeconds() {
        FireSword buff = fireSwordBuff;
        return buff == null ? 0 : buff.getRemainingSeconds();
    }

    public double getTowerDamageMultiplier() {
        FireSword buff = fireSwordBuff;
        return buff == null ? 1.0 : buff.getDamageMultiplier();
    }

    public boolean canUseFireSword() {
        FireSword currentBuff = fireSwordBuff;
        return Screen.gameState == Constants.IN_GAME && Screen.fireSword > 0
                && (currentBuff == null || currentBuff.isFinished());
    }

    public boolean useFireSword() {
        if (!canUseFireSword()) return false;
        FireSword buff = new FireSword();
        this.fireSwordBuff = buff;
        Screen.activeSpells.add(buff);
        Screen.fireSword--;
        return true;
    }

    // --- CURSED SKULL ---
    public int getCursedSkullRemainingSeconds() {
        CursedSkull buff = cursedSkullBuff;
        return buff == null ? 0 : buff.getRemainingSeconds();
    }

    public double getMonsterSpeedMultiplier() {
        CursedSkull buff = cursedSkullBuff;
        return buff == null ? 1.0 : buff.getSpeedMultiplier();
    }

    public boolean canUseCursedSkull() {
        CursedSkull currentBuff = cursedSkullBuff;
        return Screen.gameState == Constants.IN_GAME && Screen.cursedSkull > 0
                && (currentBuff == null || currentBuff.isFinished());
    }

    public boolean useCursedSkull() {
        if (!canUseCursedSkull()) return false;
        CursedSkull buff = new CursedSkull();
        this.cursedSkullBuff = buff;
        Screen.activeSpells.add(buff);
        Screen.cursedSkull--;
        return true;
    }

    // --- SKILL COOLDOWN & RESET ---
    public boolean canSelectChosenSkill() {
        return Screen.gameState == Constants.IN_GAME && isSkillReady()
                && Screen.chosenCard >= 1 && Screen.chosenCard <= 3;
    }

    public void finishSkillPlacement(int chosenCard) {
        long cooldown = chosenCard == 1 ? BLACK_HOLE_COOLDOWN_MS : MERCENARY_COOLDOWN_MS;
        this.skillReadyAt = System.currentTimeMillis() + cooldown;
    }

    public void resetSkills() {
        this.enhancement = null;
        this.goldenPickaxeBuff = null;
        this.fireSwordBuff = null;
        this.cursedSkullBuff = null;
        this.skillReadyAt = 0;
    }
}
