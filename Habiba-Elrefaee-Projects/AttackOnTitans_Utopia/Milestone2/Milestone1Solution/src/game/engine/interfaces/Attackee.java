package game.engine.interfaces;


public interface Attackee{
	
	int getCurrentHealth();

	void setCurrentHealth(int health);

	int getResourcesValue();
	
	default boolean isDefeated(){
		return this.getCurrentHealth() <= 0;
	}
	default int takeDamage(int damage){
		if(damage>0){
			this.setCurrentHealth((this.getCurrentHealth())-damage);
			if(isDefeated())
				return this.getResourcesValue();
			else
				return 0;
		}
		else
			return 0;
	}
}