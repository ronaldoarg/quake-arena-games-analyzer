package com.luizalabs.api.controller;

import com.luizalabs.api.model.http.request.GameCreateRequest;
import com.luizalabs.api.model.http.request.GameKillDetails;
import com.luizalabs.api.repository.GameRepository;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GamesController.class})
public class GamesControllerTest {

	@Autowired
	private MockMvc mock;

	@Ignore
	public void shouldCreateGame() throws Exception {

		URI uri = new URI("/games");

		GameCreateRequest request = new GameCreateRequest()
			.setTotalKills(13L)
			.setPlayers(Arrays.asList("Isgalamido", "Dono da Bola", "Mocinha"))
			.setKills(Collections.singletonList(new GameKillDetails().setKills(10L).setPlayer("Isgalamido")));

		mock.perform(MockMvcRequestBuilders
				.post(uri)
				.content(request.toString())
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers
			.status()
			.isCreated()
		);

	}

}
