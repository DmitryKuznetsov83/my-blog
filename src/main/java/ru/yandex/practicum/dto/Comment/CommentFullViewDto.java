package ru.yandex.practicum.dto.Comment;

public record CommentFullViewDto (Long id,
                                  Long postId,
                                  String body) {}