package game.engine.lanes;
import java.util.*;

import game.engine.Battle;
import game.engine.base.Wall;
import game.engine.exceptions.GameActionException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.titans.Titan;
import game.engine.weapons.Weapon;

public class Lane implements Comparable<Lane>
{
	private final Wall laneWall;
	private int dangerLevel;
	private final PriorityQueue<Titan> titans;
	private final ArrayList<Weapon> weapons;

	public Lane(Wall laneWall)
	{
		super();
		this.laneWall = laneWall;
		this.dangerLevel = 0;
		this.titans = new PriorityQueue<>();
		this.weapons = new ArrayList<>();
	}

	public Wall getLaneWall()
	{
		return this.laneWall;
	}

	public int getDangerLevel()
	{
		return this.dangerLevel;
	}

	public void setDangerLevel(int dangerLevel)
	{
		this.dangerLevel = dangerLevel;
	}

	public PriorityQueue<Titan> getTitans()
	{
		return this.titans;
	}

	public ArrayList<Weapon> getWeapons()
	{
		return this.weapons;
	}

	@Override
	public int compareTo(Lane o)
	{
		return this.dangerLevel - o.dangerLevel;
	}
	
	public void addTitan(Titan titan){
		if(!isLaneLost() && titan!=null)
			titans.add(titan);
	}
			
	public void addWeapon(Weapon weapon){
		if(!isLaneLost() && weapon!=null)
			weapons.add(weapon);
	}
	public void moveLaneTitans(){
		PriorityQueue<Titan> temp = new PriorityQueue<>();
		int size= titans.size();
		for(int i=0; i<size; i++){
			titans.peek().move();
			temp.add(titans.remove());
		}
		while(!temp.isEmpty()){
			titans.add(temp.remove());
		}
	}
	public int performLaneTitansAttacks(){
		int resourcesGath = 0;
		PriorityQueue<Titan> temp = new PriorityQueue<>();
		int size = titans.size();
		for(int i=0; i<size; i++){
			if(titans.peek().hasReachedTarget()){
				resourcesGath+=titans.peek().attack(laneWall);
				temp.add(titans.remove());
			}
			else
				break;
		}
		while(!temp.isEmpty()){
			titans.add(temp.remove());
		}
		
		return resourcesGath;
	}
	public int performLaneWeaponsAttacks(){
		int resourcesGath = 0;
		if(!isLaneLost()){
			for(int i=0; i<weapons.size(); i++){
				resourcesGath+=weapons.get(i).turnAttack(titans);
			}
		}
		return resourcesGath;
	}
	
	public boolean isLaneLost(){
		return laneWall.isDefeated();
	}
	public void updateLaneDangerLevel(){
		PriorityQueue<Titan> temp = new PriorityQueue<>();
		int size= titans.size();
		int sum=0;
		for(int i=0; i<size; i++){
			sum+=titans.peek().getDangerLevel();
			temp.add(titans.remove());
		}
		this.setDangerLevel(sum);
		while(!temp.isEmpty()){
			titans.add(temp.remove());
		}
		
	}
	
}
