package mobrepellent;

import org.bukkit.World;
import org.bukkit.block.Block;

public class MobRepeller
{
	private Block base;
	private MobRepellerStrength strength;
	
	public MobRepeller( Block base, MobRepellerStrength strength )
	{
		this.base = base;
		this.strength = strength;
	}
	
	public void setBase( Block base )
	{
		this.base = base;
	}
	
	public Block getBase()
	{
		return base;
	}
	
	public World getWorld()
	{
		return base.getWorld();
	}
	
	public MobRepellerStrength getStrength()
	{
		return strength;
	}
	
	public String toString()
	{
		String str = base.getX() + "," + base.getY() + "," + base.getZ() + "," + base.getWorld().getUID();
		
		return str;
	}
}
