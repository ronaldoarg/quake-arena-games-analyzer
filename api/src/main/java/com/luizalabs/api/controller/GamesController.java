package com.luizalabs.api.controller;

import com.luizalabs.api.entity.Game;
import com.luizalabs.api.model.http.request.GameCreateRequest;
import com.luizalabs.api.model.http.response.GameDetailsResponse;
import com.luizalabs.api.model.http.response.GameResponse;
import com.luizalabs.api.parser.GamesParser;
import com.luizalabs.api.repository.GameKillRepository;
import com.luizalabs.api.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/games")
public class GamesController {

	@Autowired
	private GameRepository gameRepository;
	private GameKillRepository gameKillRepository;

	public GamesController(GameRepository gameRepository, GameKillRepository gameKillRepository) {
		this.gameRepository = gameRepository;
		this.gameKillRepository = gameKillRepository;
	}

	@PostMapping
	@ResponseStatus(CREATED)
	public void save(@RequestBody GameCreateRequest request) {
		Game savedGame = gameRepository.save(GamesParser.parse(request));
		GamesParser.parseKills(request.getKills(), savedGame).forEach(gameKillRepository::save);
    }

	@GetMapping
	@ResponseStatus(OK)
	public GameResponse list() {
		return new GameResponse().setGames(GamesParser.parse(gameRepository.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GameDetailsResponse> find(@PathVariable Long id) {
		Game game = gameRepository.findById(id).orElse(null);

		if (game != null) {
			return new ResponseEntity(game, OK);
		} else {
			return new ResponseEntity<>(NOT_FOUND);
		}
	}
}
