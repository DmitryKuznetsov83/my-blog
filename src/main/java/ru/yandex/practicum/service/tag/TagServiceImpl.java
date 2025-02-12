package ru.yandex.practicum.service.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.tag.TagRepository;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.dto.Tag.TagDto;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto> findAllTags() {
        return tagMapper.toTagDtoList(tagRepository.findAllTags());
    }

    @Override
    @Transactional
    public void bindTagsToPost(long postId, String tags) {
        List<String> tagsInPostAsString = extractTagsFormString(tags);
        if (tagsInPostAsString.isEmpty()) {
            return;
        }

        List<Tag> tagsFoundInDb = tagRepository.findTagsByName(tagsInPostAsString);

        List<String> tagsFoundInDbAsString = tagsFoundInDb.stream().map(Tag::name).toList();
        List<String> tagsNotFoundInDbAsString = new ArrayList<>(tagsInPostAsString);
        tagsNotFoundInDbAsString.removeAll(tagsFoundInDbAsString);

        List<Long> tagsFoundInDbIds = tagsFoundInDb.stream().map(Tag::id).toList();
        List<Long> tagsCreatedIds = tagRepository.createTags(tagsNotFoundInDbAsString);

        List<Long> tagsFoundAndCreatedIds = new ArrayList<>();
        tagsFoundAndCreatedIds.addAll(tagsFoundInDbIds);
        tagsFoundAndCreatedIds.addAll(tagsCreatedIds);
        tagRepository.bindTagsToPost(postId, tagsFoundAndCreatedIds);

    }

    @Override
    @Transactional
    public void updateTags(long postId, String tags) {
        List<String> tagsInPostAsString = extractTagsFormString(tags);
        List<Tag> tagsFoundInDb = tagRepository.findTagsByName(tagsInPostAsString);

        List<String> tagsFoundInDbAsString = tagsFoundInDb.stream().map(Tag::name).toList();
        List<String> tagsNotFoundInDbAsString = new ArrayList<>(tagsInPostAsString);
        tagsNotFoundInDbAsString.removeAll(tagsFoundInDbAsString);

        List<Long> tagsFoundInDbIds = tagsFoundInDb.stream().map(Tag::id).toList();
        List<Long> tagsCreatedIds = tagRepository.createTags(tagsNotFoundInDbAsString);

        List<Long> tagsFoundAndCreatedIds = new ArrayList<>();
        tagsFoundAndCreatedIds.addAll(tagsFoundInDbIds);
        tagsFoundAndCreatedIds.addAll(tagsCreatedIds);

        List<Long> boundedTags = tagRepository.findBoundedTags(postId);

        List<Long> tagsToBind = new ArrayList<>(tagsFoundAndCreatedIds);
        tagsToBind.removeAll(boundedTags);

        List<Long> tagsToUnbind = new ArrayList<>(boundedTags);
        tagsToUnbind.removeAll(tagsFoundAndCreatedIds);

        tagRepository.bindTagsToPost(postId, tagsToBind);
        tagRepository.unbindTagsFromPost(postId, tagsToUnbind);
    }

    @Override
    @Transactional
    public void unbindAllTagsFromPost(long postId) {
        tagRepository.unbindAllTagsFromPost(postId);
    }

    // PRIVATE
    private List<String> extractTagsFormString(String tags) {
        String splitter = " ";
        return Arrays.stream(tags.split(splitter))
                .map(String::trim)
                .distinct()
                .filter(tag -> !tag.isBlank())
                .toList();
    }

}
