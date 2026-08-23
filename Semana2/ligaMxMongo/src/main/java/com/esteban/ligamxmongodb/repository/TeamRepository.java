package com.esteban.ligamxmongodb.repository;

import com.esteban.ligamxmongodb.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
}