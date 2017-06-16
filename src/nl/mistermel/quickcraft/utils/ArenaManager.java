package nl.mistermel.quickcraft.utils;

import org.bukkit.configuration.file.FileConfiguration;

import nl.mistermel.quickcraft.QuickCraft;

public class ArenaManager {
	
	private FileConfiguration data;
	
	public ArenaManager() {
		data = QuickCraft.getConfigManager().getDataFile();
	}
	
	public void createArena(String name) {
		data.set("arenas." + name + ".enabled", false);
	}
	
	public boolean exists(String name) {
		return data.contains("arenas." + name);
	}
	
}
