package ru.yandex.practicum.dao.tag;

import ru.yandex.practicum.model.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> findAllTags();

    List<Tag> findTagsByName(List<String> tagsInPostAsString);

    List<Long> createTags(List<String> tags);

    List<Long> findBoundedTags(long postId);

    void bindTagsToPost(long postId, List<Long> tagIds);

    void unbindTagsFromPost(long postId, List<Long> tagIds);

    void unbindAllTagsFromPost(long postId);

}