package com.epam.esm.giftcertificate.controllers;

import com.epam.esm.giftcertificate.model.Gift;
import com.epam.esm.giftcertificate.model.Tag;
import com.epam.esm.giftcertificate.services.GiftService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/gifts")
public class GiftRestController {

    private final GiftService giftService;

    public GiftRestController(GiftService giftService) {
        this.giftService = giftService;
    }

    @GetMapping
    public ResponseEntity<?> getAllGifts(@RequestParam(defaultValue = "0",required = false)int page,
                                         @RequestParam(defaultValue = "30",required = false)int size,
                                         @RequestParam(defaultValue = "name",required = false)String sort,
                                         @RequestParam(defaultValue = "",required = false) String tagName,
                                         @RequestParam(defaultValue = "",required = false) String nameDesc,
                                         @RequestParam(defaultValue = "true",required = false) boolean ascending){

        Map<String,Object> response = new HashMap<>();
        List<Gift> gifts;
        try{
            gifts = giftService.getGifts(page,size,sort,tagName,nameDesc,ascending);
            response.put("Gifts",gifts);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("Message","There was an error getting the gifts");
            response.put("Error",e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
