package service;

import core.Screen;

/**
 * Xu ly toan bo logic nang cap chi so, tieu diem tiem nang va len cap cho Hero.
 */
public class HeroUpgradeService {

    public static void initUpgradeCosts() {
        Screen.upHeroXp[1] = 200;
        Screen.upHeroCoin[1] = 50;
        Screen.upHeroShard[1] = 50;
        Screen.upHeroECore[1] = 1;

        for (int i = 2; i < 25; i++) {
            Screen.upHeroXp[i] = (int) (1.2 * Screen.upHeroXp[i - 1]);
            Screen.upHeroCoin[i] = (int) (1.2 * Screen.upHeroCoin[i - 1]);
            Screen.upHeroShard[i] = 50 * i;
            Screen.upHeroECore[i] = i;
        }
    }

    public static boolean canLevelUp() {
        if (Screen.heroLevel >= 25) return false;
        int curLv = Screen.heroLevel;
        return Screen.heroXp >= Screen.upHeroXp[curLv]
                && Screen.coinage >= Screen.upHeroCoin[curLv]
                && Screen.shard >= Screen.upHeroShard[curLv]
                && Screen.energyCore >= Screen.upHeroECore[curLv];
    }

    public static boolean levelUp() {
        if (!canLevelUp()) return false;
        int curLv = Screen.heroLevel;
        Screen.heroXp -= Screen.upHeroXp[curLv];
        Screen.coinage -= Screen.upHeroCoin[curLv];
        Screen.shard -= Screen.upHeroShard[curLv];
        Screen.energyCore -= Screen.upHeroECore[curLv];
        Screen.heroLevel++;
        Screen.remainingPoint += 5;
        Screen.maxPoint = 5 * Screen.heroLevel;
        return true;
    }

    public static boolean upgradeHp() {
        if (Screen.buffHp < Screen.maxBuff && Screen.remainingPoint > 0) {
            Screen.buffHp++;
            Screen.remainingPoint--;
            return true;
        }
        return false;
    }

    public static boolean upgradeHpRegen() {
        if (Screen.buffHpRe < Screen.maxBuff && Screen.remainingPoint > 0) {
            Screen.buffHpRe++;
            Screen.remainingPoint--;
            return true;
        }
        return false;
    }

    public static boolean upgradeShield() {
        if (Screen.buffShield < Screen.maxBuff && Screen.remainingPoint > 0) {
            Screen.buffShield++;
            Screen.remainingPoint--;
            return true;
        }
        return false;
    }

    public static boolean upgradeAttackDamage() {
        if (Screen.buffADmg < Screen.maxBuff && Screen.remainingPoint > 0) {
            Screen.buffADmg++;
            Screen.remainingPoint--;
            return true;
        }
        return false;
    }

    public static boolean upgradeSkillDamage() {
        if (Screen.buffSDmg < Screen.maxBuff && Screen.remainingPoint > 0) {
            Screen.buffSDmg++;
            Screen.remainingPoint--;
            return true;
        }
        return false;
    }

    public static void resetOutGame() {
        if (Screen.coinDauTran >= 60) {
            Screen.coinage = Screen.coinDauTran - 50;
        } else {
            Screen.coinage = 50;
        }

        if (Screen.healthDauTran >= 2) {
            Screen.health = Screen.healthDauTran - 1;
        } else {
            Screen.health = 1;
        }
    }
}
