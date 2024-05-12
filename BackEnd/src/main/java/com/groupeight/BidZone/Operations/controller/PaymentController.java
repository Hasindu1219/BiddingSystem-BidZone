package com.groupeight.BidZone.Operations.controller;

import com.groupeight.BidZone.Operations.dto.ListingDTO;
import com.groupeight.BidZone.Operations.dto.PaymentDTO;
import com.groupeight.BidZone.Operations.dto.ResponseDTO;
import com.groupeight.BidZone.Operations.entity.Payment;
import com.groupeight.BidZone.Operations.repo.CardTypeStrategy;
import com.groupeight.BidZone.Operations.repo.PaymentRepo;
import com.groupeight.BidZone.Operations.service.CardTypeSelectionService;
import com.groupeight.BidZone.Operations.service.ListingService;
import com.groupeight.BidZone.Operations.service.PaymentService;
import com.groupeight.BidZone.Operations.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/payment")
@CrossOrigin
public class PaymentController {

    private final PaymentRepo paymentRepo;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    public PaymentController(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @PostMapping("/addpayment")
    public ResponseEntity<String> processPayment(@RequestBody PaymentDTO paymentDTO) {
        // Convert PaymentDTO to PaymentEntity (assuming you have an entity mapping)


        System.out.println(paymentDTO);
        Payment payment = mapDtoToEntity(paymentDTO);

        // Save paymentEntity to the database using the repository
        paymentRepo.save(payment);

        return ResponseEntity.ok("Payment processed successfully!");
    }


    // Utility method to map PaymentDTO to PaymentEntity (customize as per your entity structure)
    private Payment mapDtoToEntity(PaymentDTO paymentDTO) {
        String Type = paymentDTO.getPayment_type();

        //Get card type using strategy design pattern
        String CardType = new CardTypeSelectionService().cardTypeSelection(Type);


        Payment payment = new Payment();
        // Map properties from paymentDTO to paymentEntity
        payment.setListing_Id(paymentDTO.getListing_Id());
        payment.setUser_Id(paymentDTO.getUser_Id());
        payment.setBid_Id(paymentDTO.getBid_Id());
        payment.setPaid_time(paymentDTO.getPaid_time());
        payment.setPayment_type(CardType);
        payment.setPrice(paymentDTO.getPrice());
        // Set other properties as needed
        return payment;
    }


    //Get payment list of a user
    @GetMapping(value = "/paymentsearch/{userId}")
    public ResponseEntity findPaymentsByUserId(@PathVariable int userId){
        try{
            List<PaymentDTO> paymentDTOList = paymentService.findPaymentsByUserId(userId);

            if (paymentDTOList.isEmpty()){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of the payments for userid:"+userId);
            }else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched the payments");
            }
            responseDTO.setContent(paymentDTOList);
            return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);

        }catch (Exception ex){
            System.out.println("ERROR: "+ex.getMessage());

            // Handle exceptions
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
