package com.ftwinston.KillerMinecraft.Modules.Killbox;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class WallPopulator extends BlockPopulator
{
	int minChunkX, maxChunkX, minChunkZ, maxChunkZ;
	public WallPopulator(int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ)
	{
		this.minChunkX = minChunkX;
		this.minChunkZ = minChunkZ;
		this.maxChunkX = maxChunkX;
		this.maxChunkZ = maxChunkZ;
	}

	@Override
	public void populate(World w, Random r, Chunk c)
	{
		if (c.getX() < minChunkX || c.getX() > maxChunkX || c.getZ() < minChunkZ || c.getZ() > maxChunkZ)
			return;
		
		/*
		// lava
		int num = 4, bx, by, bz;
		for ( int i=0; i<num; i++ )
		{
			int hSize = r.nextInt(3), vSize = r.nextInt(3);
			bx = getRandomCoord(r, hSize+1); by = getIntInRange(r, 2, 44); bz = getRandomCoord(r, hSize+1);
			for ( int x=bx-hSize; x<=bx+(hSize == 0 ? 1 : hSize); x++ )
				for ( int z=bz-hSize; z<=bz+(hSize == 0 ? 1 : hSize); z++ )
					for ( int y=by; y<=by+vSize; y++)
						c.getBlock(x, y, z).setType(Material.LAVA);
		}
		
		num = 5; // clumps of gravel, each 3x3x3
		for ( int i=0; i<num; i++ )
		{
			b = getRandomBlock(c, r, 40, 62, 2);
			for ( int j=0; j<3; j++ )
			{
				b.setType(Material.GRAVEL);
				b.getRelative(BlockFace.NORTH).setType(Material.GRAVEL);
				b.getRelative(BlockFace.EAST).setType(Material.GRAVEL);
				b.getRelative(BlockFace.SOUTH).setType(Material.GRAVEL);
				b.getRelative(BlockFace.WEST).setType(Material.GRAVEL);
				b.getRelative(BlockFace.NORTH_EAST).setType(Material.GRAVEL);
				b.getRelative(BlockFace.NORTH_WEST).setType(Material.GRAVEL);
				b.getRelative(BlockFace.SOUTH_EAST).setType(Material.GRAVEL);
				b.getRelative(BlockFace.SOUTH_WEST).setType(Material.GRAVEL);
				b = b.getRelative(BlockFace.DOWN);
			}
		}
		
		num = 4; // clumps of sand, each 3x3x1
		for ( int i=0; i<num; i++ )
		{
			b = getRandomBlock(c, r, 46, 62, 2);
			b.setType(Material.SAND);
			b.getRelative(BlockFace.NORTH).setType(Material.SAND);
			b.getRelative(BlockFace.EAST).setType(Material.SAND);
			b.getRelative(BlockFace.SOUTH).setType(Material.SAND);
			b.getRelative(BlockFace.WEST).setType(Material.SAND);
			b.getRelative(BlockFace.NORTH_EAST).setType(Material.SAND);
			b.getRelative(BlockFace.NORTH_WEST).setType(Material.SAND);
			b.getRelative(BlockFace.SOUTH_EAST).setType(Material.SAND);
			b.getRelative(BlockFace.SOUTH_WEST).setType(Material.SAND);
		}
		
		num = r.nextInt(4) + 4; // 4-7 veins of 2-4 diamonds
		for ( int i=0; i<num; i++ )
		{
			b = getRandomBlock(c, r, 1, 20, 1);
			b.setType(Material.DIAMOND_ORE);
			if ( r.nextInt(3) != 1 )	
				b.getRelative(r.nextBoolean() ? BlockFace.NORTH : BlockFace.SOUTH).setType(Material.DIAMOND_ORE);
			if ( r.nextInt(3) != 1 )						
				b.getRelative(r.nextBoolean() ? BlockFace.EAST : BlockFace.WEST).setType(Material.DIAMOND_ORE);
			if ( r.nextInt(3) != 1 )						
				b.getRelative(r.nextBoolean() ? BlockFace.UP : BlockFace.DOWN).setType(Material.DIAMOND_ORE);
		}
		
		num = r.nextInt(3) + 9; // 9-11 veins of up to 8 iron
		for ( int i=0; i<num; i++ )
		{
			b = getRandomBlock(c, r, 20, 44, 1);
			BlockFace f1 = r.nextBoolean() ? BlockFace.NORTH : BlockFace.SOUTH, f2 = r.nextBoolean() ? BlockFace.EAST : BlockFace.WEST;
			for ( int j=0; j<2; j++ )					
			{
				b.setType(Material.IRON_ORE);
				if ( r.nextInt(7) != 1 )	
					b.getRelative(f1).setType(Material.IRON_ORE);
				if ( r.nextInt(7) != 1 )						
					b.getRelative(f2).setType(Material.IRON_ORE);
				if ( r.nextInt(7) != 1 )						
					b.getRelative(f1).getRelative(f2).setType(Material.IRON_ORE);
				b = b.getRelative(r.nextBoolean() ? BlockFace.UP : BlockFace.DOWN);
			}
		}
		
		num = r.nextInt(4) + 5; // 5-8 veins of up to 8 coal
		for ( int i=0; i<num; i++ )
		{
			b = getRandomBlock(c, r, 32, 48, 1);
			BlockFace f1 = r.nextBoolean() ? BlockFace.NORTH : BlockFace.SOUTH, f2 = r.nextBoolean() ? BlockFace.EAST : BlockFace.WEST;
			for ( int j=0; j<2; j++ )					
			{
				b.setType(Material.COAL_ORE);
				if ( r.nextInt(7) != 1 )	
					b.getRelative(f1).setType(Material.COAL_ORE);
				if ( r.nextInt(7) != 1 )						
					b.getRelative(f2).setType(Material.COAL_ORE);
				if ( r.nextInt(7) != 1 )						
					b.getRelative(f1).getRelative(f2).setType(Material.COAL_ORE);
				b = b.getRelative(r.nextBoolean() ? BlockFace.UP : BlockFace.DOWN);
			}
		}
		
		num = r.nextInt(4) + 2; // 2-5 veins of up to 8 redstone
		for ( int i=0; i<num; i++ )
		{
			b = getRandomBlock(c, r, 32, 48, 1);
			BlockFace f1 = r.nextBoolean() ? BlockFace.NORTH : BlockFace.SOUTH, f2 = r.nextBoolean() ? BlockFace.EAST : BlockFace.WEST;
			for ( int j=0; j<2; j++ )					
			{
				b.setType(Material.REDSTONE_ORE);
				if ( r.nextInt(4) != 1 )	
					b.getRelative(f1).setType(Material.REDSTONE_ORE);
				if ( r.nextInt(4) != 1 )						
					b.getRelative(f2).setType(Material.REDSTONE_ORE);
				if ( r.nextInt(4) != 1 )						
					b.getRelative(f1).getRelative(f2).setType(Material.REDSTONE_ORE);
				b = b.getRelative(r.nextBoolean() ? BlockFace.UP : BlockFace.DOWN);
			}
		}
		*/
	}
}
