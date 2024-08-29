package game.engine.interfaces;

public interface Mobil
{
	int getDistance();

	void setDistance(int distance);

	int getSpeed();

	void setSpeed(int speed);
	
	default boolean hasReachedTarget(){
		return getDistance() <= 0;
	}
	
	default boolean move(){
		if(hasReachedTarget())
			return true;
		else{
			this.setDistance(this.getDistance()-this.getSpeed());
			if(hasReachedTarget())
				return true;
			return false;
		}
	}
}
