package com.luizalabs.api.model.http.response;

import java.util.List;

public class GameResponse {

	private List<GameDetailsResponse> games;

	public List<GameDetailsResponse> getGames() {
		return games;
	}

	public GameResponse setGames(List<GameDetailsResponse> games) {
		this.games = games;
		return this;
	}
}
