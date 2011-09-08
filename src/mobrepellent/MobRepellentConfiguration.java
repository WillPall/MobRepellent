package mobrepellent;

import org.bukkit.Material;
import org.bukkit.util.config.Configuration;

public class MobRepellentConfiguration
{
	MobRepellent plugin;
	Configuration config;
	
	public MobRepellentConfiguration( MobRepellent plugin )
	{
		this.plugin = plugin;
		this.config = plugin.getConfiguration();
		config.load();
		
		this.plugin.getLogger().info( "[Mob Repellent] Config loaded with radius: '" + getRadius() + "', block: '" + getBlockType().toString() + "'" );
	}
	
	public void setDefaults()
	{
		config.setProperty( "radius", 50 );
		config.setProperty( "block_id", 57 );
		config.save();
	}
	
	public void save()
	{
		config.save();
	}
	
	public int getRadius()
	{
		return config.getInt( "radius", 50 );
	}
	
	public Material getBlockType()
	{
		int id = config.getInt( "block_id", 57 );
		Material mat = Material.getMaterial( id );
		
		// Check for commonly found blocks
		// TODO: this would be better in an if statement, but
		//		 as a switch it allows easy selection of single blocks
		//		 to allow/disallow
		switch( id )
		{
			case 0:	// air
			case 1: // stone
			case 2: // grass
			case 3: // dirt
				
			case 7: // bedrock
			case 8: // water
			case 9: // water
			case 10: // lava
			case 11: // lava
			case 12: // sand
			case 13: // gravel
				
			case 24: // sandstone
				
			case 78: // snow (dirt)
			case 79: // ice
			
			case 82: // clay
			
			case 87: // netherack
			case 88: // soulsand
			case 89: // glowstone
				mat = Material.DIAMOND_BLOCK;
		}
		
		if( mat != null )
			return mat;
		
		return Material.DIAMOND_BLOCK;
	}
}
