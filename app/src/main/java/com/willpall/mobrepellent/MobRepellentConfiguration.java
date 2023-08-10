package com.willpall.mobrepellent;

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

		// Copy default values into the plugins/MobRepellent/config.yml file
		plugin.saveDefaultConfig();

		this.config = plugin.getConfig();
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
		convertVariable( "small_id", "Blocks.small" );
		convertVariable( "medium_id", "Blocks.medium" );
		convertVariable( "large_id", "Blocks.large" );
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
	
	public boolean shouldRepelBelow()
	{
		return !config.getBoolean( "Radius.ignore_below", false );
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
		return getRadius(repeller.getMaterial());
	}
	
	public int getRadius( Block block )
	{
		return getRadius(block.getType());
	}
	
	// TODO: replace this with getRadius(Block)
	public int getRadius(Material mat)
	{
		if(mat == getBlockType( "small" ))
			return config.getInt( "Radius.small", 20 );
		else if( mat == getBlockType( "medium" ))
			return config.getInt( "Radius.medium", 30 );
		else if(mat == getBlockType( "large" ))
			return config.getInt( "Radius.large", 50 );
		
		return -1;
	}
	
	// TODO: this function gets called every time any block event
	//       or entity event is run. make it more lightweight
	private Material getConfiguredMaterial( String name, Material def )
	{
		String configBlockName = config.getString( name, "DIAMOND_BLOCK" );

		/*if( elements.length > 1 )
			plugin.debug( "Getting type - " + elements[0] + "@" + elements[1] );
		else
			plugin.debug( "Getting type - " + elements[0] );*/
			
		Material material = def;
		
		try
		{
			material = Material.getMaterial( configBlockName );
			
			// TODO: same as above
			/*
			if( elements.length > 1 )
				plugin.debug( "Got type - " + materials[0] + ":" + materials[1] );
			else
				plugin.debug( "Got type - " + materials[0] );*/
		}
		catch( NumberFormatException nfe )
		{
			plugin.debug( "[MobRepellent] Error loading item type for '" + name + "'. Defaulting to " + def + "." );
		}
		
		return material;
	}
	
	public Material getBlockType( String size )
	{
		Material material = Material.DIAMOND_BLOCK;
		
		if( size.equals( "small" ) )
			material = getConfiguredMaterial("Blocks.small", Material.IRON_BLOCK);
		else if( size.equals( "medium" ) )
			material = getConfiguredMaterial("Blocks.medium", Material.GOLD_BLOCK);
		else if( size.equals( "large" ) )
			material = getConfiguredMaterial("Blocks.large", Material.DIAMOND_BLOCK);

		// NEXT: for some reason it's only recognizing DIAMOND_BLOCKs after changing the default from -1
		// seems like it's still getting confused about whether IRON/GOLD are valid configs. need to look
		// into what the above methods are returning with an empty config/default config
		
		// Material mat = Material.getMaterial( material );
		// Material mat = Material.IRON_BLOCK;
		
		// Check for commonly found blocks
		// TODO: figure out a better way to exclude blocks
		switch( material )
		{
			case AIR:	// air
			case STONE: // stone
			case GRASS_BLOCK: // grass
			case DIRT: // dirt
				
			case BEDROCK: // bedrock
			case WATER: // water
			// case 9: // water
			case LAVA: // lava
			// case 11: // lava
			case SAND: // sand
			case GRAVEL: // gravel
				
			case SANDSTONE: // sandstone
				
			case SNOW_BLOCK: // snow (dirt)
			case ICE: // ice
			
			case CLAY: // clay
			
			case NETHERRACK: // netherack
			case SOUL_SAND: // soulsand
			// case GLOWSTONE: // glowstone
			
			case MYCELIUM: // mycelium
				
			case END_STONE: // end stone
				material = Material.DIAMOND_BLOCK;
		}
		
		if( material != null )
			return material;
		
		return Material.DIAMOND_BLOCK;
	}
	
	public HashSet<EntityType> getMobsToRepel()
	{
		return mobsToRepel;
	}
}
