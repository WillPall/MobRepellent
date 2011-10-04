package mobrepellent;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MobRepeller
{
	private int x;
	private int y;
	private int z;
	private Material material;
	private World world;
	private int blockData;
	
	//private MobRepellerStrength strength;
	
	public MobRepeller( Block block )
	{
		this( block.getX(), block.getY(), block.getZ(), block.getWorld(), block.getType(), block.getData() );
	}
	public MobRepeller( int x, int y, int z, World world, Material material, int blockData /*, MobRepellerStrength strength*/ )
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		this.material = material;
		this.blockData = blockData;
		
		//this.strength = strength;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getZ()
	{
		return z;
	}
	
	public Material getMaterial()
	{
		return material;
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public int getBlockData()
	{
		return blockData;
	}
	
	/*public MobRepellerStrength getStrength()
	{
		return strength;
	}*/
	
	public String toString()
	{
		String str = x + "," + y + "," + z + "," + world.getUID();
		
		return str;
	}
}
