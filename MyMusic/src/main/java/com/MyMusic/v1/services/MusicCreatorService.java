package com.MyMusic.v1.services;

import com.MyMusic.v1.repositories.MusicCreatorRepository;
import com.MyMusic.v1.repositories.WalletRepository;
import com.MyMusic.v1.services.models.MusicCreator;
import com.MyMusic.v1.services.models.Song;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class MusicCreatorService {
    private final TransactionTemplate transactionTemplate;
    private final MusicCreatorRepository musicCreatorRepository;
    private final WalletRepository walletRepository;

    public MusicCreatorService(TransactionTemplate transactionTemplate, MusicCreatorRepository repository, WalletRepository walletRepository) {
        this.transactionTemplate = transactionTemplate;
        this.musicCreatorRepository = repository;
        this.walletRepository = walletRepository;
    }

    public void createCreator(int userId) {
        try {

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    musicCreatorRepository.createCreator(userId);
                    walletRepository.createWallet(userId);
                }
            });
        } catch (TransactionException e) {
            System.out.println("Add creator: "+e);
            throw new RuntimeException(e);
        }
    }

    public MusicCreator getCreator(int id) {
        return Mappers.fromMusicCreatorEntity(musicCreatorRepository.getCreator(id));
    }

    public List<Song> listCreatorSongs(int authorId, int lastShowedSong, int countSongs) {
        return musicCreatorRepository.listCreatorSongs(authorId, lastShowedSong, countSongs)
                .stream()
                .map(Mappers::fromSongEntity)
                .collect(Collectors.toList());
    }

    public List<MusicCreator> listCreators(int lastShowedCreatorId, int countCreators) {
        return musicCreatorRepository.listCreator(lastShowedCreatorId, countCreators)
                .stream()
                .map(Mappers::fromMusicCreatorEntity)
                .collect(Collectors.toList());
    }

    public void deleteCreator(int id) {
        musicCreatorRepository.deleteCreator(id);
    }
}
