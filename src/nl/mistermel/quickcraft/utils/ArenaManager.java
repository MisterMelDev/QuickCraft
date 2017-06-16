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
		
		data.set("arenas." + name + ".spawn.world", "");
		data.set("arenas." + name + ".spawn.x", 0);
		data.set("arenas." + name + ".spawn.y", 0);
		data.set("arenas." + name + ".spawn.z", 0);
	}
	
	public boolean isEnabled(String name) {
		return data.getBoolean("arenas." + name + ".enabled");
	}
	
	public GameStatus getStatus(String name) {
		return GameStatus.valueOf(data.getString("arenas." + name + ".status"));
	}
	
	public void setStatus(String name, GameStatus status) {
		data.set("arenas." + name + ".status", status.toString());
	}	
	
	public void setEnabled(String name, boolean toggled) {
		data.set("arenas." + name + ".enabled", toggled);
	}
	
	public boolean isCompleted(String name) {
		return !data.getString("arenas." + name +".lobby.world").equals("") && !data.getString("arenas." + name +".spawn.world").equals("");
	}
	
	public void setLobby(String name, Location loc) {
		data.set("arenas." + name + ".lobby.world", loc.getWorld().getName());
		data.set("arenas." + name + ".lobby.x", loc.getBlockX());
		data.set("arenas." + name + ".lobby.y",	loc.getBlockY());
		data.set("arenas." + name + ".lobby.z", loc.getBlockZ());
	}
	
	public void setSpawn(String name, Location loc) {
		data.set("arenas." + name + ".spawn.world", loc.getWorld().getName());
		data.set("arenas." + name + ".spawn.x", loc.getBlockX());
		data.set("arenas." + name + ".spawn.y",	loc.getBlockY());
		data.set("arenas." + name + ".spawn.z", loc.getBlockZ());
	}
	
	public boolean exists(String name) {
		return data.contains("arenas." + name);
	}
	
}
