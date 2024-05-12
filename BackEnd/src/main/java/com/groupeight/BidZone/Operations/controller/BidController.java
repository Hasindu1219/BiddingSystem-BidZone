package com.groupeight.BidZone.Operations.controller;


import com.groupeight.BidZone.Operations.dto.BidDTO;
import com.groupeight.BidZone.Operations.dto.BidListingDTO;
import com.groupeight.BidZone.Operations.dto.ResponseDTO;
import com.groupeight.BidZone.Operations.service.BidService;
import com.groupeight.BidZone.Operations.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/bid")
@CrossOrigin
public class BidController {

    @Autowired
    private BidService bidService;

    private ResponseDTO createResponseDTO() {
        return new ResponseDTO();
    }

    @GetMapping(value = "/view")
    public ResponseEntity getAllBid() {

        ResponseDTO responseDTO = createResponseDTO();

        List<BidDTO> bidDTOList = bidService.findAll();
        try {

            if (bidDTOList.isEmpty()) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of Bids found");
            }else{
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched all Bids");
            }
            responseDTO.setContent(bidDTOList);
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

    @GetMapping("/search/{bidId}")
    public ResponseEntity searchBidByID(@PathVariable int bidId){
        ResponseDTO responseDTO = createResponseDTO();

        try{
            BidDTO bidDTO= bidService.searchBidById(bidId);

            if (bidDTO==null){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of the Bid id");
            }else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched the Bid by bidId");
            }
            responseDTO.setContent(bidDTO);
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

    @GetMapping("/search-by-user-id/{userId}")
    public ResponseEntity searchBidByUserID(@PathVariable int userId){
        ResponseDTO responseDTO = createResponseDTO();

        try{
            List<BidDTO> bidDTOList= bidService.searchBidByUserId(userId);

            if (bidDTOList.isEmpty()){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of the Bid of user: "+userId);
            }else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched the Bid of User:"+userId);
            }
            responseDTO.setContent(bidDTOList);
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

    @GetMapping("/search-by-list-id/{listId}")
    public ResponseEntity searchBidByListingId(@PathVariable int listId){
        ResponseDTO responseDTO = createResponseDTO();

        try{
            List<BidDTO> bidDTOList= bidService.searchBidByListingId(listId);

            if (bidDTOList.isEmpty()){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of the Bid for listing id: "+listId);
            }else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched the Bid for listing id: "+listId);
            }
            responseDTO.setContent(bidDTOList);
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

    @PostMapping("/add")
    public ResponseEntity addBid(@RequestBody BidDTO bidDTO){
        ResponseDTO responseDTO = createResponseDTO();

        try{
            String response = bidService.addNewBid(bidDTO);

            if(response.equals(VarList.RSP_SUCCESS)){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully added Bid");
                responseDTO.setContent(bidDTO);
                return new ResponseEntity(responseDTO,HttpStatus.ACCEPTED);
            }
            else{
                responseDTO.setCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Bid id already exists");
                responseDTO.setContent(bidDTO);
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

    @PutMapping("/update")
    public ResponseEntity updateBid(@RequestBody BidDTO bidDTO){
        ResponseDTO responseDTO = createResponseDTO();

        try{
            String response = bidService.updateBid(bidDTO);

            if(response.equals(VarList.RSP_SUCCESS)){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully updated the Bid");
                responseDTO.setContent(bidDTO);
                return new ResponseEntity(responseDTO,HttpStatus.ACCEPTED);
            }
            else{
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Not found such a Bid");
                responseDTO.setContent(bidDTO);
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

    @DeleteMapping("/delete/{bidId}")
    public ResponseEntity deleteBidByID(@PathVariable int bidId){
        ResponseDTO responseDTO = createResponseDTO();

        try{
            String response = bidService.deleteBidByID(bidId);
            if (response.equals(VarList.RSP_SUCCESS)){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully deleted the Bid");
                responseDTO.setContent(bidId);
                return new ResponseEntity(responseDTO,HttpStatus.ACCEPTED);
            }else{
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Not found such a bid");
                responseDTO.setContent("bidId: "+bidId);
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

    @GetMapping("/highestbidaccordingtouser/{userId}/{givenDate}")
    public ResponseEntity getHighestBidsForUserAndEndingBeforeDate(
            @PathVariable int userId,
            @PathVariable LocalDateTime givenDate) {
        ResponseDTO responseDTO = createResponseDTO();

        try{
            List<BidListingDTO> bidListAccordingtoUser = bidService.findHighestBidsForUserAndEndingBeforeDate(userId, givenDate);

            if (bidListAccordingtoUser.isEmpty()){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of the Bid of user: "+userId);
            }else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched the Bid of User:"+userId);
            }
            responseDTO.setContent(bidListAccordingtoUser);
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


    @GetMapping("/aaaaaa/{userId}")
    public ResponseEntity findHighestBidsForUserAndEndingBeforeDate1111(
            @PathVariable int userId) {
        ResponseDTO responseDTO = createResponseDTO();

        try{
            List<BidListingDTO> bidListAccordingtoUser = bidService.findHighestBidsForUserAndEndingBeforeDate1111(userId);

            if (bidListAccordingtoUser.isEmpty()){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No records of the Bid of user: "+userId);
            }else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched the Bid of User:"+userId);
            }

            responseDTO.setContent(bidListAccordingtoUser);
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


    @PutMapping("/completebid/{bidId}")
    public ResponseEntity<String> completeBid(@PathVariable int bidId) {
        try {
            String response = bidService.markBidAsComplete(bidId);

            if (response.equals(VarList.RSP_SUCCESS)) {
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Successfully updated the Bid");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Not found such a Bid");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + ex.getMessage());
        }
    }
}



