package ru.yandex.practicum.dao.comment;

import ru.yandex.practicum.model.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> getCommentsByPostId(long postId);

    void createComment(Comment comment);

    boolean updateComment(Comment comment);

    boolean deleteCommentById(long postId, long commentId);

    void createCommentCounter(long postId);

    boolean deleteCommentCounter(long postId);

    void increaseCommentCounter(long commentId);

    void decreaseCommentCounter(long commentId);

}