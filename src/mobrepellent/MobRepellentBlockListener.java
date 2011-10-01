package mobrepellent;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class MobRepellentBlockListener extends BlockListener
{
	private MobRepellent plugin;

	public MobRepellentBlockListener( MobRepellent instance )
	{
		this.plugin = instance;
	}

	public void onBlockPlace( BlockPlaceEvent event )
	{
		Block block = event.getBlock();

		if( plugin.getConfig().getRadius( block ) != -1 )
		{
			ArrayList<Block> blockSet = getAdjacentRepellerBlocks( block );
			
			for( int i = 0; i < blockSet.size(); i++ )
			{
				if( !plugin.getRepellerList().contains( blockSet.get( i ) ) )
				{
					plugin.getRepellerList().add( blockSet.get( i ) );
					this.plugin.getLogger().info(
							"[MobRepellent] A new repeller was constructed by " + event.getPlayer().getName() + " at ("
									+ blockSet.get( i ).getX() + "," + blockSet.get( i ).getY() + ","
									+ blockSet.get( i ).getZ() + ")" );
					event.getPlayer().sendMessage( ChatColor.GREEN + "You've constructed a " + ChatColor.YELLOW + plugin.getConfig().getStrength( blockSet.get( i ) ).toString() + ChatColor.GREEN + " MobRepeller!" );
				}
			}
		}
	}

	public void onBlockBreak( BlockBreakEvent event )
	{
		Block block = event.getBlock();

		if( plugin.getConfig().getRadius( block ) != -1 )
		{
			if( removeBrokenRepellers( block ) )
				event.getPlayer().sendMessage( ChatColor.RED + "You've destroyed a MobRepeller!" );
		}
	}

	private boolean removeBrokenRepellers( Block block )
	{
		World w = block.getWorld();
		MobRepellentList rlist = plugin.getRepellerList();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		boolean removed = false;
		
		if( rlist.contains( w.getBlockAt( x + 1, y, z ) ) )
		{
			if( rlist.remove( w.getBlockAt( x + 1, y, z ) ) )
				removed = true;
		}
		if( rlist.contains( w.getBlockAt( x - 1, y, z ) ) )
		{
			if( rlist.remove( w.getBlockAt( x - 1, y, z ) ) )
				removed = true;
		}
		if( rlist.contains( w.getBlockAt( x, y - 1, z ) ) )
		{
			if( rlist.remove( w.getBlockAt( x, y - 1, z ) ) )
				removed = true;
		}
		if( rlist.contains( w.getBlockAt( x, y - 2, z ) ) )
		{
			if( rlist.remove( w.getBlockAt( x, y - 2, z ) ) )
				removed = true;
		}
		if( rlist.contains( w.getBlockAt( x, y, z + 1 ) ) )
		{
			if( rlist.remove( w.getBlockAt( x, y, z + 1 ) ) )
				removed = true;
		}
		if( rlist.contains( w.getBlockAt( x, y, z - 1 ) ) )
		{
			if( rlist.remove( w.getBlockAt( x, y, z - 1 ) ) )
				removed = true;
		}
		if( rlist.contains( block ) )
		{
			if( rlist.remove( block ) )
				removed = true;
		}

		return removed;
	}
	
	private ArrayList<Block> getAdjacentRepellerBlocks( Block block )
	{
		ArrayList<Block> blockSet = new ArrayList<Block>();
		ArrayList<Block> repellerBlocks;
		
		// Add all the adjacent blocks to the set
		addToSet( block, blockSet );
		
		// TODO: this seems like too much work
		// Determine if any blocks in the set match the pattern
		repellerBlocks = getMatchingPatternBlocks( blockSet );

		return repellerBlocks;
	}

	private void addToSet( Block block, ArrayList<Block> blockSet )
	{
		blockSet.add( block );
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();

		World currentWorld = block.getWorld();

		// TODO: this algorithm seems very hacky. must be a better way to determine
		//		 all adjacent blocks
		//		 NEW STUFF: There is a better way. convert this to a 3d matrix and match
		//		 against the matrix
		if( ( plugin.getConfig().getRadius( currentWorld.getBlockAt( x + 1, y, z ) ) != -1 ) &&
			( !blockSet.contains( currentWorld.getBlockAt( x + 1, y, z ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x + 1, y, z ), blockSet );
		}
		if( ( plugin.getConfig().getRadius( currentWorld.getBlockAt( x - 1, y, z ) ) != -1 ) &&
				( !blockSet.contains( currentWorld.getBlockAt( x - 1, y, z ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x - 1, y, z ), blockSet );
		}
		if( ( plugin.getConfig().getRadius( currentWorld.getBlockAt( x, y + 1, z ) ) != -1 ) &&
				( !blockSet.contains( currentWorld.getBlockAt( x, y + 1, z ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x, y + 1, z ), blockSet );
		}
		if( ( plugin.getConfig().getRadius( currentWorld.getBlockAt( x, y - 1, z ) ) != -1 ) &&
				( !blockSet.contains( currentWorld.getBlockAt( x, y - 1, z ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x, y - 1, z ), blockSet );
		}
		if( ( plugin.getConfig().getRadius( currentWorld.getBlockAt( x, y, z + 1 ) ) != -1 ) &&
				( !blockSet.contains( currentWorld.getBlockAt( x, y, z + 1 ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x, y, z + 1 ), blockSet );
		}
		if( ( plugin.getConfig().getRadius( currentWorld.getBlockAt( x, y, z - 1 ) ) != -1 ) &&
				( !blockSet.contains( currentWorld.getBlockAt( x, y, z - 1 ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x, y, z - 1 ), blockSet );
		}
	}
	
	private ArrayList<Block> getMatchingPatternBlocks( ArrayList<Block> blockSet )
	{
		ArrayList<Block> repellerBlocks = new ArrayList<Block>();
		
		for( int i = 0; i < blockSet.size(); i++ )
		{
			if( MobRepellent.isBaseOfRepeller( blockSet.get( i ), plugin.getConfig() ) )
				repellerBlocks.add( blockSet.get( i ) );
		}
		
		return repellerBlocks;
	}
}