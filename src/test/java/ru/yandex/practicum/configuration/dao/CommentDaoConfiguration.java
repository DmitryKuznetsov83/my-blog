package ru.yandex.practicum.configuration.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.yandex.practicum.dao.comment.CommentRepository;

import static org.mockito.Mockito.mock;

@Configuration
public class CommentDaoConfiguration {

    @Bean
    @Profile("mock_repo")
    public CommentRepository mockCommentRepository() {
        return mock(CommentRepository.class);
    }

}
