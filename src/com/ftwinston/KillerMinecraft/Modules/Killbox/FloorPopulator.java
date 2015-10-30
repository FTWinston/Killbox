package com.ftwinston.KillerMinecraft.Modules.Killbox;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

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
	}

	@SuppressWarnings("deprecation")
	private void populatePlains(World w, Random r, Chunk c)
	{
		int numGrass = r.nextInt(20) + 30;
		for (int i=0; i<numGrass; i++)
		{
			Block b = c.getBlock(r.nextInt(16), treeY, r.nextInt(16));
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
	}

	private void populateSwamp(World w, Random r, Chunk c)
	{
		w.generateTree(c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).getLocation(), TreeType.SWAMP);
		
		Block b = c.getBlock(2 + r.nextInt(12), treeY - 1, 2 + r.nextInt(12));
		b.setType(Material.STATIONARY_WATER);
		
		int steps = r.nextInt(20) + 3;
		for (int i=0; i<steps; i++)
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
			
			if (b.getType() == Material.GRASS)
				b.setType(Material.STATIONARY_WATER);
		}
	}

	private void populateDesert(World w, Random r, Chunk c)
	{
		if (r.nextInt(4) == 0)
			w.generateTree(c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).getLocation(), TreeType.ACACIA);
	}

	private void populateForest(World w, Random r, Chunk c)
	{
		int numTrees = r.nextInt(3) + 3;
		
		for (int i=0; i<numTrees; i++)
			w.generateTree(c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).getLocation(), TreeType.TREE);
	}
	
	
}
