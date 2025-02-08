package ru.yandex.practicum.view_model;

import io.micrometer.common.util.StringUtils;

public record PostCreateDto(String title,
                            String body,
                            String tags) {
    public static PostCreateDto emptyPost() {
        return new PostCreateDto(null, null, null);
    }
}
