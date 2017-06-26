package nl.mistermel.quickcraft;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import nl.mistermel.quickcraft.utils.ArenaManager;
import nl.mistermel.quickcraft.utils.ConfigManager;
import nl.mistermel.quickcraft.utils.LanguageManager;
import nl.mistermel.quickcraft.utils.SignManager;

public class QuickCraft extends JavaPlugin {
	
	private static QuickCraft instance;
	private static ConfigManager configManager;
	private static SignManager signManager;
	private static LanguageManager langManager;
	
	public static final String PREFIX = ChatColor.AQUA + "QuickCraft" + ChatColor.GRAY + " >> ";
	
	public void onEnable() {
		instance = this;
		
		configManager = new ConfigManager();
		signManager = new SignManager();
		langManager = new LanguageManager();
		
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
				sender.sendMessage(ChatColor.GOLD + langManager.getTranslation("command-help"));
				sender.sendMessage(ChatColor.AQUA + "+------------------------------+");
				return true;
			}
			if(args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.AQUA + "+----------=Commands=----------+");
				sender.sendMessage(ChatColor.GOLD + "/quickcraft or /qc" + ChatColor.GRAY + " - " + langManager.getTranslation("main-command"));
				sender.sendMessage(ChatColor.GOLD + "/qc help" + ChatColor.GRAY + " - " + langManager.getTranslation("this-message"));
				sender.sendMessage(ChatColor.GOLD + "/qc join <Name>" + ChatColor.GRAY + " - " + langManager.getTranslation("join-game"));
				sender.sendMessage(ChatColor.GOLD + "/qc list" + ChatColor.GRAY + " - " + langManager.getTranslation("list-arenas"));
				sender.sendMessage(ChatColor.GOLD + "/qc leave" + ChatColor.GRAY + " - " + langManager.getTranslation("leave-game"));
				sender.sendMessage(ChatColor.GOLD + "/qc create <Name>" + ChatColor.GRAY + " - " + langManager.getTranslation("create-arena"));
				sender.sendMessage(ChatColor.GOLD + "/qc setlobby <Name>" + ChatColor.GRAY + " - " + langManager.getTranslation("set-lobby"));
				sender.sendMessage(ChatColor.GOLD + "/qc setspawn <Name>" + ChatColor.GRAY + " - " + langManager.getTranslation("set-spawn"));
				sender.sendMessage(ChatColor.GOLD + "/qc setmainlobby" + ChatColor.GRAY + " - " + langManager.getTranslation("set-main-lobby"));
				sender.sendMessage(ChatColor.GOLD + "/qc setmin <Name> <Amount>" + ChatColor.GRAY + " - " + langManager.getTranslation("set-min-players"));
				sender.sendMessage(ChatColor.GOLD + "/qc setmax <Name> <Amount>" + ChatColor.GRAY + " - " +  langManager.getTranslation("set-max-players"));
				sender.sendMessage(ChatColor.GOLD + "/qc setrounds <Name> <Amount>" + ChatColor.GRAY + " - " + langManager.getTranslation("set-amount-of-rounds"));
				sender.sendMessage(ChatColor.GOLD + "/qc toggle <Name>" + ChatColor.GRAY + " - " + langManager.getTranslation("toggle-arena"));
				sender.sendMessage(ChatColor.GOLD + "/qc remove <Name>" + ChatColor.GRAY + " - " + langManager.getTranslation("delete-arena"));
				sender.sendMessage(ChatColor.GOLD + "/qc stats" + ChatColor.GRAY + " - " + langManager.getTranslation("show-stats"));
				sender.sendMessage(ChatColor.AQUA + "+------------------------------+");
				return true;
			}
			if(args[0].equalsIgnoreCase("create")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(PREFIX + langManager.getTranslation("usage").replaceAll("%command%", "/qc create <Arena>"));
					return true;
				}
				if(!configManager.mainLobbySet()) {
					sender.sendMessage(PREFIX + langManager.getTranslation("nomainlobby"));
					return true;
				}
				if(ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-already-exists"));
					return true;
				}
				ArenaManager.createArena(args[1]);
				sender.sendMessage(PREFIX + langManager.getTranslation("arena-created"));
				sender.sendMessage(PREFIX + langManager.getTranslation("things-you-should-do-now"));
				sender.sendMessage(PREFIX + langManager.getTranslation("set-the-lobby").replaceAll("%arena%", args[1]));
				sender.sendMessage(PREFIX + langManager.getTranslation("set-the-spawn").replaceAll("%arena%", args[1]));
				sender.sendMessage(PREFIX + langManager.getTranslation("set-min").replaceAll("%arena%", args[1]));
				sender.sendMessage(PREFIX + langManager.getTranslation("set-max").replaceAll("%arena%", args[1]));
				sender.sendMessage(PREFIX + langManager.getTranslation("set-rounds").replaceAll("%arena%", args[1]));
				sender.sendMessage(PREFIX + langManager.getTranslation("enable-arena").replaceAll("%arena%", args[1]));
				return true;
			}
			if(args[0].equalsIgnoreCase("setlobby")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(PREFIX + langManager.getTranslation("usage").replaceAll("%command%", "/qc setlobby <Arena>"));
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-doesnt-exist"));
					return true;
				}
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + langManager.getTranslation("onlyplayer"));
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-enabled"));
					return true;
				}
				Player p = (Player) sender;
				ArenaManager.setLobby(args[1], p.getLocation());
				sender.sendMessage(PREFIX + langManager.getTranslation("lobby-set"));
				return true;
			}
			if(args[0].equalsIgnoreCase("stats")) {
				if(!configManager.getScoreFile().contains("players." + sender.getName() + ".wins")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("language.no-stats"));
				} else {
					sender.sendMessage(PREFIX + langManager.getTranslation("language.stats").replaceAll("%games%", configManager.getScoreFile().getString("players." + sender.getName() + ".wins")));
				}
			}
			if(args[0].equalsIgnoreCase("leave")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + langManager.getTranslation("onlyplayer"));
					return true;
				}
				Player p = (Player) sender;
				if(!ArenaManager.isInGame(p)) {
					sender.sendMessage(PREFIX + langManager.getTranslation("not-in-game"));
					return true;
				}
				ArenaManager.getArena(p).leave(p);
				return true;
			}
			if(args[0].equalsIgnoreCase("setspawn")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(PREFIX + langManager.getTranslation("usage").replaceAll("%command%", "/qc setspawn <Arena>"));
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-doesnt-exist"));
					return true;
				}
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + langManager.getTranslation("onlyplayer"));
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-enabled"));
					return true;
				}
				Player p = (Player) sender;
				ArenaManager.setSpawn(args[1], p.getLocation());
				sender.sendMessage(PREFIX + langManager.getTranslation("spawn-set"));
				return true;
			}
			if(args[0].equalsIgnoreCase("toggle")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(PREFIX + langManager.getTranslation("usage").replaceAll("%command%", "/qc toggle <Arena>"));
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-doesnt-exist"));
					return true;
				}
				boolean toggled = ArenaManager.isEnabled(args[1]);
				if(toggled) {
					ArenaManager.setEnabled(args[1], false);
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-disabled"));
				} else {
					if(!ArenaManager.isCompleted(args[1])) {
						sender.sendMessage(PREFIX + langManager.getTranslation("not-completed"));
						return true;
					}
					ArenaManager.setEnabled(args[1], true);
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-enabled"));
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("join")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + langManager.getTranslation("onlyplayer"));
					return true;
				}
				Player p = (Player) sender;
				if(args.length == 1) {
					sender.sendMessage(PREFIX + langManager.getTranslation("usage").replaceAll("%command%", "/qc join <Arena>"));
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-doesnt-exist"));
					return true;
				}
				if(ArenaManager.isInGame(p)) {
					sender.sendMessage(PREFIX + langManager.getTranslation("already-in-game"));
					return true;
				}
				if(!ArenaManager.join(args[1], p)) {
					sender.sendMessage(PREFIX + langManager.getTranslation("not-joinable"));
				} else {
					sender.sendMessage(PREFIX + langManager.getTranslation("joined-game"));
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("setmainlobby")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(PREFIX + langManager.getTranslation("onlyplayer"));
					return true;
				}
				Player p = (Player) sender;
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				configManager.setMainLobby(p.getLocation());
				p.sendMessage(PREFIX + langManager.getTranslation("main-lobby-set"));
				return true;
			}
			if(args[0].equalsIgnoreCase("setmin")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				if(args.length <= 2) {
					sender.sendMessage(PREFIX + langManager.getTranslation("usage").replaceAll("%command%", "/qc setmin <Arena> <Amount>"));
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-doesnt-exist"));
					return true;
				}
				if(!validInt(args[2])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("enter-valid-number"));
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-enabled"));
					return true;
				}
				if(Integer.parseInt(args[2]) < 2) {
					sender.sendMessage(PREFIX + langManager.getTranslation("min-too-low"));
					return true;
				}
				ArenaManager.setMinPlayers(args[1], Integer.parseInt(args[2]));
				sender.sendMessage(PREFIX + langManager.getTranslation("min-set"));
				return true;
			}
			if(args[0].equalsIgnoreCase("setmax")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				if(args.length <= 2) {
					sender.sendMessage(PREFIX + langManager.getTranslation("usage").replaceAll("%command%", "/qc setmax <Arena> <Amount>"));
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-doesnt-exist"));
					return true;
				}
				if(!validInt(args[2])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("enter-valid-number"));
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-enabled"));
					return true;
				}
				ArenaManager.setMaxPlayers(args[1], Integer.parseInt(args[2]));
				sender.sendMessage(PREFIX + langManager.getTranslation("max-set"));
				return true;
			}
			if(args[0].equalsIgnoreCase("list")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				StringBuilder str = new StringBuilder();
				for(Arena arena : ArenaManager.getArenas()) {
					String name = arena.getName();
					str.append(name + ", ");
				}
				sender.sendMessage(PREFIX + str.toString());
				return true;
			}
			if(args[0].equalsIgnoreCase("setrounds")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				if(args.length <= 2) {
					sender.sendMessage(PREFIX + langManager.getTranslation("usage").replaceAll("%command%", "/qc setrounds <Arena> <Amount>"));
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-doesnt-exist"));
					return true;
				}
				if(!validInt(args[2])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("enter-valid-number"));
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-enabled"));
					return true;
				}
				ArenaManager.getArena(args[1]).setRounds(Integer.parseInt(args[2]));
				sender.sendMessage(PREFIX + langManager.getTranslation("rounds-set"));
				return true;
			}
			if(args[0].equalsIgnoreCase("remove")) {
				if(!sender.hasPermission("quickcraft.admin")) {
					sender.sendMessage(PREFIX + langManager.getTranslation("noperm"));
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(PREFIX + langManager.getTranslation("usage").replaceAll("%command%", "/qc remove <Arena>"));
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-doesnt-exist"));
					return true;
				}
				if(ArenaManager.isEnabled(args[1])) {
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-enabled"));
					return true;
				}
				if(args.length == 2) {
					sender.sendMessage(PREFIX + langManager.getTranslation("are-you-sure").replaceAll("%command%", "/qc remove " + args[1] + " confirm"));
					return true;
				}
				if(args[2].equalsIgnoreCase("confirm")) {
					ArenaManager.remove(args[1]);
					sender.sendMessage(PREFIX + langManager.getTranslation("arena-removed"));
				}
				return true;
			}
			sender.sendMessage(PREFIX + langManager.getTranslation("unknowncommand"));
		}
		return true;
	}
	
	public static SignManager getSignManager() {
		return signManager;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
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
	
	public static LanguageManager getLanguageManager() {
		return langManager;
	}
}
