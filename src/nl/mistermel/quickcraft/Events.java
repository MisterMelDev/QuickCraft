package nl.mistermel.quickcraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.mistermel.quickcraft.utils.ArenaManager;

public class Events implements Listener {
	
	private ArenaManager arenaManager;
	
	public Events() {
		this.arenaManager = QuickCraft.getArenaManager();
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(arenaManager.isInGame(e.getPlayer())) {
			arenaManager.getArena(e.getPlayer()).leave(e.getPlayer());
		}
	}
	
}
