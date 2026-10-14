/**
 * Lớp đại diện cho thông tin cấu hình sinh quái (spawn) của một loại quái vật cụ thể trong 1 Wave.
 * Hệ thống Data-Driven sử dụng lớp này để không phải hard-code logic sinh quái.
 */
public class MobSpawnInfo {
    public int mobType; // ID của quái vật: Value.ORC_MONSTER (Orc), Value.BLOOD_DEMON_MONSTER (Demon), Value.SLIME_MONSTER (Slime)
    public int totalCount;
    public int spawnInterval; // in frames (each frame is ~1ms sleep in Screen.java)
    public double hpMultiplier;
    public double speedMultiplier;
    public double rewardMultiplier;

    public int cohortNum = 1;
    public int spawnedCount = 0;
    public int frameCounter = 0;
    public boolean isCohort = true;
    public int currentSpawnDelay;

    public MobSpawnInfo(int mobType, int totalCount, int spawnInterval, double hpMultiplier, double speedMultiplier, double rewardMultiplier, int cohortNum) {
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
