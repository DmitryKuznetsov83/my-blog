package ru.yandex.practicum.service.Post;

import ru.yandex.practicum.view_model.*;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostPreviewDto> findPostsByPage(int page, int size, String filterByTag);

    Optional<PostFullViewDto> findPostById(Long id);

    long createPost(PostCreateDto post);

    boolean updatePost(PostUpdateDto post);

    boolean deletePostById(Long id);

    long getPagesTotalCount(Integer pageSize, String filterByTag);

    void likePost(Long id);

    List<TagDto> findAllTags();
}
