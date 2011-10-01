package mobrepellent;

import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityListener;

public class MobRepellentEntityListener extends EntityListener
{
	private MobRepellent plugin;
	
	public MobRepellentEntityListener( MobRepellent plugin )
	{
		this.plugin = plugin;
	}
	
	public void onCreatureSpawn( CreatureSpawnEvent event )
	{
		if( event.isCancelled() ||
			( event.getSpawnReason() == SpawnReason.EGG ) ||
			( !plugin.getConfig().shouldRepelNeutralMobs() &&
			( event.getEntity() instanceof Animals ) ) )
			return;
		
		if( plugin.getRepellerList().isRepelled( event.getLocation() ) )
		{
			if( plugin.getConfig().getDebugMode() )
			{
				int num = plugin.getRepellerList().getRepelledBaseId( event.getLocation() );
				this.plugin.debug( "[MobRepellent] Entity '" + event.getCreatureType().getName() + "' repelled by #" + num + ": " + Math.round( event.getLocation().getX() ) + ", " + Math.round( event.getLocation().getY() ) + ", " + Math.round( event.getLocation().getZ() ) + ", " + event.getLocation().getWorld().getName() );
			}
			
			event.setCancelled( true );
		}
	}
}