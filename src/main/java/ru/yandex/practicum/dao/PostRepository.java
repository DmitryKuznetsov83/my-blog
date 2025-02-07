package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(Long id);

    long create(Post post);

    boolean update(Post post);

    boolean deleteById(Long id);

    List<Post> findByPage(int page, int size);

    long postsCount();

}