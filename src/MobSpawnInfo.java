public class MobSpawnInfo {
    public int mobType; // Value.mobMonster1 (Orc), Value.mobMonster2 (Demon), Value.mobMonster3 (Slime)
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
