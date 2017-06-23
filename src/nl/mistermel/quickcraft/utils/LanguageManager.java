package nl.mistermel.quickcraft.utils;

import org.bukkit.configuration.file.FileConfiguration;

import nl.mistermel.quickcraft.QuickCraft;

public class LanguageManager {
	
	private FileConfiguration lang;
	
	public LanguageManager() {
		lang = QuickCraft.getConfigManager().getLangFile();
		
		addDefaults();
	}
	
	private void addDefaults() {
		lang.addDefault("language.noperm", "&cYou dont have permission to use this command!");
		lang.addDefault("language.onlyplayer", "&cThis command can only be used by players!");
		lang.addDefault("language.arena-enabled", "&cThis arena is currently enabled. To make changes, please disable the arena.");
		lang.addDefault("language.arena-doesnt-exist", "&cThat arena doesn't exist.");
		lang.addDefault("language.unknowncommand", "&cUnknown subcommand. Use /qc help for command help.");
		lang.addDefault("language.usage", "&cUse: %command%");
		lang.options().copyDefaults(true);
		QuickCraft.getConfigManager().save();
	}
	
	public String getTranslation(String key) {
		return lang.getString("language." + key);
	}
}
