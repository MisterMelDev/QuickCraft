package nl.mistermel.quickcraft.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.mistermel.quickcraft.QuickCraft;

public class ConfigManager {
	
	private File configFile;
	private FileConfiguration config;
	
	private File dataFile;
	private FileConfiguration data;
	
	private File langFile;
	private FileConfiguration lang;
	
	private File scoreFile;
	private FileConfiguration score;
	
	private QuickCraft pl;
	
	public ConfigManager() {
		pl = QuickCraft.getInstance();
		
		dataFile = new File(pl.getDataFolder(), "data.yml");
		data = YamlConfiguration.loadConfiguration(dataFile);
		
		configFile = new File(pl.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		
		langFile = new File(pl.getDataFolder(), "data.yml");
		lang = YamlConfiguration.loadConfiguration(langFile);
		
		scoreFile = new File(pl.getDataFolder(), "scores.yml");
		score = YamlConfiguration.loadConfiguration(scoreFile);
		
		config.addDefault("servername", "&6ExampleServer");
		config.options().copyDefaults(true);
	}
	
	public void setMainLobby(Location loc) {
		data.set("lobby.world", loc.getWorld().getName());
		data.set("lobby.x", loc.getBlockX());
		data.set("lobby.y", loc.getBlockY());
		data.set("lobby.z", loc.getBlockZ());
		save();
	}
	
	public boolean mainLobbySet() {
		return data.contains("lobby");
	}
	
	public Location getMainLobby() {
		return new Location(Bukkit.getWorld(data.getString("lobby.world")), data.getInt("lobby.x"), data.getInt("lobby.y"), data.getInt("lobby.z"));
	}
	
	public void save() {
		try {
			data.save(dataFile);
			lang.save(langFile);
			score.save(scoreFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getData() {
		return dataFile;
	}
	
	public FileConfiguration getDataFile() {
		return data;
	}
	
	public FileConfiguration getLangFile() {
		return lang;
	}
	
	public FileConfiguration getScoreFile() {
		return score;
	}
	
	public FileConfiguration getConfigFile() {
		return config;
	}
}
