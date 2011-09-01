package mobrepellent;

import org.bukkit.World;
import org.bukkit.block.Block;

public class MobRepeller
{
	private Block base;
	private World world;
	
	public MobRepeller( Block base )
	{
		this.base = base;
		this.world = base.getWorld();
	}
	
	public void setBase( Block base )
	{
		this.base = base;
	}
	
	public Block getBase()
	{
		return base;
	}
	
	public void setWorld( World world )
	{
		this.world = world;
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public String toString()
	{
		String str = base.getX() + "," + base.getY() + "," + base.getZ() + "," + world.getUID();
		
		return str;
	}
}
