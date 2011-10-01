package mobrepellent;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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

		delayLoadTask = getServer().getScheduler().scheduleSyncDelayedTask( this, new MobRepellentDelayLoadRunnable( this ) );

		PluginDescriptionFile description = getDescription();
		getServer().getLogger().info( description.getName() + " (v" + description.getVersion() + ") is enabled!" );
	}

	public void onDisable()
	{
		config.save();
	}
	
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
	{
		// This is for whether we want console-only or player command
		Player player = null;
		
		if( sender instanceof Player )
			player = (Player) sender;
		
		if( command.getName().equalsIgnoreCase( "mrlist" ) )
		{
			// decline if player doesn't have permission
			if( ( player != null ) &&
				!player.hasPermission( "mobrepellent.list" ) )
			{
				player.sendMessage( ChatColor.RED + "You do not have permission to use that command." );
				return true;
			}
			
			ArrayList<MobRepeller> list = repellers.getList();
			
			if( list.isEmpty() )
			{
				if( player != null )
				{
					player.sendMessage( ChatColor.GREEN + "No repellers loaded." );
				}
				else
					log.info( "[MobRepellent] No repellers loaded." );
			}
			else
			{
				if( player != null )
					player.sendMessage( ChatColor.DARK_GREEN + "Loaded Repellers:" );
				else
					log.info( "[MobRepellent] Loaded Repellers:" );
				
				for( int i = 0; i < list.size(); i++ )
				{
					Block base = list.get( i ).getBase();

					if( player != null )
					{
						player.sendMessage( ChatColor.GREEN.toString() + ( i + 1 ) + " - W: "
								+ list.get( i ).getWorld().getName() + " - Co-ord: " + base.getX() + ", " + base.getY()
								+ ", " + base.getZ() );
					}
					else
						log.info( "    " + ( i + 1 ) + " - W: " + list.get( i ).getWorld().getName() + " - Co-ord: "
								+ base.getX() + ", " + base.getY() + ", " + base.getZ() );
				}
			}
			return true;
		}
		else if( command.getName().equalsIgnoreCase( "mrreload" ) )
		{
			// decline if player doesn't have permission
			if( ( player != null ) &&
				!player.hasPermission( "mobrepellent.list" ) )
			{
				player.sendMessage( ChatColor.RED + "You do not have permission to use that command." );
				return true;
			}
			
			this.config.reload();
			if( player != null )
			{
				player.sendMessage( ChatColor.GREEN.toString() + "MobRepellent config successfully reloaded." );
			}
			else
				log.info( "[MobRepellent] Config successfully reloaded." );
		
			return true;
		}
		else if( command.getName().equalsIgnoreCase( "mrremove" ) )
		{
			// decline if player doesn't have permission
			if( ( player != null ) &&
				!player.hasPermission( "mobrepellent.remove" ) )
			{
				player.sendMessage( ChatColor.RED + "You do not have permission to use that command." );
				return true;
			}
			
			int repellerNum = -1;
			try
			{
				repellerNum = Integer.parseInt( args[0] );

				if( repellers.remove( repellerNum ) )
				{
					if( player != null )
						player.sendMessage( ChatColor.GREEN + "Removed repeller #" + repellerNum );
					else
						log.info( "[MobRepellent] Removed repeller #" + repellerNum );
					
					return true;
				}
			}
			catch( Exception e )
			{
				// Do nothing
			}
			return false;
		}
		else if( command.getName().equalsIgnoreCase( "mrremoveall" ) )
		{
			// decline if player doesn't have permission
			if( ( player != null ) &&
				!player.hasPermission( "mobrepellent.removeall" ) )
			{
				player.sendMessage( ChatColor.RED + "You do not have permission to use that command." );
				return true;
			}
			
			repellers.removeAll();
			if( player != null )
				player.sendMessage( ChatColor.GREEN + "Removed all repellers" );
			else
				log.info( "[MobRepellent] Removed all repellers" );
			
			return true;
		}
		
		return false;
	}
	
	public void reloadRepellers()
	{
		worlds = (ArrayList<World>) getServer().getWorlds();
		repellers = new MobRepellentList( "plugins/MobRepellent/", this );
	}
	
	public void loadPlugin()
	{
		getServer().getScheduler().cancelTask( delayLoadTask );
		// reload worlds, just in case
		worlds = (ArrayList<World>) getServer().getWorlds();
		repellers = new MobRepellentList( "plugins/MobRepellent/", this );
		
		MobRepellentEntityListener entityListener = new MobRepellentEntityListener( this );
		MobRepellentBlockListener blockListener = new MobRepellentBlockListener( this );
	
		getServer().getPluginManager().registerEvent( Event.Type.CREATURE_SPAWN, entityListener, Event.Priority.High,
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
	
	public void debug( String message )
	{
		if( config.getDebugMode() )
			log.info( message );
	}
	
	public static boolean isBaseOfRepeller( Block block, MobRepellentConfiguration configuration )
	{
		World w = block.getWorld();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		if( ( configuration.getRadius( w.getBlockAt( x, y + 1, z ) ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x, y + 2, z ) ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x + 1, y, z ) ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x - 1, y, z ) ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x, y, z + 1 ) ) != -1 ) &&
			( configuration.getRadius( w.getBlockAt( x, y, z - 1 ) ) != -1 ) )
			return true;
		
		return false;
	}
}
