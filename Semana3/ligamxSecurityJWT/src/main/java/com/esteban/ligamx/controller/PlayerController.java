package com.esteban.ligamx.controller;

import com.esteban.ligamx.model.Player;
import com.esteban.ligamx.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .map(player -> ResponseEntity.ok(player))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/teams/{teamId}/players")
    public List<Player> getPlayersByTeam(@PathVariable Long teamId) {
        return playerService.getPlayersByTeam(teamId);
    }

    @PostMapping("/teams/{teamId}/players")
    public ResponseEntity<Player> createPlayer(
            @PathVariable Long teamId,
            @Valid @RequestBody Player player) {

        return playerService.createPlayerForTeam(teamId, player)
                .map(savedPlayer -> ResponseEntity.ok(savedPlayer))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(
            @PathVariable Long id,
            @Valid @RequestBody Player updatedPlayer) {

        return playerService.updatePlayer(id, updatedPlayer)
                .map(player -> ResponseEntity.ok(player))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {

        boolean deleted = playerService.deletePlayer(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}