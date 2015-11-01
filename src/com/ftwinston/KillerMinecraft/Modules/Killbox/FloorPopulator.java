package com.ftwinston.KillerMinecraft.Modules.Killbox;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import com.ftwinston.KillerMinecraft.Helper;

public class FloorPopulator extends BlockPopulator
{
	int stoneY, grassY, treeY, minChunkX, maxChunkX, minChunkZ, maxChunkZ, midChunkX, midChunkZ;
	public FloorPopulator(int stoneY, int grassY, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ, int midChunkX, int midChunkZ)
	{
		this.stoneY = stoneY;
		this.grassY = grassY;
		this.treeY = grassY + 1;
		this.minChunkX = minChunkX;
		this.minChunkZ = minChunkZ;
		this.maxChunkX = maxChunkX;
		this.maxChunkZ = maxChunkZ;
		this.midChunkX = midChunkX;
		this.midChunkZ = midChunkZ;
	}

	@Override
	public void populate(World w, Random r, Chunk c)
	{
		if (c.getX() < minChunkX || c.getX() > maxChunkX || c.getZ() < minChunkZ || c.getZ() > maxChunkZ)
			return;
		
		if (c.getX() < midChunkX)
			if (c.getZ() < midChunkZ)
				populatePlains(w,r,c);
			else
				populateSwamp(w,r,c);
		else
			if (c.getZ() < midChunkZ)
				populateDesert(w,r,c);
			else
				populateForest(w,r,c);
		
		addPatch(r, c, stoneY, Material.COAL_ORE, (byte)0, Material.STONE, r.nextInt(4) + 2);
		addPatch(r, c, stoneY, Material.STONE, (byte)1, Material.STONE, r.nextInt(16));
		addPatch(r, c, stoneY, Material.STONE, (byte)3, Material.STONE, r.nextInt(16));
		addPatch(r, c, stoneY, Material.STONE, (byte)5, Material.STONE, r.nextInt(16));
	}
	
	@SuppressWarnings("deprecation")
	private void populatePlains(World w, Random r, Chunk c)
	{
		int numGrass = r.nextInt(20) + 30;
		for (int i=0; i<numGrass; i++)
		{
			Block b = c.getBlock(r.nextInt(16), treeY, r.nextInt(16));
			if (b.getType() != Material.AIR)
				continue;
			
			switch (r.nextInt(4))
			{
			case 0:
				b.setType(r.nextInt(2) == 0 ? Material.YELLOW_FLOWER : Material.RED_ROSE); break;
			default:
				b.setType(Material.LONG_GRASS);
				b.setData((byte)1);
				break;
			}
		}
		
		// create a SMALL village
		if (c.getX() == midChunkX - 1 && c.getZ() == midChunkZ - 1)
		{
			int villageX = 16, villageZ = 16, radius = 15;
			Helper.generateVillage(c.getBlock(villageX, grassY, villageZ).getLocation(), r, radius);
		}
	}

	private void populateSwamp(World w, Random r, Chunk c)
	{
		w.generateTree(c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).getLocation(), TreeType.SWAMP);
		
		addPatch(r, c, grassY, Material.STATIONARY_WATER, (byte)0, Material.GRASS, 2 + r.nextInt(12));
		addPatch(r, c, stoneY, Material.CLAY, (byte)0, Material.STONE, 8 + r.nextInt(12));
	}

	private void populateDesert(World w, Random r, Chunk c)
	{
		if (r.nextInt(4) == 0)
		{
			Block b = c.getBlock(r.nextInt(16), treeY, r.nextInt(16)); 
			if (b.getType() == Material.AIR)
				b.setType(Material.LONG_GRASS);
		}
		
		if (r.nextInt(4) == 0)
			w.generateTree(c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).getLocation(), TreeType.ACACIA);
		
		addPatch(r, c, grassY, Material.SAND, (byte)0, Material.GRASS, 12 + r.nextInt(12));
	}

	private void populateForest(World w, Random r, Chunk c)
	{
		int numTrees = r.nextInt(3) + 3;
		
		for (int i=0; i<numTrees; i++)
			w.generateTree(c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).getLocation(), TreeType.TREE);
		
		Block b = c.getBlock(r.nextInt(16), treeY, r.nextInt(16)); 
		if (b.getType() == Material.AIR)
			b.setType(Material.BROWN_MUSHROOM);
		
		b = c.getBlock(r.nextInt(16), treeY, r.nextInt(16)); 
		if (b.getType() == Material.AIR)
			b.setType(Material.RED_MUSHROOM);
		
	}

	@SuppressWarnings("deprecation")
	private void addPatch(Random r, Chunk c, int y, Material type, byte dataValue, Material typeToReplace, int num)
	{
		Block b = c.getBlock(r.nextInt(16), y, r.nextInt(16));
		b.setType(type);
		b.setData(dataValue);
		
		for (int i=0; i<num; i++)
		{
			switch (r.nextInt(4))
			{
			case 0:
				b = b.getRelative(BlockFace.NORTH); break;
			case 1:
				b = b.getRelative(BlockFace.SOUTH); break;
			case 2:
				b = b.getRelative(BlockFace.EAST); break;
			case 3:
				b = b.getRelative(BlockFace.WEST); break;
			}
			
			if (b.getType() == typeToReplace)
			{
				b.setType(type);
				b.setData(dataValue);
			}
		}
	}
}
