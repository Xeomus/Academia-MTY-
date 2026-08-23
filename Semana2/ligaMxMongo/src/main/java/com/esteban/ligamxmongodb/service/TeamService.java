package com.esteban.ligamxmongodb.service;

import com.esteban.ligamxmongodb.model.Player;
import com.esteban.ligamxmongodb.model.Team;
import com.esteban.ligamxmongodb.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                    player.setId(UUID.randomUUID().toString());

                    team.addPlayer(player);

                    return teamRepository.save(team);
                });
    }
    public Optional<List<Player>> getPlayersByTeamId(String teamId) {
        return teamRepository.findById(teamId)
                .map(team -> team.getPlayers());
    }

    public Optional<Player> updatePlayer(
            String teamId,
            String playerId,
            Player updatedPlayer) {

        return teamRepository.findById(teamId)
                .flatMap(team -> team.getPlayers().stream()
                        .filter(player -> playerId.equals(player.getId()))
                        .findFirst()
                        .map(player -> {
                            player.setName(updatedPlayer.getName());
                            player.setNumber(updatedPlayer.getNumber());
                            player.setPosition(updatedPlayer.getPosition());
                            player.setNationality(updatedPlayer.getNationality());

                            teamRepository.save(team);

                            return player;
                        }));
    }
}