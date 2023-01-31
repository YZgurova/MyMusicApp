package com.MyMusic.v1.payments;

import com.MyMusic.v1.auth.MyUser;
import com.MyMusic.v1.services.SongService;
import com.MyMusic.v1.services.TransactionsService;
import com.MyMusic.v1.services.WalletService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.math.BigDecimal;

public class StripeChargeService {

    @Resource
    private TransactionsService transactionsService;
    @Resource
    private WalletService walletService;
    @Resource
    private SongService songService;
    private String transactionDetails="";
    private String status;

    public ResponseEntity<String> createCharge(@AuthenticationPrincipal MyUser user, @RequestBody StripeCharge stripeCharge) {
        try {
            Stripe.apiKey = "sk_test_51MPNmYBE524ie8PIW2whFoiGiFS9ZDDJwzNlfZfnEx8WOn88EgkbSWz0DyBFUIlkWOof1UmuuNwJkti9BDImQ6sP002BhVCKdy";
            Charge charge = Charge.create(stripeCharge.getCharge());
            transactionDetails=charge.getId() + " was charged $" + charge.getAmount()/100 +" "+ charge.getCurrency();
            status=charge.getStatus();
            transactionsService.createTransaction(user.id, transactionDetails, status);
            BigDecimal amount = BigDecimal.valueOf(charge.getAmount());
            BigDecimal commissionPercents =new BigDecimal("10");
            BigDecimal adminPart = amount.divide(commissionPercents);
            amount=amount.subtract(adminPart);
            walletService.addMoney(1, adminPart);
            walletService.addMoneyToSongcreator(stripeCharge.songId, amount);
            songService.addToBoughtSong(user.id, stripeCharge.songId);
            return new ResponseEntity<String>("Success", HttpStatus.CREATED);
        } catch (StripeException e) {
            e.printStackTrace();
            status="failed";
            transactionsService.createTransaction(user.id, transactionDetails, status);
            return new ResponseEntity<String>("Failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
