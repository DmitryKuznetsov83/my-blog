package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.configuration.DataSourceTestConfiguration;
import ru.yandex.practicum.configuration.WebTestConfiguration;
import ru.yandex.practicum.utils.DbUtils;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(classes = {DataSourceTestConfiguration.class, WebTestConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    private final MockMultipartFile emptyImage = new MockMultipartFile(
            "image",
            "empty.jpeg",
            "image/*",
            new byte[0]
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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

    // PRIVATE


}
