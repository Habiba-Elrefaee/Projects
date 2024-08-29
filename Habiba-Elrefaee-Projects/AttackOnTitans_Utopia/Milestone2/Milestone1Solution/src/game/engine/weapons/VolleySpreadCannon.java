package game.engine.weapons;

import game.engine.titans.Titan;

import java.util.PriorityQueue;

public class VolleySpreadCannon extends Weapon
{
	public static final int WEAPON_CODE = 3;

	private final int minRange;
	private final int maxRange;

	public VolleySpreadCannon(int baseDamage, int minRange, int maxRange)
	{
		super(baseDamage);
		this.minRange = minRange;
		this.maxRange = maxRange;
	}

	public int getMinRange()
	{
		return minRange;
	}

	public int getMaxRange()
	{
		return maxRange;
	}

	@Override
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		PriorityQueue<Titan> x = new PriorityQueue<>();
		int size= laneTitans.size();
		int sum=0;
		int temp=0;
		for(int i =0; i<size; i++){
			if((laneTitans.peek().getDistance())>= minRange && (laneTitans.peek().getDistance())<=maxRange)
				temp = attack(laneTitans.peek());
			if(!laneTitans.peek().isDefeated())
				x.add(laneTitans.remove());
			
			else{
				laneTitans.remove();
				sum+=temp;
			}
		}
		while(!x.isEmpty()){
			laneTitans.add(x.remove());
		}
	return sum;
	}
}
