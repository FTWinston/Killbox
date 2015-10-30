package com.ftwinston.KillerMinecraft.Modules.Killbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class BoxGenerator extends org.bukkit.generator.ChunkGenerator
{
	static final int solidChance1x1 = 16, solidChance2x2 = 11, solidChance3x3 = 30;

	@SuppressWarnings("deprecation")
	final byte air = (byte)Material.AIR.getId();
	@SuppressWarnings("deprecation")
	final byte dirt = (byte)Material.DIRT.getId();
	@SuppressWarnings("deprecation")
	final byte grass = (byte)Material.GRASS.getId();
	@SuppressWarnings("deprecation")
	final byte stone = (byte)Material.STONE.getId();
	@SuppressWarnings("deprecation")
	final byte bedrock = (byte)Material.BEDROCK.getId();
	@SuppressWarnings("deprecation")
	final byte invisible = (byte)Material.BARRIER.getId();
	@SuppressWarnings("deprecation")
	final byte lava = (byte)Material.STATIONARY_LAVA.getId();
	@SuppressWarnings("deprecation")
	final byte water = (byte)Material.STATIONARY_WATER.getId();

	int minBorderCX = -1, minBorderCZ = -1, maxBorderCX, maxBorderCY, maxBorderCZ, minY = 48, maxY;
	int middleChunkX1, middleChunkX2, middleChunkZ1, middleChunkZ2, middleX, middleZ;
	
	int maxX, maxZ;
	public BoxGenerator(int sizeX, int sizeY, int sizeZ)
	{
		maxX = sizeX;
		maxY = sizeY + minY + 1;
		maxZ = sizeZ;
		maxBorderCX = sizeX >> 4;
		maxBorderCY = maxY >> 4;
		maxBorderCZ = sizeZ >> 4;
		
		middleChunkX1 = (maxBorderCX - 1) / 2;
		middleChunkX2 = (maxBorderCX) / 2;
		
		middleChunkZ1 = (maxBorderCZ - 1) / 2;
		middleChunkZ2 = (maxBorderCZ) / 2;
		
		middleX = middleChunkX2 * 16;
		middleZ = middleChunkZ2 * 16;
	}
	
	@Override
	public boolean canSpawn(World world, int x, int z)
	{
		return x > middleX - 6 && x < middleX + 6 && z > middleZ + 6 && z < middleZ + 6;
	}
	
	public byte[][] generateBlockSections(World world, Random random, int cx, int cz, BiomeGrid biomes)
	{
		if (cx < minBorderCX || cz < minBorderCZ || cx > maxBorderCX || cz > maxBorderCZ)
			return new byte[1][];

		byte[][] chunk = new byte[maxBorderCY + 1][];
		for (int layer = 0; layer <= maxBorderCY; layer++)
			chunk[layer] = new byte[4096];
		
		setBiome(biomes, cx, cz);
		
		boolean isBorderChunk = createWalls(cx, cz, chunk);
		
		if (isBorderChunk)
		{// no floor/ceiling should go in this chunk
			return chunk;
		}
		
		createFloor(cx, cz, chunk);

		createCeiling(cx, cz, chunk);
		
		return chunk; 
	}

	private void createCeiling(int cx, int cz, byte[][] chunk)
	{
		// fill in ceiling, with an invisible-barrier hole in the middle, to let light in
		for (int x=0; x<16; x++)
			for (int z=0; z<16; z++)
			{
				/*
				if (edge)
				{
					setMaterialAt(chunk, x, maxY - 1, z, stone);
					setMaterialAt(chunk, x, maxY, z, stone);				
					setMaterialAt(chunk, x, maxY + 1, z, bedrock);
				}
				*/
				setMaterialAt(chunk, x, maxY + 1, z, invisible);
				
				setMaterialAt(chunk, x, maxY + 2, z, invisible);
			}
	}

	private void createFloor(int cx, int cz, byte[][] chunk)
	{	
		boolean middleChunk = (cx == middleChunkX1 || cx == middleChunkX2) && (cz == middleChunkZ1 || cz == middleChunkZ2);
		
		// fill in floor
		for (int x=0; x<16; x++)
			for (int z=0; z<16; z++)
			{
				if (middleChunk)
				{// a buried lava pool
					setMaterialAt(chunk, x, 0, z, bedrock);

					for (int y=1; y<minY; y++)
						setMaterialAt(chunk, x, y, z, lava);
				}
				else
					for (int y=0; y<minY; y++)
						setMaterialAt(chunk, x, y, z, bedrock);
				
				setMaterialAt(chunk, x, minY, z, stone);
				setMaterialAt(chunk, x, minY + 1, z, grass);
			}
		
		// add buried water near the walls
		if (cx == minBorderCX + 1)
		{
			int minZ = cz == minBorderCZ + 1 ? 4 : 0;
			int maxZ = cz == maxBorderCZ - 1 ? 11 : 15;
			for (int z=minZ; z<=maxZ; z++)
				for (int x=1; x<=3; x++)
					for (int y = minY - 2; y < minY; y++)
						setMaterialAt(chunk, x, y, z, water);
		}
		else if (cx == maxBorderCX - 1)
		{
			int minZ = cz == minBorderCZ + 1 ? 4 : 0;
			int maxZ = cz == maxBorderCZ - 1 ? 11 : 15;
			for (int z=minZ; z<=maxZ; z++)
				for (int x=12; x<=14; x++)
					for (int y = minY - 2; y < minY; y++)
						setMaterialAt(chunk, x, y, z, water);
		}
		if (cz == minBorderCZ + 1)
		{
			int minX = cx == minBorderCX + 1 ? 4 : 0;
			int maxX = cx == maxBorderCX - 1 ? 11 : 15;
			for (int x=minX; x<=maxX; x++)
				for (int z=1; z<=3; z++)
					for (int y = minY - 2; y < minY; y++)
						setMaterialAt(chunk, x, y, z, water);
		}
		else if (cz == maxBorderCZ - 1)
		{
			int minX = cx == minBorderCX + 1 ? 4 : 0;
			int maxX = cx == maxBorderCX - 1 ? 11 : 15;
			for (int x=minX; x<=maxX; x++)
				for (int z=12; z<=14; z++)
					for (int y = minY - 2; y < minY; y++)
						setMaterialAt(chunk, x, y, z, water);
		}
	}

	private boolean createWalls(int cx, int cz, byte[][] chunk)
	{
		// set up walls - a bedrock border, and a single thickness of stone
		boolean border = true;
		
		if (cx == minBorderCX)
		{
			int x = 15, minZ, maxZ; // bedrock at x=15
			
			if (cz == minBorderCZ)
			{
				minZ = maxZ = 15;
			}
			else if (cz == maxBorderCZ)
			{
				minZ = maxZ = 0;
			}
			else
			{
				minZ = 0; maxZ = 15;	
			}
			
			for (int y=0; y<=maxY + 1; y++)
				for (int z=minZ; z<=maxZ; z++)
					setMaterialAt(chunk, x, y, z, bedrock);
		}
		else if (cx == maxBorderCX)
		{
			int x = 0, minZ, maxZ; // bedrock at x=0
			
			if (cz == minBorderCZ)
			{
				minZ = maxZ = 15;
			}
			else if (cz == maxBorderCZ)
			{
				minZ = maxZ = 0;
			}
			else
			{
				minZ = 0; maxZ = 15;	
			}
			
			for (int y=0; y<=maxY + 1; y++)
				for (int z=minZ; z<=maxZ; z++)
					setMaterialAt(chunk, x, y, z, bedrock);
		}
		else if (cz == minBorderCZ)
		{
			int z = 15; // bedrock at z=15
			for (int x=0; x<16; x++)
				for (int y=0; y<=maxY + 1; y++)
					setMaterialAt(chunk, x, y, z, bedrock);
		}
		else if (cz == maxBorderCZ)
		{
			int z = 0; // bedrock at z=0
			for (int x=0; x<16; x++)
				for (int y=0; y<=maxY + 1; y++)
					setMaterialAt(chunk, x, y, z, bedrock);
		}
		else
			border = false;
		
		if (border)
			return true;

		int stoneStartY = minY + 16;
		if (cx == minBorderCX + 1)
		{
			int x = 0; // stone at x=0
			for (int z=0; z<16; z++)
			{
				for (int y=minY; y<stoneStartY; y++)
					setMaterialAt(chunk, x, y, z, dirt);
				
				for (int y=stoneStartY; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, stone);
				
				setMaterialAt(chunk, x, maxY + 1, z, bedrock);
			}
		}
		else if (cx == maxBorderCX - 1)
		{
			int x = 15; // stone at x=15
			for (int z=0; z<16; z++)
			{
				for (int y=minY; y<stoneStartY; y++)
					setMaterialAt(chunk, x, y, z, dirt);

				for (int y=stoneStartY; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, stone);
				
				setMaterialAt(chunk, x, maxY + 1, z, bedrock);
			}
		}
		if (cz == minBorderCZ + 1)
		{
			int z = 0; // stone at z=0
			for (int x=0; x<16; x++)
			{
				for (int y=minY; y<stoneStartY; y++)
					setMaterialAt(chunk, x, y, z, dirt);
				
				for (int y=stoneStartY; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, stone);

				setMaterialAt(chunk, x, maxY + 1, z, bedrock);
			}
		}
		else if (cz == maxBorderCZ - 1)
		{
			int z = 15; // stone at z=15
			for (int x=0; x<16; x++)
			{
				for (int y=minY; y<stoneStartY; y++)
					setMaterialAt(chunk, x, y, z, dirt);
				
				for (int y=stoneStartY; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, stone);
				
				setMaterialAt(chunk, x, maxY + 1, z, bedrock);
			}
		}
		return false;
	}

	private void setBiome(BiomeGrid grid, int cx, int cz)
	{
		// set up biomes - world divides into 4 quarters
		Biome biome;
		if (cx < middleChunkX2)
		{
			if (cz < middleChunkZ2)
				biome = Biome.PLAINS;
			else
				biome = Biome.SWAMPLAND;
		}
		else
		{
			if (cz < middleChunkZ2)
				biome = Biome.DESERT;
			else
				biome = Biome.FOREST;
		}
		
		for (int x=0; x<16; x++)
			for (int z=0; z<16; z++)
				grid.setBiome(x, z, biome);
	}
	
	private void setMaterialAt(byte[][] chunk, int x, int y, int z, byte material)
	{
		int sec_id = (y >> 4);
		int yy = y & 0xF;
		//if (chunk[sec_id] == null)
			//chunk[sec_id] = new byte[4096];

		chunk[sec_id][(yy << 8) | (z << 4) | x] = material;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world)
	{
		List<BlockPopulator> list = new ArrayList<BlockPopulator>();
		list.add(new WallPopulator(0, maxBorderCX - 1, 0, maxBorderCZ - 1));
		list.add(new FloorPopulator(minY, minY + 1, 0, maxBorderCX - 1, 0, maxBorderCZ - 1, middleChunkX2, middleChunkZ2));
		list.add(new CeilingPopulator(maxY, 0, maxBorderCX - 1, 0, maxBorderCZ - 1));
		return list;
	}
}