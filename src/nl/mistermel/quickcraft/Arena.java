package nl.mistermel.quickcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.google.common.collect.Iterables;

import nl.mistermel.quickcraft.utils.ConfigManager;
import nl.mistermel.quickcraft.utils.GameState;
import nl.mistermel.quickcraft.utils.ItemUtils;
import nl.mistermel.quickcraft.utils.LanguageManager;

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
	private LanguageManager langManager;
	private int round = 1;
	private int rounds;

	private Scoreboard board;
	private Objective obj;

	private String name;

	private List<UUID> players = new ArrayList<UUID>();
	private List<UUID> crafted = new ArrayList<UUID>();
	private Map<UUID, Integer> points = new HashMap<UUID, Integer>();

	public Arena(Location lobbyLoc, Location spawnLoc, boolean enabled, String name, int minPlayers, int maxPlayers, int rounds) {
		this.lobbyLoc = lobbyLoc;
		this.spawnLoc = spawnLoc;
		this.enabled = enabled;
		this.name = name;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.rounds = rounds;

		this.state = GameState.WAITING;
		
		this.initialVariableSetup();
		this.scoreboardCreation();
		this.signUpdate();
	}
	
	private void scoreboardCreation() {
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		this.obj = board.registerNewObjective("QC" + name, "");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.AQUA + "QuickCraft");
		
		Score filler1 = obj.getScore(ChatColor.GRAY + "");
		filler1.setScore(3);

		Score status = obj.getScore(ChatColor.GREEN + langManager.getTranslation("state-waiting-extended"));
		status.setScore(2);

		Score filler2 = obj.getScore("");
		filler2.setScore(1);

		Score serverName = obj.getScore(ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
		serverName.setScore(0);
	}
	
	private void initialVariableSetup() {
		this.scheduler = Bukkit.getServer().getScheduler();
		this.configManager = QuickCraft.getConfigManager();
		this.langManager = QuickCraft.getLanguageManager();
		this.mat = ItemUtils.getRandomMaterial();
		this.pl = QuickCraft.getInstance();
	}
	
	private void signUpdate() {
		if (QuickCraft.getArenaManager().signCreated(name)) {
			QuickCraft.getSignManager().updateSign(QuickCraft.getArenaManager().getSign(name), this);
		}
	}

	public void tick() {
		if (state == GameState.STARTING) {
			for (String score : board.getEntries()) {
				board.resetScores(score);
			}

			Score filler1 = obj.getScore(ChatColor.GRAY + "");
			filler1.setScore(3);

			Score status = obj.getScore(langManager.getTranslation("countdown").replaceAll("%seconds%", Integer.toString(countdown - 1)));
			status.setScore(2);

			Score filler2 = obj.getScore("");
			filler2.setScore(1);

			Score serverName = obj.getScore(ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
			serverName.setScore(0);

			countdown--;
			setExp(countdown);
			if (countdown <= 0) {
				state = GameState.IN_GAME;
				
				this.signUpdate();
				sendMessage(langManager.getTranslation("starting"));
				for (String score : board.getEntries()) {
					board.resetScores(score);
				}

				Score filler3 = obj.getScore(ChatColor.GRAY + "");
				filler3.setScore(3);

				Score status1 = obj.getScore(langManager.getTranslation("starting"));
				status1.setScore(2);

				Score filler4 = obj.getScore("");
				filler4.setScore(1);

				Score serverName1 = obj.getScore(ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
				serverName1.setScore(0);

				setExp(0);
				teleport(spawnLoc);
				
				this.finalCountdown();

				scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
					public void run() {
						sendTitle(langManager.getTranslation("go"), langManager.getTranslation("item").replaceAll("%item%", mat.name()));
						sendMessage(langManager.getTranslation("item").replaceAll("%item%", mat.name()));
						
						for (ItemStack item : ItemUtils.getIngredients(mat)) {
							giveItem(item);
						}

						for (String score : board.getEntries()) {
							board.resetScores(score);
						}

						Score filler1 = obj.getScore(ChatColor.GRAY + "");
						filler1.setScore(3);

						Score status = obj.getScore(langManager.getTranslation("item").replaceAll("%item%", mat.name()));
						status.setScore(2);

						Score filler2 = obj.getScore("");
						filler2.setScore(1);

						Score serverName = obj.getScore(ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
						serverName.setScore(0);

					}
				}, 80);

				return;
			}
			this.updateCountdown();
		}
	}
	
	private void updateCountdown() {
		if(countdown % 10 == 0 || countdown <= 5) {
			sendMessage(langManager.getTranslation("countdown").replaceAll("%seconds%", Integer.toString(countdown)));
			makeSound(Sound.BLOCK_NOTE_PLING);
		}
	}
	
	private void finalCountdown() {
		scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
			public void run() {
				sendTitle(ChatColor.GREEN + "3", langManager.getTranslation("subtitle"));
			}
		}, 20);

		scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
			public void run() {
				sendTitle(ChatColor.YELLOW + "2", langManager.getTranslation("subtitle"));
			}
		}, 40);

		scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
			public void run() {
				sendTitle(ChatColor.YELLOW + "1", langManager.getTranslation("subtitle"));
			}
		}, 60);
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
		
		if(crafted.size() == 0) {
			p.sendMessage(QuickCraft.PREFIX + langManager.getTranslation("crafted-item-first"));
			points.put(p.getUniqueId(), points.get(p.getUniqueId()) + 2);
		} else {
			p.sendMessage(QuickCraft.PREFIX + langManager.getTranslation("crafted-item"));
			points.put(p.getUniqueId(), points.get(p.getUniqueId()) + 1);
		}
		crafted.add(p.getUniqueId());

		if (crafted.size() >= players.size()) {
			round++;

			if (round > rounds) {
				end();
				return;
			} else {
				crafted.clear();
				for (UUID u : players) {
					Player p2 = Bukkit.getPlayer(u);
					p2.getInventory().clear();
				}

				mat = ItemUtils.getRandomMaterial();

				sendMessage(langManager.getTranslation("rounds-left").replaceAll("%rounds-left%", Integer.toString(rounds - round + 1)));

				for (String score : board.getEntries()) {
					board.resetScores(score);
				}

				sendTitle("", langManager.getTranslation("item").replaceAll("%item%", mat.name()));

				Score filler1 = obj.getScore(ChatColor.GRAY + "");
				filler1.setScore(3);

				Score status = obj.getScore(langManager.getTranslation("item").replaceAll("%item%", mat.name()));
				status.setScore(2);

				Score filler2 = obj.getScore("");
				filler2.setScore(1);

				Score serverName = obj.getScore(ChatColor.translateAlternateColorCodes('&',
						configManager.getConfigFile().getString("servername")));
				serverName.setScore(0);

				for (UUID u : players) {
					Player p2 = Bukkit.getPlayer(u);
					for (ItemStack item : ItemUtils.getIngredients(mat)) {
						p2.getInventory().addItem(item);
					}
				}
			}
		}
	}
	
	public void addToScoreFile(String name) {
		Player p = Bukkit.getPlayer(name);
		if(configManager.getScoreFile().contains("players." + p.getName() + ".wins")) {
			int newWins = configManager.getScoreFile().getInt("players." + p.getName() + ".wins") + 1;
			configManager.getScoreFile().set("players." + p.getName() + ".wins", newWins);
			configManager.save();
		} else {
			configManager.getScoreFile().set("players." + p.getName() + ".wins", 1);
			configManager.save();
		}
	}

	public void end() {
		sendMessage(ChatColor.DARK_AQUA + "Everybody finished!");
		Map<UUID, Integer> sortedPoints = QuickCraft.sortByValue(points);
		addToScoreFile(Bukkit.getPlayer(Iterables.get(sortedPoints.keySet(), 0)).getName());
		sendMessage(ChatColor.GOLD + "1: " + Bukkit.getPlayer(Iterables.get(sortedPoints.keySet(), 0)).getName() + ChatColor.GRAY + " - " + sortedPoints.get(Iterables.get(sortedPoints.keySet(), 0)) + " points");
		sendMessage(ChatColor.GOLD + "2: " + Bukkit.getPlayer(Iterables.get(sortedPoints.keySet(), 1)).getName() + ChatColor.GRAY + " - " + sortedPoints.get(Iterables.get(sortedPoints.keySet(), 1)) + " points");
		if(crafted.size() >= 3)
			sendMessage(ChatColor.GOLD + "3: " + Bukkit.getPlayer(Iterables.get(sortedPoints.keySet(), 2)).getName() + ChatColor.GRAY + " - " + sortedPoints.get(Iterables.get(sortedPoints.keySet(), 2)) + " points");
		sendMessage("");
		scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
			public void run() {
				reset();
			}
		}, 80L);
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
				this.signUpdate();
				sendMessage(langManager.getTranslation("starting-countdown"));
			}
		}

		points.put(p.getUniqueId(), 0);

		p.getInventory().clear();

		p.setScoreboard(board);

		return true;
	}

	public void leave(Player p) {
		p.teleport(QuickCraft.getConfigManager().getMainLobby());
		p.sendMessage(QuickCraft.PREFIX + langManager.getTranslation("left"));
		players.remove(p.getUniqueId());
		p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		p.getInventory().clear();

		this.signUpdate();

		if (players.size() < minPlayers && state == GameState.STARTING) {
			state = GameState.WAITING;

			sendMessage(langManager.getTranslation("not-enough-players"));

			for (String score : board.getEntries()) {
				board.resetScores(score);
			}

			Score filler1 = obj.getScore(ChatColor.GRAY + "");
			filler1.setScore(3);

			Score status = obj.getScore(langManager.getTranslation("state-waiting-extended"));
			status.setScore(2);

			Score filler2 = obj.getScore("");
			filler2.setScore(1);

			Score serverName = obj.getScore(
					ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
			serverName.setScore(0);
		}

		if (players.size() == 0) {
			reset();
		}
	}

	public void reset() {
		this.state = GameState.RESETTING;
		this.signUpdate();
		for (int i = 0; i < players.size(); i++) {
			Player p = Bukkit.getPlayer(players.get(i));
			p.teleport(configManager.getMainLobby());
			p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			p.getInventory().clear();
			;
		}
		countdown = 30;
		players.clear();

		Score filler1 = obj.getScore(ChatColor.GRAY + "");
		filler1.setScore(3);

		Score status = obj.getScore(langManager.getTranslation("state-waiting-extended"));
		status.setScore(2);

		Score filler2 = obj.getScore("");
		filler2.setScore(1);

		Score serverName = obj.getScore(ChatColor.translateAlternateColorCodes('&', configManager.getConfigFile().getString("servername")));
		serverName.setScore(0);

		crafted.clear();
		points.clear();
		mat = ItemUtils.getRandomMaterial();
		round = 1;
		this.state = GameState.WAITING;
		this.signUpdate();
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
	
	public void giveItem(ItemStack item) {
		for(UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.getInventory().addItem(item);
		}
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public int getRounds() {
		return rounds;
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

	public String getName() {
		return name;
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
