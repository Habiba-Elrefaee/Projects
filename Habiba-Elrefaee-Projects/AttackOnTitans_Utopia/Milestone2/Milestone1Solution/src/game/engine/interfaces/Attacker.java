package game.engine.interfaces;

import game.engine.base.Wall;
import game.engine.titans.Titan;

public interface Attacker{
	int getDamage(); // gets the damage value to be applied
	default int attack(Attackee target){
		if(target!=null)
			return target.takeDamage(getDamage());
		else
			return 0;
	}
}