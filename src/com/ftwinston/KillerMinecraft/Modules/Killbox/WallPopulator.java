package com.ftwinston.KillerMinecraft.Modules.Killbox;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class WallPopulator extends BlockPopulator
{
	boolean fixedX;
	int minY, maxY, chunkPos, blockPos, minChunk, maxChunk;
	BlockFace indentBlockFace;
	public WallPopulator(boolean fixedX, int minY, int maxY, int chunkPos, int blockPos, BlockFace indentBlockFace, int minChunk, int maxChunk)
	{
		this.minY = minY;
		this.maxY = maxY;
		this.fixedX = fixedX;
		this.chunkPos = chunkPos;
		this.blockPos = blockPos;		
		this.minChunk = minChunk;
		this.maxChunk = maxChunk;
		this.indentBlockFace = indentBlockFace;
		
		moveFaces = new BlockFace[] { BlockFace.UP, BlockFace.DOWN, fixedX ? BlockFace.NORTH : BlockFace.EAST, fixedX ? BlockFace.SOUTH : BlockFace.WEST, indentBlockFace };
	}

	BlockFace[] moveFaces;
	
	@Override
	public void populate(World w, Random r, Chunk c)
	{
		if (chunkPos != (fixedX ? c.getX() : c.getZ()))
			return;
		
		int otherAxisPos = fixedX ? c.getZ() : c.getX();
		if (otherAxisPos < minChunk || otherAxisPos > maxChunk)
			return;
		
		int yStep = 16;
		for (int y = minY; y < maxY - yStep; y += yStep)
			addPatch(r, c, y, y + yStep, Material.COAL_ORE, (byte)0, Material.STONE, r.nextInt(4) + 2);
		
		yStep = 32;
		for (int y = minY; y < maxY - yStep; y += yStep)
		{
			addPatch(r, c, y, y + yStep, Material.STONE, (byte)1, Material.STONE, r.nextInt(8) + 1);
			addPatch(r, c, y, y + yStep, Material.STONE, (byte)3, Material.STONE, r.nextInt(8) + 1);
			addPatch(r, c, y, y + yStep, Material.STONE, (byte)5, Material.STONE, r.nextInt(8) + 1);
			addPatch(r, c, y, y + yStep, Material.GRAVEL, (byte)0, Material.STONE, r.nextInt(8) + 1);
		}
		
		for (int y = minY + 16; y < maxY - yStep; y += yStep)
		{
			addPatch(r, c, y, y + yStep, Material.IRON_ORE, (byte)0, Material.STONE, r.nextInt(3) + 2);
			addPatch(r, c, y, y + yStep, Material.IRON_ORE, (byte)0, Material.STONE, r.nextInt(3) + 2);
			addPatch(r, c, y, y + yStep, Material.REDSTONE_ORE, (byte)0, Material.STONE, r.nextInt(6) + 2);
			
			addHiddenBlock(r, c, y, y + yStep, Material.STATIONARY_WATER, (byte)0, Material.STONE);
		}
		
		yStep = 16;
		for (int y = minY + ((maxY - minY) / 2); y < maxY - yStep; y += yStep)
		{
			addHiddenBlock(r, c, y, y + yStep, Material.STATIONARY_WATER, (byte)0, Material.STONE);
			addHiddenBlock(r, c, y, y + yStep, Material.STATIONARY_LAVA, (byte)0, Material.STONE);
			addHiddenBlock(r, c, y, y + yStep, Material.DIAMOND_ORE, (byte)0, Material.STONE);
			addHiddenBlock(r, c, y, y + yStep, Material.IRON_ORE, (byte)0, Material.STONE);
		}
		
		for (int y = maxY - ((maxY - minY) / 3); y < maxY - yStep; y += yStep)
		{
			addHiddenBlock(r, c, y, y + yStep, Material.STATIONARY_LAVA, (byte)0, Material.STONE);
			addHiddenBlock(r, c, y, y + yStep, Material.DIAMOND_ORE, (byte)0, Material.STONE);
		}
		
		yStep = 8;
		for (int y = maxY - ((maxY - minY) / 4); y < maxY - yStep; y += yStep)
		{
			addHiddenBlock(r, c, y, y + yStep, Material.STATIONARY_LAVA, (byte)0, Material.STONE);
			addHiddenBlock(r, c, y, y + yStep, Material.DIAMOND_ORE, (byte)0, Material.STONE);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void addHiddenBlock(Random r, Chunk c, int minY, int maxY, Material type, byte dataValue, Material typeToReplace)
	{
		Block b = c.getBlock(fixedX ? blockPos : r.nextInt(16), r.nextInt(maxY - minY) + minY, fixedX ? r.nextInt(16) : blockPos);
		b = b.getRelative(indentBlockFace);
		
		if (b.getType() == Material.BEDROCK)
			b = b.getRelative(moveFaces[r.nextInt(4)]);
		
		if (b.getType() != typeToReplace)
			return;
		
		b.setType(type);
		b.setData(dataValue);
	}

	@SuppressWarnings("deprecation")
	private void addPatch(Random r, Chunk c, int minY, int maxY, Material type, byte dataValue, Material typeToReplace, int num)
	{
		Block b = c.getBlock(fixedX ? blockPos : r.nextInt(16), r.nextInt(maxY - minY) + minY, fixedX ? r.nextInt(16) : blockPos);
		b.setType(type);
		b.setData(dataValue);
		
		for (int i=0; i<num; i++)
		{
			b = b.getRelative(moveFaces[r.nextInt(moveFaces.length)]);
			
			if (b.getType() == typeToReplace)
			{
				b.setType(type);
				b.setData(dataValue);
			}
		}
	}
}
