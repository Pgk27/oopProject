package wave;
import core.Screen;
import core.Value;

/**
 * Lớp quản lý toàn bộ hệ thống sinh quái (Wave Management).
 * Hoạt động độc lập bằng cách đếm số frame (globalTick) trong Game Loop.
 * Theo dõi thời gian để kích hoạt Wave mới, spawn quái và kiểm tra điều kiện kết thúc màn.
 */
public class WaveManager {
    public LevelConfig config;
    public int globalTick = 0;
    
    public WaveManager(int level) {
        this.config = LevelConfig.getLevelConfig(level);
        this.globalTick = 0;
    }
    
    public void update() {
        globalTick++; // 1 tick = 1ms in Screen.java's Thread.sleep(1)
        
        // Tự động tua nhanh thời gian đến Wave tiếp theo nếu đã diệt sạch quái của wave trước
        if (!isAnyMobAlive()) {
            // check xem đã có wave nào chưa
            boolean hasAnyStarted = false;
            boolean allStartedFinished = true;
            WaveData nextWave = null;
            for (WaveData wave : config.waves) {
                if (wave.started) {
                    hasAnyStarted = true;
                    if (!wave.finished) {
                        allStartedFinished = false;
                        break;
                    }
                } else if (nextWave == null) {
                    nextWave = wave;
                }
            }
            // Chỉ tua nhanh khi ĐÃ CÓ ít nhất 1 wave diễn ra và toàn bộ quái đã bị tiêu diệt
            if (hasAnyStarted && allStartedFinished && nextWave != null) {
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
                        if (!info.isCohort) {
                            if (info.frameCounter >= info.spawnInterval + 3000) {
                                info.isCohort = true;
                                info.frameCounter = 0;
                            }
                        }
                        if (info.isCohort && info.frameCounter >= info.currentSpawnDelay) {
                            spawnMob(info);
                            info.spawnedCount++;
                            info.frameCounter = 0;
                            info.rollNextSpawnDelay();
                            if (info.spawnedCount % info.cohortNum == 0 || info.spawnedCount == info.totalCount) {
                                info.isCohort = false;
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
        if (info.mobType == Value.SLIME_MONSTER) { // Slime
            for(int i = 0; i < Screen.mobsss.length; i++) {
                if(!Screen.mobsss[i].inGame) {
                    Screen.mobsss[i].spawnMob(info.mobType);
                    Screen.mobsss[i].applyMultiplier(info.hpMultiplier, info.speedMultiplier);
                    break;
                }
            }
        } else if (info.mobType == Value.ORC_MONSTER) { // Orc
            for(int i = 0; i < Screen.mobs.length; i++) {
                if(!Screen.mobs[i].inGame) {
                    Screen.mobs[i].spawnMob(info.mobType);
                    Screen.mobs[i].applyMultiplier(info.hpMultiplier, info.speedMultiplier);
                    break;
                }
            }
        } else if (info.mobType == Value.BLOOD_DEMON_MONSTER) { // Demon
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
        if (Screen.mobs != null) {
            for(int i = 0; i < Screen.mobs.length; i++) {
                if (Screen.mobs[i] != null && Screen.mobs[i].inGame) return true;
            }
        }
        if (Screen.mobss != null) {
            for(int i = 0; i < Screen.mobss.length; i++) {
                if (Screen.mobss[i] != null && Screen.mobss[i].inGame) return true;
            }
        }
        if (Screen.mobsss != null) {
            for(int i = 0; i < Screen.mobsss.length; i++) {
                if (Screen.mobsss[i] != null && Screen.mobsss[i].inGame) return true;
            }
        }
        return false;
    }
}
