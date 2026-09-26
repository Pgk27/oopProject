/**
 * Lớp quản lý toàn bộ hệ thống sinh quái (Wave Management).
 * Hoạt động độc lập bằng cách đếm số frame (globalTick) trong Game Loop.
 * Theo dõi thời gian để kích hoạt Wave mới, spawn quái và kiểm tra điều kiện kết thúc màn.
 */
public class WaveManager {
    public LevelConfig config;
    public int globalTick = 0;
    boolean isCohort = true;
    int cohortNum = 3;
    
    public WaveManager(int level) {
        this.config = LevelConfig.getLevelConfig(level);
        this.globalTick = 0;
    }
    
    public void update() {
        globalTick++; // 1 tick = 1ms in Screen.java's Thread.sleep(1)
        
        // Tự động tua nhanh thời gian đến Wave tiếp theo nếu đã diệt sạch quái
        if (!isAnyMobAlive()) {
            boolean allStartedFinished = true;
            WaveData nextWave = null;
            for (WaveData wave : config.waves) {
                if (wave.started && !wave.finished) {
                    allStartedFinished = false;
                    break;
                }
                if (!wave.started && nextWave == null) {
                    nextWave = wave;
                }
            }
            if (allStartedFinished && nextWave != null) {
                if (globalTick < nextWave.startTime) {
                    globalTick = nextWave.startTime; // Tua nhanh!
                }
            }
        }

        for (WaveData wave : config.waves) {
            // Check if it's time to start this wave
            if (globalTick >= wave.startTime && !wave.finished) {
                wave.started = true;
                boolean allMobsSpawnedInThisWave = true;
                
                for (MobSpawnInfo info : wave.mobSpawns) {
                    if (info.spawnedCount < info.totalCount) {
                        allMobsSpawnedInThisWave = false;
                        
                        info.frameCounter++;
                        // randomize interval between each mobs
                        if(!isCohort){
                            if (info.frameCounter >= info.spawnInterval + 1500){
                                isCohort = true;
                                info.frameCounter = 0;
                            }
                        }
                        if (isCohort && info.frameCounter >= (info.spawnInterval - info.spawnInterval*(0.7+ (int) (Math.random() * (0.9 - 0.7))))) {
                            spawnMob(info);
                            info.spawnedCount++;
                            info.frameCounter = 0;
                            if(info.spawnedCount % cohortNum == 0 || info.spawnedCount == info.totalCount){
                                isCohort = false;
                            }
                        }
                    }
                }
                
                if (allMobsSpawnedInThisWave) {
                    wave.finished = true;
                }
            }
        }
    }
    
    public int getCurrentWave() {
        int count = 0;
        for (WaveData wave : config.waves) {
            if (wave.started) count++;
        }
        return Math.max(1, count);
    }
    
    public int getTotalWaves() {
        return config.waves.size();
    }
    
    private void spawnMob(MobSpawnInfo info) {
        if (info.mobType == Value.mobMonster3) { // Slime
            for(int i = 0; i < Screen.mobsss.length; i++) {
                if(!Screen.mobsss[i].inGame) {
                    Screen.mobsss[i].spawnMob(info.mobType);
                    Screen.mobsss[i].applyMultiplier(info.hpMultiplier, info.speedMultiplier);
                    break;
                }
            }
        } else if (info.mobType == Value.mobMonster1) { // Orc
            for(int i = 0; i < Screen.mobs.length; i++) {
                if(!Screen.mobs[i].inGame) {
                    Screen.mobs[i].spawnMob(info.mobType);
                    Screen.mobs[i].applyMultiplier(info.hpMultiplier, info.speedMultiplier);
                    break;
                }
            }
        } else if (info.mobType == Value.mobMonster2) { // Demon
            for(int i = 0; i < Screen.mobss.length; i++) {
                if(!Screen.mobss[i].inGame) {
                    Screen.mobss[i].spawnMob(info.mobType);
                    Screen.mobss[i].applyMultiplier(info.hpMultiplier, info.speedMultiplier);
                    break;
                }
            }
        }
    }
    
    public boolean isAllWavesFinished() {
        for (WaveData wave : config.waves) {
            if (!wave.finished) return false;
        }
        return true;
    }
    
    public boolean isAnyMobAlive() {
        for(int i = 0; i < Screen.mobs.length; i++) {
            if (Screen.mobs[i].inGame) return true;
        }
        for(int i = 0; i < Screen.mobss.length; i++) {
            if (Screen.mobss[i].inGame) return true;
        }
        for(int i = 0; i < Screen.mobsss.length; i++) {
            if (Screen.mobsss[i].inGame) return true;
        }
        return false;
    }
}
