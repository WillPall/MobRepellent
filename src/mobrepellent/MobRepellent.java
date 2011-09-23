package mobrepellent;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MobRepellent extends JavaPlugin
{
	private Logger log = Logger.getLogger( "Minecraft" );
	private MobRepellentList repellers;
	private ArrayList<World> worlds;
	private MobRepellentConfiguration config;
	private int delayLoadTask;

	public void onEnable()
	{
		// Config is needed before anything else
		config = new MobRepellentConfiguration( this );
		
		worlds = (ArrayList<World>) getServer().getWorlds();
		
		// Set up the default config, if one doesn't exist
		File configFile = new File( "plugins/MobRepellent/config.yml" );
		if( !configFile.isFile() )
		{
			config.setDefaults();
		}

		File pluginDir = new File( "plugins/MobRepellent/" );
		if( !pluginDir.exists() )
		{
			pluginDir.mkdir();
			this.log.info( "[MobRepellent] Folder not found, creating new one." );
		}

		// TODO: fix this to delay loading based on configuration
		repellers = new MobRepellentList( "plugins/MobRepellent/", this, true );
		
		if( repellers.isLoaded() )
		{
			loadPlugin();
		}
		else
		{
			delayLoadTask = getServer().getScheduler().scheduleSyncDelayedTask( this, new MobRepellentDelayLoadRunnable( this ) );
		}

		PluginDescriptionFile description = getDescription();
		getServer().getLogger().info( description.getName() + " (v" + description.getVersion() + ") is enabled!" );
	}

	public void onDisable()
	{
		config.save();
	}
	
	public void loadPlugin()
	{
		loadPlugin( false );
	}
	
	public void loadPlugin( boolean delayed )
	{	
		if( delayed )
		{
			getServer().getScheduler().cancelTask( delayLoadTask );
			// reload worlds, just in case
			worlds = (ArrayList<World>) getServer().getWorlds();
			repellers.load();
		}
			
		
		MobRepellentEntityListener entityListener = new MobRepellentEntityListener( this );
		MobRepellentBlockListener blockListener = new MobRepellentBlockListener( this );
	
		getServer().getPluginManager().registerEvent( Event.Type.CREATURE_SPAWN, entityListener, Event.Priority.Normal,
				this );
		getServer().getPluginManager().registerEvent( Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal,
				this );
		getServer().getPluginManager().registerEvent( Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal,
				this );
	}
	
	public MobRepellentConfiguration getConfig()
	{
		return this.config;
	}

	public Logger getLogger()
	{
		return this.log;
	}

	public MobRepellentList getRepellerList()
	{
		return this.repellers;
	}
	
	public ArrayList<World> getWorlds()
	{
		return worlds;
	}
	
	public static boolean isBaseOfRepeller( Block block, MobRepellentConfiguration configuration )
	{
		World w = block.getWorld();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		if( ( configuration.getRadius( w.getBlockAt( x, y + 1, z ).getType() ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x, y + 2, z ).getType() ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x + 1, y, z ).getType() ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x - 1, y, z ).getType() ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x, y, z + 1 ).getType() ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x, y, z - 1 ).getType() ) != -1 ) )
			return true;
		
		return false;
	}
}
