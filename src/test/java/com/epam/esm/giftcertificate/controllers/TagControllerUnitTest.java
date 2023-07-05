package com.epam.esm.giftcertificate.controllers;

import com.epam.esm.giftcertificate.model.Tag;
import com.epam.esm.giftcertificate.services.TagService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TagRestController.class)
public class TagControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private TagRestController tagRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testPostTagSuccess() throws Exception {

        Tag createdTag = new Tag();
        createdTag.setName("TestTag");
        when(tagService.add(any(Tag.class))).thenReturn(createdTag);
        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestTag\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("Message").value("The tag was created successfully"))
                .andExpect(jsonPath("Tag.name").value("TestTag"));


    }

    @Test
    public void testPostTagMissingRequestBody()throws Exception{
        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("Message").value("Request Body is missing"));
    }

    @Test
    public void testPostTagInsertionError() throws Exception{
        String errorMessage = "Error inserting Tag";

        when(tagService.add(any(Tag.class))).thenThrow(new DataAccessException(errorMessage) {
        });

        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestTag\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("Message").value("There was an error inserting tag: TestTag"))
                .andExpect(jsonPath("Error").value(errorMessage));


    }

    @Test
    public void testGetTagsSuccess(){
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1L,"Tag1"));
        tags.add(new Tag(2L,"Tag2"));
        Map<String,Object> expectedResponse = new HashMap<>();
        expectedResponse.put("Tags", tags);

        when(tagService.getAllTags()).thenReturn(tags);

        ResponseEntity<?> response = tagRestController.getTags();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(tagService, times(1)).getAllTags();


    }

    @Test
    public void testGetTagsError() {
        // Arrange
        String errorMessage = "Error getting tags";
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("Message", "There was an error getting the tags");
        expectedResponse.put("Error", errorMessage);

        when(tagService.getAllTags()).thenThrow(new DataAccessException(errorMessage) {});

        // Act
        ResponseEntity<?> response = tagRestController.getTags();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(tagService, times(1)).getAllTags();
    }

    @Test
    public void testGetTagsSuccessHTTP() throws Exception {
        // Arrange
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1L, "Tag1"));
        tags.add(new Tag(2L, "Tag2"));

        when(tagService.getAllTags()).thenReturn(tags);


        mockMvc.perform(get("/tags"))
                .andDo(log())
                .andExpect(
                        status().isOk())
                .andExpect(
                        content().json(readTestResourceFile(Path.of("GetAll.txt")), false))
                .andReturn();

        verify(tagService, times(1)).getAllTags();
    }

    @Test
    public void testDeleteTagSuccessHTTP() throws Exception{
        Tag tag = new Tag(1L,"Tag Test");

        when(tagService.deleteTag(any(Long.class))).thenReturn(tag);

        mockMvc.perform(delete("/tags/1"))
                .andDo(log())
                .andExpect(
                        status().isOk())
                .andExpect(jsonPath("Message").value("The tag has been deleted"))
                .andReturn();

        verify(tagService, times(1)).deleteTag(any(Long.class));
    }

    @Test
    public void testDeleteTagErrorHTTP() throws Exception{
        when(tagService.deleteTag(any(Long.class))).thenReturn(null);
        mockMvc.perform(delete("/tags/1"))
                .andDo(log())
                .andExpect(
                        status().isNotFound())
                .andExpect(jsonPath("Message").value("The tag with the Id 1 doesn't exist"))
                .andReturn();

        verify(tagService, times(1)).deleteTag(any(Long.class));
    }

    static String readTestResourceFile(final Path path) {
        Path testResPath = Paths.get("src", "test", "resources");
        try {
            return Files.lines(testResPath.resolve(path), StandardCharsets.UTF_8)
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
