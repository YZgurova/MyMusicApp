package com.MyMusic.v1.repositories;

import com.MyMusic.v1.repositories.entities.SongEntity;

import java.math.BigDecimal;
import java.util.List;

public interface SongRepository {
    SongEntity createSong(String name, String url, int authorId, BigDecimal price);
    SongEntity addToBoughtSongs(int userId, int songId);
    SongEntity addToLovedSongs(int userId, int songId);
    void removedLovedSongs(int userId, int songId);
    void updateSongVotes(int songId, int votes);

    SongEntity getSong(int id);
    int getAutorId(int songId);

    List<SongEntity> listAllSong(int lastShowedSongId, int countSongs);

    List<SongEntity> listBoughtSongs(int userId);
    List<SongEntity> listLovedSongs(int userId);
    List<SongEntity> listRecommendedSongs(int userId, int lastShowedSong, int countSongs);

    void deleteSong(int id);
}
