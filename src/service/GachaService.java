package service;

import core.Screen;

/**
 * Xu ly toan bo logic quay thuong (Gacha), ty le roi do va doi manh (Shard Exchange).
 */
public class GachaService {
    public static final int TARGET_BLACK_HOLE = 1;
    public static final int TARGET_ENHANCE = 2;
    public static final int TARGET_MERCENARY = 3;

    /**
     * Quay gacha ky nang don (BlackHole, Enhance, Mercenary)
     * Chi phi: 1 Coin (khi coinage >= 11)
     */
    public static void rollSkillGacha(int target) {
        if (Screen.coinage < 11) {
            Screen.ok = false;
            return;
        }

        Screen.coinage -= 1;
        Screen.ok = true;
        Screen.randomNum = Screen.rand.nextInt(100) + 1;

        if (Screen.randomNum <= 2) {
            if (target == TARGET_BLACK_HOLE) Screen.unlockBH = true;
            else if (target == TARGET_ENHANCE) Screen.unlockEnhance = true;
            else if (target == TARGET_MERCENARY) Screen.unlockMercenary = true;
        } else if (Screen.randomNum <= 5) {
            Screen.shard += 50;
        } else if (Screen.randomNum <= 10) {
            Screen.shard += 20;
        } else if (Screen.randomNum <= 20) {
            Screen.shard += 10;
        } else if (Screen.randomNum <= 30) {
            Screen.shard += 5;
        } else if (Screen.randomNum <= 45) {
            Screen.shard += 3;
        } else if (Screen.randomNum <= 65) {
            Screen.shard += 2;
        } else {
            Screen.shard += 1;
        }
    }

    /**
     * Trieu hoi dac biet (Summon)
     * Chi phi: 2 Coin (khi coinage >= 12)
     */
    public static void rollSummon() {
        if (Screen.coinage < 12) {
            Screen.ok = false;
            return;
        }

        Screen.coinage -= 2;
        Screen.ok = true;
        Screen.randomNum = Screen.rand.nextInt(100) + 1;

        if (Screen.randomNum == 1) {
            Screen.energyCore++;
        } else if (Screen.randomNum == 2) {
            Screen.shard += 100;
        } else if (Screen.randomNum <= 5) {
            Screen.shard += 50;
        } else if (Screen.randomNum <= 20) {
            Screen.shard += 35;
        } else if (Screen.randomNum <= 40) {
            Screen.shard += 20;
        } else if (Screen.randomNum <= 60) {
            Screen.shard += 10;
        } else if (Screen.randomNum <= 80) {
            Screen.shard += 5;
        } else {
            Screen.shard += 3;
        }
    }

    public static boolean exchangeBlackHole() {
        if (Screen.coinage >= 100 && Screen.shard >= 100 && !Screen.unlockBH) {
            Screen.coinage -= 100;
            Screen.shard -= 100;
            Screen.unlockBH = true;
            return true;
        }
        return false;
    }

    public static boolean exchangeEnhance() {
        if (Screen.coinage >= 100 && Screen.shard >= 100 && !Screen.unlockEnhance) {
            Screen.coinage -= 100;
            Screen.shard -= 100;
            Screen.unlockEnhance = true;
            return true;
        }
        return false;
    }

    public static boolean exchangeMercenary() {
        if (Screen.coinage >= 100 && Screen.shard >= 100 && !Screen.unlockMercenary) {
            Screen.coinage -= 100;
            Screen.shard -= 100;
            Screen.unlockMercenary = true;
            return true;
        }
        return false;
    }

    public static boolean exchangeEnergyCore() {
        if (Screen.coinage >= 200 && Screen.shard >= 200) {
            Screen.coinage -= 200;
            Screen.shard -= 200;
            Screen.energyCore += 1;
            return true;
        }
        return false;
    }
}
