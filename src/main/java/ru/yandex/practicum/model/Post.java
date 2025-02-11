package ru.yandex.practicum.model;

public record Post(Long id,
                   String title,
                   String body,
                   String shortBody,
                   long likeCounter,
                   long commentCounter,
                   String tags,
                   byte[] image) {}