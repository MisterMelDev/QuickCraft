package nl.mistermel.quickcraft.utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import nl.mistermel.quickcraft.QuickCraft;

public class ArenaManager {
	
	private FileConfiguration data;
	
	public ArenaManager() {
		data = QuickCraft.getConfigManager().getDataFile();
	}
	
	public void createArena(String name) {
		data.set("arenas." + name + ".enabled", false);
		data.set("arenas." + name + ".lobby.world", "");
		data.set("arenas." + name + ".lobby.x", 0);
		data.set("arenas." + name + ".lobby.y", 0);
		data.set("arenas." + name + ".lobby.z", 0);
	}
	
	public void setLobby(String name, Location loc) {
		data.set("arenas." + name + ".lobby.world", loc.getWorld().getName());
		data.set("arenas." + name + ".lobby.x", loc.getBlockX());
		data.set("arenas." + name + ".lobby.y",	loc.getBlockY());
		data.set("arenas." + name + ".lobby.z", loc.getBlockZ());
	}
	
	public void addSpawn(String name, Location loc) {
		
	}
	
	public boolean exists(String name) {
		return data.contains("arenas." + name);
	}
	
}
