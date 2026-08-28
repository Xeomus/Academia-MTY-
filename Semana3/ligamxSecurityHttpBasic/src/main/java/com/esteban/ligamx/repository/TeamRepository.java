package com.esteban.ligamx.repository;

import com.esteban.ligamx.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository  extends JpaRepository<Team, Long> {

}
