package nl.mistermel.quickcraft.utils;

import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;
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
		lang.addDefault("language.arena-doesnt-exist", "&cThat arena doesn't exist!");
		lang.addDefault("language.arena-already-exists", "&cThat arena already exists!");
		lang.addDefault("language.arena-created", "&6Arena created!");
		lang.addDefault("language.unknowncommand", "&cUnknown subcommand. Use /qc help for command help.");
		lang.addDefault("language.usage", "&cUse: %command%");
		lang.addDefault("language.nomainlobby", "&cPlease set the main lobby first!");
		lang.addDefault("language.things-you-should-do-now", "&6Things you should do now:");
		lang.addDefault("language.set-the-lobby", "&7- Set the lobby. /qc setlobby %arena%");
		lang.addDefault("language.set-the-spawn", "&7- Set the spawn. /qc setspawn %arena%");
		lang.addDefault("language.set-min", "&7- Set the minimum amount of players. /qc setmin %arena% <Amount>");
		lang.addDefault("language.set-max", "&7- Set the maximum amount of players. /qc setmax %arena% <Amount>");
		lang.addDefault("language.set-rounds", "&7- Set the amount of rounds. /qc setrounds %arena% <Amount>");
		lang.addDefault("language.enable-arena", "&7- Enable the arena. /qc toggle %arena%");
		lang.addDefault("language.lobby-set", "&6Lobby set.");
		lang.addDefault("language.spawn-set", "&6Spawn set.");
		lang.addDefault("language.arena-enabled", "&6Arena enabled.");
		lang.addDefault("language.arena-disabled", "&6Arena disabled.");
		lang.addDefault("language.not-completed", "&6That arena isn't completed yet.");
		lang.addDefault("language.already-in-game", "&cYou are already in a game!");
		lang.addDefault("language.not-joinable", "&cThat arena is currently not joinable.");
		lang.addDefault("language.joined-game", "&6Joined game!");
		lang.addDefault("language.main-lobby-set", "&6Main lobby set!");
		lang.addDefault("language.enter-valid-number", "&cPlease enter a valid number.");
		lang.addDefault("language.min-too-low", "&cThe minimum players must be at least 2.");
		lang.addDefault("language.min-set", "&6Minimum players set.");
		lang.addDefault("language.max-set", "&6Maximum players set.");
		lang.addDefault("language.rounds-set", "&6Amount of rounds set.");
		lang.addDefault("language.are-you-sure", "&cAre you sure? Use %command% if you really want to delete this arena.");
		lang.addDefault("language.arena-removed", "&6Arena removed.");
		lang.addDefault("language.not-in-game", "&cYou aren't in a game!");
		lang.addDefault("language.command-help", "&6Use /qc help for a list of commands.");
		lang.addDefault("language.main-command", "Main command.");
		lang.addDefault("language.this-message", "Shows this message.");
		lang.addDefault("language.join-game", "Join a game.");
		lang.addDefault("language.list-arenas", "List all the arenas.");
		lang.addDefault("language.leave-game", "Leave a game.");
		lang.addDefault("language.create-arena", "Creates a arena.");
		lang.addDefault("language.set-lobby", "Set the lobby of a arena.");
		lang.addDefault("language.set-spawn", "Set the spawn of a arena.");
		lang.addDefault("language.set-main-lobby", "Set the main lobby.");
		lang.addDefault("language.set-min-players", "Set the minimum players of an arena.");
		lang.addDefault("language.set-max-players", "Set the maximum players of an arena.");
		lang.addDefault("language.set-amount-of-rounds", "Set the amount of rounds.");
		lang.addDefault("language.toggle-arena", "Toggle an arena.");
		lang.addDefault("language.delete-arena", "Delete an arena.");
		lang.addDefault("language.cant-do-this", "&cYou can't do this while in a game!");
		lang.options().copyDefaults(true);
		QuickCraft.getConfigManager().save();
	}
	
	public String getTranslation(String key) {
		if(!lang.contains(key)) {
			return ChatColor.RED + "Message not set.";
		}
		return " " + ChatColor.translateAlternateColorCodes('&', lang.getString("language." + key));
	}
}
