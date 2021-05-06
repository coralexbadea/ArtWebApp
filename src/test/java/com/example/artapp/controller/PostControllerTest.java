package com.example.artapp.controller;

import com.example.artapp.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;


@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Test
    @WithMockUser(username = "Aedab", password = "scoala3", roles = "ARTIST")
    void index() throws Exception {
        Long pid = 30L;
        Mockito.when(postService.getPosts(pid)).thenReturn(
                Collections.emptyList()
        );

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/post/index/"+pid)
                    .accept(MediaType.APPLICATION_JSON)
        ).andReturn();

        Mockito.verify(postService).getPosts(pid);

    }
}