public class MinionStats {
    public static int maxHealth = 1200 + 100 * Screen.buffHp;
    
    public static long HEAL_COOL_DOWN = 1000*(100-2*Screen.buffHpRe)/100; //tgian giữa các lần hồi hp
	public static int healAmount = 150+5*Screen.buffHpRe;  //lượng hp hồi 1 cục

    public static int dmgReduction = Screen.buffShield;

	public static int arrowDmg = 150 + 20*Screen.buffSDmg;  //st cung
    public static int swordDmg = 200 + 25*Screen.buffSDmg;  //st 


}
