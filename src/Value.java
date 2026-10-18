public class Value { // CẤM ĐƯỢC THAY ĐỔI
	private static int idCounter = 2; // Start at 2 since 0 (BLACK_HOLE) and 1 (TRASH_CAN) map to tileset_air[0..1]

	// hàm next id dùng để tạo id không trùng lặp.
	private static int nextId() {
		return idCounter++;
	}

	static protected final int groundVoid = 0;
	static protected final int groundRoad = 1; 
	
	static protected final int PLACEHOLDER = -1;
	static protected final int TOWER_OCCUPIED = -2;
	static protected final int CASTLE = 0; // Đích đến / Nhà chính (đổi từ BLACK_HOLE cũ)
	static protected final int TRASH_CAN = 1;


	static protected final int ARCHER_TOWER = nextId();
	static protected final int MAGE_TOWER = nextId();
	static protected final int CANNON_TOWER = nextId();
	static protected final int GOLD_MINER = nextId(); 

	static protected final int SPELL_BLACK_HOLE = nextId(); 
	static protected final int SPELL_BOMBER_PLANE = nextId(); 
	static protected final int SPELL_MERCENARY = nextId();


	static protected final int ORC_MONSTER = nextId();
	static protected final int BLOOD_DEMON_MONSTER = nextId();	
	static protected final int SLIME_MONSTER = nextId();

	static protected final int MINION_SOLDIER = nextId();
	
	
	static int[] deathReward = {5, 10, 15, 0}; // öldüğünde moblar ne kadar para veriyor
}
