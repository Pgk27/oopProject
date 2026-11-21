package wave;
import java.util.ArrayList;
import java.util.List;

import core.Constants;

/**
 * Lớp cấu hình toàn bộ Data cho các Màn chơi (Level).
 * Được thiết kế theo hướng Data-Driven: tách biệt logic game và dữ liệu màn chơi.
 * Bạn có thể dễ dàng thay đổi thời gian, số lượng quái và sức mạnh quái ở đây mà không cần sửa code cốt lõi.
 */
public class LevelConfig {
    public List<WaveData> waves;

    public LevelConfig() {
        this.waves = new ArrayList<>();
    }

    public static LevelConfig getLevelConfig(int level) {
        LevelConfig config = new LevelConfig();

        if (level == 1) {
            // LEVEL 1: The Frontier Defense (Trung bình - Dễ)
            // Wave 1: 8 Orc + 2 Slime
            WaveData w1 = new WaveData(3000);
            w1.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 8, 500, 1.0, 1.0, 1.0, 2));
            w1.addSpawn(new MobSpawnInfo(Constants.SLIME_MONSTER, 2, 700, 1.0, 1.0, 1.0, 1));
            config.waves.add(w1);

            // Wave 2: 12 Orc + 4 Demon + 3 Slime
            WaveData w2 = new WaveData(30000);
            w2.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 12, 450, 1.0, 1.0, 1.0, 3));
            w2.addSpawn(new MobSpawnInfo(Constants.BLOOD_DEMON_MONSTER, 4, 600, 1.0, 1.0, 1.0, 2));
            w2.addSpawn(new MobSpawnInfo(Constants.SLIME_MONSTER, 3, 650, 1.0, 1.0, 1.0, 1));
            config.waves.add(w2);

            // Wave 3: 16 Orc + 8 Demon + 5 Slime
            WaveData w3 = new WaveData(65000);
            w3.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 16, 420, 1.05, 1.0, 1.0, 4));
            w3.addSpawn(new MobSpawnInfo(Constants.BLOOD_DEMON_MONSTER, 8, 500, 1.05, 1.0, 1.0, 2));
            w3.addSpawn(new MobSpawnInfo(Constants.SLIME_MONSTER, 5, 600, 1.1, 1.0, 1.0, 2));
            config.waves.add(w3);

        } else if (level == 2) {
            // LEVEL 2: The Blighted Valley (Trung bình - Dễ)
            // Wave 1: 15 Orc + 6 Demon
            WaveData w1 = new WaveData(4000);
            w1.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 15, 420, 1.05, 1.0, 1.0, 3));
            w1.addSpawn(new MobSpawnInfo(Constants.BLOOD_DEMON_MONSTER, 6, 550, 1.05, 1.0, 1.0, 2));
            config.waves.add(w1);

            // Wave 2: 12 Demon + 8 Slime + 14 Orc
            WaveData w2 = new WaveData(40000);
            w2.addSpawn(new MobSpawnInfo(Constants.BLOOD_DEMON_MONSTER, 12, 450, 1.1, 1.0, 1.0, 3));
            w2.addSpawn(new MobSpawnInfo(Constants.SLIME_MONSTER, 8, 550, 1.15, 1.0, 1.0, 2));
            w2.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 14, 400, 1.1, 1.0, 1.0, 3));
            config.waves.add(w2);

            // Wave 3: 20 Orc + 14 Demon + 10 Slime
            WaveData w3 = new WaveData(85000);
            w3.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 20, 380, 1.15, 1.05, 1.0, 4));
            w3.addSpawn(new MobSpawnInfo(Constants.BLOOD_DEMON_MONSTER, 14, 450, 1.15, 1.0, 1.0, 3));
            w3.addSpawn(new MobSpawnInfo(Constants.SLIME_MONSTER, 10, 500, 1.2, 1.0, 1.0, 2));
            config.waves.add(w3);

        } else {
            // LEVEL 3: The Abyssal Citadel (Trung bình - Dễ)
            // Wave 1: 18 Orc + 10 Demon + 6 Slime
            WaveData w1 = new WaveData(4000);
            w1.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 18, 400, 1.1, 1.05, 1.0, 3));
            w1.addSpawn(new MobSpawnInfo(Constants.BLOOD_DEMON_MONSTER, 10, 500, 1.1, 1.0, 1.0, 2));
            w1.addSpawn(new MobSpawnInfo(Constants.SLIME_MONSTER, 6, 550, 1.15, 1.0, 1.0, 2));
            config.waves.add(w1);

            // Wave 2: 18 Demon + 10 Slime + 20 Orc
            WaveData w2 = new WaveData(45000);
            w2.addSpawn(new MobSpawnInfo(Constants.BLOOD_DEMON_MONSTER, 18, 420, 1.15, 1.0, 1.0, 3));
            w2.addSpawn(new MobSpawnInfo(Constants.SLIME_MONSTER, 10, 500, 1.2, 1.0, 1.0, 2));
            w2.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 20, 380, 1.15, 1.05, 1.0, 4));
            config.waves.add(w2);

            // Wave 3: 24 Orc + 16 Demon + 12 Slime
            WaveData w3 = new WaveData(95000);
            w3.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 24, 350, 1.2, 1.1, 1.0, 4));
            w3.addSpawn(new MobSpawnInfo(Constants.BLOOD_DEMON_MONSTER, 16, 400, 1.2, 1.05, 1.0, 3));
            w3.addSpawn(new MobSpawnInfo(Constants.SLIME_MONSTER, 12, 480, 1.25, 1.0, 1.0, 3));
            config.waves.add(w3);

            // Wave 4: Final Wave (12 Slime + 18 Demon + 25 Orc)
            WaveData w4 = new WaveData(150000);
            w4.addSpawn(new MobSpawnInfo(Constants.SLIME_MONSTER, 12, 420, 1.35, 1.0, 1.0, 3));
            w4.addSpawn(new MobSpawnInfo(Constants.BLOOD_DEMON_MONSTER, 18, 380, 1.25, 1.05, 1.0, 3));
            w4.addSpawn(new MobSpawnInfo(Constants.ORC_MONSTER, 25, 340, 1.2, 1.1, 1.0, 5));
            config.waves.add(w4);
        }

        return config;
    }

    public static class WaveData {
        public int startTime;
        public List<MobSpawnInfo> mobSpawns;
        public boolean started = false;
        public boolean finished = false;
        public WaveData(int startTime) {
            this.startTime = startTime;
            this.mobSpawns = new ArrayList<>();
        }
        public void addSpawn(MobSpawnInfo spawnInfo) {
            this.mobSpawns.add(spawnInfo);
        }
        public void reset() {
            started = false;
            finished = false;
            for (MobSpawnInfo info : mobSpawns) {
                info.reset();
            }
        }
    }

    public static class MobSpawnInfo {
        public int mobType;
        public int totalCount;
        public int spawnInterval;
        public double hpMultiplier;
        public double speedMultiplier;
        public double rewardMultiplier;
        public int cohortNum = 1;
        public int spawnedCount = 0;
        public int frameCounter = 0;
        public boolean isCohort = true;
        public int currentSpawnDelay;
        public MobSpawnInfo(int mobType, int totalCount, int spawnInterval, double hpMultiplier, 
                            double speedMultiplier, double rewardMultiplier, int cohortNum) {
            this.mobType = mobType;
            this.totalCount = totalCount;
            this.spawnInterval = spawnInterval;
            this.hpMultiplier = hpMultiplier;
            this.speedMultiplier = speedMultiplier;
            this.rewardMultiplier = rewardMultiplier;
            this.cohortNum = cohortNum;
            rollNextSpawnDelay();
        }
        public void rollNextSpawnDelay() {
            this.currentSpawnDelay = (int) (spawnInterval - spawnInterval * (0.7 + Math.random() * (0.9 - 0.7)));
        }
        public void reset() {
            spawnedCount = 0;
            frameCounter = 0;
            isCohort = true;
            rollNextSpawnDelay();
        }
    }
}
