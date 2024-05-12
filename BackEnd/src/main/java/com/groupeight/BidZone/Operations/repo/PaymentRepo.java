package com.groupeight.BidZone.Operations.repo;

import com.groupeight.BidZone.Operations.entity.Listing;
import com.groupeight.BidZone.Operations.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {

    // For saving payment data
    Payment save(Payment payment);


    //Get all the payments done by a user
    @Query(value = "SELECT * FROM Payment WHERE user_id = ?1",nativeQuery = true)
    List<Payment> findPaymentsByUserId(int userId);


}
