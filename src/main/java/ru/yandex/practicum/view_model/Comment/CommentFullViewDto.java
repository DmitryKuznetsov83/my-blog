package ru.yandex.practicum.view_model.Comment;

public record CommentFullViewDto (Long id,
                                  Long postId,
                                  String body) {}