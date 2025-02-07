package ru.yandex.practicum.service;

import ru.yandex.practicum.view_model.PostFullViewDto;
import ru.yandex.practicum.view_model.PostPreviewDto;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostPreviewDto> findPostsByPage(int page, int size);

    Optional<PostFullViewDto> findPostById(Long id);

    long createPost(PostFullViewDto post);

    boolean updatePost(PostFullViewDto post);

    boolean deletePostById(Long id);

    long getPagesTotalCount(Integer pageSize);

}
