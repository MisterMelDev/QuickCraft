package nl.mistermel.quickcraft.utils;

import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;
import nl.mistermel.quickcraft.QuickCraft;

public class LanguageManager {
	
	private FileConfiguration lang;
	
	public LanguageManager() {
		lang = QuickCraft.getConfigManager().getLangFile();
	}
	
	public String getTranslation(String key) {
		if(!lang.contains(key)) {
			return ChatColor.RED + "Message undefined.";
		}
		return " " + ChatColor.translateAlternateColorCodes('&', lang.getString("language." + key));
	}
}
