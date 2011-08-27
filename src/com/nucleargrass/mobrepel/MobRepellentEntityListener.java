package com.nucleargrass.mobrepel;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
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
		if( ( event.getEntity() instanceof Animals ) )
			return;

		Location loc = event.getLocation();
		
		if( plugin.getRepellerList().isRepelled( loc.getX(), loc.getY(), loc.getZ() ) )
		{

			//this.plugin.getLogger().info(
			//		"[MobRepellent] A mob has been repelled at " + loc.getX() + "," + loc.getY() + "," + loc.getZ()  );
			event.setCancelled( true );
		}
		/*World world = event.getEntity().getWorld();
		Location loc = event.getLocation();

		int no_spawn_blocks = 0;
		int adjacent_ns_blocks = 0;
		int total_ns_blocks = 0;

		int MIN_LEAVES = 4;
		int MIN_MM_BLOCKS = 2;
		int MIN_ADJ_BLOCKS = 4;
		int MIN_TOTAL_BLOCKS = 750;

		for( int x = loc.getBlockX() - 5; x < loc.getBlockX() + 5; x++ )
		{
			for( int y = loc.getBlockY() - 5; y < loc.getBlockY() + 5; y++ )
			{
				for( int z = loc.getBlockZ() - 5; z < loc.getBlockZ() + 5; z++ )
				{
					int mat = world.getBlockTypeIdAt( x, y, z );
					if( Material.getMaterial( mat ) == Material.MOB_SPAWNER )
					{
						return;
					}
				}
			}
		}

		for( int x = loc.getBlockX() - 5; x < loc.getBlockX() + 5; x++ )
		{
			for( int y = loc.getBlockY() - 1; y < loc.getBlockY() + 1; y++ )
			{
				for( int z = loc.getBlockZ() - 5; z < loc.getBlockZ() + 5; z++ )
				{
					int mat = world.getBlockTypeIdAt( x, y, z );
					if( Material.getMaterial( mat ) == Material.LEAVES )
					{
						no_spawn_blocks++;
					}
				}
			}
		}

		if( no_spawn_blocks >= 4 )
		{
			event.setCancelled( true );
			return;
		}
		no_spawn_blocks = 0;

		for( int x = loc.getBlockX() - 20; x < loc.getBlockX() + 20; x++ )
		{
			for( int y = loc.getBlockY() - 5; y < loc.getBlockY() + 5; y++ )
			{
				for( int z = loc.getBlockZ() - 20; z < loc.getBlockZ() + 20; z++ )
				{
					int mat = world.getBlockTypeIdAt( x, y, z );
					if( ( Material.getMaterial( mat ) == Material.COBBLESTONE )
							|| ( Material.getMaterial( mat ) == Material.DIAMOND_BLOCK )
							|| ( Material.getMaterial( mat ) == Material.BRICK )
							|| ( Material.getMaterial( mat ) == Material.FENCE )
							|| ( Material.getMaterial( mat ) == Material.GLASS )
							|| ( Material.getMaterial( mat ) == Material.GOLD_BLOCK )
							|| ( Material.getMaterial( mat ) == Material.IRON_BLOCK )
							|| ( Material.getMaterial( mat ) == Material.LAPIS_BLOCK )
							|| ( Material.getMaterial( mat ) == Material.SNOW_BLOCK )
							|| ( Material.getMaterial( mat ) == Material.WOOD )
							|| ( Material.getMaterial( mat ) == Material.COBBLESTONE_STAIRS )
							|| ( Material.getMaterial( mat ) == Material.WOOD_STAIRS )
							|| ( Material.getMaterial( mat ) == Material.WOODEN_DOOR )
							|| ( Material.getMaterial( mat ) == Material.IRON_DOOR )
							|| ( Material.getMaterial( mat ) == Material.STEP )
							|| ( Material.getMaterial( mat ) == Material.WOOL ) )
					{
						for( int inner_x = loc.getBlockX() - 5; inner_x < loc
								.getBlockX() + 5; inner_x++ )
						{
							for( int inner_y = loc.getBlockY() - 2; inner_y < loc
									.getBlockY() + 2; inner_y++ )
							{
								for( int inner_z = loc.getBlockZ() - 5; inner_z < loc
										.getBlockZ() + 5; inner_z++ )
								{
									int adj_mat = world.getBlockTypeIdAt(
											inner_x, inner_y, inner_z );
									if( ( Material.getMaterial( adj_mat ) == Material.COBBLESTONE )
											|| ( Material.getMaterial( adj_mat ) == Material.DIAMOND_BLOCK )
											|| ( Material.getMaterial( adj_mat ) == Material.BRICK )
											|| ( Material.getMaterial( adj_mat ) == Material.FENCE )
											|| ( Material.getMaterial( adj_mat ) == Material.GLASS )
											|| ( Material.getMaterial( adj_mat ) == Material.GOLD_BLOCK )
											|| ( Material.getMaterial( adj_mat ) == Material.IRON_BLOCK )
											|| ( Material.getMaterial( adj_mat ) == Material.LAPIS_BLOCK )
											|| ( Material.getMaterial( adj_mat ) == Material.SNOW_BLOCK )
											|| ( Material.getMaterial( adj_mat ) == Material.WOOD )
											|| ( Material.getMaterial( adj_mat ) == Material.COBBLESTONE_STAIRS )
											|| ( Material.getMaterial( adj_mat ) == Material.WOOD_STAIRS )
											|| ( Material.getMaterial( adj_mat ) == Material.WOODEN_DOOR )
											|| ( Material.getMaterial( adj_mat ) == Material.IRON_DOOR )
											|| ( Material.getMaterial( adj_mat ) == Material.STEP )
											|| ( Material.getMaterial( adj_mat ) == Material.WOOL ) )
									{
										adjacent_ns_blocks++;
									}
								}
							}
						}
						if( adjacent_ns_blocks >= 4 )
							no_spawn_blocks++;
						total_ns_blocks++;
						adjacent_ns_blocks = 0;
					}
				}
			}
		}
		if( ( no_spawn_blocks >= 2 ) || ( total_ns_blocks >= 750 ) )
		{
			event.setCancelled( true );
			return;
		}*/
	}
}