package nl.mistermel.quickcraft.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import net.md_5.bungee.api.ChatColor;
import nl.mistermel.quickcraft.QuickCraft;

public class SignManager implements Listener {
	
	private ArenaManager arenaManager;
	
	public SignManager() {
		this.arenaManager = QuickCraft.getArenaManager();
	}
	
	public void updateSign(Location loc, String name) {
		if(loc.getBlock().getState() instanceof Sign) {
			Sign s = (Sign) loc.getBlock().getState();
			s.setLine(0, ChatColor.AQUA + "QuickCraft");
			s.setLine(2, ChatColor.GOLD + name);
			s.setLine(3, arenaManager.getArena(name).getState().getDisplayText());
			s.setLine(4, arenaManager.getArena(name).getAmountOfPlayers() + "/" + arenaManager.getArena(name).getMaxPlayers());
		}
 	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND && e.hasBlock()) {
			if(e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
				Sign s = (Sign) e.getClickedBlock().getState();
				if(s.getLine(0).equals(ChatColor.AQUA + "QuickCraft")) {
					if(s.getLine(1).equals(ChatColor.GOLD + "Join") && s.getLine(2) != null) {
						updateSign(e.getClickedBlock().getLocation(), s.getLine(2));
						e.getPlayer().performCommand("gc join " + s.getLine(2));
					}
					if(s.getLine(1).equals(ChatColor.RED + "Leave")) {
						e.getPlayer().performCommand("gc leave");
					}
				}
			}
		}
	}
	
}
