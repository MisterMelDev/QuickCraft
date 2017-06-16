package nl.mistermel.quickcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import nl.mistermel.quickcraft.utils.ArenaManager;
import nl.mistermel.quickcraft.utils.ConfigManager;

public class QuickCraft extends JavaPlugin {
	
	private static QuickCraft instance;
	private static ConfigManager configManager;
	private static ArenaManager arenaManager;
	
	public static final String PREFIX = ChatColor.AQUA + "QuickCraft" + ChatColor.GRAY + " >> ";
	
	public void onEnable() {
		instance = this;
		
		configManager = new ConfigManager();
		arenaManager = new ArenaManager();
		
		getServer().getPluginManager().registerEvents(new Events(), this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("quickcraft")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "+---------=QuickCraft=---------+");
				sender.sendMessage(ChatColor.GOLD + "QuickCraft");
				sender.sendMessage(ChatColor.GOLD + "Made by " + ChatColor.DARK_AQUA + "MisterMel & rens4000");
				sender.sendMessage(ChatColor.GOLD + "Use /qc help for a list of commands.");
				sender.sendMessage(ChatColor.AQUA + "+------------------------------+");
				return true;
			}
			if(args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.AQUA + "+----------=Commands=----------+");
				sender.sendMessage(ChatColor.GOLD + "/quickcraft or /qc" + ChatColor.GRAY + " - Main command.");
				sender.sendMessage(ChatColor.GOLD + "/qc help" + ChatColor.GRAY + " - Shows this message.");
				sender.sendMessage(ChatColor.GOLD + "/qc create <Name>" + ChatColor.GRAY + " - Creates a new arena.");
				sender.sendMessage(ChatColor.GOLD + "/qc setlobby <Name>" + ChatColor.GRAY + " - Sets the lobby of a arena.");
				sender.sendMessage(ChatColor.GOLD + "/qc toggle <Name>" + ChatColor.GRAY + " - Toggles a arena.");
				sender.sendMessage(ChatColor.AQUA + "+------------------------------+");
				return true;
			}
			if(args[0].equalsIgnoreCase("create")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You dont have permission to use this command!");
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Use: /qc create <Name>");
					return true;
				}
				if(arenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena already exists!");
					return true;
				}
				arenaManager.createArena(args[1]);
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Arena created!");
				return true;
			}
			if(args[0].equalsIgnoreCase("setlobby")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You dont have permission to use this command!");
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Use: /qc setlobby <Name>");
					return true;
				}
				if(!arenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena does not exist!");
					return true;
				}
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This command can only be used as a player!");
					return true;
				}
				Player p = (Player) sender;
				arenaManager.setLobby(args[1], p.getLocation());
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Lobby set!");
				return true;
			}
			if(args[0].equalsIgnoreCase("toggle")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You dont have permission to use this command!");
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Use: /qc toggle <Name>");
					return true;
				}
				if(!arenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena does not exist!");
					return true;
				}
				boolean toggled = arenaManager.isEnabled(args[1]);
				if(toggled) {
					arenaManager.setEnabled(args[1], false);
					sender.sendMessage(PREFIX + ChatColor.GOLD + "Arena disabled.");
				} else {
					if(!arenaManager.isCompleted(args[1])) {
						sender.sendMessage(PREFIX + ChatColor.RED + "This arena isnt completed yet!");
						return true;
					}
					arenaManager.setEnabled(args[1], true);
					sender.sendMessage(PREFIX + ChatColor.GOLD + "Arena enabled.");
				}
				return true;
			}
			sender.sendMessage(PREFIX + ChatColor.RED + "Unknown command. Use /qc help for a list of commands.");
		}
		return true;
	}
	
	public static ConfigManager getConfigManager() {
		return configManager;
	}
	
	public static ArenaManager getArenaManager() {
		return arenaManager;
	}
	
	public static QuickCraft getInstance() {
		return instance;
	}
	
}
