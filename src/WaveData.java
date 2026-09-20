import java.util.ArrayList;
import java.util.List;

public class WaveData {
    public int startTime; // Start time in frames (e.g., 0:25 means 25000 frames)
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
