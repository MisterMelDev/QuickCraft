package nl.mistermel.quickcraft;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.mistermel.quickcraft.utils.ArenaManager;

public class Events implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(ArenaManager.isInGame(e.getPlayer())) {
			ArenaManager.getArena(e.getPlayer()).leave(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e) {
		if(ArenaManager.isInGame(e.getPlayer())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(QuickCraft.PREFIX + ChatColor.RED +
					"You can't drop items ingame!");
		}
	}
	
	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent e) {
		if(ArenaManager.isInGame(e.getPlayer())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(QuickCraft.PREFIX + ChatColor.RED +
					"You can't pickup items ingame!");
		}
	}
	
}
