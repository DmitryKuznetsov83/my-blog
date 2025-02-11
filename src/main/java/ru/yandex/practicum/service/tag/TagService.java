package ru.yandex.practicum.service.tag;

import ru.yandex.practicum.view_model.Tag.TagDto;

import java.util.List;

public interface TagService {

    List<TagDto> findAllTags();

    void bindTagsToPost(long postId, String tags);

    void updateTags(long postId, String tags);

    void unbindAllTagsFromPost(Long postId);

}