package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.dto.Post.PostCreateDto;
import ru.yandex.practicum.service.post.PostService;
import ru.yandex.practicum.utils.DbUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockitoSpyBean
    PostService postService;

    private final MockMultipartFile emptyImage = new MockMultipartFile(
            "image",
            "empty.jpeg",
            "image/*",
            new byte[0]
    );

    @BeforeEach
    void setUp() {
        DbUtils.cleanupDb(jdbcTemplate);
    }

    @Test
    void addPost_shouldRedirectToHtmlWithPost() throws Exception {

        // add new post
        MvcResult result = mockMvc.perform(multipart("/posts")
                        .file(emptyImage)
                        .param("title", "title 1")
                        .param("body", "body 1")
                        .param("tags", "tag_1, tag_2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/posts/*"))
                .andReturn();

        verify(postService, times(1)).createPost(any(PostCreateDto.class));

        // redirect URL
        String redirectedUrl = result.getResponse().getRedirectedUrl();

        // redirect request - checking new post page
        mockMvc.perform(get(redirectedUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//textarea[@id='title']").string("title 1"))
                .andExpect(xpath("//textarea[@id='body']").string("body 1"))
                .andExpect(xpath("//textarea[@id='tags']").string("tag_1, tag_2"));
    }

    @Test
    void addPostAndLike_shouldReturnHtmlWithLike() throws Exception {

        // add new post
        MvcResult result = mockMvc.perform(multipart("/posts")
                        .file(emptyImage)
                        .param("title", "title 1")
                        .param("body", "body 1")
                        .param("tags", "tag_1, tag_2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/posts/*"))
                .andReturn();

        // redirect URL
        String redirectedUrl = result.getResponse().getRedirectedUrl();

        // add like
        mockMvc.perform(post(redirectedUrl + "/like"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectedUrl));

        // redirect request
        mockMvc.perform(get(redirectedUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//span[@id='like_counter']").string("likes: 1"))
                .andExpect(xpath("//textarea[@id='title']").string("title 1"))
                .andExpect(xpath("//textarea[@id='body']").string("body 1"))
                .andExpect(xpath("//textarea[@id='tags']").string("tag_1, tag_2"));

    }

}
