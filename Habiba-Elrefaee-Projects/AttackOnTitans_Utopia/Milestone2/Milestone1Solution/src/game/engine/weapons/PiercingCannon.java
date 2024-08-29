package game.engine.weapons;

import game.engine.lanes.Lane;
import game.engine.titans.Titan;

import java.util.*;

public class PiercingCannon extends Weapon
{
	public static final int WEAPON_CODE = 1;

	public PiercingCannon(int baseDamage)
	{
		super(baseDamage);
	}

	@Override
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		PriorityQueue<Titan> x = new PriorityQueue<>();
		int size= laneTitans.size();
		if(size>5){
			int sum=0;
			int temp;
			for(int i =0; i<5; i++){
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
		else{
			int sum=0;
			int temp;
			for(int i =0; i<size; i++){
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
	
}
