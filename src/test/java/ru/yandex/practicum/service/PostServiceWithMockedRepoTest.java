package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dao.post.PostRepository;
import ru.yandex.practicum.dto.Post.PostFullViewDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.comment.CommentService;
import ru.yandex.practicum.service.post.PostServiceImp;
import ru.yandex.practicum.service.tag.TagService;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
public class PostServiceWithMockedRepoTest {

    @InjectMocks
    private PostServiceImp postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    TagService tagService;

    @Mock
    CommentService commentService;

    @Spy
    PostMapper postMapper = new PostMapper();


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
