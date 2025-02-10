package ru.yandex.practicum.service.Comment;

import ru.yandex.practicum.view_model.Comment.CommentCreateDto;
import ru.yandex.practicum.view_model.Comment.CommentFullViewDto;

import java.util.List;

public interface CommentService {

    List<CommentFullViewDto> getCommentsByPostId(long postId);

    long createComment(CommentCreateDto commentCreateDto);

    boolean updateComment(CommentFullViewDto commentFullViewDto);

    boolean deleteCommentById(long postId, long commentId);

    void createCommentCounter(long postId);

    boolean deleteCommentCounter(long postId);
}