package com.MyMusic.v1.api.rest;

import com.MyMusic.v1.api.rest.models.WalletDto;
import com.MyMusic.v1.auth.MyUser;
import com.MyMusic.v1.services.WalletService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.annotation.Resource;


@RestController
@RequestMapping(value = "api/wallet")
@SecurityRequirement(name = "bearerAuth")
public class WalletController {
    @Resource
    private WalletService service;

//    @PostMapping
//    public WalletDto createWallet(@AuthenticationPrincipal MyUser user) {
//        return Mappers.fromWallet(service.createWallet(user.id));
//    }

//    @PutMapping
//    public int addMoney(@AuthenticationPrincipal MyUser user, @RequestParam BigDecimal amount) {
//        return service.addMoney(user.id, amount);
//    }

    @GetMapping
    public WalletDto getWallet(@AuthenticationPrincipal MyUser user) {
        return Mappers.fromWallet(service.getWallet(user.id));
    }




}
