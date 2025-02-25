package ru.yandex.practicum.dto.Comment;

public record CommentCreateDto(Long postId,
                               String body) {}