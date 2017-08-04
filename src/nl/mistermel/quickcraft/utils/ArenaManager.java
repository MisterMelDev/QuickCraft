package nl.mistermel.quickcraft.utils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import net.md_5.bungee.api.ChatColor;
import nl.mistermel.quickcraft.Arena;
import nl.mistermel.quickcraft.QuickCraft;

public class ArenaManager implements Runnable, Listener {
	
	private Map<String, Arena> arenas = new HashMap<String, Arena>();
	
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
	
	public void setSign(Location loc, String name) {
		data.set("arenas." + name + ".sign.world", loc.getWorld().getName());
		data.set("arenas." + name + ".sign.x", loc.getBlockX());
		data.set("arenas." + name + ".sign.y", loc.getBlockY());
		data.set("arenas." + name + ".sign.z", loc.getBlockZ());
		QuickCraft.getConfigManager().save();
	}
	
	public Location getSign(String name) {
		return new Location(Bukkit.getWorld(data.getString("arenas." + name + ".sign.world")), data.getInt("arenas." + name + ".sign.x"), data.getInt("arenas." + name + ".sign.y"), data.getInt("arenas." + name + ".sign.z"));
	}
	
	public boolean signCreated(String name) {
		return data.contains("arenas." + name + ".sign");
	}
	
	public Arena getArena(Player p) {
		for(Arena arena : arenas.values()) {
			if(arena.inGame(p)) {
				return arena;
			}
		}
		return null;
	}
	
	public Arena getArena(String name) {
		return arenas.get(name);
	}
	
	public boolean isInGame(Player p) {
		for(Arena arena : arenas.values()) {
			if(arena.inGame(p)) {
				return true;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerCraft(CraftItemEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(!isInGame(p)) return;
		for(Arena arena : arenas.values()) {
			if(arena.getPlayers().contains(p)) {
				if(arena.getItemType() == e.getCurrentItem().getType()) {
					arena.crafted(p);
				}
			}
		}
	}
	
	public void refreshConfig() {
		save();
		for(Arena arena : arenas.values()) {
			for(Player p : arena.getPlayers()) {
				arena.leave(p);
			}
		}
		arenas.clear();
		if(data.getConfigurationSection("arenas") == null)
			data.createSection("arenas");
		for(String key : data.getConfigurationSection("arenas").getKeys(false)) {
			Location lobbyLoc = new Location(Bukkit.getWorld(data.getString("arenas." + key + ".lobby.world")), data.getInt("arenas." + key + ".lobby.x"), data.getInt("arenas." + key + ".lobby.y"), data.getInt("arenas." + key + ".lobby.z"));
			Location spawnLoc = new Location(Bukkit.getWorld(data.getString("arenas." + key + ".spawn.world")), data.getInt("arenas." + key + ".spawn.x"), data.getInt("arenas." + key + ".spawn.y"), data.getInt("arenas." + key + ".spawn.z"));
			Arena arena = new Arena(lobbyLoc, spawnLoc, data.getBoolean("arenas." + key + ".enabled"), key, data.getInt("arenas." + key + ".minplayers"), data.getInt("arenas." + key + ".maxplayers"), data.getInt("arenas." + key + ".rounds"));
			arenas.put(key, arena);
		}
	}
	
	public Collection<Arena> getArenas() {
		return arenas.values();
	}
	
	public void setEnabled(String name, boolean enabled) {
		data.set("arenas." + name + ".enabled", enabled);
		Arena arena = arenas.get(name);
		arena.setEnabled(enabled);
		for(Player p : arena.getPlayers()) {
			p.sendMessage(QuickCraft.PREFIX + ChatColor.RED + "This arena has been disabled by a admin.");
			arena.leave(p);
		}
		QuickCraft.getConfigManager().save();
	}
	
	private void save() {
		try {
			for(String name : arenas.keySet()) {
				Arena arena = arenas.get(name);
				if(arena.getLobbyLocation() != null) {
					data.set("arenas." + name + ".lobby.world", arena.getLobbyLocation().getWorld().getName());
					data.set("arenas." + name + ".lobby.x", arena.getLobbyLocation().getX());
					data.set("arenas." + name + ".lobby.y", arena.getLobbyLocation().getY());
					data.set("arenas." + name + ".lobby.z", arena.getLobbyLocation().getZ());
				}
				if(arena.getSpawnLocation() != null) {
					data.set("arenas." + name + ".spawn.world", arena.getSpawnLocation().getWorld().getName());
					data.set("arenas." + name + ".spawn.x", arena.getSpawnLocation().getX());
					data.set("arenas." + name + ".spawn.y", arena.getSpawnLocation().getY());
					data.set("arenas." + name + ".spawn.z", arena.getSpawnLocation().getZ());
				}
				data.set("arenas." + name + ".enabled", arena.isEnabled());
				data.set("arenas." + name + ".minplayers", arena.getMinPlayers());
				data.set("arenas." + name + ".maxplayers", arena.getMaxPlayers());
				data.set("arenas." + name + ".rounds", arena.getRounds());
			}
			data.save(QuickCraft.getConfigManager().getData());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void remove(String name) {
		data.set("arenas." + name, null);
		arenas.remove(name);
		save();
	}
	
	public boolean join(String name, Player p) {
		Arena arena = arenas.get(name);
		return arena.join(p);
	}
	
	public void createArena(String name) {
		Location emptyLoc = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		arenas.put(name, new Arena(emptyLoc, emptyLoc, false, name, 2, 10, 3));
		save();
	}
	
	public void setMinPlayers(String name, int value) {
		arenas.get(name).setMinPlayers(value);
	}
	
	public void setMaxPlayers(String name, int value) {
		arenas.get(name).setMaxPlayers(value);
	}
	
	public void setLobby(String name, Location loc) {
		arenas.get(name).setLobbyLocation(loc);
		save();
	}
	
	public void setSpawn(String name, Location loc) {
		arenas.get(name).setSpawnLocation(loc);
		save();
	}
	
	public boolean exists(String name) {
		return arenas.containsKey(name);
	}
	
	public boolean isCompleted(String name) {
		Location spawn = arenas.get(name).getSpawnLocation();
		Location lobby = arenas.get(name).getLobbyLocation();
		return spawn.getX() != 0 && spawn.getY() != 0 && spawn.getZ() != 0 && lobby.getX() != 0 && lobby.getY() != 0 && lobby.getZ() != 0;
	}
	
	public boolean isEnabled(String name) {
		return arenas.get(name).isEnabled();
	}
	
}
