package nl.mistermel.quickcraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import net.md_5.bungee.api.ChatColor;
import nl.mistermel.quickcraft.utils.ArenaManager;

public class Events implements Listener {
	
	private ArenaManager arenaManager;
	
	public Events() {
		arenaManager = QuickCraft.getArenaManager();
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if(!e.getLine(0).equals("[QuickCraft]")) return;
		Player p = e.getPlayer();
		if(!p.hasPermission("quickcraft.admin")) {
			p.sendMessage(QuickCraft.PREFIX + ChatColor.RED + "You do not have permission to create QuickCraft signs!");
			e.setCancelled(true);
			return;
		}
		if(e.getLine(1) == null) {
			p.sendMessage(QuickCraft.PREFIX + ChatColor.RED + "Put one of the following on the second line: Join, Leave");
			e.setCancelled(true);
			return;
		}
		if(e.getLine(1).equals("Join")) {
			if(e.getLine(2) == null) {
				p.sendMessage(QuickCraft.PREFIX + ChatColor.RED + "Please enter a arena name on the third line.");
				e.setCancelled(true);
				return;
			}
			if(!arenaManager.exists(e.getLine(2))) {
				p.sendMessage(QuickCraft.PREFIX + ChatColor.RED + "That arena doesnt exist.");
				e.setCancelled(true);
				return;
			}
			if(!arenaManager.isEnabled(e.getLine(2))) {
				p.sendMessage(QuickCraft.PREFIX + ChatColor.RED + "That arena isnt enabled.");
				e.setCancelled(true);
				return;
			}
			e.setLine(0, ChatColor.AQUA + "QuickCraft");
			e.setLine(1, ChatColor.GOLD + "Join");
			e.setLine(2, ChatColor.GOLD + e.getLine(2));
			p.sendMessage(QuickCraft.PREFIX + ChatColor.GOLD + "Join sign created.");
		}
		if(e.getLine(1).equals("Leave")) {
			p.sendMessage(QuickCraft.PREFIX + ChatColor.GOLD + "Leave sign created.");
		}
	}
	
}
