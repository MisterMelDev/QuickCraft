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
	
	private QuickCraft pl;
	
	public ConfigManager() {
		pl = QuickCraft.getInstance();
		
		dataFile = new File(pl.getDataFolder(), "data.yml");
		data = YamlConfiguration.loadConfiguration(dataFile);
		
		configFile = new File(pl.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		
		langFile = new File(pl.getDataFolder(), "lang.yml");
		lang = YamlConfiguration.loadConfiguration(langFile);
		
		if(!config.contains("servername"))
			config.set("servername", "&6ExampleServer");
		
		if(!config.contains("language")) {
			lang.set("language.noperm", "&cYou dont have permission to use this command!");
			lang.set("language.onlyplayer", "&cThis command can only be used by players!");
			lang.set("language.arena-enabled", "&cThis arena is currently enabled. To make changes, please disable the arena.");
			lang.set("language.arena-doesnt-exist", "&cThat arena doesn't exist!");
			lang.set("language.arena-already-exists", "&cThat arena already exists!");
			lang.set("language.arena-created", "&6Arena created!");
			lang.set("language.unknowncommand", "&cUnknown subcommand. Use /qc help for command help.");
			lang.set("language.usage", "&cUse: %command%");
			lang.set("language.nomainlobby", "&cPlease set the main lobby first!");
			lang.set("language.things-you-should-do-now", "&6Things you should do now:");
			lang.set("language.set-the-lobby", "&7- Set the lobby. /qc setlobby %arena%");
			lang.set("language.set-the-spawn", "&7- Set the spawn. /qc setspawn %arena%");
			lang.set("language.set-min", "&7- Set the minimum amount of players. /qc setmin %arena% <Amount>");
			lang.set("language.set-max", "&7- Set the maximum amount of players. /qc setmax %arena% <Amount>");
			lang.set("language.set-rounds", "&7- Set the amount of rounds. /qc setrounds %arena% <Amount>");
			lang.set("language.enable-arena", "&7- Enable the arena. /qc toggle %arena%");
			lang.set("language.lobby-set", "&6Lobby set.");
			lang.set("language.spawn-set", "&6Spawn set.");
			lang.set("language.arena-enabled", "&6Arena enabled.");
			lang.set("language.arena-disabled", "&6Arena disabled.");
			lang.set("language.not-completed", "&6That arena isn't completed yet.");
			lang.set("language.already-in-game", "&cYou are already in a game!");
			lang.set("language.not-joinable", "&cThat arena is currently not joinable.");
			lang.set("language.joined-game", "&6Joined game!");
			lang.set("language.main-lobby-set", "&6Main lobby set!");
			lang.set("language.enter-valid-number", "&cPlease enter a valid number.");
			lang.set("language.min-too-low", "&cThe minimum players must be at least 2.");
			lang.set("language.min-set", "&6Minimum players set.");
			lang.set("language.max-set", "&6Maximum players set.");
			lang.set("language.rounds-set", "&6Amount of rounds set.");
			lang.set("language.are-you-sure", "&cAre you sure? Use %command% if you really want to delete this arena.");
			lang.set("language.arena-removed", "&6Arena removed.");
			lang.set("language.not-in-game", "&cYou aren't in a game!");
			lang.set("language.command-help", "&6Use /qc help for a list of commands.");
			lang.set("language.main-command", "Main command.");
			lang.set("language.this-message", "Shows this message.");
			lang.set("language.join-game", "Join a game.");
			lang.set("language.list-arenas", "List all the arenas.");
			lang.set("language.leave-game", "Leave a game.");
			lang.set("language.create-arena", "Creates a arena.");
			lang.set("language.set-lobby", "Set the lobby of a arena.");
			lang.set("language.set-spawn", "Set the spawn of a arena.");
			lang.set("language.set-main-lobby", "Set the main lobby.");
			lang.set("language.set-min-players", "Set the minimum players of an arena.");
			lang.set("language.set-max-players", "Set the maximum players of an arena.");
			lang.set("language.set-amount-of-rounds", "Set the amount of rounds.");
			lang.set("language.toggle-arena", "Toggle an arena.");
			lang.set("language.delete-arena", "Delete an arena.");
			lang.set("language.cant-do-this", "&cYou can't do this while in a game!");
			lang.set("language.not-enough-players", "&cCountdown cancelled. Not enough players anymore!");
			lang.set("language.left", "&cYou left the game.");
			lang.set("language.state-waiting", "&3Waiting");
			lang.set("language.state-starting", "&3Starting");
			lang.set("language.state-game", "&2In Game");
			lang.set("language.state-reset", "&cResetting");
			lang.set("language.state-waiting-extended", "&3Waiting for players");
			lang.set("language.countdown", "&6The game is starting in &3%seconds% &6seconds");
			lang.set("language.starting", "&6The game is starting!");
			lang.set("language.subtitle", "&6Get ready to craft!");
			lang.set("language.item", "&6Craft a %item%");
			lang.set("language.crafted-item", "&6You crafted the item! +1 Point");
			lang.set("language.crafted-item-first", "&6You were the first to craft the item! +2 Points");
			lang.set("language.rounds-left", "&6Everybody finished! There are %rounds% rounds left!");
			lang.set("language.starting-countdown", "&6Starting countdown!");
			lang.set("language.go", "&3GO!");
		}
		save();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getData() {
		return dataFile;
	}
	
	public File getLang() {
		return langFile;
	}
	
	public FileConfiguration getDataFile() {
		return data;
	}
	
	public FileConfiguration getLangFile() {
		return lang;
	}
	
	
	public FileConfiguration getConfigFile() {
		return config;
	}
}
