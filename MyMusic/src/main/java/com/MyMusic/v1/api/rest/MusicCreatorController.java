package com.MyMusic.v1.api.rest;

import com.MyMusic.v1.api.rest.models.MusicCreatorDto;
import com.MyMusic.v1.api.rest.models.MusicCreatorInput;
import com.MyMusic.v1.api.rest.models.SongDto;
import com.MyMusic.v1.api.rest.models.SongInput;
import com.MyMusic.v1.auth.MyUser;
import com.MyMusic.v1.services.MusicCreatorService;
import com.MyMusic.v1.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/creator")
@SecurityRequirement(name = "bearerAuth")
public class MusicCreatorController {

    @Resource
    private MusicCreatorService creatorService;
    @Resource
    private SongService songService;

//    @GetMapping(value="/list")
//    public List<MusicCreatorDto> listCreators(@RequestParam Integer lastShowedCreator, @RequestParam Integer countCreators) {
//        return creatorService.listCreators(lastShowedCreator,countCreators)
//                .stream()
//                .map(Mappers::fromCreator)
//                .collect(Collectors.toList());
//    }
//
    @GetMapping(value="/list/songs")
    public List<SongDto> listCreatorSong(@AuthenticationPrincipal MyUser user, @RequestParam("lastShowedSong") Integer lastShowedCreator, @RequestParam("countSongs") Integer countCreators) {
        return creatorService.listCreatorSongs(user.id, lastShowedCreator, countCreators)
                .stream()
                .map(Mappers::fromSong)
                .collect(Collectors.toList());
    }

    @GetMapping
    public MusicCreatorDto getCreator(@AuthenticationPrincipal MyUser user) {
        return Mappers.fromCreator(creatorService.getCreator(user.id));
    }

    @PostMapping
    @CrossOrigin(value = "*")
    public SongDto createSong(@RequestBody SongInput song, @AuthenticationPrincipal MyUser user) {
        return Mappers.fromSong(songService.createSong(song.name,song.url, user.id, song.price));
    }

    @DeleteMapping
    public void deleteCreator(@AuthenticationPrincipal MyUser user) {
        creatorService.deleteCreator(user.id);
    }
}

