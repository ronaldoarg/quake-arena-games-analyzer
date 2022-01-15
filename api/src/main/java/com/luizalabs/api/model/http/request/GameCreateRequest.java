package com.luizalabs.api.model.http.request;

import java.util.List;
import java.util.Map;

public class GameCreateRequest {

	private Long totalKills;
	private List<String> players;
	private List<GameKillDetails> kills;

	public GameCreateRequest() {}

	public Long getTotalKills() {
		return totalKills;
	}

	public GameCreateRequest setTotalKills(Long totalKill) {
		this.totalKills = totalKill;
		return this;
	}

	public List<String> getPlayers() {
		return players;
	}

	public GameCreateRequest setPlayers(List<String> players) {
		this.players = players;
		return this;
	}

	public List<GameKillDetails> getKills() {
		return kills;
	}

	public GameCreateRequest setKills(List<GameKillDetails> kills) {
		this.kills = kills;
		return this;
	}
}
