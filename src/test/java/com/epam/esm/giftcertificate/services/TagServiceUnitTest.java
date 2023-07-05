package com.epam.esm.giftcertificate.services;

import com.epam.esm.giftcertificate.data.TagRepository;
import com.epam.esm.giftcertificate.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TagServiceUnitTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;


    @Test
    public void addATagTest(){
        final Tag tag = new Tag();
        tag.setName("Tag test");

        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag createdTag = tagService.add(new Tag());

        assertThat(createdTag).usingRecursiveComparison().isEqualTo(tag);
        verify(tagRepository,times(1)).save(any(Tag.class));
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    public void findAllTest(){
        List<Tag> mockTags = new ArrayList<>();
        mockTags.add(new Tag(1L, "Tag1"));
        mockTags.add(new Tag(2L, "Tag2"));

        when(tagRepository.findAll()).thenReturn(mockTags);


        List<Tag> result = tagService.getAllTags();

        assertEquals(mockTags.size(), result.size());
        assertEquals(mockTags.get(0), result.get(0));
        assertEquals(mockTags.get(1), result.get(1));
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    public void deleteTagTest(){
        Tag tag = new Tag(1L,"Tag Test");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));

        Tag createdTag = tagService.deleteTag(1L);
        assertEquals(tag.getName(),createdTag.getName());

        verify(tagRepository, times(1)).findById(anyLong());
        verify(tagRepository, times(1)).deleteById(anyLong());
    }

}
