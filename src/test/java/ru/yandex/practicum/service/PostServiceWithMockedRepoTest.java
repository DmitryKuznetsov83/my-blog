package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.configuration.service.PostServiceConfiguration;
import ru.yandex.practicum.dao.post.PostRepository;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.post.PostService;
import ru.yandex.practicum.dto.Post.PostFullViewDto;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostServiceConfiguration.class)
@ActiveProfiles("mock_repo")
public class PostServiceWithMockedRepoTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void findPostById_shouldReturnPost() {

        Post mockPost = new Post(1L,
                "Title 1",
                "Body 1",
                "Body 1",
                0,
                0,
                null,
                null);

        Mockito.doReturn(Optional.of(mockPost))
                .when(postRepository).findPostById(1L);

        Optional<PostFullViewDto> optionalPost = postService.findPostById(1L);
        assertThat(optionalPost.isPresent(), is(true));
        PostFullViewDto post = optionalPost.get();
        assertThat(post.id(), is(1L));
        assertThat(post.title(), is("Title 1"));
        assertThat(post.body(), is("Body 1"));
    }

}
