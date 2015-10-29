package com.ftwinston.KillerMinecraft.Modules.Killbox;

import org.bukkit.Material;

import com.ftwinston.KillerMinecraft.Option;
import com.ftwinston.KillerMinecraft.WorldConfig;
import com.ftwinston.KillerMinecraft.WorldGenerator;
import com.ftwinston.KillerMinecraft.Configuration.ChoiceOption;

public class Killbox extends WorldGenerator
{
	enum BoxSize
	{
		Tiny(64, 64, Material.STONE_PLATE),
		Small(96, 96, Material.STONE_SLAB2),
		Medium(128, 128, Material.BRICK_STAIRS),
		Large(160, 160, Material.BRICK),
		Huge(192, 192, Material.QUARTZ_BLOCK);
		
		int width, height;
		Material material;
		BoxSize(int width, int height, Material mat)
		{
			this.width = width;
			this.height = height;
			this.material = mat;
		}
		
		public String getDescription()
		{
			return "World is " + width + " blocks long and " + height + " blocks high";
		}
	}
	
	ChoiceOption<BoxSize> boxSize;
	
	@Override
	public Option[] setupOptions()
	{
		boxSize = new ChoiceOption<BoxSize>("World Size");
		
		for (BoxSize size : BoxSize.values())
			boxSize.addChoice(size.name(), size, size.material, size.getDescription());

		return new Option[] { boxSize };
	}
	
	@Override
	public void setupWorld(WorldConfig world, Runnable runWhenDone)
	{
		BoxSize size = boxSize.getValue();
		world.setGenerator(new BoxGenerator(size.width, size.height, size.width));
		//world.setExtraPopulators(extraPopulators); TODO: add block populators
		createWorld(world, runWhenDone);
	}
}