package ru.yandex.practicum.view_model;

import io.micrometer.common.util.StringUtils;

public record PostFullViewDto(Long id,
                              String title,
                              String body,
                              long likeCounter,
                              String tags) {}
