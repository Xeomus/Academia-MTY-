package com.esteban.ligamxmongodb.service;

import com.esteban.ligamxmongodb.model.Player;
import com.esteban.ligamxmongodb.model.Team;
import com.esteban.ligamxmongodb.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeamById(String id) {
        return teamRepository.findById(id);
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public boolean deleteTeam(String id) {
        if (!teamRepository.existsById(id)) {
            return false;
        }

        teamRepository.deleteById(id);
        return true;
    }

    public List<Team> createTeams(List<Team> teams) {
        return teamRepository.saveAll(teams);
    }

    public Optional<Team> addPlayerToTeam(String teamId, Player player) {
        return teamRepository.findById(teamId)
                .map(team -> {
                    team.addPlayer(player);
                    return teamRepository.save(team);
                });
    }

    public Optional<List<Player>> getPlayersByTeamId(String teamId) {
        return teamRepository.findById(teamId)
                .map(team -> team.getPlayers());
    }
}