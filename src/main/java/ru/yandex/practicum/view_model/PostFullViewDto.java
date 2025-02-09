package ru.yandex.practicum.view_model;

public record PostFullViewDto(Long id,
                              String title,
                              String body,
                              long likeCounter,
                              String tags,
                              boolean hasImage) {}
