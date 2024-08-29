package game.engine.weapons;

import game.engine.titans.Titan;

import java.util.PriorityQueue;

public class WallTrap extends Weapon
{
	public static final int WEAPON_CODE = 4;

	public WallTrap(int baseDamage)
	{
		super(baseDamage);
	}

	@Override
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		int temp = 0;
		int size= laneTitans.size();
		if(size>0){
			if(laneTitans.peek().hasReachedTarget()){
				temp = attack(laneTitans.peek());
			if(laneTitans.peek().isDefeated())
				laneTitans.remove();
			}
		}
		return temp;
	}
	
}
