package com.MyMusic.v1.payments;

import com.MyMusic.v1.auth.MyUser;
import com.MyMusic.v1.services.SongService;
import com.MyMusic.v1.services.TransactionsService;
import com.MyMusic.v1.services.WalletService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/user/charge")
public class StripeChargeController {

    @Resource
    private StripeChargeService service;

    @PostMapping
    public ResponseEntity<String> createCharge(@AuthenticationPrincipal MyUser user, @RequestBody StripeCharge stripeCharge) {
       return service.createCharge(user, stripeCharge);

    }
}
