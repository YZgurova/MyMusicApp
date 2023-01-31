//package com.MyMusic.v1.payments;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.stripe.exception.SignatureVerificationException;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Event;
//import com.stripe.model.PaymentIntent;
//import com.stripe.net.Webhook;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/webhooks/stripe")
//public class StripeWebhookController {
//    private final String endpointSecret = "whsec_c70850789a6d6dc88fe83db920e9472107947ce8fd62df9c886f04a09114265f";
//
//        @PostMapping
//        public ResponseEntity handleWebhook(@RequestBody String json, @RequestHeader("Stripe-Signature") String stripeSignature) {
//            try {
//                Event event = Webhook.constructEvent(json, stripeSignature, endpointSecret);
//                switch (event.getType()) {
//                    case "charge.succeeded":
//                        PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
//                        System.out.println(event.getType());
//                        // handle the payment_intent.succeeded event
////                    case "payment_intent.succeeded":
////                        PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
//                        break;
//                    // handle other event types as needed
//                }
//                return new ResponseEntity(HttpStatus.OK);
//            } catch (SignatureVerificationException e) {
//                return new ResponseEntity(HttpStatus.BAD_REQUEST);
//            } catch (StripeException e) {
//                return new ResponseEntity(HttpStatus.BAD_REQUEST);
//            }
//        }
//    }
//
//
