package nl.mistermel.quickcraft.utils;

import org.bukkit.ChatColor;

public enum GameState {
	
	WAITING(ChatColor.GREEN + "Waiting", true), STARTING(ChatColor.GREEN + "Starting", true), IN_GAME(ChatColor.RED + "In Game", false), RESETTING(ChatColor.RED + "Resetting", false);
	
	private String displayText;
	private boolean joinable;
	
	GameState(String displayText, boolean joinable) {
		this.displayText = displayText;
		this.joinable = joinable;
	}
	
	public boolean isJoinable() {
		return joinable;
	}
	
	public String getDisplayText() {
		return displayText;
	}
}
