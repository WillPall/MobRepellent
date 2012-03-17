package mobrepellent;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.CreatureType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MobRepellentEntityListener implements Listener
{
	private MobRepellent plugin;
	
	public MobRepellentEntityListener( MobRepellent plugin )
	{
		this.plugin = plugin;
	}

	@EventHandler( priority = EventPriority.HIGHEST )
	public void onCreatureSpawn( CreatureSpawnEvent event )
	{
		// Check for other plugins that have cancelled the event,
		// egg spawns, spawner spawns, and neutral mobs.
		if( event.isCancelled() ||
			( event.getSpawnReason() == SpawnReason.EGG ) ||
			( event.getSpawnReason() == SpawnReason.SPAWNER ) ||
			( !plugin.getMobRepellentConfiguration().shouldRepelNeutralMobs() &&
			( event.getEntity() instanceof Animals ) ) )
			return;
		
		HashSet<CreatureType> mobsToRepel = plugin.getMobRepellentConfiguration().getMobsToRepel();
		// Now check to make sure the mob is in the list
		if( !mobsToRepel.isEmpty() )
		{
			if( !mobsToRepel.contains( event.getCreatureType() ) )
				return;
		}
		
		if( plugin.getRepellerList().isRepelled( event.getLocation() ) )
		{
			if( plugin.getMobRepellentConfiguration().getDebugMode() )
			{
				int num = plugin.getRepellerList().getRepelledBaseId( event.getLocation() );
				this.plugin.debug( "[MobRepellent] Entity '" + event.getCreatureType().getName() + "' repelled by #" + num + ": " + Math.round( event.getLocation().getX() ) + ", " + Math.round( event.getLocation().getY() ) + ", " + Math.round( event.getLocation().getZ() ) + ", " + event.getLocation().getWorld().getName() );
			}
			
			event.setCancelled( true );
		}
	}
}