package ru.yandex.practicum.view_model.Post;

public record PostPreviewDto(Long id,
                             String title,
                             String shortBody,
                             long likeCounter,
                             long commentCounter,
                             String tags,
                             boolean hasImage) {}
