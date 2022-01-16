package com.luizalabs.api.parser;

import com.luizalabs.api.entity.Game;
import com.luizalabs.api.entity.GameKill;
import com.luizalabs.api.model.http.request.GameCreateRequest;
import com.luizalabs.api.model.http.request.GameKillDetails;
import com.luizalabs.api.model.http.response.GameDetailsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class GamesParser {

	public static Game parse(GameCreateRequest request) {
		return new Game()
				.setPlayers(request.getPlayers())
				.setTotalKills(request.getTotalKills());
	}

	public static List<GameDetailsResponse> parse(List<Game> games) {
		return games.stream().map(GamesParser::parse).collect(toList());

	}

	public static GameDetailsResponse parse(Game game) {
		return new GameDetailsResponse()
				.setKills(parseKills(game.getKills()))
				.setPlayers(game.getPlayers())
				.setTotalKill(game.getTotalKills())
				.setId(game.getId());
	}

	public static List<GameKill> parseKills(List<GameKillDetails> gameKills, Game game) {
		return gameKills.stream()
				.map(gameKill -> new GameKill()
						.setKills(gameKill.getKills())
						.setPlayer(gameKill.getPlayer())
						.setGame(game)
				).collect(toList());
	}

	public static Map<String, Long> parseKills(List<GameKill> gameKills) {
		Map<String, Long> result = new HashMap<>();

		gameKills.stream().forEach(gameKill -> {
			result.put(gameKill.getPlayer(), gameKill.getKills());
		});

		return result;
	}
}
