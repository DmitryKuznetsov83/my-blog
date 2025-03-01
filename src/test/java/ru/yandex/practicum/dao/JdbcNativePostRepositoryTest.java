package ru.yandex.practicum.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.configuration.DataSourceTestConfiguration;
import ru.yandex.practicum.dao.post.JdbcNativePostRepositoryImpl;
import ru.yandex.practicum.dao.post.PostRepository;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.utils.DbUtils;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringJUnitConfig(classes = {DataSourceTestConfiguration.class, JdbcNativePostRepositoryImpl.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativePostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        DbUtils.cleanupDb(jdbcTemplate);
        jdbcTemplate.execute("INSERT INTO posts (title, body, short_body) VALUES ('title 1', 'full body 1', 'short body 1')");
        jdbcTemplate.execute("INSERT INTO posts (title, body, short_body) VALUES ('title 2', 'full body 2', 'short body 2')");
        jdbcTemplate.execute("INSERT INTO posts (title, body, short_body) VALUES ('title 3', 'full body 3', 'short body 3')");
    }

    @Test
    void findAllByPage_shouldReturnAllPosts() {
        List<Post> posts = postRepository.findPostByPage(1, 10, null);

        assertThat(posts, notNullValue());
        assertThat(posts.size(), is(3));
    }

    @Test
    void AddNewPost_FindAllByPage_shouldReturnNewPostFirstOnTheList() {
        jdbcTemplate.execute("INSERT INTO posts (title, body, short_body) VALUES ('title 4', 'full body 4', 'short body 4')");
        List<Post> posts = postRepository.findPostByPage(1, 10, null);
        assertThat(posts, notNullValue());
        assertThat(posts.size(), is(4));
        Post newPost = posts.stream().findFirst().orElse(null);
        assertThat(newPost, notNullValue());
        assertThat(newPost.title(), is("title 4"));
    }

}
