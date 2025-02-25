package ru.yandex.practicum.configuration.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.configuration.dao.CommentDaoConfiguration;
import ru.yandex.practicum.mapper.CommentMapper;

@Configuration
@Import({ CommentDaoConfiguration.class,
        CommentMapper.class })
@ComponentScan("ru.yandex.practicum.service.comment")
public class CommentServiceConfiguration {

}
