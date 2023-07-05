package com.epam.esm.giftcertificate.services;

import com.epam.esm.giftcertificate.data.TagRepository;
import com.epam.esm.giftcertificate.model.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag add(Tag tag) {
      return tagRepository.save(tag);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElse(null);

        if(tag!= null){
            tagRepository.deleteById(tagId);
        }
        return tag;
    }
}
