package com.luizalabs.api.repository;

import com.luizalabs.api.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {}
