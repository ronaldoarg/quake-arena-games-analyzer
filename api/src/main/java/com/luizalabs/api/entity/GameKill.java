package com.luizalabs.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Transactional
public class GameKill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIgnore
	private Game game;

	private String player;
	private Long kills;

	public Long getId() {
		return id;
	}

	public GameKill setId(Long id) {
		this.id = id;
		return this;
	}

	public Game getGame() {
		return game;
	}

	public GameKill setGame(Game game) {
		this.game = game;
		return this;
	}

	public String getPlayer() {
		return player;
	}

	public GameKill setPlayer(String player) {
		this.player = player;
		return this;
	}

	public Long getKills() {
		return kills;
	}

	public GameKill setKills(Long kills) {
		this.kills = kills;
		return this;
	}
}
