package nl.mistermel.quickcraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
			e.getPlayer().sendMessage(QuickCraft.PREFIX + QuickCraft.getLanguageManager().getTranslation("cant-do-this"));
		}
	}
	
	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent e) {
		if(ArenaManager.isInGame(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerBuild(BlockPlaceEvent e) {
		if(ArenaManager.isInGame(e.getPlayer())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(QuickCraft.PREFIX + QuickCraft.getLanguageManager().getTranslation("cant-do-this"));
		}
	}
	
	@EventHandler
	public void onPlayerBrak(BlockBreakEvent e) {
		if(ArenaManager.isInGame(e.getPlayer())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(QuickCraft.PREFIX + QuickCraft.getLanguageManager().getTranslation("cant-do-this"));
		}
	}
	@EventHandler
	public void onDamager(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			if(ArenaManager.isInGame((Player) e.getEntity())) {
				e.setCancelled(true);
			}
		}
	}
	
}
