package nl.mistermel.quickcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import nl.mistermel.quickcraft.utils.ArenaManager;
import nl.mistermel.quickcraft.utils.ConfigManager;
import nl.mistermel.quickcraft.utils.SignManager;

public class QuickCraft extends JavaPlugin {
	
	private static QuickCraft instance;
	private static ConfigManager configManager;
	private static SignManager signManager;
	
	public static final String PREFIX = ChatColor.AQUA + "QuickCraft" + ChatColor.GRAY + " >> ";
	
	public void onEnable() {
		instance = this;
		
		configManager = new ConfigManager();
		signManager = new SignManager();
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		getServer().getPluginManager().registerEvents(new ArenaManager(), this);
		getServer().getPluginManager().registerEvents(new SignManager(), this);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new ArenaManager(), 0, 20);
	}
	
	public void onDisable() {
		for(Arena arena : ArenaManager.getArenas()) {
			for(Player p : arena.getPlayers()) {
				arena.leave(p);
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("quickcraft")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "+---------=QuickCraft=---------+");
				sender.sendMessage(ChatColor.GOLD + "QuickCraft");
				sender.sendMessage(ChatColor.GOLD + "Made by " + ChatColor.DARK_AQUA + "MisterMel");
				sender.sendMessage(ChatColor.GOLD + "With help from " + ChatColor.DARK_AQUA + "rens4000");
				sender.sendMessage(ChatColor.GOLD + "Use /qc help for a list of commands.");
				sender.sendMessage(ChatColor.AQUA + "+------------------------------+");
				return true;
			}
			if(args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.AQUA + "+----------=Commands=----------+");
				sender.sendMessage(ChatColor.GOLD + "/quickcraft or /qc" + ChatColor.GRAY + " - Main command.");
				sender.sendMessage(ChatColor.GOLD + "/qc help" + ChatColor.GRAY + " - Shows this message.");
				sender.sendMessage(ChatColor.GOLD + "/qc join <Name>" + ChatColor.GRAY + " - Join a game.");
				sender.sendMessage(ChatColor.GOLD + "/qc leave" + ChatColor.GRAY + " - Leave a game.");
				sender.sendMessage(ChatColor.GOLD + "/qc create <Name>" + ChatColor.GRAY + " - Creates a new arena.");
				sender.sendMessage(ChatColor.GOLD + "/qc setlobby <Name>" + ChatColor.GRAY + " - Sets the lobby of a arena.");
				sender.sendMessage(ChatColor.GOLD + "/qc setspawn <Name>" + ChatColor.GRAY + " - Sets the spawn of a arena.");
				sender.sendMessage(ChatColor.GOLD + "/qc setmainlobby" + ChatColor.GRAY + " - Sets the main lobby.");
				sender.sendMessage(ChatColor.GOLD + "/qc toggle <Name>" + ChatColor.GRAY + " - Toggles a arena.");
				sender.sendMessage(ChatColor.GOLD + "/qc reload" + ChatColor.GRAY + " - Reloads the plugin.");
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
				if(!configManager.mainLobbySet()) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Please set the main lobby first!");
					return true;
				}
				if(ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena already exists!");
					return true;
				}
				ArenaManager.createArena(args[1]);
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Arena created!");
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Things you should do now:");
				sender.sendMessage(PREFIX + ChatColor.GRAY + "- Set the lobby. /qc setlobby " + args[1]);
				sender.sendMessage(PREFIX + ChatColor.GRAY + "- Set the spawn. /qc setspawn " + args[1]);
				sender.sendMessage(PREFIX + ChatColor.GRAY + "- Enable the arena. /qc toggle " + args[1]);
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
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena does not exist!");
					return true;
				}
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This command can only be used as a player!");
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This arena is currently enabled. To make changes, please disable it first.");
					return true;
				}
				Player p = (Player) sender;
				ArenaManager.setLobby(args[1], p.getLocation());
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Lobby set!");
				return true;
			}
			if(args[0].equalsIgnoreCase("leave")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This command can only be used as a player!");
					return true;
				}
				Player p = (Player) sender;
				if(!ArenaManager.isInGame(p)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You aren't in a game!");
					return true;
				}
				ArenaManager.getArena(p).leave(p);
				return true;
			}
			if(args[0].equalsIgnoreCase("setspawn")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You dont have permission to use this command!");
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Use: /qc setspawn <Name>");
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena does not exist!");
					return true;
				}
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This command can only be used as a player!");
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This arena is currently enabled. To make changes, please disable it first.");
					return true;
				}
				Player p = (Player) sender;
				ArenaManager.setSpawn(args[1], p.getLocation());
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Spawn set!");
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
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena does not exist!");
					return true;
				}
				boolean toggled = ArenaManager.isEnabled(args[1]);
				if(toggled) {
					ArenaManager.setEnabled(args[1], false);
					sender.sendMessage(PREFIX + ChatColor.GOLD + "Arena disabled.");
				} else {
					if(!ArenaManager.isCompleted(args[1])) {
						sender.sendMessage(PREFIX + ChatColor.RED + "This arena isnt completed yet!");
						return true;
					}
					ArenaManager.setEnabled(args[1], true);
					sender.sendMessage(PREFIX + ChatColor.GOLD + "Arena enabled.");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("reload")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You dont have permission to use this command!");
					return true;
				}
				ArenaManager.refreshConfig();
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Plugin reloaded.");
				return true;
			}
			if(args[0].equalsIgnoreCase("join")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This command can only be used by players!");
					return true;
				}
				Player p = (Player) sender;
				if(args.length == 1) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Use: /qc join <Name>");
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena does not exist!");
					return true;
				}
				if(!ArenaManager.join(args[1], p)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This game is currently not joinable.");
				} else {
					sender.sendMessage(PREFIX + ChatColor.GOLD + "Joined game!");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("setmainlobby")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This command can only be used by players!");
					return true;
				}
				Player p = (Player) sender;
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You dont have permission to use this command!");
					return true;
				}
				configManager.setMainLobby(p.getLocation());
				p.sendMessage(PREFIX + ChatColor.GOLD + "Main lobby set!");
				return true;
			}
			if(args[0].equalsIgnoreCase("setmin")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You dont have permission to use this command!");
					return true;
				}
				if(args.length <= 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Use: /qc setmin <Arena> <Amount>");
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena does not exist!");
					return true;
				}
				if(!validInt(args[2])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Please enter a valid number.");
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This arena is currently enabled. To make changes, please disable it first.");
					return true;
				}
				if(Integer.parseInt(args[2]) < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "The minimum players must be at least 2.");
					return true;
				}
				ArenaManager.setMinPlayers(args[1], Integer.parseInt(args[2]));
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Minimum players set.");
				return true;
			}
			if(args[0].equalsIgnoreCase("setmax")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You dont have permission to use this command!");
					return true;
				}
				if(args.length <= 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Use: /qc setmax <Arena> <Amount>");
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena does not exist!");
					return true;
				}
				if(!validInt(args[2])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Please enter a valid number.");
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This arena is currently enabled. To make changes, please disable it first.");
					return true;
				}
				ArenaManager.setMaxPlayers(args[1], Integer.parseInt(args[2]));
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Maximum players set.");
				return true;
			}
			sender.sendMessage(PREFIX + ChatColor.RED + "Unknown command. Use /qc help for a list of commands.");
		}
		return true;
	}
	
	public static SignManager getSignManager() {
		return signManager;
	}
	
	public boolean validInt(String input) {
		try {
			Integer.parseInt(input);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static ConfigManager getConfigManager() {
		return configManager;
	}
	
	public static QuickCraft getInstance() {
		return instance;
	}
	
}
