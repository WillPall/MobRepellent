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
	
	// TODO: none of these getRadius methods should be here,
	//		 move them to the MobRepeller class
	public int getRadius( MobRepeller repeller )
	{
		return getRadius( repeller.getMaterial(), repeller.getBlockData() );
	}
	
	public int getRadius( Block block )
	{
		return getRadius( block.getType(), block.getData() );
	}
	
	// TODO: replace this with getRadius(Block)
	public int getRadius( Material mat, int blockData )
	{
		if( mat == getBlockType( "small" ) &&
			( getBlockDamage( "small" ) == -1 ||
			blockData == getBlockDamage( "small" ) ) )
			return config.getInt( "small_radius", 20 );
		else if( mat == getBlockType( "medium" ) &&
			( getBlockDamage( "medium" ) == -1 ||
			blockData == getBlockDamage( "medium" )  ) )
			return config.getInt( "medium_radius", 30 );
		else if( mat == getBlockType( "large" ) &&
			( getBlockDamage( "large" ) == -1 ||
			blockData == getBlockDamage( "large" )  ) )
			return config.getInt( "large_radius", 50 );
		else if( config.getInt( "block_id", -1 ) != -1 )
		{
			// Backwards compatibility
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
	
	private int[] getItemType( String name, int def )
	{
		String elements[] = config.getString( name, "" ).split( ":" );

		// TODO: check these out again, seems to be getting called
		//		 way too many times
		/*if( elements.length > 1 )
			plugin.debug( "Getting type - " + elements[0] + ":" + elements[1] );
		else
			plugin.debug( "Getting type - " + elements[0] );*/
			
		int values[] = new int[2];
		
		// Setup defaults
		values[0] = def;
		values[1] = -1;
		
		try
		{
			values[0] = Integer.parseInt( elements[0] );
			if( elements.length > 1 )
				values[1] = Integer.parseInt( elements[1] );
			
			// TODO: same as above
			/*
			if( elements.length > 1 )
				plugin.debug( "Got type - " + values[0] + ":" + values[1] );
			else
				plugin.debug( "Got type - " + values[0] );*/
		}
		catch( NumberFormatException nfe )
		{
			plugin.debug( "[MobRepellent] Error loading item type for '" + name + "'. Defaulting to " + def + "." );
		}
		
		return values;
	}
	
	public Material getBlockType( String size )
	{
		int id = -1;
		
		if( size.equals( "small" ) )
			id = getItemType( "small_id", 42 )[0];
		else if( size.equals( "medium" ) )
			id = getItemType( "medium_id", 41 )[0];
		else if( size.equals( "large" ) )
			id = getItemType( "large_id", 57 )[0];
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
	
	public int getBlockDamage( String size )
	{
		int damage = -1;
		
		if( size.equals( "small" ) )
			damage = getItemType( "small_id", 42 )[1];
		else if( size.equals( "medium" ) )
			damage = getItemType( "medium_id", 41 )[1];
		else if( size.equals( "large" ) )
			damage = getItemType( "large_id", 57 )[1];
		
		return damage;
	}
}
