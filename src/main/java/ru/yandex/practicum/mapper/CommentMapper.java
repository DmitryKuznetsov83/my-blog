package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.dto.Comment.CommentCreateDto;
import ru.yandex.practicum.dto.Comment.CommentFullViewDto;

import java.util.List;

@Service
public class CommentMapper {
    public List<CommentFullViewDto> mapToCommentFullViewDtoList(List<Comment> comments) {
        return comments.stream().map(this::mapToCommentFullViewDto).toList();
    }

    private CommentFullViewDto mapToCommentFullViewDto(Comment comment) {
        return new CommentFullViewDto(comment.id(), comment.postId(), comment.body());
    }

    public Comment mapToComment(CommentCreateDto commentCreateDto) {
        return new Comment(null, commentCreateDto.postId(), commentCreateDto.body());
    }

    public Comment mapToComment(CommentFullViewDto commentFullViewDto) {
        return new Comment(commentFullViewDto.id(), commentFullViewDto.postId(), commentFullViewDto.body());
    }
}
