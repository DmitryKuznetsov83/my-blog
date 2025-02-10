package ru.yandex.practicum.model;

public record Post(Long id,
                   String title,
                   String body,
                   String shortBody,
                   Long likeCounter,
                   Long commentCounter,
                   String tags,
                   byte[] image) {}