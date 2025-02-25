package ru.yandex.practicum.configuration.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.configuration.dao.TagDaoConfiguration;
import ru.yandex.practicum.mapper.TagMapper;

@Configuration
@Import({ TagDaoConfiguration.class,
        TagMapper.class})
@ComponentScan("ru.yandex.practicum.service.tag")
public class TagServiceConfiguration {

}
