package com.ftwinston.KillerMinecraft.Modules.Killbox;

import org.bukkit.Material;
import org.bukkit.World.Environment;

import com.ftwinston.KillerMinecraft.WorldGenerator;
import com.ftwinston.KillerMinecraft.WorldGeneratorPlugin;

public class Plugin extends WorldGeneratorPlugin
{
	@Override
	public Environment getWorldType() { return Environment.NORMAL; }
	
	@Override
	public String[] getDescriptionText() { return new String[] { "Play in a confined space,", "with nowhere to hide" }; }
	
	@Override
	public Material getMenuIcon() { return Material.CHEST; }
	
	@Override
	public WorldGenerator createInstance()
	{
		return new Killbox();
	}
}