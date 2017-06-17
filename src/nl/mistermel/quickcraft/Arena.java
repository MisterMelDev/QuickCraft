package nl.mistermel.quickcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import nl.mistermel.quickcraft.utils.ArenaManager;
import nl.mistermel.quickcraft.utils.ConfigManager;
import nl.mistermel.quickcraft.utils.GameState;

public class Arena {

	private Location lobbyLoc;
	private Location spawnLoc;
	private GameState state;
	private Material mat;
	private QuickCraft pl;
	private int minPlayers = 2, maxPlayers = 10;
	private int countdown = 30;
	private boolean enabled;
	private BukkitScheduler scheduler;
	private ConfigManager configManager;
	private ArenaManager arenaManager;

	private Scoreboard board;
	private Objective obj;

	private String name;

	private List<UUID> players = new ArrayList<UUID>();

	private List<UUID> crafted = new ArrayList<UUID>();

	public Arena(Location lobbyLoc, Location spawnLoc, boolean enabled, ArenaManager arenaManager, String name) {
		this.lobbyLoc = lobbyLoc;
		this.spawnLoc = spawnLoc;
		this.enabled = enabled;
		this.name = name;
		this.arenaManager = arenaManager;

		this.state = GameState.WAITING;
		if(arenaManager.signCreated(name)) {
			QuickCraft.getSignManager().updateSign(arenaManager.getSign(name), name);
		}
		this.scheduler = Bukkit.getServer().getScheduler();
		this.configManager = QuickCraft.getConfigManager();
		this.mat = arenaManager.getRandomMaterial();
		this.pl = QuickCraft.getInstance();
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		this.obj = board.registerNewObjective("QC" + name, "");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.AQUA + "QuickCraft");

		Score filler1 = obj.getScore(ChatColor.GRAY + "");
		filler1.setScore(3);

		Score status = obj.getScore(ChatColor.GREEN + "Waiting for players");
		status.setScore(2);

		Score filler2 = obj.getScore("");
		filler2.setScore(1);

		Score serverName = obj.getScore(
				ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
		serverName.setScore(0);
	}

	public void tick() {
		if (state == GameState.STARTING) {
			for (String score : board.getEntries()) {
				board.resetScores(score);
			}

			Score filler1 = obj.getScore(ChatColor.GRAY + "");
			filler1.setScore(3);

			Score status = obj.getScore(
					ChatColor.GREEN + "Starting in " + ChatColor.DARK_AQUA + countdown + ChatColor.GREEN + " seconds!");
			status.setScore(2);

			Score filler2 = obj.getScore("");
			filler2.setScore(1);

			Score serverName = obj.getScore(
					ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
			serverName.setScore(0);

			countdown--;
			setExp(countdown);
			if (countdown <= 0) {
				state = GameState.IN_GAME;
				if(arenaManager.signCreated(name)) {
					QuickCraft.getSignManager().updateSign(arenaManager.getSign(name), name);
				}
				sendMessage(ChatColor.GOLD + "The game is starting!");
				for (String score : board.getEntries()) {
					board.resetScores(score);
				}
				
				Score filler3 = obj.getScore(ChatColor.GRAY + "");
				filler3.setScore(3);

				Score status1 = obj.getScore(ChatColor.GREEN + "The game is starting!");
				status1.setScore(2);

				Score filler4 = obj.getScore("");
				filler4.setScore(1);

				Score serverName1 = obj.getScore(
						ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
				serverName1.setScore(0);
				
				setExp(0);
				teleport(spawnLoc);

				scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
					public void run() {
						sendTitle(ChatColor.GREEN + "3", ChatColor.GOLD + "Get ready to craft!");
					}
				}, 20);

				scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
					public void run() {
						sendTitle(ChatColor.YELLOW + "2", ChatColor.GOLD + "Get ready to craft!");
					}
				}, 40);

				scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
					public void run() {
						sendTitle(ChatColor.YELLOW + "1", ChatColor.GOLD + "Get ready to craft!");
					}
				}, 60);

				scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
					public void run() {
						sendTitle(ChatColor.GREEN + "GO!", ChatColor.GOLD + "Craft a " + mat.toString().toLowerCase());
						sendMessage(ChatColor.GOLD + "Craft a " + mat.toString().toLowerCase());
						
						for (String score : board.getEntries()) {
							board.resetScores(score);
						}
						
						Score filler1 = obj.getScore(ChatColor.GRAY + "");
						filler1.setScore(3);

						Score status = obj.getScore(ChatColor.GREEN + "Craft a " + mat.toString().toLowerCase());
						status.setScore(2);

						Score filler2 = obj.getScore("");
						filler2.setScore(1);

						Score serverName = obj.getScore(
								ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
						serverName.setScore(0);
						
					}
				}, 60);

				return;
			}
			if (countdown % 10 == 0) {
				sendMessage(ChatColor.GOLD + "The game will start in " + ChatColor.DARK_AQUA + countdown
						+ ChatColor.GOLD + " seconds");
				makeSound(Sound.BLOCK_NOTE_PLING);
			}
			if (countdown <= 5) {
				sendMessage(ChatColor.GOLD + "The game will start in " + ChatColor.DARK_AQUA + countdown
						+ ChatColor.GOLD + " seconds");
				makeSound(Sound.BLOCK_NOTE_PLING);
			}
		}
	}

	public Material getItemType() {
		return mat;
	}

	public void crafted(Player p) {
		if (state != GameState.IN_GAME)
			return;
		if (hasCraftedItem(p))
			return;
		p.getInventory().clear();
		p.sendMessage(QuickCraft.PREFIX + ChatColor.GREEN + "You crafted the item!");
		crafted.add(p.getUniqueId());

		if (crafted.size() >= players.size()) {
			sendMessage(ChatColor.DARK_AQUA + "Everybody finished!");
			sendMessage(ChatColor.GOLD + "1: " + Bukkit.getPlayer(crafted.get(0)).getName());
			sendMessage(ChatColor.GREEN + "2: " + Bukkit.getPlayer(crafted.get(1)).getName());
			if(crafted.size() >= 3)
				sendMessage(ChatColor.DARK_GRAY + "3: " + Bukkit.getPlayer(crafted.get(2)).getName());
			for(UUID u : players) {
				Player p2 = Bukkit.getPlayer(u);
				p2.sendMessage(ChatColor.GREEN + "You came " + (crafted.indexOf(p2.getUniqueId()) + 1) + "st");
			}
			scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
				public void run() {
					reset();
				}
			}, 80L);
		}
	}

	public boolean hasCraftedItem(Player p) {
		return crafted.contains(p.getUniqueId());
	}

	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (UUID u : this.players) {
			players.add(Bukkit.getPlayer(u));
		}
		return players;
	}

	public boolean join(Player p) {
		if (!state.isJoinable())
			return false;
		if (!enabled)
			return false;
		if (players.size() == maxPlayers)
			return false;
		players.add(p.getUniqueId());
		p.teleport(lobbyLoc);

		if (state == GameState.WAITING) {
			if (players.size() >= minPlayers) {
				state = GameState.STARTING;
				if(arenaManager.signCreated(name)) {
					QuickCraft.getSignManager().updateSign(arenaManager.getSign(name), name);
				}
				sendMessage(ChatColor.GOLD + "Starting countdown!");
			}
		}

		p.setScoreboard(board);

		return true;
	}

	public void leave(Player p) {
		p.teleport(QuickCraft.getConfigManager().getMainLobby());
		p.sendMessage(QuickCraft.PREFIX + ChatColor.RED + "You left the game.");
		players.remove(p.getUniqueId());
		p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	public void reset() {
		this.state = GameState.RESETTING;
		if(arenaManager.signCreated(name)) {
			QuickCraft.getSignManager().updateSign(arenaManager.getSign(name), name);
		}
		for (int i = 0; i < players.size(); i++) {
			Player p = Bukkit.getPlayer(players.get(i));
			p.teleport(configManager.getMainLobby());
		}
		countdown = 30;
		players.clear();
		crafted.clear();
		mat = QuickCraft.getArenaManager().getRandomMaterial();
		this.state = GameState.WAITING;
		if(arenaManager.signCreated(name)) {
			QuickCraft.getSignManager().updateSign(arenaManager.getSign(name), name);
		}
	}

	public void makeSound(Sound sound) {
		for (UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.playSound(p.getLocation(), sound, 1, 1);
		}
	}

	public void sendTitle(String title, String subTitle) {
		for (UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.sendTitle(title, subTitle, 5, 20, 10);
		}
	}

	public void setMinPlayers(int amount) {
		minPlayers = amount;
	}

	public void setMaxPlayers(int amount) {
		maxPlayers = amount;
	}

	public void sendMessage(String message) {
		for (UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.sendMessage(QuickCraft.PREFIX + message);
		}
	}

	public void setExp(int amount) {
		for (UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.setTotalExperience(amount);
		}
	}

	public void teleport(Location loc) {
		for (UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.teleport(loc);
		}
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public int getMinPlayers() {
		return minPlayers;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getAmountOfPlayers() {
		return players.size();
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean inGame(Player p) {
		return players.contains(p.getUniqueId());
	}

	public GameState getState() {
		return state;
	}

	public void setLobbyLocation(Location loc) {
		this.lobbyLoc = loc;
	}

	public void setSpawnLocation(Location loc) {
		this.spawnLoc = loc;
	}

	public Location getLobbyLocation() {
		return lobbyLoc;
	}

	public Location getSpawnLocation() {
		return spawnLoc;
	}
}
