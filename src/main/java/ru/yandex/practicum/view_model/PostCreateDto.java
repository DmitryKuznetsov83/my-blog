package ru.yandex.practicum.view_model;

import org.springframework.web.multipart.MultipartFile;

public record PostCreateDto(String title,
                            String body,
                            String tags,
                            MultipartFile image) {
    public static PostCreateDto emptyPost() {
        return new PostCreateDto(null, null, null, null);
    }
}
