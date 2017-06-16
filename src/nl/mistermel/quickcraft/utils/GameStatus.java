package nl.mistermel.quickcraft.utils;

public enum GameStatus {
	
	IN_LOBBY("In Lobby", true), IN_GAME("In Game", false);
	
	private String displayText;
	private boolean joinable;
	
	GameStatus(String displayText, boolean joinable) {
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
