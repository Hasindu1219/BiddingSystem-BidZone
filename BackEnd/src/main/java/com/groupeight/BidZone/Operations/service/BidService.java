package com.groupeight.BidZone.Operations.service;

import com.groupeight.BidZone.Operations.dto.BidDTO;
import com.groupeight.BidZone.Operations.dto.BidListingDTO;
import com.groupeight.BidZone.Operations.entity.Bid;
import com.groupeight.BidZone.Operations.repo.BidRepository;
import com.groupeight.BidZone.Operations.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<BidDTO> findAll() {
        List<Bid> bids = bidRepository.findAll();
        return modelMapper.map(bids,new TypeToken<List<BidDTO>>(){}.getType());
    }

    public BidDTO searchBidById(int id) {
        if(bidRepository.existsById(id)){
            Bid bid = bidRepository.findById(id).orElse(null);
            return modelMapper.map(bid,BidDTO.class);
        }else{
            return null;
        }
    }
    public List<BidDTO> searchBidByUserId(int userId) {
        List<Bid> bids = bidRepository.findBidByUserId(userId);
        return modelMapper.map(bids,new TypeToken<List<BidDTO>>(){}.getType());
    }

    public List<BidDTO> searchBidByListingId(int listingID) {
        List<Bid> bids = bidRepository.findBidByListingId(listingID);
        return modelMapper.map(bids,new TypeToken<List<BidDTO>>(){}.getType());
    }

    public synchronized String addNewBid(BidDTO bidDTO) {
        if(bidRepository.existsById(bidDTO.getId())){
            return VarList.RSP_DUPLICATED;
        }else{
            bidRepository.save(modelMapper.map(bidDTO, Bid.class));
            return VarList.RSP_SUCCESS;
        }
    }

    public String updateBid(BidDTO bidDTO){
        if(bidRepository.existsById(bidDTO.getId())){
            bidRepository.save(modelMapper.map(bidDTO,Bid.class));
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    public String deleteBidByID(int bidId){
        if (bidRepository.existsById(bidId)){
            bidRepository.deleteById(bidId);
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    //for get the highest bids according to the users for ended listings
    public List<BidListingDTO> findHighestBidsForUserAndEndingBeforeDate(int userId, LocalDateTime givenDate) {
        List<Object[]> resultList = bidRepository.findHighestBidsForUserAndEndingBeforeDate(userId, givenDate);

        return resultList.stream()
                .map(row -> new BidListingDTO(
                        (int) row[0],            // bid_id
                        (int) row[1],            // listing_Id
                        (int) row[2],            // user_id
                        (String) row[3],         // listing_name
                        (String) row[4],         // listing_description
                        (String) row[5],         // listing_category
                        ((Timestamp) row[6]).toLocalDateTime(),  // listing_ending
                        ((Timestamp) row[7]).toLocalDateTime(),  // bid_timestamp
                        (float) row[8]           // bid_price
                ))
                .collect(Collectors.toList());
    }


    public List<BidListingDTO> findHighestBidsForUserAndEndingBeforeDate1111(int userId) {
        List<Object[]> resultList = bidRepository.findHighestBidsForUserAndEndingBeforeDate1111(userId);

        return resultList.stream()
                .map(row -> new BidListingDTO(
                        (int) row[0],            // bid_id
                        (int) row[1],            // listing_Id
                        (int) row[2],            // user_id
                        (String) row[3],         // listing_name
                        (String) row[4],         // listing_description
                        (String) row[5],         // listing_category
                        ((Timestamp) row[6]).toLocalDateTime(),  // listing_ending
                        ((Timestamp) row[7]).toLocalDateTime(),
                        (float) row[8]           // bid_price
                ))
                .collect(Collectors.toList());
    }


    public String markBidAsComplete(int bidId) {
        if(bidRepository.existsById(bidId)){
            System.out.println("service class");
            bidRepository.updateBidStatusToComplete(bidId);
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    //------------------------------------------------------------------------------------------------------------
    /*public List<Bid> findUserActiveBids(Integer userId) {
        return bidRepository.findUserActiveBids(userId);
    }

    public Map<String, Long> retrieveAgeRangeStatisticsPerCategory(Integer min, Integer max) {
        List<Object[]> statistics = bidRepository.retrieveAgeRangeStatisticsPerCategory(min, max);
        return extractStatisticsMap(statistics);
    }

    public Map<String, Long> retrieveCategoryStatisticsPerAgeRange(String category) {
        List<Object[]> statistics = bidRepository.retrieveCategoryStatisticsPerAgeRange(category);
        return extractStatisticsMap(statistics);
    }

    public Map<String, Long> retrieveGenderStatisticsPerCategory(String gender) {
        List<Object[]> statistics = bidRepository.retrieveGenderStatisticsPerCategory(gender);
        return extractStatisticsMap(statistics);
    }

    public Map<String, Long> retrieveCategoryStatisticsPerGender(String category) {
        List<Object[]> statistics = bidRepository.retrieveCategoryStatisticsPerGender(category);
        return extractStatisticsMap(statistics);
    }

    public Map<String, Long> retrieveCombinationStatisticsPerCategory(String gender, Integer min, Integer max) {
        List<Object[]> statistics = bidRepository.retrieveCombinationStatisticsPerCategory(gender, min, max);
        return extractStatisticsMap(statistics);
    }

    public Map<String, Long> retrieveCategoryStatisticsPerCombination(String category) {
        List<Object[]> statistics = bidRepository.retrieveCategoryStatisticsPerCombination(category);
        return extractCombinationStatisticsMap(statistics);
    }

    private Map<String, Long> extractStatisticsMap(List<Object[]> statistics) {
        Map<String, Long> statisticsMap = new HashMap<>();
        for (Object[] statistic : statistics) {
            statisticsMap.put(statistic[0].toString(), (Long) statistic[1]);
        }
        return statisticsMap;
    }

    private Map<String, Long> extractCombinationStatisticsMap(List<Object[]> statistics) {
        Map<String, Long> statisticsMap = new HashMap<>();
        for (Object[] statistic : statistics) {
            statisticsMap.put(statistic[0].toString() + " -> " + statistic[1].toString(), (Long) statistic[2]);
        }
        return statisticsMap;
    }*/
}
