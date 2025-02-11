package ru.yandex.practicum.configuration.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.configuration.dao.PostDaoConfiguration;
import ru.yandex.practicum.mapper.PostMapper;

@Configuration
@Import({ TagServiceConfiguration.class,
        CommentServiceConfiguration.class,
        PostDaoConfiguration.class,
        PostMapper.class})
@ComponentScan("ru.yandex.practicum.service.post")
public class PostServiceConfiguration {

}
