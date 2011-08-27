package mobrepellent;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MobRepellent extends JavaPlugin
{
	private Logger log = Logger.getLogger( "Minecraft" );
	private MobRepellentList repellers;
	private ArrayList<World> worlds;

	public void onEnable()
	{
		MobRepellentEntityListener entityListener = new MobRepellentEntityListener( this );
		MobRepellentBlockListener blockListener = new MobRepellentBlockListener( this );
		
		worlds = (ArrayList) getServer().getWorlds();
		repellers = new MobRepellentList( "plugins/MobRepellent/", this );

		File pluginDir = new File( "plugins/MobRepellent/" );
		if( !pluginDir.exists() )
		{
			pluginDir.mkdir();
			this.log.info( "[MobRepellent] Folder not found, creating new one." );
		}

		getServer().getPluginManager().registerEvent( Event.Type.CREATURE_SPAWN, entityListener, Event.Priority.Normal,
				this );
		getServer().getPluginManager().registerEvent( Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal,
				this );
		getServer().getPluginManager().registerEvent( Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal,
				this );

		PluginDescriptionFile description = getDescription();
		getServer().getLogger().info( description.getName() + " (v" + description.getVersion() + ") is enabled!" );
	}

	public void onDisable()
	{
	}

	public Logger getLogger()
	{
		return this.log;
	}

	public MobRepellentList getRepellerList()
	{
		return this.repellers;
	}
}
