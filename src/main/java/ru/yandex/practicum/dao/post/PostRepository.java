package ru.yandex.practicum.dao.post;

import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> findPostById(long id);

    long createPost(Post post);

    boolean updatePost(Post post);

    boolean deletePostById(long id);

    List<Post> findPostByPage(int page, int size, String filterByTag);

    long postsCount(String filterByTag);

    void createLikeCounter(long postId);

    boolean deleteLikeCounter(long id);

    void likePost(long postId);
}