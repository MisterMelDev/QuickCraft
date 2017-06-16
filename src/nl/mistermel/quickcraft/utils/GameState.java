package nl.mistermel.quickcraft.utils;

public enum GameState {
	
	IN_LOBBY("In Lobby", true), IN_GAME("In Game", false), RESETTING("Resetting", false);
	
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
