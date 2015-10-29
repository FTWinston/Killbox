package com.ftwinston.KillerMinecraft.Modules.Killbox;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class FloorPopulator extends BlockPopulator
{
	public FloorPopulator(int stoneY, int grassY)
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void populate(World w, Random r, Chunk c)
	{
		if (r.nextInt(4) == 0)
			w.generateTree(c.getBlock(0 + r.nextInt(8), 3, 0 + r.nextInt(8)).getLocation(), TreeType.DARK_OAK);
	}
}
