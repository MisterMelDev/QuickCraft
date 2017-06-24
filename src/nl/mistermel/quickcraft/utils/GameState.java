package nl.mistermel.quickcraft.utils;

import nl.mistermel.quickcraft.QuickCraft;

public enum GameState {
	
	WAITING("state-waiting", true), STARTING("state-starting", true), IN_GAME("state-game", false), RESETTING("state-reset", false);
	
	private boolean joinable;
	private String langKey;
	
	GameState(String langKey, boolean joinable) {
		this.joinable = joinable;
		this.langKey = langKey;
	}
	
	public boolean isJoinable() {
		return joinable;
	}
	
	public String getDisplayText() {
		return QuickCraft.getLanguageManager().getTranslation(langKey);
	}
}
