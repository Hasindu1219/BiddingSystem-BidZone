package com.groupeight.BidZone.Operations.service;

import com.groupeight.BidZone.Operations.dto.ListingDTO;
import com.groupeight.BidZone.Operations.entity.Listing;
import com.groupeight.BidZone.Operations.repo.ListingRepository;
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
public class ListingService {
    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ListingDTO> findAllListing() {
        List<Listing> listings = listingRepository.findAll();
        return modelMapper.map(listings,new TypeToken<List<ListingDTO>>(){}.getType());
    }

    public ListingDTO searchListingById(int id) {
        if(listingRepository.existsById(id)){
            Listing listing = listingRepository.findById(id).orElse(null);
            return modelMapper.map(listing,ListingDTO.class);
        }else{
            return null;
        }
    }

    public List<ListingDTO> findListingByDateExceptUserId(LocalDateTime date, int userId) {
        List<Listing> listings = listingRepository.findListingsByDateExceptUserId(date,userId);
        return modelMapper.map(listings,new TypeToken<List<ListingDTO>>(){}.getType());
    }

    public List<ListingDTO> findListingByUserId(int userId){
        List<Listing> listings = listingRepository.findListingByUserId(userId);
        return modelMapper.map(listings,new TypeToken<List<ListingDTO>>(){}.getType());
    }

    public String addNewListing(ListingDTO listingDTO,MultipartFile image,String username) throws IOException {
        Listing listing = modelMapper.map(listingDTO, Listing.class);

        System.out.println("Image came to Listing Service method: "+image);

        if (image != null && !image.isEmpty()) {
            String imagePath = saveImageToDisk(image,username);
            System.out.println(imagePath);
            listing.setImage(imagePath);
        }
        if(listingRepository.existsById(listingDTO.getId())){
            return VarList.RSP_DUPLICATED;
        }else{
            listingRepository.save(listing);
            return VarList.RSP_SUCCESS;
        }
    }

    public String updateListing(ListingDTO listingDTO){
        if(listingRepository.existsById(listingDTO.getId())){
            listingRepository.save(modelMapper.map(listingDTO,Listing.class));
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    public String deleteListingByID(int listingId){
        if (listingRepository.existsById(listingId)){
            listingRepository.deleteById(listingId);
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    public String saveImageToDisk(MultipartFile image, String username) throws IOException {
        String path = "C:\\Users\\Public\\BidZone\\Listing\\" + username;
        File directory = new File(path);

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + path);
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File targetFile = new File(directory, fileName);

        try {
            image.transferTo(targetFile);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new IOException("Failed to save file to disk", e);
        }

        return "C:/Users/Public/BidZone/Listing/"+username+"/"+ fileName;
    }

}