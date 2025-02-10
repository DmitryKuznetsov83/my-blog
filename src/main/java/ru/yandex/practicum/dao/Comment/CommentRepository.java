package ru.yandex.practicum.dao.Comment;

import ru.yandex.practicum.model.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> getCommentsByPostId(Long postId);

    long createComment(Comment comment);

    boolean updateComment(Comment comment);

    boolean deleteCommentById(Long postId, Long commentId);

    void createCommentCounter(long postId);

    boolean deleteCommentCounter(long postId);

    void increaseCommentCounter(long commentId);

    void decreaseCommentCounter(Long commentId);

}