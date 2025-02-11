package ru.yandex.practicum.configuration.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.yandex.practicum.dao.post.PostRepository;

import static org.mockito.Mockito.mock;

@Configuration
public class PostDaoConfiguration {

    @Bean
    @Profile("mock_repo")
    public PostRepository mockPostRepository() {
        return mock(PostRepository.class);
    }

}
