package com.ftwinston.KillerMinecraft.Modules.Killbox;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class CeilingPopulator extends BlockPopulator
{
	int maxY, minChunkX, maxChunkX, minChunkZ, maxChunkZ;
	public CeilingPopulator(int maxY, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ)
	{
		this.maxY = maxY;
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
		
	}
}
