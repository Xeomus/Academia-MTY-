package com.esteban.ligamxmongodb.controller;

import com.esteban.ligamxmongodb.model.Player;
import com.esteban.ligamxmongodb.model.Team;
import com.esteban.ligamxmongodb.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable String id) {
        return teamService.getTeamById(id)
                .map(team -> ResponseEntity.ok(team))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Team> createTeam(
            @Valid @RequestBody Team team) {

        Team savedTeam = teamService.saveTeam(team);

        return ResponseEntity.ok(savedTeam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable String id) {

        boolean deleted = teamService.deleteTeam(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public List<Team> createTeams(@RequestBody List<Team> teams) {
        return teamService.createTeams(teams);
    }

    @PostMapping("/{teamId}/players")
    public ResponseEntity<Team> addPlayerToTeam(
            @PathVariable String teamId,
            @Valid @RequestBody Player player) {

        return teamService.addPlayerToTeam(teamId, player)
                .map(team -> ResponseEntity.ok(team))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{teamId}/players")
    public ResponseEntity<List<Player>> getPlayersByTeamId(
            @PathVariable String teamId) {

        return teamService.getPlayersByTeamId(teamId)
                .map(players -> ResponseEntity.ok(players))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<Player> updatePlayer(
            @PathVariable String teamId,
            @PathVariable String playerId,
            @Valid @RequestBody Player updatedPlayer) {

        return teamService.updatePlayer(teamId, playerId, updatedPlayer)
                .map(player -> ResponseEntity.ok(player))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<Void> deletePlayer(
            @PathVariable String teamId,
            @PathVariable String playerId) {

        boolean deleted = teamService.deletePlayer(teamId, playerId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}