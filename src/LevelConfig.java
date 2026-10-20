import java.util.ArrayList;
import java.util.List;

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
            // LEVEL 1
            // Wave 1 (0:00 - 0:25): 10x Slime (Spawn mỗi 2s).
            WaveData w1 = new WaveData(0); // 0s
            w1.addSpawn(new MobSpawnInfo(Value.BLOOD_DEMON_MONSTER, 1, 500, 1.0, 1.0, 1.0, 4));
            w1.addSpawn(new MobSpawnInfo(Value.SLIME_MONSTER, 1, 500, 1.0, 1.0, 1.0, 4)); // Slime
            config.waves.add(w1);

            // Wave 2 (0:30 - 0:55): 8x Slime + 3x Orc xuất hiện xen kẽ (Spawn mỗi 1.5s).
            WaveData w2 = new WaveData(30000); // 30s
            w2.addSpawn(new MobSpawnInfo(Value.BLOOD_DEMON_MONSTER, 28, 500, 1.0, 1.0, 1.0, 4)); // Slime
            w2.addSpawn(new MobSpawnInfo(Value.ORC_MONSTER, 28, 500, 1.0, 1.0, 1.0, 4)); // Orc
            config.waves.add(w2);

        } else if (level == 2) {
            // LEVEL 2
            // Wave 1 (0:00 - 0:45): 15x Slime + 5x Orc.
            WaveData w1 = new WaveData(0);
            w1.addSpawn(new MobSpawnInfo(Value.SLIME_MONSTER, 35, 500, 1.0, 1.0, 1.0, 5));
            w1.addSpawn(new MobSpawnInfo(Value.ORC_MONSTER, 35, 500, 1.0, 1.0, 1.0, 5));
            config.waves.add(w1);

            // Wave 2 (0:55 - 1:45): 10x Orc + 5x Demon (Máu tăng nhẹ 10%).
            WaveData w2 = new WaveData(55000); // 55s
            w2.addSpawn(new MobSpawnInfo(Value.ORC_MONSTER, 38, 500, 1.1, 1.0, 1.0, 5)); // Orc
            w2.addSpawn(new MobSpawnInfo(Value.BLOOD_DEMON_MONSTER, 38, 500, 1.1, 1.0, 1.0, 5)); // Demon
            config.waves.add(w2);

            // Wave 3 (1:55 - 2:45): 15x Slime + 10x Orc + 3x Demon xuất hiện hỗn hợp.
            WaveData w3 = new WaveData(115000); // 1m55s = 115s
            w3.addSpawn(new MobSpawnInfo(Value.SLIME_MONSTER, 30, 500, 1.0, 1.0, 1.0, 6));
            w3.addSpawn(new MobSpawnInfo(Value.ORC_MONSTER, 30, 500, 1.0, 1.0, 1.0, 5));
            w3.addSpawn(new MobSpawnInfo(Value.BLOOD_DEMON_MONSTER, 30, 500, 1.0, 1.0, 1.0, 5));
            config.waves.add(w3);

        } else {
            // LEVEL 3
            // Wave 1 (0:00 - 1:15): 20x Slime + 10x Orc.
            WaveData w1 = new WaveData(0);
            w1.addSpawn(new MobSpawnInfo(Value.SLIME_MONSTER, 32, 500, 1.0, 1.0, 1.0, 4));
            w1.addSpawn(new MobSpawnInfo(Value.ORC_MONSTER, 32, 500, 1.0, 1.0, 1.0, 4));
            config.waves.add(w1);

            // Wave 2 (1:25 - 2:30): 15x Orc + 8x Demon (Máu tăng 20%).
            WaveData w2 = new WaveData(85000); // 1m25s = 85s
            w2.addSpawn(new MobSpawnInfo(Value.ORC_MONSTER, 40, 500, 1.2, 1.0, 1.2, 5));
            w2.addSpawn(new MobSpawnInfo(Value.BLOOD_DEMON_MONSTER, 30, 500, 1.2, 1.0, 1.2, 5));
            config.waves.add(w2);

            // Wave 3 (2:40 - 3:45): 25x Slime (Spam tốc độ cao) + 12x Orc + 6x Demon.
            WaveData w3 = new WaveData(160000); // 2m40s = 160s
            w3.addSpawn(new MobSpawnInfo(Value.SLIME_MONSTER, 30, 500, 1.0, 1.0, 1.0, 6)); // Spam Slime
            w3.addSpawn(new MobSpawnInfo(Value.ORC_MONSTER, 30, 500, 1.0, 1.0, 1.0, 5));
            w3.addSpawn(new MobSpawnInfo(Value.BLOOD_DEMON_MONSTER, 30, 500, 1.0, 1.0, 1.0, 5));
            config.waves.add(w3);

            // Wave 4 (3:55 - 4:45): Wave tổng hợp dồn dập: 20x Slime + 15x Orc + 10x Demon (Máu & Tốc độ tăng 15-20%).
            WaveData w4 = new WaveData(235000); // 3m55s = 235s
            w4.addSpawn(new MobSpawnInfo(Value.SLIME_MONSTER, 30, 500, 1.2, 1.2, 1.5, 6));
            w4.addSpawn(new MobSpawnInfo(Value.ORC_MONSTER, 30, 500, 1.2, 1.15, 1.5, 6));
            w4.addSpawn(new MobSpawnInfo(Value.BLOOD_DEMON_MONSTER, 30, 500, 1.2, 1.15, 1.5, 6));
            config.waves.add(w4);
        }

        return config;
    }
}
