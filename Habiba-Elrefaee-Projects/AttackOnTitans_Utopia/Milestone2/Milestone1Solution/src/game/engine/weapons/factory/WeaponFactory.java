package game.engine.weapons.factory;

import java.io.IOException;
import java.util.HashMap;

import game.engine.dataloader.DataLoader;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.weapons.Weapon;
import game.engine.weapons.WeaponRegistry;

public class WeaponFactory
{
	private final HashMap<Integer, WeaponRegistry> weaponShop;

	public WeaponFactory() throws IOException
	{
		super();
		weaponShop = DataLoader.readWeaponRegistry();
	}

	public HashMap<Integer, WeaponRegistry> getWeaponShop()
	{
		return weaponShop;
	}
	public void addWeaponToShop(int code, int price){
		WeaponRegistry w = new WeaponRegistry(code, price);
		weaponShop.put(code, w);
	}
	public void addWeaponToShop(int code, int price, int damage, String name){
		WeaponRegistry w = new WeaponRegistry(code, price, damage, name);
		weaponShop.put(code, w);
	}
	public void addWeaponToShop(int code, int price, int damage, String name, int minRange, int maxRange){
		WeaponRegistry w = new WeaponRegistry(code, price, damage, name, minRange, maxRange);
		weaponShop.put(code, w);
	}
	
	public FactoryResponse buyWeapon(int resources, int weaponCode) throws InsufficientResourcesException{
		if(weaponShop.get(weaponCode).getPrice()>resources)
			throw (new InsufficientResourcesException(resources));
		else{
			int remRes = resources - weaponShop.get(weaponCode).getPrice();
			Weapon temp = weaponShop.get(weaponCode).buildWeapon();
			return new FactoryResponse(temp, remRes);
		}
	}
}
