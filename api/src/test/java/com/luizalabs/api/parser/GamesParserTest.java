package com.luizalabs.api.parser;

import com.luizalabs.api.entity.Game;
import com.luizalabs.api.entity.GameKill;
import com.luizalabs.api.model.http.request.GameCreateRequest;
import com.luizalabs.api.model.http.request.GameKillDetails;
import com.luizalabs.api.model.http.response.GameDetailsResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GamesParserTest {

	@Test
	public void shouldParseToGame() {
		GameCreateRequest request = new GameCreateRequest()
			.setTotalKills(13L)
			.setPlayers(Arrays.asList("Isgalamido", "Dono da Bola", "Mocinha"))
			.setKills(Collections.singletonList(new GameKillDetails().setKills(10L).setPlayer("Isgalamido")));

		Game result = GamesParser.parse(request);

		assert(result.getTotalKills().equals(13L));
		assert(result.getPlayers().size() == 3);
		assert(result.getPlayers().contains("Isgalamido"));
		assert(result.getPlayers().contains("Dono da Bola"));
		assert(result.getPlayers().contains("Mocinha"));

	}

	@Test
	public void shouldParseToGameDetailsResponseList() {
		Game game = new Game()
			.setPlayers(Collections.singletonList("Isgalamido"))
			.setTotalKills(13L)
			.setId(1L);

		GameKill gameKill = new GameKill()
			.setKills(13L)
			.setPlayer("Isgalamido")
			.setGame(game)
			.setId(1L);

		game.setKills(Collections.singletonList(gameKill));

		List<Game> gameList = Collections.singletonList(game);
		List<GameDetailsResponse> result = GamesParser.parse(gameList);

		assert(result.size() == 1);
		assert(result.get(0).getPlayers().contains("Isgalamido"));
		assert(result.get(0).getTotalKills().equals(13L));
		assert(result.get(0).getId().equals(1L));
		assert(!result.get(0).getKills().isEmpty());
	}


	@Test
	public void shouldParseToGameKillList() {
		Game game = new Game()
			.setPlayers(Collections.singletonList("Isgalamido"))
			.setTotalKills(13L)
			.setId(1L);

		GameKill gameKill = new GameKill()
			.setKills(13L)
			.setPlayer("Isgalamido")
			.setGame(game)
			.setId(1L);

		game.setKills(Collections.singletonList(gameKill));

		GameKillDetails details = new GameKillDetails()
				.setPlayer("Isgalamido")
				.setKills(13L);

		List<GameKillDetails> detailsList = Collections.singletonList(details);

		List<GameKill> result = GamesParser.parseKills(detailsList, game);

		assert(result.size() == 1);
		assert (result.get(0).getPlayer().equals("Isgalamido"));
		assert (result.get(0).getKills().equals(13L));
		assert (gameKill.getGame().getId().equals(1L));
		assert (gameKill.getId().equals(1L));
	}



}
