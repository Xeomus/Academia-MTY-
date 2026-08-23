package com.esteban.ligamx.service;

import com.esteban.ligamx.model.Player;
import com.esteban.ligamx.repository.PlayerRepository;
import com.esteban.ligamx.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(
            PlayerRepository playerRepository,
            TeamRepository teamRepository) {

        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public List<Player> getPlayersByTeam(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }

    public Optional<Player> createPlayerForTeam(Long teamId, Player player) {

        return teamRepository.findById(teamId)
                .map(team -> {
                    player.setTeam(team);
                    return playerRepository.save(player);
                });
    }

    public Optional<Player> updatePlayer(Long id, Player updatedPlayer) {
        return playerRepository.findById(id)
                .map(player -> {
                    player.setName(updatedPlayer.getName());
                    player.setNumber(updatedPlayer.getNumber());
                    player.setPosition(updatedPlayer.getPosition());
                    player.setNationality(updatedPlayer.getNationality());

                    return playerRepository.save(player);
                });
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public boolean deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            return false;
        }

        playerRepository.deleteById(id);
        return true;
    }
}
