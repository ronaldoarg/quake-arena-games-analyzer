package com.luizalabs.api.entity;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Transactional
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long totalKills;

	@ElementCollection
	private List<String> players;

	@OneToMany(mappedBy = "game")
	private List<GameKill> kills;

	public Long getId() {
		return id;
	}

	public Game setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getTotalKills() {
		return totalKills;
	}

	public Game setTotalKills(Long totalKills) {
		this.totalKills = totalKills;
		return this;
	}

	public List<String> getPlayers() {
		return players;
	}

	public Game setPlayers(List<String> players) {
		this.players = players;
		return this;
	}

	public List<GameKill> getKills() {
		return kills;
	}

	public Game setKills(List<GameKill> kills) {
		this.kills = kills;
		return this;
	}
}
