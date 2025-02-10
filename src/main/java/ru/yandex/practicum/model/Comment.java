package ru.yandex.practicum.model;

public record Comment(Long id,
                      Long postId,
                      String body) {}
