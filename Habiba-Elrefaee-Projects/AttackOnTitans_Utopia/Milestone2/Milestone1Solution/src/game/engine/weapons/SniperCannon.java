package game.engine.weapons;

import game.engine.titans.Titan;

import java.util.PriorityQueue;

public class SniperCannon extends Weapon
{
	public static final int WEAPON_CODE = 2;

	public SniperCannon(int baseDamage)
	{
		super(baseDamage);
	}

	@Override
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		int x=0;
		if(laneTitans.size()>0){
			x = attack(laneTitans.peek());
			if(laneTitans.peek().isDefeated())
				laneTitans.remove();
		}
		return x;
	}
}
