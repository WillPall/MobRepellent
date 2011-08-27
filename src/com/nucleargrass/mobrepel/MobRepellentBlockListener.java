package com.nucleargrass.mobrepel;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
			/*int newRepeller[] = new int[3];
			newRepeller[ 0 ] = block.getX();
			newRepeller[ 1 ] = block.getY();
			newRepeller[ 2 ] = block.getZ();*/

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
			
			//plugin.getRepellerList().add( newRepeller );
			/*this.plugin.getLogger().info(
					"[MobRepellent] A Mob Repellent has been constructed by " + event.getPlayer().getName() + " at "
							+ newRepeller[ 0 ] + "," + newRepeller[ 1 ] + "," + newRepeller[ 2 ] );*/
		}
	}

	public void onBlockBreak( BlockBreakEvent event )
	{
		Block block = event.getBlock();

		if( block.getType() == Material.DIAMOND_BLOCK )
		{
			/*int repeller[] = new int[4];
			repeller[ 0 ] = block.getX();
			repeller[ 1 ] = block.getY();
			repeller[ 2 ] = block.getZ();

			if( plugin.getRepellerList().remove( repeller ) )
			{
				this.plugin.getLogger().info(
						"[MobRepellent] A Mob Repellent has been destroyed by " + event.getPlayer().getName() );
			}*/
			
			removeBrokenRepellers( block );
		}
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
			if( isBaseOfRepeller( blockSet.get( i ) ) )
				repellerBlocks.add( blockSet.get( i ) );
		}
		
		return repellerBlocks;
	}
	
	private boolean isBaseOfRepeller( Block block )
	{
		World w = block.getWorld();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		if( ( w.getBlockAt( x, y + 1, z ).getType() == Material.DIAMOND_BLOCK ) &&
			( w.getBlockAt( x, y + 2, z ).getType() == Material.DIAMOND_BLOCK ) &&
			( w.getBlockAt( x + 1, y, z ).getType() == Material.DIAMOND_BLOCK ) &&
			( w.getBlockAt( x - 1, y, z ).getType() == Material.DIAMOND_BLOCK ) &&
			( w.getBlockAt( x, y, z + 1 ).getType() == Material.DIAMOND_BLOCK ) &&
			( w.getBlockAt( x, y, z - 1 ).getType() == Material.DIAMOND_BLOCK ) )
			return true;
		
		return false;
	}

	/*private ArrayList<Block> getHeighestBlocks( ArrayList<Block> blockSet )
	{
		ArrayList<Block> highest = new ArrayList<Block>();
		int highestY = blockSet.get( 0 ).getY();
		
		for( int i = 0; i < blockSet.size(); i++ )
		{
			if( blockSet.get( i ).getY() > highestY )
			{
				highest.clear();
				highest.add( blockSet.get( i ) );
				highestY = blockSet.get( i ).getY();
			}
			else if( blockSet.get( i ).getY() == highestY )
			{
				highest.add( blockSet.get( i ) );
			}
		}
		
		return highest;
	}*/

	/*
	 * public void onBlockPlace( BlockPlaceEvent event ) { ThunderTowerSettings
	 * properties = this.plugin.getProperties( event.getPlayer().getWorld() );
	 * ThunderTowerList towersList = this.plugin.getTowersList(
	 * event.getPlayer().getWorld() ); if(
	 * properties.getActive().equalsIgnoreCase( "true" ) ) { Block block =
	 * event.getBlock(); if( ( this.plugin.askPermissions( event.getPlayer(),
	 * "thundertower.create" ) ) && ( block.getTypeId() ==
	 * properties.getTypeID() ) ) { Block highestBlock =
	 * ThunderTowerList.getHighest( block, properties.getTypeID() ); if( (
	 * ThunderTowerList.taille( highestBlock, properties.getTypeID() ) >=
	 * properties.getLength() ) && ( highestBlock.getY() >= 64 +
	 * properties.getHauteur() ) ) { ArrayList tab = towersList.list;
	 * 
	 * boolean already = false; for( int i = 0; i < tab.size(); i++ ) { int[]
	 * lineTab = (int[]) tab.get( i ); if( ( lineTab[ 0 ] == highestBlock.getX()
	 * ) && ( lineTab[ 2 ] == highestBlock.getZ() ) ) { lineTab[ 1 ] =
	 * highestBlock.getY(); lineTab[ 3 ] = ThunderTowerList.taille(
	 * highestBlock, properties.getTypeID() ); tab.set( i, lineTab );
	 * event.getPlayer().sendMessage( properties.getColor() +
	 * "You've modified a ThunderTower." ); if(
	 * properties.getLog().equalsIgnoreCase( "true" ) ) {
	 * this.plugin.getLog().info(
	 * "[ThunderTower] A Tower has been increased by " +
	 * event.getPlayer().getName() + " at " + lineTab[ 0 ] + "," + lineTab[ 1 ]
	 * + "," + lineTab[ 2 ] ); } already = true; break; } } if( !already ) {
	 * int[] newTower = new int[4]; newTower[ 0 ] = highestBlock.getX();
	 * newTower[ 1 ] = highestBlock.getY(); newTower[ 2 ] = highestBlock.getZ();
	 * newTower[ 3 ] = ThunderTowerList.taille( highestBlock,
	 * properties.getTypeID() ); tab.add( newTower );
	 * event.getPlayer().sendMessage( properties.getColor() +
	 * "You've created a ThunderTower." ); if(
	 * properties.getLog().equalsIgnoreCase( "true" ) ) {
	 * this.plugin.getLog().info(
	 * "[ThunderTower] A Tower has been constructed by " +
	 * event.getPlayer().getName() + " at " + newTower[ 0 ] + "," + newTower[ 1
	 * ] + "," + newTower[ 2 ] ); } } towersList.save(); } } } }
	 * 
	 * public void onBlockBreak( BlockBreakEvent event ) { Block block =
	 * event.getBlock(); ThunderTowerSettings properties =
	 * this.plugin.getProperties( event.getPlayer().getWorld() );
	 * ThunderTowerList towersList = this.plugin.getTowersList(
	 * event.getPlayer().getWorld() ); if( (
	 * properties.getActive().equalsIgnoreCase( "true" ) ) && (
	 * block.getTypeId() == properties.getTypeID() ) ) { Block highestBlock =
	 * ThunderTowerList.getHighest( block, properties.getTypeID() ); Block
	 * downBlock = event.getPlayer().getWorld().getBlockAt( block.getX(),
	 * block.getY() - 1, block.getZ() ); ArrayList tab = towersList.list;
	 * 
	 * for( int i = 0; i < tab.size(); i++ ) { int[] lineTab = (int[]) tab.get(
	 * i ); if( ( lineTab[ 0 ] == highestBlock.getX() ) && ( lineTab[ 2 ] ==
	 * highestBlock.getZ() ) ) if( this.plugin.askPermissions(
	 * event.getPlayer(), "thundertower.destroy" ) ) { if( (
	 * ThunderTowerList.taille( highestBlock, properties.getTypeID() ) -
	 * ThunderTowerList.taille( block, properties.getTypeID() ) >=
	 * properties.getLength() ) && ( highestBlock.getY() >= 64 +
	 * properties.getHauteur() ) ) { lineTab[ 1 ] = highestBlock.getY();
	 * lineTab[ 3 ] = ( ThunderTowerList.taille( highestBlock,
	 * properties.getTypeID() ) - ThunderTowerList .taille( block,
	 * properties.getTypeID() ) ); tab.set( i, lineTab );
	 * event.getPlayer().sendMessage( properties.getColor() +
	 * "You've modified a ThunderTower." ); if(
	 * properties.getLog().equalsIgnoreCase( "true" ) ) {
	 * this.plugin.getLog().info( "[ThunderTower] A Tower has been reduced by "
	 * + event.getPlayer().getName() + " at " + lineTab[ 0 ] + "," + lineTab[ 1
	 * ] + "," + lineTab[ 2 ] ); } } else if( ( ThunderTowerList.taille(
	 * downBlock, properties.getTypeID() ) >= properties .getLength() ) && (
	 * highestBlock.getY() >= 64 + properties.getHauteur() ) ) { int[] newTower
	 * = new int[4]; newTower[ 0 ] = downBlock.getX(); newTower[ 1 ] =
	 * downBlock.getY(); newTower[ 2 ] = downBlock.getZ(); newTower[ 3 ] =
	 * ThunderTowerList.taille( downBlock, properties.getTypeID() ); tab.set( i,
	 * newTower ); event.getPlayer().sendMessage( properties.getColor() +
	 * "You've modified a ThunderTower." ); if(
	 * properties.getLog().equalsIgnoreCase( "true" ) )
	 * this.plugin.getLog().info( "[ThunderTower] A Tower has been reduced by "
	 * + event.getPlayer().getName() + " at " + newTower[ 0 ] + "," + newTower[
	 * 1 ] + "," + newTower[ 2 ] ); } else { tab.remove( i );
	 * event.getPlayer().sendMessage( properties.getColor() +
	 * "You've destroyed a ThunderTower." ); if(
	 * properties.getLog().equalsIgnoreCase( "true" ) ) {
	 * this.plugin.getLog().info(
	 * "[ThunderTower] A Tower has been destroyed by " +
	 * event.getPlayer().getName() + " at " + lineTab[ 0 ] + "," + lineTab[ 1 ]
	 * + "," + lineTab[ 2 ] ); } } towersList.save(); } else {
	 * event.setCancelled( true ); event.getPlayer().sendMessage( ChatColor.RED
	 * + "You've not the right to destroy this ThunderTower." ); } } } }
	 * 
	 * public boolean equalsLocation( Block b1, Block b2 ) { return ( b1.getX()
	 * == b2.getX() ) && ( b1.getY() == b2.getY() ) && ( b1.getZ() == b2.getZ()
	 * ); }
	 */
}