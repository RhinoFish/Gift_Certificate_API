package com.epam.esm.giftcertificate.controllers;

import com.epam.esm.giftcertificate.model.Tag;
import com.epam.esm.giftcertificate.services.TagService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/tags")
public class TagRestController {

    private final TagService tagService;

    public TagRestController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<?> getTags(){
        Map<String,Object> response = new HashMap<>();
        List<Tag> tags;
        try{
            tags = tagService.getAllTags();
            response.put("Tags",tags);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("Message","There was an error getting the tags");
            response.put("Error",e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> postTag(@RequestBody(required = false) Tag tagRequest){
        Map<String,Object> response = new HashMap<>();
        if(tagRequest == null){
                response.put("Message","Request Body is missing");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            Tag tag = tagService.add(tagRequest);
            response.put("Message","The tag was created successfully");
            response.put("Tag",tag);
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }catch (DataAccessException e ){
            response.put("Message","There was an error inserting tag: " + tagRequest.getName());
            response.put("Error",e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long tagId){
        Map<String,Object> response = new HashMap<>();
        Tag tag = tagService.deleteTag(tagId);
        if(tag != null){
            response.put("Message","The tag has been deleted");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            response.put("Message","The tag with the Id " + tagId + " doesn't exist");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }
}
