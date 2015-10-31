package com.ftwinston.KillerMinecraft.Modules.Killbox;

import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

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
	
	BoxGenerator generator;
	
	@Override
	public void setupWorld(WorldConfig world, Runnable runWhenDone)
	{
		BoxSize size = boxSize.getValue();
		generator = new BoxGenerator(size.width, size.height, size.width);
		world.setWorldType(WorldType.FLAT);
		world.setGenerator(generator);
		createWorld(world, runWhenDone);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEvent(org.bukkit.event.world.WorldInitEvent event) throws EventException
	{
		event.getWorld().setSpawnLocation(generator.middleX, generator.minY + 2, generator.middleZ);
	}
}
