package mobrepellent;

import java.util.ArrayList;
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

		if( block.getType() == Material.DIAMOND_BLOCK )
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
				}
			}
		}
	}

	public void onBlockBreak( BlockBreakEvent event )
	{
		Block block = event.getBlock();

		if( block.getType() == Material.DIAMOND_BLOCK )	
			removeBrokenRepellers( block );
	}

	private void removeBrokenRepellers( Block block )
	{
		World w = block.getWorld();
		MobRepellentList rlist = plugin.getRepellerList();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		if( rlist.contains( w.getBlockAt( x + 1, y, z ) ) )
		{
			rlist.remove( w.getBlockAt( x + 1, y, z ) );
		}
		if( rlist.contains( w.getBlockAt( x - 1, y, z ) ) )
		{
			rlist.remove( w.getBlockAt( x - 1, y, z ) );
		}
		if( rlist.contains( w.getBlockAt( x, y - 1, z ) ) )
		{
			rlist.remove( w.getBlockAt( x, y - 1, z ) );
		}
		if( rlist.contains( w.getBlockAt( x, y - 2, z ) ) )
		{
			rlist.remove( w.getBlockAt( x, y - 2, z ) );
		}
		if( rlist.contains( w.getBlockAt( x, y, z + 1 ) ) )
		{
			rlist.remove( w.getBlockAt( x, y, z + 1 ) );
		}
		if( rlist.contains( w.getBlockAt( x, y, z - 1 ) ) )
		{
			rlist.remove( w.getBlockAt( x, y, z - 1 ) );
		}
		if( rlist.contains( block ) )
		{
			rlist.remove( block );
		}
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
		if( ( currentWorld.getBlockAt( x + 1, y, z ).getType() == Material.DIAMOND_BLOCK ) &&
			( !blockSet.contains( currentWorld.getBlockAt( x + 1, y, z ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x + 1, y, z ), blockSet );
		}
		if( ( currentWorld.getBlockAt( x - 1, y, z ).getType() == Material.DIAMOND_BLOCK ) &&
				( !blockSet.contains( currentWorld.getBlockAt( x - 1, y, z ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x - 1, y, z ), blockSet );
		}
		if( ( currentWorld.getBlockAt( x, y + 1, z ).getType() == Material.DIAMOND_BLOCK ) &&
				( !blockSet.contains( currentWorld.getBlockAt( x, y + 1, z ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x, y + 1, z ), blockSet );
		}
		if( ( currentWorld.getBlockAt( x, y - 1, z ).getType() == Material.DIAMOND_BLOCK ) &&
				( !blockSet.contains( currentWorld.getBlockAt( x, y - 1, z ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x, y - 1, z ), blockSet );
		}
		if( ( currentWorld.getBlockAt( x, y, z + 1 ).getType() == Material.DIAMOND_BLOCK ) &&
				( !blockSet.contains( currentWorld.getBlockAt( x, y, z + 1 ) ) ) )
		{
			addToSet( currentWorld.getBlockAt( x, y, z + 1 ), blockSet );
		}
		if( ( currentWorld.getBlockAt( x, y, z - 1 ).getType() == Material.DIAMOND_BLOCK ) &&
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
			if( MobRepellent.isBaseOfRepeller( blockSet.get( i ) ) )
				repellerBlocks.add( blockSet.get( i ) );
		}
		
		return repellerBlocks;
	}
}