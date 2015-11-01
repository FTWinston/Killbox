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
		Tiny(64, 64, Material.STICK),
		Small(96, 96, Material.WOOD_PICKAXE),
		Medium(128, 128, Material.STONE_PICKAXE),
		Large(160, 160, Material.IRON_PICKAXE),
		Huge(192, 192, Material.DIAMOND_PICKAXE);
		
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
			return width + " blocks long, " + height + " blocks high";
		}
	}
	
	ChoiceOption<BoxSize> boxSize;
	
	@Override
	public Option[] setupOptions()
	{
		boxSize = new ChoiceOption<BoxSize>("World Size", BoxSize.Small);
		
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
		event.getWorld().setSpawnLocation(generator.middleX, generator.minY + 4, generator.middleZ);
	}
}
