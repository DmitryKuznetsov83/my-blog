package ru.yandex.practicum.view_model;

import io.micrometer.common.util.StringUtils;

public record PostUpdateDto(Long id,
                            String title,
                            String body,
                            String tags) {}
