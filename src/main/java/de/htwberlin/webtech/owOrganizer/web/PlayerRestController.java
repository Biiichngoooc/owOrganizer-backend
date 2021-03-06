package de.htwberlin.webtech.owOrganizer.web;

import de.htwberlin.webtech.owOrganizer.service.PlayerService;
import de.htwberlin.webtech.owOrganizer.web.api.Player;
import de.htwberlin.webtech.owOrganizer.web.api.PlayerManipulationRequest;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class PlayerRestController {
    private final PlayerService playerService;

    public PlayerRestController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // team binding
    @GetMapping(path = "/api/v1/players/unbound")
    public ResponseEntity<List<Player>> fetchUnboundPlayers() {
        return ResponseEntity.ok(playerService.findAllUnbound());
    }

    @GetMapping(path = "/api/v1/players/bound/{teamId}")
    public ResponseEntity<List<Player>> fetchBoundPlayers(@PathVariable Integer teamId) {
        return ResponseEntity.ok(playerService.findAllBoundToTeam(teamId));
    }

    @PutMapping(path = "/api/v1/players/{id}/bind/{teamId}")
    public ResponseEntity<Player> bindPlayerToTeam(@PathVariable Integer id, @PathVariable Integer teamId) throws NotFoundException {
        var player = playerService.bindToTeam(id, teamId);
        return player != null ? ResponseEntity.ok(player) : ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/api/v1/players/{id}/unbind/{teamId}")
    public ResponseEntity<Player> unbindPlayerToTeam(@PathVariable Integer id, @PathVariable Integer teamId) throws NotFoundException {
        var player = playerService.unbindFromTeam(id, teamId);
        return player != null ? ResponseEntity.ok(player) : ResponseEntity.notFound().build();
    }

    // player methods
    @GetMapping(path = "/api/v1/players")
    public ResponseEntity<List<Player>> fetchPlayers() {
        return ResponseEntity.ok(playerService.findAll());
    }

    @GetMapping(path = "/api/v1/players/{id}")
    public ResponseEntity<Player> fetchPlayerById(@PathVariable Integer id) {
        var player = playerService.findById(id);
        return player != null ? ResponseEntity.ok(player) : ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/api/v1/players")
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody PlayerManipulationRequest request) throws URISyntaxException {
        var player = playerService.create(request);
        URI uri = new URI("/api/v1/players/" + player.getId());
        return ResponseEntity
                .created(uri)
                .header("Access-Controller-Expose-Headers", "Location")
                .build();
    }

    @PutMapping(path = "/api/v1/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Integer id, @RequestBody PlayerManipulationRequest request) {
        var player = playerService.update(id, request);
        return player != null ? ResponseEntity.ok(player) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/api/v1/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Integer id) {
        boolean successful = playerService.deleteById(id);
        return successful ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}


