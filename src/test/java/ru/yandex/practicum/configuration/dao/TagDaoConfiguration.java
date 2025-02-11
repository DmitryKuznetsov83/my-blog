package ru.yandex.practicum.configuration.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.yandex.practicum.dao.tag.TagRepository;

import static org.mockito.Mockito.mock;

@Configuration
public class TagDaoConfiguration {

    @Bean
    @Profile("mock_repo")
    public TagRepository mockTagRepository() {
        return mock(TagRepository.class);
    }

}
