package com.luizalabs.api.model.http.request;

public class GameKillDetails {

	private String player;
	private Long kills;

	public GameKillDetails() {}

	public String getPlayer() {
		return player;
	}

	public GameKillDetails setPlayer(String player) {
		this.player = player;
		return this;
	}

	public Long getKills() {
		return kills;
	}

	public GameKillDetails setKills(Long kills) {
		this.kills = kills;
		return this;
	}
}
