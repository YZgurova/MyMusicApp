package com.MyMusic.v1.beans;

import com.MyMusic.v1.payments.StripeChargeService;
import com.MyMusic.v1.repositories.*;
import com.MyMusic.v1.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@ComponentScan
public class CoreBeans {
    @Bean
    public UserService userService(UserRepository repository) {
        return new UserService(repository);
    }

    @Bean
    public MusicCreatorService creatorService(TransactionTemplate txTemplate, MusicCreatorRepository repository, WalletRepository walletRepository) {
        return new MusicCreatorService(txTemplate, repository, walletRepository);
    }

    @Bean
    public SongService songService(SongRepository repository) {
        return new SongService(repository);
    }
    @Bean
    public WalletService walletService(WalletRepository walletRepository, SongRepository songRepository) {
        return new WalletService(walletRepository, songRepository);
    }
    @Bean
    public TransactionsService transactionsService(TransactionsRepository transactionsRepository) {
        return  new TransactionsService(transactionsRepository);
    }

    @Bean
    public AuthService authService(CredentialsRepository credentialsRepository, UserRepository userRepository, TransactionTemplate transactionTemplate) {
        return new AuthService(credentialsRepository, userRepository, transactionTemplate);
    }

    @Bean
    public AmazonClientService amazonClientService() {
        return new AmazonClientService();
    }

    @Bean
    public StripeChargeService paymentService() {
        return new StripeChargeService();
    }
}
