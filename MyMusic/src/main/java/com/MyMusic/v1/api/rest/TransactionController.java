package com.MyMusic.v1.api.rest;

import com.MyMusic.v1.api.rest.models.TransactionDto;
import com.MyMusic.v1.auth.MyUser;
import com.MyMusic.v1.services.TransactionsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "api/transaction")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    TransactionsService service;

//    @PostMapping
//    public TransactionDto createTransaction(@AuthenticationPrincipal MyUser user, String transactionDescr, String status) {
//        return Mappers.fromTransaction(service.createTransaction(user.id, transactionDescr, status));
//    }

    @GetMapping
    public TransactionDto getTransaction(int transactionId) {
        return Mappers.fromTransaction(service.getTransaction(transactionId));
    }
}
