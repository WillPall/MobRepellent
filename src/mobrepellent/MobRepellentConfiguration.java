package mobrepellent;

import org.bukkit.Material;
import org.bukkit.block.Block;
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
	}
	
	public void reload()
	{
		this.config = plugin.getConfiguration();
		this.config.load();
		plugin.reloadRepellers();
	}
	
	public void setDefaults()
	{
		config.setProperty( "small_radius", 20 );
		config.setProperty( "small_id", 42 );
		config.setProperty( "medium_radius", 30 );
		config.setProperty( "medium_id", 41 );
		config.setProperty( "large_radius", 50 );
		config.setProperty( "large_id", 57 );
		// TODO: figure something out
		//config.setProperty( "ignite_on_completion", false );
		config.setProperty( "repel_neutral_mobs", false );
		config.save();
	}
	
	public void save()
	{
		if( config.getBoolean( "debug_mode", false ) == false )
			config.removeProperty( "debug_mode" );
		if( config.getInt( "block_id", -1 ) == -1 )
			config.removeProperty( "block_id" );
		config.save();
	}
	
	public boolean shouldRepelNeutralMobs()
	{
		return config.getBoolean( "repel_neutral_mobs", false );
	}
	
	public boolean getDebugMode()
	{
		return config.getBoolean( "debug_mode", false );
	}
	
	public MobRepellerStrength getStrength( Block block )
	{
		return getStrength( block.getType() );
	}
	
	public MobRepellerStrength getStrength( Material mat )
	{
		if( mat == getBlockType( "small" ) )
			return MobRepellerStrength.SMALL;
		else if( mat == getBlockType( "medium" ) )
			return MobRepellerStrength.MEDIUM;
		else if( mat == getBlockType( "large" ) )
			return MobRepellerStrength.LARGE;
		else if( config.getInt( "block_id", -1 ) != -1 )
		{
			int largeId = config.getInt( "block_id", 57 );
			int largeRad = config.getInt( "radius", 50 );
			config.removeProperty( "block_id" );
			config.removeProperty( "radius" );
			this.setDefaults();			
			
			config.setProperty( "large_id", largeId );
			config.setProperty( "large_radius", largeRad );
			
			return MobRepellerStrength.LARGE;
		}
		
		return MobRepellerStrength.INVALID;
	}
	
	public int getRadius( Block block )
	{
		return getRadius( block.getType() );
	}
	
	public int getRadius( Material mat )
	{
		if( mat == getBlockType( "small" ) )
			return config.getInt( "small_radius", 20 );
		else if( mat == getBlockType( "medium" ) )
			return config.getInt( "medium_radius", 30 );
		else if( mat == getBlockType( "large" ) )
			return config.getInt( "large_radius", 50 );
		else if( config.getInt( "block_id", -1 ) != -1 )
		{
			int largeId = config.getInt( "block_id", 57 );
			int largeRad = config.getInt( "radius", 50 );
			config.removeProperty( "block_id" );
			config.removeProperty( "radius" );
			this.setDefaults();			
			
			config.setProperty( "large_id", largeId );
			config.setProperty( "large_radius", largeRad );
			
			return largeRad;
		}
		
		return -1;
	}
	
	public Material getBlockType( String size )
	{
		int id = -1;
		
		if( size.equals( "small" ) )
			id = config.getInt( "small_id", 42 );
		else if( size.equals( "medium" ) )
			id = config.getInt( "medium_id", 41 );
		else if( size.equals( "large" ) )
			id = config.getInt( "large_id", 57 );
		else
			id = 57;
		
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
