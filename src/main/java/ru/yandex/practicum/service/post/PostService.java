package ru.yandex.practicum.service.post;

import ru.yandex.practicum.view_model.Post.PostCreateDto;
import ru.yandex.practicum.view_model.Post.PostFullViewDto;
import ru.yandex.practicum.view_model.Post.PostPreviewDto;
import ru.yandex.practicum.view_model.Post.PostUpdateDto;
import ru.yandex.practicum.view_model.Tag.TagDto;

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

    Optional<byte[]> findImageByPostId(Long id);
}
