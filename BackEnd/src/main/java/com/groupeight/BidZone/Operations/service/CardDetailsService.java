package com.groupeight.BidZone.Operations.service;

import com.groupeight.BidZone.Operations.entity.Card;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;

@Service
public class CardDetailsService {

    private static final String AES = "AES";
    private static final String FILE_PATH = "E:\\Advance Java Project\\BiddingSystem-BidZone\\BackEnd\\encrypted_card_details.scr";

    public void storeCardDetails(Card cardDetails) throws Exception {
        // Generate AES secret key
        SecretKey secretKey = generateSecretKey();

        // Encrypt card details
        String encryptedData = encrypt(cardDetails.toString(), secretKey);

        // Store encrypted data in a file
        storeEncryptedDataToFile(encryptedData);
        System.out.println("Successfully saved card details");
    }

    private SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(256); // Use 256-bit key size
        return keyGenerator.generateKey();
    }

    private String encrypt(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private void storeEncryptedDataToFile(String encryptedData) throws Exception {
        Path filePath = Paths.get(FILE_PATH);
        Files.write(filePath, encryptedData.getBytes());
    }
}

