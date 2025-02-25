package ru.yandex.practicum.dto.Post;

public record PostFullViewDto(Long id,
                              String title,
                              String body,
                              long likeCounter,
                              String tags,
                              boolean hasImage) {}
