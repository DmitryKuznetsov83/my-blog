package ru.yandex.practicum.dao.Post;

import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> findPostById(Long id);

    long createPost(Post post);

    boolean updatePost(Post post);

    boolean deletePostById(Long id);

    List<Post> findPostByPage(int page, int size, String filterByTag);

    long postsCount(String filterByTag);

    void createLikeCounter(long postId);

    boolean deleteLikeCounter(Long id);

    long likePost(long postId);
}