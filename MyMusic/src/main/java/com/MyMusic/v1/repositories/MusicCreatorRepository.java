package com.MyMusic.v1.repositories;

import com.MyMusic.v1.repositories.entities.MusicCreatorEntity;
import com.MyMusic.v1.repositories.entities.SongEntity;

import java.util.List;

public interface MusicCreatorRepository {
    void createCreator(int userId);

    MusicCreatorEntity getCreator(int id);

    List<SongEntity> listCreatorSongs(int id, int lastShowedSong, int countSongs);
    List<MusicCreatorEntity> listCreator(int lastShowedCreatorId, int countCreators);

    void deleteCreator(int id);
}
