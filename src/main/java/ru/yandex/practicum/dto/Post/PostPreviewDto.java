package ru.yandex.practicum.dto.Post;

public record PostPreviewDto(Long id,
                             String title,
                             String shortBody,
                             long likeCounter,
                             long commentCounter,
                             String tags,
                             boolean hasImage) {}
