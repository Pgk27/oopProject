package core;
public class Value { // CẤM ĐƯỢC THAY ĐỔI
	private static int idCounter = 2; // Start at 2 since 0 (BLACK_HOLE) and 1 (TRASH_CAN) map to tileset_air[0..1]

	// hàm next id dùng để tạo id không trùng lặp.
	private static int nextId() {
		return idCounter++;
	}

	static public final int groundVoid = 0;
	static public final int groundRoad = 1; 
	
	static public final int PLACEHOLDER = -1;
	static public final int TOWER_OCCUPIED = -2;
	static public final int CASTLE = 0; // Đích đến / Nhà chính (đổi từ BLACK_HOLE cũ)
	static public final int TRASH_CAN = 1;


	static public final int ARCHER_TOWER = nextId();
	static public final int MAGE_TOWER = nextId();
	static public final int CANNON_TOWER = nextId();
	static public final int GOLD_MINER = nextId(); 

	static public final int SPELL_BLACK_HOLE = nextId(); 
	static public final int SPELL_BOMBER_PLANE = nextId(); 
	static public final int SPELL_MERCENARY = nextId();


	static public final int ORC_MONSTER = nextId();
	static public final int BLOOD_DEMON_MONSTER = nextId();	
	static public final int SLIME_MONSTER = nextId();

	static public final int MINION_SOLDIER = nextId();

	static public final int TITLE_SCREEN = nextId();
	static public final int IN_GAME = nextId();
	static public final int SETTING = nextId();
	static public final int SKILL_SELECT = nextId();
	static public final int SHOP = nextId();
	static public final int ITEM_SHOP = nextId();
	static public final int GACHA = nextId();
	static public final int GACHA_RATE = nextId();
	static public final int SHARD_SHOP = nextId();
	static public final int NOTI = nextId();
	static public final int VICTORY_SCREEN = nextId();
	static public final int DEFEAT_SCREEN = nextId();
	
	
	static int[] deathReward = {5, 10, 15, 0}; // öldüğünde moblar ne kadar para veriyor
}
