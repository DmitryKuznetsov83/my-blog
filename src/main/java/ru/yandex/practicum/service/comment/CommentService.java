package ru.yandex.practicum.service.comment;

import ru.yandex.practicum.dto.Comment.CommentCreateDto;
import ru.yandex.practicum.dto.Comment.CommentFullViewDto;

import java.util.List;

public interface CommentService {

    List<CommentFullViewDto> getCommentsByPostId(long postId);

    void createComment(CommentCreateDto commentCreateDto);

    boolean updateComment(CommentFullViewDto commentFullViewDto);

    boolean deleteCommentById(long postId, long commentId);

    void createCommentCounter(long postId);

    boolean deleteCommentCounter(long postId);
}