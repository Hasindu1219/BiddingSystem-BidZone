package com.groupeight.BidZone.Operations.controller;

import com.groupeight.BidZone.Operations.dto.ListingDTO;
import com.groupeight.BidZone.Operations.dto.ResponseDTO;
import com.groupeight.BidZone.Operations.service.ListingService;
import com.groupeight.BidZone.Operations.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/listing")
@CrossOrigin
public class ListingController {

    @Autowired
    private ListingService listingService;

    private ResponseDTO createResponseDTO() {
        return new ResponseDTO();
    }

    @GetMapping(value = "/view")
    public ResponseEntity getAllListing() {
        ResponseDTO responseDTO = createResponseDTO();

        List<ListingDTO> listingDTOList = listingService.findAllListing();
        try {

            if (listingDTOList.isEmpty()) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of Listings found");
            }else{
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched all Listings");
            }
            responseDTO.setContent(listingDTOList);
            return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);

        }catch(Exception ex){
            System.out.println("ERROR: "+ex.getMessage());

            // Handle exceptions
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/search-by-id/{listingId}")
    public ResponseEntity searchListingByID(@PathVariable int listingId){
        ResponseDTO responseDTO = createResponseDTO();
        try{
            ListingDTO listingDTO= listingService.searchListingById(listingId);

            if (listingDTO==null){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of the listing");
            }else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched the Listing");
            }
            responseDTO.setContent(listingDTO);
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

    @GetMapping(value = "/search-by-date-except-userid/{date}/{userId}")
    public ResponseEntity searchListingByDateExceptUserId(@PathVariable LocalDateTime date,@PathVariable int userId){
        ResponseDTO responseDTO = createResponseDTO();
        try{
            List<ListingDTO> listingDTOList = listingService.findListingByDateExceptUserId(date,userId);

            if (listingDTOList.isEmpty()){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of the listings");
            }else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched the Listings");
            }
            responseDTO.setContent(listingDTOList);
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

    @GetMapping(value = "/search-by-userid/{userId}")
    public ResponseEntity findListingByUserId(@PathVariable int userId){
        ResponseDTO responseDTO = createResponseDTO();
        try{
            List<ListingDTO> listingDTOList = listingService.findListingByUserId(userId);

            if (listingDTOList.isEmpty()){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of the listings for userid:"+userId);
            }else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched the Listings");
            }
            responseDTO.setContent(listingDTOList);
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

    @PutMapping(value = "/update")
    public ResponseEntity updateListing(@RequestBody ListingDTO listingDTO){
        ResponseDTO responseDTO = createResponseDTO();
        try{
            String response = listingService.updateListing(listingDTO);

            if(response.equals(VarList.RSP_SUCCESS)){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully updated the Listing");
                responseDTO.setContent(listingDTO);
                return new ResponseEntity(responseDTO,HttpStatus.ACCEPTED);
            }
            else{
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Not found such a Listing");
                responseDTO.setContent(listingDTO);
                return new ResponseEntity(responseDTO,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            System.out.println("ERROR: "+ex.getMessage());

            // Handle exceptions
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity addListing(@RequestPart("listingDTO") ListingDTO listingDTO,
                                     @RequestPart(value = "image") MultipartFile image,
                                     @RequestParam(value = "username") String username) {
        ResponseDTO responseDTO = createResponseDTO();

        try {
            String response = listingService.addNewListing(listingDTO, image,username);

            if (response.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully added Listing");
                responseDTO.setContent(listingDTO);
                return new ResponseEntity(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Listing id is already exists");
                responseDTO.setContent(listingDTO);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());

            // Handle exceptions
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/delete/{listingId}")
    public ResponseEntity deleteListingByID(@PathVariable int listingId){
        ResponseDTO responseDTO = createResponseDTO();
        try{
            String response = listingService.deleteListingByID(listingId);
            if (response.equals(VarList.RSP_SUCCESS)){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully deleted the Listing");
                responseDTO.setContent(listingId);
                return new ResponseEntity(responseDTO,HttpStatus.ACCEPTED);
            }else{
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Not found such a Listing");
                responseDTO.setContent("listingId: "+listingId);
                return new ResponseEntity(responseDTO,HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            System.out.println("ERROR: "+ex.getMessage());

            // Handle exceptions
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}