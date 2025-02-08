package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.view_model.TagDto;

import java.util.List;

@Component
public class TagMapper {
    public List<TagDto> toTagDtoList(List<Tag> tagsDto) {
        return tagsDto.stream().map(this::toTagDto).toList();
    }

    private TagDto toTagDto(Tag tag) {
        return new TagDto(tag.id(), tag.name());
    }
}