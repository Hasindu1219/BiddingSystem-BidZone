package com.groupeight.BidZone.Operations.controller;

import com.groupeight.BidZone.Operations.entity.Card;
import com.groupeight.BidZone.Operations.service.CardDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CardDetailsController {

    private final CardDetailsService cardDetailsService;

    @Autowired
    public CardDetailsController(CardDetailsService cardDetailsService) {
        this.cardDetailsService = cardDetailsService;
    }

    @PostMapping("/cardDetailsSave")
    public ResponseEntity<String> storeCardDetails(@RequestBody Card cardDetails) {
        try {
            cardDetailsService.storeCardDetails(cardDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body("Card details stored successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store card details");
        }
    }
}

