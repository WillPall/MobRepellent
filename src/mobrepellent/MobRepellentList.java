package mobrepellent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MobRepellentList
{
	private File file;
	private ArrayList<MobRepeller> list;
	private MobRepellent plugin;

	public MobRepellentList( String workingDir, MobRepellent plugin )
	{
		this.plugin = plugin;
		this.file = new File( workingDir + "repellers.list" );
		this.list = new ArrayList<MobRepeller>();
		
		load();
	}
	
	// TODO: get rid of this function. this is for debug purposes only
	public int getRepelledBaseId( Location loc )
	{
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		World world = loc.getWorld();
		
		for( int i = 0; i < list.size(); i++ )
		{
			MobRepeller repeller = list.get( i );
			int radius = plugin.getMobRepellentConfiguration().getRadius( repeller );
			
			if( ( ( repeller.getX() - radius ) < x ) && ( ( repeller.getX() + radius ) > x ) &&
				( ( repeller.getY() - radius ) < y ) && ( ( repeller.getY() + radius ) > y ) &&
				( ( repeller.getZ() - radius ) < z ) && ( ( repeller.getZ() + radius ) > z ) &&
				( ( repeller.getWorld().getUID().equals( world.getUID() ) ) ) )
				return (i + 1);
		}
		
		return -1;
	}
	
	public boolean isRepelled( Location loc )
	{	
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		World world = loc.getWorld();
		
		for( int i = 0; i < list.size(); i++ )
		{
			MobRepeller repeller = list.get( i );
			int radius = plugin.getMobRepellentConfiguration().getRadius( repeller );
			
			// Don't repel if we aren't worrying about mobs below the repeller
			if( !plugin.getMobRepellentConfiguration().shouldRepelBelow() && ( repeller.getY() > y ) )
				return false;
			
			if( ( ( repeller.getX() - radius ) < x ) && ( ( repeller.getX() + radius ) > x ) &&
				( ( repeller.getY() - radius ) < y ) && ( ( repeller.getY() + radius ) > y ) &&
				( ( repeller.getZ() - radius ) < z ) && ( ( repeller.getZ() + radius ) > z ) &&
				( ( repeller.getWorld().getUID().equals( world.getUID() ) ) ) )
				return true;
		}
		
		return false;
	}
	
	public void add( Block block )
	{
		MobRepeller newRepeller = new MobRepeller( block );
		this.list.add( newRepeller );
		save();
	}
	
	public void add( int x, int y, int z, World world, Material material, int blockData )
	{
		MobRepeller newRepeller = new MobRepeller( x, y, z, world, material, blockData );
		this.list.add( newRepeller );
		save();
	}
	
	public boolean remove( Block base )
	{
		int pos = getPositionOfRepeller( base );
		
		if( pos > -1 )
		{
			list.remove( pos );
			plugin.getLogger().info( "[MobRepellent] A repeller has been destroyed at (" + base.getX() + "," + base.getY() + "," + base.getZ() + ")" );
			save();
			return true;
		}
		
		return false;
	}
	
	public boolean remove( int repellerNumber )
	{
		if( ( repellerNumber > 0 ) &&
			( repellerNumber <= list.size() ) )
		{
			list.remove( repellerNumber - 1 );
			save();
			return true;
		}
		
		return false;
	}
	
	public void removeAll()
	{
		list.clear();
		save();
	}
	
	/**
	 * Finds the position in the repeller ArrayList of the given base block.
	 * 
	 * @param base The base of the repeller that is being searched for.
	 * @return The index of the repeller or -1 if none are found.
	 */
	private int getPositionOfRepeller( Block base )
	{
		// TODO: check on this, this may cause problems when blocks don't exist
		for( int i = 0; i < list.size(); i++ )
		{
			MobRepeller repeller = list.get( i );
			
			if( ( repeller.getX() == base.getX() ) &&
				( repeller.getY() == base.getY() ) &&
				( repeller.getZ() == base.getZ() ) &&
				( repeller.getWorld().getUID().toString().equals( base.getWorld().getUID().toString() ) ) )
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public ArrayList<MobRepeller> getList()
	{
		return list;
	}
	
	public boolean contains( Block base )
	{
		int pos = getPositionOfRepeller( base );
		
		if( pos > -1 )
			return true;
		
		return false;
	}

	private void load()
	{
		if( this.file.exists() )
		{
			try
			{
				FileInputStream fis = new FileInputStream( this.file );
				BufferedReader buffer = new BufferedReader( new InputStreamReader( fis ) );

				try
				{
					String line;
					int lineNum = 0;
					while( ( line = buffer.readLine() ) != null )
					{
						lineNum++;
						if( line.trim() == "" )
							continue;
						
						String fields[] = line.split( "\\," );

						try
						{
							int x = Integer.parseInt( fields[ 0 ] );
							int y = Integer.parseInt( fields[ 1 ] );
							int z = Integer.parseInt( fields[ 2 ] );
							String wUID = null;
							ArrayList<World> worlds = plugin.getWorlds();
							boolean added = false;
							
							plugin.debug( "[MobRepellent] Searching for repellent (#" + lineNum + ") at " + x + "," + y + "," + z );

							// Try to load the world, otherwise, this may be an old save file
							if( fields.length >= 4 )
							{
								wUID = fields[ 3 ];
								plugin.debug( "[MobRepellent] #" + lineNum + " has wUID = " + wUID );
							}
								
							for( int i = 0; i < worlds.size(); i++ )
							{
								plugin.debug( "[MobRepellent] Block matching repller in world is type: " + worlds.get( i ).getBlockAt( x, y, z ).getType().toString() );
								
								// If the file has a world, find the world with the UID and add it
								// otherwise, try to find a world that has a repeller at this position and
								// add that instead
								if( ( (wUID != null ) &&
									worlds.get(i).getUID().toString().equals( wUID ) ) ||
									MobRepellent.isBaseOfRepeller( worlds.get( i ).getBlockAt( x, y, z ), plugin.getMobRepellentConfiguration() ) )
								{
									// TODO: determine what to do with this
									// Send a debug message that the repeller may not be valid
									if( !MobRepellent.isBaseOfRepeller( worlds.get( i ).getBlockAt( x, y, z ), plugin.getMobRepellentConfiguration() ) )
										plugin.debug( "[MobRepellent] WARNING: Repeller at (" + x + ", " + y + ", " + z + ") in world '" + worlds.get(i).getName() + "' may be invalid.");
										
									// TODO: this is hacky. The repeller loaded from the file must equal the block type
									//		 loaded from the config. However, block type is sometimes checked in the above
									//		 conditional because of backwards compatibility. Find a better way to do this.
									// TODO AGAIN: removed the block checking. this should never be a problem anyway
									// TODO: add the block checking back in, we've changed the way repellers are stored
									if( !this.contains( worlds.get(i).getBlockAt( x, y, z ) ) /*&&
										MobRepellent.isBaseOfRepeller( worlds.get( i ).getBlockAt( x, y, z ), plugin.getConfig() )*/ )
									{
										plugin.debug( "[MobRepellent] Found repeller #" + lineNum + " in world '" + worlds.get( i ).getName() + "'" );
										list.add( new MobRepeller( worlds.get( i ).getBlockAt( x, y, z ) ) );
										added = true;
										break;
									}
								}
							}
							
							// If no block or world was found that matches the repeller list
							if( !added )
							{
								plugin.getLogger().info( "[MobRepellent] No repeller was found that matches entry on line " + lineNum + ". Ignoring entry." );
							}
						}
						catch( NumberFormatException nfe )
						{
							plugin.getLogger().info( "[MobRepellent] Malformed repeller entry on line " + lineNum + ". Ignoring entry." );
						}
					}
				}
				catch( IOException e )
				{
					plugin.getLogger().info( "[MobRepellent] Error reading from 'repellers.list'. Aborting load." );
					//e.printStackTrace();
					
					// return so that we don't overwrite a possibly fine repellers.list
					return;
				}
				
				buffer.close();
				fis.close();
			}
			catch( FileNotFoundException e )
			{
				e.getStackTrace();
			}
			catch( IOException ioe )
			{
				ioe.printStackTrace();
			}
		}
		else
		{
			this.plugin.getLogger().info( "[MobRepellent] No repeller file was found. Creating new file." );
		}

		if( list.size() > 0 )
			this.plugin.getLogger().info( "[MobRepellent] Finished loading plugin." );
		else
			this.plugin.getLogger().info( "[MobRepellent] Finished loading plugin. No repellers were loaded." );
		
		
		save();
	}

	private void save()
	{
		try
		{
			if( this.file.exists() )
				this.file.delete();

			this.file.createNewFile();
			FileWriter fw = new FileWriter( this.file );

			for( int i = 0; i < this.list.size(); i++ )
			{
				MobRepeller r = this.list.get( i );
				
				fw.write( r.toString() + "\n" );
			}
			fw.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
}
