package mobrepellent;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

/**
 * Handles configuration variables for MobRepellent.
 * 
 * TODO: with the new configuration API, this is bootstrapped and
 * 		 horribly out of date. Needs to be updated to the new API.
 * @author Will
 *
 */
public class MobRepellentConfiguration
{
	MobRepellent plugin;
	FileConfiguration config;
	
	// Loaded list of mobs to repel
	HashSet<EntityType> mobsToRepel;
	
	public MobRepellentConfiguration( MobRepellent plugin )
	{
		this.plugin = plugin;
		this.config = plugin.getConfig();
		// Load any default values that aren't set from the default config.yml
		this.config.options().copyDefaults( true );
		// Make sure the old style of configuration isn't in use
		convertOldFile();
		// Save the defaults back to the plugins/MobRepellent/config.yml file
		this.plugin.saveConfig();
		
		this.load();
	}
	
	// TODO: move this to a different function and get rid of load()
	private void load()
	{
		// Load the mobs_to_repel list
		this.mobsToRepel = new HashSet<EntityType>();
		
		for( String creature : config.getStringList( "Mobs.list" ) )
		{
			EntityType type = EntityType.fromName( creature );
			
			if( type != null )
				mobsToRepel.add( type );
			else
				plugin.getLogger().warning( "[MobRepellent] Unknown creature type '" + creature + "' in 'mobs_to_repel'." );
		}
	}
	
	/**
	 * Removes configuration variables that were used in the past and converts
	 * them to the new variable names.
	 */
	private void convertOldFile()
	{
		// Old old way before multiple repeller types
		convertVariable( "block_id", "large_id" );
		convertVariable( "radius", "large_radius" );
		// Before v0.7.1
		convertVariable( "small_radius", "Radius.small" );
		convertVariable( "medium_radius", "Radius.medium" );
		convertVariable( "large_radius", "Radius.large" );
		convertVariable( "small_id", "BlockID.small" );
		convertVariable( "medium_id", "BlockID.medium" );
		convertVariable( "large_id", "BlockID.large" );
		convertVariable( "mobs_to_repel", "Mobs.list" );
		convertVariable( "repel_neutral_mobs", "Mobs.repel_neutral" );
	}
	
	/**
	 * If it exists, copies the value of oldVariable to newVariable and
	 * removes oldVariable from the configuration.
	 * 
	 * @param oldVariable name of the old variable to convert
	 * @param newVariable name of the new variable
	 */
	private void convertVariable( String oldVariable, String newVariable )
	{
		if( config.get( oldVariable ) != null )
		{
			config.set( newVariable, config.get( oldVariable ) );
			config.set( oldVariable, null );
		}
	}
	
	public void reload()
	{
		plugin.reloadConfig();
		this.config = plugin.getConfig();
		this.load();
		plugin.reloadRepellers();
	}
	
	public void save()
	{		
		plugin.saveConfig();
	}
	
	public boolean shouldRepelNeutralMobs()
	{
		return config.getBoolean( "Mobs.repel_neutral", false );
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
		/*else if( config.getInt( "block_id", -1 ) != -1 )
		{
			int largeId = config.getInt( "block_id", 57 );
			int largeRad = config.getInt( "radius", 50 );
			
			// TODO: make sure we don't need removeProperty() here anymore
			//config.removeProperty( "block_id" );
			//config.removeProperty( "radius" );
			//this.setDefaults();
			
			plugin.saveDefaultConfig();
			
			config.set( "large_id", largeId );
			config.set( "large_radius", largeRad );
			
			return MobRepellerStrength.LARGE;
		}*/
		
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
			return config.getInt( "Radius.small", 20 );
		else if( mat == getBlockType( "medium" ) &&
			( getBlockDamage( "medium" ) == -1 ||
			blockData == getBlockDamage( "medium" )  ) )
			return config.getInt( "Radius.medium", 30 );
		else if( mat == getBlockType( "large" ) &&
			( getBlockDamage( "large" ) == -1 ||
			blockData == getBlockDamage( "large" )  ) )
			return config.getInt( "Radius.large", 50 );
		
		return -1;
	}
	
	// TODO: this function gets called every time any block event
	//       or entity event is run. make it more lightweight
	private int[] getItemType( String name, int def )
	{
		String elements[] = config.getString( name, "" ).split( "@" );

		/*if( elements.length > 1 )
			plugin.debug( "Getting type - " + elements[0] + "@" + elements[1] );
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
			id = getItemType( "BlockID.small", 42 )[0];
		else if( size.equals( "medium" ) )
			id = getItemType( "BlockID.medium", 41 )[0];
		else if( size.equals( "large" ) )
			id = getItemType( "BlockID.large", 57 )[0];
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
			//case 89: // glowstone
			
			case 110: // mycelium
				
			case 121: // end stone
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
			damage = getItemType( "BlockID.small", 42 )[1];
		else if( size.equals( "medium" ) )
			damage = getItemType( "BlockID.medium", 41 )[1];
		else if( size.equals( "large" ) )
			damage = getItemType( "BlockID.large", 57 )[1];
		
		return damage;
	}
	
	public HashSet<EntityType> getMobsToRepel()
	{
		return mobsToRepel;
	}
}
