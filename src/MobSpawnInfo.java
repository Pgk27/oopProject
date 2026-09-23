/**
 * Lớp đại diện cho thông tin cấu hình sinh quái (spawn) của một loại quái vật cụ thể trong 1 Wave.
 * Hệ thống Data-Driven sử dụng lớp này để không phải hard-code logic sinh quái.
 */
public class MobSpawnInfo {
    public int mobType; // ID của quái vật: Value.mobMonster1 (Orc), Value.mobMonster2 (Demon), Value.mobMonster3 (Slime)
    public int totalCount;
    public int spawnInterval; // in frames (each frame is ~1ms sleep in Screen.java)
    public double hpMultiplier;
    public double speedMultiplier;
    public double rewardMultiplier;

    public int spawnedCount = 0;
    public int frameCounter = 0;

    public MobSpawnInfo(int mobType, int totalCount, int spawnInterval, double hpMultiplier, double speedMultiplier, double rewardMultiplier) {
        this.mobType = mobType;
        this.totalCount = totalCount;
        this.spawnInterval = spawnInterval;
        this.hpMultiplier = hpMultiplier;
        this.speedMultiplier = speedMultiplier;
        this.rewardMultiplier = rewardMultiplier;
    }

    public void reset() {
        spawnedCount = 0;
        frameCounter = 0;
    }
}
