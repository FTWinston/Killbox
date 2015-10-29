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

	int minBorderCX = -1, minBorderCZ = -1, maxBorderCX, maxBorderCY, maxBorderCZ, maxY;
	int middleChunkX1, middleChunkX2, middleChunkZ1, middleChunkZ2, middleX, middleZ;
	
	int maxX, maxZ;
	public BoxGenerator(int sizeX, int sizeY, int sizeZ)
	{
		maxX = sizeX; maxZ = sizeZ;
		maxBorderCX = sizeX >> 4;
		maxBorderCY = (sizeY >> 4) + 1;
		maxBorderCZ = sizeZ >> 4;
		maxY = sizeY;
		
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
		
		createFloor(chunk);

		createCeiling(cx, cz, chunk);
		
		return chunk; 
	}

	private void createCeiling(int cx, int cz, byte[][] chunk)
	{
		// fill in ceiling, with an invisible-barrier hole in the middle, to let light in
		boolean middleChunk = (cx == middleChunkX1 || cx == middleChunkX2) && (cz == middleChunkZ1 || cz == middleChunkZ2);
		
		for (int x=0; x<16; x++)
			for (int z=0; z<16; z++)
			{
				if (middleChunk)
					setMaterialAt(chunk, x, maxY + 1, z, invisible);
				else
				{
					setMaterialAt(chunk, x, maxY - 1, z, stone);
					setMaterialAt(chunk, x, maxY, z, stone);				
					setMaterialAt(chunk, x, maxY + 1, z, bedrock);
				}

				setMaterialAt(chunk, x, maxY + 2, z, invisible);
			}
	}

	private void createFloor(byte[][] chunk)
	{
		// fill in floor
		for (int x=0; x<16; x++)
			for (int z=0; z<16; z++)
			{
				setMaterialAt(chunk, x, 0, z, bedrock);
				setMaterialAt(chunk, x, 1, z, stone);
				setMaterialAt(chunk, x, 2, z, grass);
			}
	}

	private boolean createWalls(int cx, int cz, byte[][] chunk)
	{
		// set up walls - a bedrock border, and a single thickness of stone
		boolean border = false;
		if (cx == minBorderCX)
		{
			border = true;
			int x = 15; // bedrock at x=15
			for (int z=0; z<16; z++)
				for (int y=0; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, bedrock);
		}
		else if (cx == maxBorderCX)
		{
			border = true;
			int x = 0; // bedrock at x=0
			for (int z=0; z<16; z++)
				for (int y=0; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, bedrock);
		}
		else if (cx == minBorderCX + 1)
		{
			int x = 0; // stone at x=0
			for (int z=0; z<16; z++)
			{
				for (int y=1; y<17; y++)
					setMaterialAt(chunk, x, y, z, dirt);
				for (int y=17; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, stone);
			}
		}
		else if (cx == maxBorderCX - 1)
		{
			int x = 15; // stone at x=15
			for (int z=0; z<16; z++)
			{
				for (int y=1; y<17; y++)
					setMaterialAt(chunk, x, y, z, dirt);
				for (int y=17; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, stone);
			}
		}
		
		if (cz == minBorderCZ)
		{
			border = true;
			int z = 15; // bedrock at z=15
			for (int x=0; x<16; x++)
				for (int y=0; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, bedrock);
		}
		else if (cz == maxBorderCZ)
		{
			border = true;
			int z = 0; // bedrock at z=0
			for (int x=0; x<16; x++)
				for (int y=0; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, bedrock);
		}
		else if (!border && cz == minBorderCZ + 1)
		{
			int z = 0; // stone at z=0
			for (int x=0; x<16; x++)
			{
				for (int y=1; y<17; y++)
					setMaterialAt(chunk, x, y, z, dirt);
				for (int y=17; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, stone);
			}
		}
		else if (!border && cz == maxBorderCZ - 1)
		{
			int z = 15; // stone at z=15
			for (int x=0; x<16; x++)
			{
				for (int y=1; y<17; y++)
					setMaterialAt(chunk, x, y, z, dirt);
				for (int y=17; y<=maxY; y++)
					setMaterialAt(chunk, x, y, z, stone);
			}
		}
		return border;
	}

	private void setBiome(BiomeGrid grid, int cx, int cz)
	{
		// set up biomes - world divides into 4 quarters
		Biome biome;
		if (cx <= middleChunkX1)
		{
			if (cz <= middleChunkZ1)
				biome = Biome.PLAINS;
			else
				biome = Biome.SWAMPLAND;
		}
		else
		{
			if (cz <= middleChunkZ1)
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
		list.add(new WallPopulator(0, maxX, 0, maxZ));
		list.add(new FloorPopulator(1, 2));
		list.add(new CeilingPopulator(maxY));
		return list;
	}
}