package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.dto.Post.PostCreateDto;
import ru.yandex.practicum.dto.Post.PostFullViewDto;
import ru.yandex.practicum.service.comment.CommentService;
import ru.yandex.practicum.service.post.PostService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private CommentService commentService;

    private final MockMultipartFile emptyImage = new MockMultipartFile(
            "image",
            "empty.jpeg",
            "image/*",
            new byte[0]
    );

    @Test
    void addPost_shouldRedirectToHtmlWithPost() throws Exception {

        PostFullViewDto newPost = new PostFullViewDto(1L, "title 1", "body 1", 0, "tag_1, tag_2", false);
        when(postService.createPost(any(PostCreateDto.class))).thenReturn(1L);
        when(postService.findPostById(1L)).thenReturn(Optional.of(newPost));

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
    void getNonExistentPostById_shouldRedirectTo404() throws Exception {

        int nonExistentPostById = 999;
        when(postService.findPostById(anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(get("/posts/" + nonExistentPostById))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("status_404"));

    }

}
