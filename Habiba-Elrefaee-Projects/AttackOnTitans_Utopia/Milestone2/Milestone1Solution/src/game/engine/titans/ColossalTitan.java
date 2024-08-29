package game.engine.titans;

import game.engine.interfaces.Mobil;

public class ColossalTitan extends Titan
{
	public static final int TITAN_CODE = 4;

	public ColossalTitan(int baseHealth, int baseDamage, int heightInMeters, int distanceFromBase, int speed,
			int resourcesValue, int dangerLevel)
	{
		super(baseHealth, baseDamage, heightInMeters, distanceFromBase, speed, resourcesValue, dangerLevel);
	}

	@Override
	public boolean move() {
		boolean x = super.move();
		this.setSpeed(getSpeed()+1);
		return x;
	}
	
	
}
