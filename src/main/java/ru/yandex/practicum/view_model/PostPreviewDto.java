package ru.yandex.practicum.view_model;

public record PostPreviewDto(Long id,
                             String title,
                             String shortBody,
                             long likeCounter,
                             String tags) {}
