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
			
			( !plugin.getConfig().shouldRepelNeutralMobs() &&
			( event.getEntity() instanceof Animals ) &&
			( event.getSpawnReason() == SpawnReason.EGG ) ) )
			return;

		Location loc = event.getLocation();
		
		if( plugin.getRepellerList().isRepelled( loc.getX(), loc.getY(), loc.getZ(), loc.getWorld() ) )
		{
			if( plugin.getConfig().getDebugMode() )
			{
				int num = plugin.getRepellerList().getRepelledBaseId( loc.getX(), loc.getY(), loc.getZ(), loc.getWorld() );
				this.plugin.debug( "[MobRepellent] Repelled by #" + num + ": " + Math.round( loc.getX() ) + ", " + Math.round( loc.getY() ) + ", " + Math.round( loc.getZ() ) + ", " + loc.getWorld().getName() );
			}
			
			event.setCancelled( true );
		}
	}
}