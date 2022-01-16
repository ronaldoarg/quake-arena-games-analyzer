package com.luizalabs.api.controller;

import com.luizalabs.api.entity.Game;
import com.luizalabs.api.entity.GameKill;
import com.luizalabs.api.repository.GameKillRepository;
import com.luizalabs.api.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GamesController.class})
@ComponentScan("com.luizalabs.api")
public class GamesControllerTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private GameKillRepository gameKillRepository;

	@Test
	public void shouldCreateGame() throws Exception {

		URI uri = new URI("/games");

		String json = "{\"totalKills\":11,\"players\":[\"Isgalamido\",\"Dono da Bola\",\"Mocinha\"],\"kills\":[{\"player\": \"Isgalamido\", \"kills\": 13}]}";

		mock.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers
			.status()
			.isCreated()
		);

	}

	@Test
	public void shouldFindGame() throws Exception {

		Game game = new Game()
				.setPlayers(Collections.singletonList("Isgalamido"))
				.setTotalKills(13L)
				.setId(1L);

		Game g = gameRepository.save(game);

		GameKill gameKill = new GameKill()
				.setKills(13L)
				.setPlayer("Isgalamido")
				.setGame(g);

		gameKillRepository.save(gameKill);

		URI uri = new URI("/games/1");

		mock.perform(MockMvcRequestBuilders
				.get(uri)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers
				.status()
				.isOk()
		);
	}

	@Test
	public void shouldListGame() throws Exception {

		URI uri = new URI("/games");

		mock.perform(MockMvcRequestBuilders
				.get(uri)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers
				.status()
				.isOk()
		);
	}

}
