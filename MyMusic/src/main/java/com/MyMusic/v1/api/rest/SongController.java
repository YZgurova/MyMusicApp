package com.MyMusic.v1.api.rest;

import com.MyMusic.v1.api.rest.models.AddLikeSongInput;
import com.MyMusic.v1.api.rest.models.SongDto;
import com.MyMusic.v1.api.rest.models.SongInput;
import com.MyMusic.v1.auth.MyUser;
import com.MyMusic.v1.services.SongService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/song")
@SecurityRequirement(name = "bearerAuth")
public class SongController {
    @Resource
    private SongService songService;

    @GetMapping(value = "/user/loved")
    public List<SongDto> listLovedSongs(@AuthenticationPrincipal MyUser user) {
        return songService.listLovedSongs(user.id)
                .stream()
                .map(Mappers::fromSong)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/user/bought")
    public List<SongDto> listBoughtSongs(@AuthenticationPrincipal MyUser user) {
        return songService.listBoughtSongs(user.id)
                .stream()
                .map(Mappers::fromSong)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/user/list")
    public List<SongDto> listSongs(@AuthenticationPrincipal MyUser user, @RequestParam Integer lastShowedSong, @RequestParam Integer countSongs) {
        return songService.listRecommendedSongs(user.id,lastShowedSong, countSongs )
                .stream()
                .map(Mappers::fromSong)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{songId}")
    public SongDto getSong(@PathVariable Integer songId) {
        return Mappers.fromSong(songService.getSong(songId));
    }


    @PostMapping(value="/{songId}/like") // song/1/like
    @CrossOrigin(value = "*")
    public SongDto addLovedSong(@AuthenticationPrincipal MyUser user, @PathVariable("songId") Integer songId) {
        return Mappers.fromSong(songService.addLovedSong(user.id, songId));
    }

    @DeleteMapping(value="/{songId}/unlike")
    @CrossOrigin(value = "*")
    public void removedLovedSong(@AuthenticationPrincipal MyUser user, @PathVariable int songId) {
        songService.removedLovedSong(user.id, songId);
    }

    @PostMapping(value="/{songId}/bought")
    @CrossOrigin(value = "*")
    public SongDto addBoughtSong(@AuthenticationPrincipal MyUser user, @PathVariable int songId) {
        return Mappers.fromSong(songService.addToBoughtSong(user.id, songId));
    }

    @DeleteMapping(value = "/{id}")
    @CrossOrigin(value = "*")
    public void deleteSong(@PathVariable Integer id) {
        songService.deleteSong(id);
    }



}
