package com.luizalabs.api.model.http.response;

import java.util.List;
import java.util.Map;

public class GameDetailsResponse {
	private Long id;
	private Long totalKills;
	private List<String> players;
	private Map<String, Long> kills;

	public GameDetailsResponse() {}

	public Long getId() {
		return id;
	}

	public GameDetailsResponse setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getTotalKills() {
		return totalKills;
	}

	public GameDetailsResponse setTotalKill(Long totalKills) {
		this.totalKills = totalKills;
		return this;
	}

	public List<String> getPlayers() {
		return players;
	}

	public GameDetailsResponse setPlayers(List<String> players) {
		this.players = players;
		return this;
	}

	public Map<String, Long> getKills() {
		return kills;
	}

	public GameDetailsResponse setKills(Map<String, Long> kills) {
		this.kills = kills;
		return this;
	}
}
