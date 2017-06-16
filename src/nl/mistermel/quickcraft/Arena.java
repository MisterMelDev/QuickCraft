package nl.mistermel.quickcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import nl.mistermel.quickcraft.utils.GameState;

public class Arena {
	
	private Location lobbyLoc;
	private Location spawnLoc;
	private GameState state;
	
	private List<UUID> players = new ArrayList<UUID>();
	
	private boolean enabled;
	
	public Arena(Location lobbyLoc, Location spawnLoc, boolean enabled) {
		this.lobbyLoc = lobbyLoc;
		this.spawnLoc = spawnLoc;
		this.enabled = enabled;
		
		this.state = GameState.IN_LOBBY;
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
		players.add(p.getUniqueId());
		return true;
	}
	
	public boolean isEnabled() {
		return enabled;
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
