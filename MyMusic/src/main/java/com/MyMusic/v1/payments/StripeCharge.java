package com.MyMusic.v1.payments;

import com.MyMusic.v1.auth.MyUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.HashMap;
import java.util.Map;

public class StripeCharge {

    public final int songId;
    private final long amount;
    private final String source;
    private final String currency;
    private final String songName;
//    private final Map<String, String> metadata;

    public StripeCharge(@JsonProperty("songId") int songId, @JsonProperty("amount") long amount, @JsonProperty("token") String token, @JsonProperty("songName") String songName) {
        this.songId=songId;
        this.amount = amount;
        this.source = token;
        this.currency = "usd";
        this.songName=songName;
//        metadata = new HashMap<>();
//        metadata.put("userId", "5");
    }

    public Map<String, Object> getCharge() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", this.amount);
        params.put("currency", this.currency);
        params.put("source", this.source);
        params.put(
                "description",
                "Bought "+songName
        );
//        params.put("metadata", metadata);
        return params;
    }
}
