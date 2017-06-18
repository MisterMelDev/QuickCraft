package nl.mistermel.quickcraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.mistermel.quickcraft.utils.ArenaManager;

public class Events implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(ArenaManager.isInGame(e.getPlayer())) {
			ArenaManager.getArena(e.getPlayer()).leave(e.getPlayer());
		}
	}
	
}
