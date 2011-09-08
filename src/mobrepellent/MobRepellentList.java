package mobrepellent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.block.Block;

public class MobRepellentList
{
	private File file;
	//private ArrayList<int[]> list;
	private ArrayList<Block> list;
	private MobRepellent plugin;

	public MobRepellentList( String workingDir, MobRepellent plugin )
	{
		this.plugin = plugin;
		this.file = new File( workingDir + "repellers.list" );
		this.list = new ArrayList<Block>();
		load();
	}
	
	// TODO: two methods may be unnecessary since the radius is
	//		 now pulled from the config file.
	/*public boolean isRepelled( double x, double y, double z, World world )
	{
		return isRepelled( x, y, z, world, 100 );
	}*/
	
	public boolean isRepelled( double x, double y, double z, World world )
	{
		int radius = plugin.getConfig().getRadius();
		
		for( int i = 0; i < list.size(); i++ )
		{
			if( ( ( list.get(i).getX() - radius ) < x ) && ( ( list.get(i).getX() + radius ) > x ) &&
				( ( list.get(i).getY() - radius ) < y ) && ( ( list.get(i).getY() + radius ) > y ) &&
				( ( list.get(i).getZ() - radius ) < z ) && ( ( list.get(i).getZ() + radius ) > z ) &&
				( ( list.get(i).getWorld().getUID().equals( world.getUID() ) ) ) )
				return true;
		}
		
		return false;
	}
	
	public void add( Block block )
	{
		this.list.add( block );
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
	
	/**
	 * Finds the position in the repeller ArrayList of the given base block.
	 * 
	 * @param base The base of the repeller that is being searched for.
	 * @return The index of the repeller or -1 if none are found.
	 */
	private int getPositionOfRepeller( Block base )
	{
		for( int i = 0; i < list.size(); i++ )
		{
			Block cur = list.get( i );
			
			if( ( cur.getX() == base.getX() ) &&
				( cur.getY() == base.getY() ) &&
				( cur.getZ() == base.getZ() ) &&
				( cur.getWorld().getUID().equals( base.getWorld().getUID() ) ) )
			{
				return i;
			}
		}
		
		return -1;
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
					while( ( line = buffer.readLine() ) != null )
					{
						String fields[] = line.split( "\\," );

						try
						{
							int x = Integer.parseInt( fields[ 0 ] );
							int y = Integer.parseInt( fields[ 1 ] );
							int z = Integer.parseInt( fields[ 2 ] );
							String wUID = null;
							ArrayList<World> worlds = plugin.getWorlds();
							boolean added = false;

							// Try to load the world, otherwise, this may be an old save file
							if( fields.length >= 4 )
							{
								wUID = fields[ 3 ];
							}
								
							for( int i = 0; i < worlds.size(); i++ )
							{
								// If the file has a world, find the world with the UID and add it
								// otherwise, try to find a world that has a repeller at this position and
								// add that instead
								if( ( (wUID != null ) &&
									worlds.get(i).getUID().toString().equals( wUID ) ) ||
									MobRepellent.isBaseOfRepeller( worlds.get( i ).getBlockAt( x, y, z ), plugin.getConfig().getBlockType() ) )
								{
									// TODO: this is hacky. The repeller loaded from the file must equal the block type
									//		 loaded from the config. However, block type is sometimes checked in the above
									//		 conditional because of backwards compatibility. Find a better way to do this.
									if( !this.contains( worlds.get(i).getBlockAt( x, y, z ) ) &&
										MobRepellent.isBaseOfRepeller( worlds.get( i ).getBlockAt( x, y, z ), plugin.getConfig().getBlockType() ) )
									{
										list.add( worlds.get(i).getBlockAt( x, y, z ) );
										added = true;
										break;
									}
								}
							}
							
							// If no block or world was found that matches the repeller list
							if( !added )
							{
								plugin.getLogger().info( "[MobRepellent] Error loading a repeller from the save file. Removing entry." );
							}
						}
						catch( Exception e )
						{
							plugin.getLogger().info( "[MobRepellent] Error loading a repeller from the save file. Removing entry." );
						}
					}
				}
				catch( IOException e )
				{
					e.printStackTrace();
				}
			}
			catch( FileNotFoundException e )
			{
				e.getStackTrace();
			}
		}
		else
		{
			this.plugin.getLogger().info( "[MobRepellent] No repeller file was found. Creating new file." );
		}
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
				Block r = this.list.get( i );
				
				fw.write( r.getX() + "," + r.getY() + "," + r.getZ() + "," + r.getWorld().getUID() + "\n" );
			}
			fw.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
}
