package com.MyMusic.v1.services;

import com.MyMusic.v1.repositories.SongRepository;
import com.MyMusic.v1.services.models.Song;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class SongService {
    private final SongRepository repository;

    public SongService(SongRepository repository) {
        this.repository = repository;
    }

    public Song createSong(String name, String url, int authorId, BigDecimal price) {
        return Mappers.fromSongEntity(repository.createSong(name, url, authorId, price));
    }

    public Song addLovedSong(int userId, int songId) {
        Song song = getSong(songId);
        repository.updateSongVotes(songId, song.votes+1);
        return Mappers.fromSongEntity(repository.addToLovedSongs(userId,songId));
    }

    public void removedLovedSong(int userId, int songId) {
        Song song = getSong(songId);
        repository.updateSongVotes(songId, song.votes-1);
        repository.removedLovedSongs(userId,songId);
    }

    public Song addToBoughtSong(int userId, int songId) {
        return Mappers.fromSongEntity(repository.addToBoughtSongs(userId,songId));
    }
    public Song getSong(int songId) {
        return Mappers.fromSongEntity(repository.getSong(songId));
    }

//    public Boolean getIfSongLoved(int userId, int songId) {
//        return repository.getIfSongLoved(userId, songId);
//    }

    public List<Song> listSongs(int lastShowedSong, int songCount) {
        return repository.listAllSong(lastShowedSong, songCount)
                .stream()
                .map(Mappers::fromSongEntity)
                .collect(Collectors.toList());
    }

    public List<Song> listLovedSongs(int userId) {
        return repository.listLovedSongs(userId)
                .stream()
                .map(Mappers::fromSongEntity)
                .collect(Collectors.toList());
    }

    public List<Song> listBoughtSongs(int userId) {
        return repository.listBoughtSongs(userId)
                .stream()
                .map(Mappers::fromSongEntity)
                .collect(Collectors.toList());
    }

    public List<Song> listRecommendedSongs(int userId, int lastShowedSong, int countSongs) {
        return repository.listRecommendedSongs(userId, lastShowedSong, countSongs)
                .stream()
                .map(Mappers::fromSongEntity)
                .collect(Collectors.toList());
    }

    public void deleteSong(int id) {
        repository.deleteSong(id);
    }
}
