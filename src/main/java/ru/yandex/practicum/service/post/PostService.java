package ru.yandex.practicum.service.post;

import ru.yandex.practicum.dto.Post.PostCreateDto;
import ru.yandex.practicum.dto.Post.PostFullViewDto;
import ru.yandex.practicum.dto.Post.PostPreviewDto;
import ru.yandex.practicum.dto.Post.PostUpdateDto;
import ru.yandex.practicum.dto.Tag.TagDto;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostPreviewDto> findPostsByPage(int page, int size, String filterByTag);

    Optional<PostFullViewDto> findPostById(long id);

    long createPost(PostCreateDto post);

    boolean updatePost(PostUpdateDto post);

    boolean deletePostById(long id);

    long getPagesTotalCount(Integer pageSize, String filterByTag);

    void likePost(long id);

    List<TagDto> findAllTags();

    Optional<byte[]> findImageByPostId(long id);
}
