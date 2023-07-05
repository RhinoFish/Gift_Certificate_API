package com.epam.esm.giftcertificate.services;

import com.epam.esm.giftcertificate.data.GiftRepository;
import com.epam.esm.giftcertificate.model.Gift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftService {

    private final GiftRepository giftRepository;

    public GiftService(GiftRepository giftRepository) {
        this.giftRepository = giftRepository;
    }

    public List<Gift> getGifts(int page, int size, String sort,
                               String tagName, String nameDesc, boolean ascending) {
        StringBuilder sortMode = new StringBuilder();
        Pageable sortAndPage;
        if(sort.equalsIgnoreCase("lastupdatedate")){
            sortMode.append("lastUpdateDate");
        } else if (sort.equalsIgnoreCase("createdate")) {
            sortMode.append("createDate");
        }else {
            sortMode.append("name");
        }
        if(ascending){
             sortAndPage = PageRequest.of(page,size, Sort.by(sortMode.toString()).ascending());
        }else{
             sortAndPage = PageRequest.of(page,size, Sort.by(sortMode.toString()).descending());
        }
        if(tagName.isBlank() && nameDesc.isBlank()){
            List<Gift> gifts = giftRepository.findAll(sortAndPage).toList();
            return gifts;
        } else if (!(tagName.isBlank()) && nameDesc.isBlank()) {
            List<Gift> gifts = giftRepository.findByTags_name(tagName,sortAndPage).toList();
            return gifts;
        }else if(tagName.isBlank() && !(nameDesc.isBlank())){
            List<Gift> gifts = giftRepository.findByNameContainingOrDescriptionContaining(nameDesc,nameDesc,
                    sortAndPage).toList();
            return gifts;
        }else{
           Page<Gift> giftsP = giftRepository.findByTags_name(tagName,
                    sortAndPage);
            List<Gift> gifts = giftsP.stream().filter(gift -> gift.getName().contains(nameDesc) || gift.getDescription().contains(nameDesc))
                    .collect(Collectors.toList());
            return gifts;
        }
    }
}
