package ru.yandex.practicum.service.Tag;

import ru.yandex.practicum.view_model.TagDto;

import java.util.List;

public interface TagService {

    List<TagDto> findAllTags();

    void bindTagsToPost(long postId, String tags);

    void updateTags(long postId, String tags);

    void unbindAllTagsFromPost(Long postId);

}