package com.nucleargrass.mobrepel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.block.Block;

public class MobRepellentList
{
	private File file;
	private ArrayList<int[]> list;
	private MobRepellent plugin;

	public MobRepellentList( String workingDir, MobRepellent plugin )
	{
		this.plugin = plugin;
		this.file = new File( workingDir + "repellers.list" );
		this.list = new ArrayList();
		load();
	}
	
	public boolean isRepelled( double x, double y, double z )
	{
		return isRepelled( x, y, z, 100 );
	}
	
	public boolean isRepelled( double x, double y, double z, int radius )
	{
		for( int i = 0; i < list.size(); i++ )
		{
			if( ( ( list.get(i)[0] - radius ) < x ) && ( ( list.get(i)[0] + radius ) > x ) &&
				( ( list.get(i)[1] - radius ) < y ) && ( ( list.get(i)[1] + radius ) > y ) &&
				( ( list.get(i)[2] - radius ) < z ) && ( ( list.get(i)[2] + radius ) > z ) )
				return true;
		}
		
		return false;
	}

	public void add( int[] repeller )
	{
		this.list.add( repeller );
		save();
	}
	
	public void add( Block block )
	{
		int coords[] = { block.getX(), block.getY(), block.getZ() };
		this.add( coords );
	}
	
	public boolean remove( Block block )
	{
		int coords[] = { block.getX(), block.getY(), block.getZ() };
		return this.remove( coords );
	}

	public boolean remove( int[] repeller )
	{
		boolean removed = false;
		
		for( int i = 0; i < list.size(); i++ )
		{
			int current[] = this.list.get( i );
			
			if( ( current[0] == repeller[0] ) &&
				( current[1] == repeller[1] ) &&
				( current[2] == repeller[2] ) )
			{
				this.list.remove( i );
				removed = true;
				this.plugin.getLogger().info(
						"[MobRepellent] A Mob Repellent has been destroyed." );
			}
		}
		save();
		return removed;
	}
	
	public boolean contains( Block block )
	{
		int coords[] = { block.getX(), block.getY(), block.getZ() };
		//this.plugin.getLogger().info( "Block: (" + coords[0] + coords[1] + coords[2] + ")" );
		for( int i = 0; i < list.size(); i++ )
		{
			//this.plugin.getLogger().info( "List:  (" + list.get(i)[0] + list.get(i)[1] + list.get(i)[2] + ")" );
			if( ( coords[0] == list.get(i)[0] ) &&
				( coords[1] == list.get(i)[1] ) &&
				( coords[2] == list.get(i)[2] ) )
				return true;
		}
		
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

				int i = 0;
				int[] linePos = new int[3];
				try
				{
					String line;
					while( ( line = buffer.readLine() ) != null )
					{
						String fields[] = line.split( "\\," );

						linePos[ 0 ] = Integer.parseInt( fields[ 0 ] );
						linePos[ 1 ] = Integer.parseInt( fields[ 1 ] );
						linePos[ 2 ] = Integer.parseInt( fields[ 2 ] );

						this.plugin.getLogger().info(
								"[MobRepellent] A Mob Repellent has been loaded at " + linePos[0] + "," + linePos[1] + "," + linePos[2]  );
						this.list.add( i, linePos );
						i++;
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
			this.plugin.getLogger().info( "[MobRepellent] No repellers were found." );
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
				int[] tabLine = (int[]) this.list.get( i );
				fw.write( tabLine[ 0 ] + "," + tabLine[ 1 ] + "," + tabLine[ 2 ] + "\n" );
			}
			fw.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
}
