package com.esteban.ligamx.repository;

import com.esteban.ligamx.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
}
