package nl.mistermel.quickcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nl.mistermel.quickcraft.utils.GameState;

public class Arena {
	
	private Location lobbyLoc;
	private Location spawnLoc;
	private GameState state;
	
	private Material mat = QuickCraft.getArenaManager().getRandomMaterial();
	
	private int minPlayers = 2, maxPlayers = 10;
	
	private int countdown = 30;
	
	private List<UUID> players = new ArrayList<UUID>();
	
	private boolean enabled;
	
	public Arena(Location lobbyLoc, Location spawnLoc, boolean enabled) {
		this.lobbyLoc = lobbyLoc;
		this.spawnLoc = spawnLoc;
		this.enabled = enabled;
		
		this.state = GameState.WAITING;
	}
	
	public void tick() {
		if(state == GameState.STARTING) {
			countdown--;
			setExp(countdown);
			if(countdown <= 0) {
				state = GameState.IN_GAME;
				sendMessage(ChatColor.GOLD + "The game is starting!");
				sendMessage(ChatColor.GOLD + "Craft a " + mat.toString().toLowerCase());
				setExp(0);
				teleport(spawnLoc);
			}
		}
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for(UUID u : this.players) {
			players.add(Bukkit.getPlayer(u));
		}
		return players;
	}
	
	public boolean join(Player p) {
		if(!state.isJoinable()) return false;
		if(!enabled) return false;
		if(players.size() == maxPlayers) return false;
		players.add(p.getUniqueId());
		p.teleport(lobbyLoc);
		
		if(state == GameState.WAITING) {
			if(players.size() >= minPlayers) {
				state = GameState.STARTING;
				sendMessage(ChatColor.GOLD + "Starting countdown!");
			}
		}
		
		return true;
	}
	
	public void reset() {
		for(UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			leave(p);
		}
		countdown = 60;
		mat = QuickCraft.getArenaManager().getRandomMaterial();
	}
	
	public void sendMessage(String message) {
		for(UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.sendMessage(QuickCraft.PREFIX + message);
		}
	}
	
	public void setExp(int amount) {
		for(UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.setExp(amount);
		}
	}
	
	public void teleport(Location loc) {
		for(UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.teleport(loc);
		}
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
	
	public void leave(Player p) {
		players.remove(p.getUniqueId());
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
