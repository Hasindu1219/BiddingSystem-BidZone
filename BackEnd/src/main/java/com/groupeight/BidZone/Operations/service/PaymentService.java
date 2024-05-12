package com.groupeight.BidZone.Operations.service;

import com.groupeight.BidZone.Operations.dto.ListingDTO;
import com.groupeight.BidZone.Operations.dto.PaymentDTO;
import com.groupeight.BidZone.Operations.entity.Listing;
import com.groupeight.BidZone.Operations.entity.Payment;
import com.groupeight.BidZone.Operations.repo.ListingRepository;
import com.groupeight.BidZone.Operations.repo.PaymentRepo;
import com.groupeight.BidZone.Operations.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<ListingDTO> findpayments() {
        List<Payment> listings = paymentRepo.findAll();
        return modelMapper.map(listings,new TypeToken<List<ListingDTO>>(){}.getType());
    }

    public List<PaymentDTO> findPaymentsByUserId(int userId){
        List<Payment> payments = paymentRepo.findPaymentsByUserId(userId);
        return modelMapper.map(payments,new TypeToken<List<PaymentDTO>>(){}.getType());
    }
}





