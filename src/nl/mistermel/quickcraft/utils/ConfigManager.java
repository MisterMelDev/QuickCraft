package nl.mistermel.quickcraft.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.mistermel.quickcraft.QuickCraft;

public class ConfigManager {
	
	private File dataFile;
	private FileConfiguration data;
	
	private QuickCraft pl;
	
	public ConfigManager() {
		pl = QuickCraft.getInstance();
		
		dataFile = new File(pl.getDataFolder(), "data.yml");
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	
	public void save() {
		try {
			data.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FileConfiguration getDataFile() {
		return data;
	}
	
}
