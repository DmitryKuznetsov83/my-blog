package ru.yandex.practicum.view_model.Comment;

public record CommentCreateDto(Long postId,
                               String body) {}