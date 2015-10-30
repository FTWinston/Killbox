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
		
		addPatchToStone(r, c, Material.COAL_ORE, (byte)0, r.nextInt(4) + 2);
		addPatchToStone(r, c, Material.STONE, (byte)1, r.nextInt(16));
		addPatchToStone(r, c, Material.STONE, (byte)3, r.nextInt(16));
		addPatchToStone(r, c, Material.STONE, (byte)5, r.nextInt(16));
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
		
		// create a SMALL village
		if (c.getX() == midChunkX - 1 && c.getZ() == midChunkZ - 1)
		{
			Block max = c.getBlock(0, 0, 0);
			int minX = Math.max(4, max.getX() - 48);
			int minZ = Math.max(4, max.getZ() - 48);
			Helper.generateVillage(w, minX, max.getX(), minZ, max.getZ());  
		}
	}

	private void populateSwamp(World w, Random r, Chunk c)
	{
		w.generateTree(c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).getLocation(), TreeType.SWAMP);
		
		Block b = c.getBlock(2 + r.nextInt(12), grassY, 2 + r.nextInt(12));
		b.setType(Material.STATIONARY_WATER);
		surroundWithSand(b);
		
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
			{
				b.setType(Material.STATIONARY_WATER);
				surroundWithSand(b);
			}
		}
	}

	private void surroundWithSand(Block b)
	{
		b.getRelative(BlockFace.DOWN).setType(Material.SAND);
		
		Block other = b.getRelative(BlockFace.NORTH);
		if (other.getType() == Material.GRASS)
			other.setType(Material.SAND);
		
		other = b.getRelative(BlockFace.SOUTH);
		if (other.getType() == Material.GRASS)
			other.setType(Material.SAND);
		
		other = b.getRelative(BlockFace.EAST);
		if (other.getType() == Material.GRASS)
			other.setType(Material.SAND);
		
		other = b.getRelative(BlockFace.WEST);
		if (other.getType() == Material.GRASS)
			other.setType(Material.SAND);
	}

	private void populateDesert(World w, Random r, Chunk c)
	{
		if (r.nextInt(4) == 0)
			c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).setType(Material.LONG_GRASS);
		
		if (r.nextInt(4) == 0)
			w.generateTree(c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).getLocation(), TreeType.ACACIA);
	}

	private void populateForest(World w, Random r, Chunk c)
	{
		int numTrees = r.nextInt(3) + 3;
		
		c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).setType(Material.BROWN_MUSHROOM);
		c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).setType(Material.RED_MUSHROOM);
		
		for (int i=0; i<numTrees; i++)
			w.generateTree(c.getBlock(r.nextInt(16), treeY, r.nextInt(16)).getLocation(), TreeType.TREE);
	}

	@SuppressWarnings("deprecation")
	private void addPatchToStone(Random r, Chunk c, Material type, byte dataValue, int num)
	{
		Block b = c.getBlock(r.nextInt(16), stoneY, r.nextInt(16));
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
			
			if (b.getType() == Material.STONE)
			{
				b.setType(type);
				b.setData(dataValue);
			}
		}
	}
}
