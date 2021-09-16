package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PageFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "rest/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<Player>> players(@RequestParam Map<String, String> param){
        if (param.isEmpty())
        {
            return new ResponseEntity<>(playerService.players(PageRequest.of(0,3)), HttpStatus.OK);
        }
        return new ResponseEntity<>(playerService.findByParams(param), HttpStatus.OK);
    }
    @PostMapping
    public @ResponseBody
    ResponseEntity<Player> add(@RequestBody Map<String, String> params) {
        Player player = playerService.add(params);
        if (player == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else return new ResponseEntity<>(player, HttpStatus.OK);
    }
    @GetMapping(path = "count")
    public Integer player(@RequestParam Map<String, String> params){
        if (params.isEmpty()) {
            return playerService.allPlayers().size();
        } else {
            return playerService.count(params);
        }
    }
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Player> getPlayer(
            @PathVariable Long id
    ){
        try {
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (!playerService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Player>(playerService.findById(id), HttpStatus.OK);
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Map<String, String> params){
        ResponseEntity<Player> badResponse = new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Player> nfResponse = new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        if (id <= 0 || !playerService.isParamsValid(params)) {
            return badResponse;
        }
        params.putIfAbsent("id", null);
        Player result = playerService.updatePlayer(id, params);
        if (result == null) {
            return nfResponse;
        } else {
            return new ResponseEntity<Player>(result, HttpStatus.OK);
        }
    }
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Player> deleteShip(@PathVariable Long id){
        ResponseEntity<Player> okResponse = new ResponseEntity<Player>(HttpStatus.OK);
        ResponseEntity<Player> badResponse = new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Player> nfResponse = new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        try {
            if (id <= 0) return badResponse;
            String result = playerService.deleteById(id);
            if (result == null) return badResponse;
            if ("404".equals(result)) return nfResponse;
            if ("200".equals(result)) return okResponse;
        } catch (NullPointerException | IllegalArgumentException e) {
            return badResponse;
        }
        return badResponse;
    }
}
