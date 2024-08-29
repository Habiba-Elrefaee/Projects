package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import game.engine.base.Wall;
import game.engine.dataloader.DataLoader;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;
import game.engine.titans.PureTitan;
import game.engine.titans.Titan;
import game.engine.titans.TitanRegistry;
import game.engine.weapons.factory.FactoryResponse;
import game.engine.weapons.factory.WeaponFactory;

public class Battle
{
	private static final int[][] PHASES_APPROACHING_TITANS =
	{
		{ 1, 1, 1, 2, 1, 3, 4 },
		{ 2, 2, 2, 1, 3, 3, 4 },
		{ 4, 4, 4, 4, 4, 4, 4 } 
	}; // order of the types of titans (codes) during each phase
	private static final int WALL_BASE_HEALTH = 10000;

	private int numberOfTurns;
	private int resourcesGathered;
	private BattlePhase battlePhase;
	private int numberOfTitansPerTurn; // initially equals to 1
	private int score; // Number of Enemies Killed
	private int titanSpawnDistance;
	private final WeaponFactory weaponFactory;
	private final HashMap<Integer, TitanRegistry> titansArchives;//search inside this hashmap on the code from the titan registry
	private final ArrayList<Titan> approachingTitans; // treated as a Queue
	private final PriorityQueue<Lane> lanes;//containing active lanes based on danger level(least dang -> high prior)
	private final ArrayList<Lane> originalLanes;//all lanes starting from the beginning

	public Battle(int numberOfTurns, int score, int titanSpawnDistance, int initialNumOfLanes,
			int initialResourcesPerLane) throws IOException
	{
		super();
		this.numberOfTurns = numberOfTurns;
		this.battlePhase = BattlePhase.EARLY;
		this.numberOfTitansPerTurn = 1;
		this.score = score;
		this.titanSpawnDistance = titanSpawnDistance;
		this.resourcesGathered = initialResourcesPerLane * initialNumOfLanes;
		this.weaponFactory = new WeaponFactory();
		this.titansArchives = DataLoader.readTitanRegistry();
		this.approachingTitans = new ArrayList<Titan>();
		this.lanes = new PriorityQueue<>();
		this.originalLanes = new ArrayList<>();
		this.initializeLanes(initialNumOfLanes);
	}

	public int getNumberOfTurns()
	{
		return numberOfTurns;
	}

	public void setNumberOfTurns(int numberOfTurns)
	{
		this.numberOfTurns = numberOfTurns;
	}

	public int getResourcesGathered()
	{
		return resourcesGathered;
	}

	public void setResourcesGathered(int resourcesGathered)
	{
		this.resourcesGathered = resourcesGathered;
	}

	public BattlePhase getBattlePhase()
	{
		return battlePhase;
	}

	public void setBattlePhase(BattlePhase battlePhase)
	{
		this.battlePhase = battlePhase;
	}

	public int getNumberOfTitansPerTurn()
	{
		return numberOfTitansPerTurn;
	}

	public void setNumberOfTitansPerTurn(int numberOfTitansPerTurn)
	{
		this.numberOfTitansPerTurn = numberOfTitansPerTurn;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public int getTitanSpawnDistance()
	{
		return titanSpawnDistance;
	}

	public void setTitanSpawnDistance(int titanSpawnDistance)
	{
		this.titanSpawnDistance = titanSpawnDistance;
	}

	public WeaponFactory getWeaponFactory()
	{
		return weaponFactory;
	}

	public HashMap<Integer, TitanRegistry> getTitansArchives()
	{
		return titansArchives;
	}

	public ArrayList<Titan> getApproachingTitans()
	{
		return approachingTitans;
	}

	public PriorityQueue<Lane> getLanes()
	{
		return lanes;
	}

	public ArrayList<Lane> getOriginalLanes()
	{
		return originalLanes;
	}

	private void initializeLanes(int numOfLanes)
	{
		for (int i = 0; i < numOfLanes; i++)
		{
			Wall w = new Wall(WALL_BASE_HEALTH);
			Lane l = new Lane(w);

			this.getOriginalLanes().add(l);
			this.getLanes().add(l);
		}
	}
	public void refillApproachingTitans(){
		int i;
		if(battlePhase==BattlePhase.EARLY)
			i=0;
		else{ 
			if(battlePhase==BattlePhase.INTENSE)
				i=1;
			else{
				i=2;
			}
		}
		for(int j=0; j<7; j++){
			Titan t = titansArchives.get(PHASES_APPROACHING_TITANS[i][j]).spawnTitan(titanSpawnDistance);
			if(t!=null)
				approachingTitans.add(t);
		}
	}
	public void purchaseWeapon(int weaponCode, Lane lane) throws InsufficientResourcesException, InvalidLaneException{
		if(weaponCode>=1 && weaponCode<=4){
			if(lane != null && !lane.isLaneLost() && (lanes.contains(lane) && !lane.getLaneWall().isDefeated())){
				FactoryResponse f = weaponFactory.buyWeapon(resourcesGathered, weaponCode);
				if(f.getWeapon()!=null){
					lane.addWeapon(f.getWeapon());
					setResourcesGathered(f.getRemainingResources());
				}
				performTurn();
			}
			else{
				throw new InvalidLaneException();
			}
		}
	}
		
	public void passTurn(){
		performTurn();
	}
	private void addTurnTitansToLane(){
		for(int i=0; i<numberOfTitansPerTurn; i++){
			if(approachingTitans.size()==0)
				refillApproachingTitans();
			lanes.peek().addTitan(approachingTitans.remove(0));
		}
	}
	private void moveTitans(){
		PriorityQueue<Lane> temp = new PriorityQueue<>();
		int size= lanes.size();
		for(int i=0; i<size; i++){
			lanes.peek().moveLaneTitans();
			temp.add(lanes.remove());
		}
		while(!temp.isEmpty()){
			lanes.add(temp.remove());
		}
	}
	private int performWeaponsAttacks(){
		int resourcesGath =0;
		PriorityQueue<Lane> temp = new PriorityQueue<>();
		int size= lanes.size();
		for(int i=0; i<size; i++){
			resourcesGath+=lanes.peek().performLaneWeaponsAttacks();
			temp.add(lanes.remove());
		}
		while(!temp.isEmpty()){
			lanes.add(temp.remove());
		}
		setResourcesGathered(resourcesGath+getResourcesGathered());
		setScore(resourcesGath+getScore());
	return resourcesGath;
	}
	private int performTitansAttacks(){
		int resourcesGath =0;
		PriorityQueue<Lane> temp = new PriorityQueue<>();
		int size = lanes.size();
		for(int i=0; i<size; i++){
			resourcesGath+=lanes.peek().performLaneTitansAttacks();
			if(lanes.peek().isLaneLost()){
				lanes.remove(lanes.peek());
			}
			else
				temp.add(lanes.remove());
		}
		
		while(!temp.isEmpty()){
			lanes.add(temp.remove());
		}
		return resourcesGath;
	}
	private void updateLanesDangerLevels(){
		PriorityQueue<Lane> temp = new PriorityQueue<>();
		int size= lanes.size();
		for(int i =0; i<size; i++){
			lanes.peek().updateLaneDangerLevel();
			temp.add(lanes.remove());
		}
		while(!temp.isEmpty()){
			lanes.add(temp.remove());
		}
	}
	private void finalizeTurns(){
		numberOfTurns++;
		if(numberOfTurns<15)
			battlePhase=battlePhase.EARLY;
		else if(numberOfTurns<30)
			battlePhase=battlePhase.INTENSE;
			else if(numberOfTurns>=30)
				battlePhase=battlePhase.GRUMBLING;
		if(numberOfTurns>30 && (numberOfTurns%5==0))
				numberOfTitansPerTurn=numberOfTitansPerTurn*2;
	}
	private void performTurn(){
		moveTitans();
		performWeaponsAttacks();
		performTitansAttacks();
		addTurnTitansToLane();
		updateLanesDangerLevels();
		finalizeTurns();
	}
	public boolean isGameOver(){
		if(lanes.isEmpty())
			return true;
		return false;
	}
	
}
