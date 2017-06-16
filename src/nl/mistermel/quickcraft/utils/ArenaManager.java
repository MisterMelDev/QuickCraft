package nl.mistermel.quickcraft.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import nl.mistermel.quickcraft.Arena;
import nl.mistermel.quickcraft.QuickCraft;

public class ArenaManager implements Runnable {
	
	private Map<String, Arena> arenas = new HashMap<String, Arena>();
	private Random r = new Random();
	
	private List<Material> items = Arrays.asList(Material.IRON_PICKAXE, Material.IRON_HELMET, Material.IRON_CHESTPLATE,
			Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.ARMOR_STAND, Material.CAKE, Material.ARROW);
	
	private FileConfiguration data;
	
	public ArenaManager() {
		data = QuickCraft.getConfigManager().getDataFile();
		refreshConfig();
	}
	
	@Override
	public void run() {
		for(Arena arena : arenas.values()) {
			arena.tick();
		}
	}
	
	public Material getRandomMaterial() {
		return items.get(r.nextInt(items.size()));
	}
	
	public void refreshConfig() {
		arenas.clear();
		for(Arena arena : arenas.values()) {
			for(Player p : arena.getPlayers()) {
				arena.leave(p);
			}
		}
		for(String key : data.getConfigurationSection("arenas").getKeys(false)) {
			Location lobbyLoc = new Location(Bukkit.getWorld(data.getString("arenas." + key + ".lobby.world")), data.getInt("arenas." + key + ".lobby.x"), data.getInt("arenas." + key + ".lobby.y"), data.getInt("arenas." + key + ".lobby.z"));
			Location spawnLoc = new Location(Bukkit.getWorld(data.getString("arenas." + key + ".spawn.world")), data.getInt("arenas." + key + ".spawn.x"), data.getInt("arenas." + key + ".spawn.y"), data.getInt("arenas." + key + ".spawn.z"));
			Arena arena = new Arena(lobbyLoc, spawnLoc, data.getBoolean("arenas." + key + ".enabled"));
			arenas.put(key, arena);
		}
	}
	
	public void setEnabled(String name, boolean enabled) {
		data.set("arenas." + name + ".enabled", enabled);
		save();
		arenas.get(name).setEnabled(enabled);
	}
	
	private void save() {
		try {
			data.save(QuickCraft.getConfigManager().getData());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean join(String name, Player p) {
		Arena arena = arenas.get(name);
		return arena.join(p);
	}
	
	public void createArena(String name) {
		Location emptyLoc = new Location(null, 0, 0, 0);
		arenas.put(name, new Arena(emptyLoc, emptyLoc, false));
	}
	
	public void setLobby(String name, Location loc) {
		arenas.get(name).setLobbyLocation(loc);
	}
	
	public void setSpawn(String name, Location loc) {
		arenas.get(name).setSpawnLocation(loc);
	}
	
	public boolean exists(String name) {
		return arenas.containsKey(name);
	}
	
	public boolean isCompleted(String name) {
		Location spawn = arenas.get(name).getSpawnLocation();
		Location lobby = arenas.get(name).getLobbyLocation();
		return spawn.getWorld() != null && lobby.getWorld() != null;
	}
	
	public boolean isEnabled(String name) {
		return arenas.get(name).isEnabled();
	}
	
}
