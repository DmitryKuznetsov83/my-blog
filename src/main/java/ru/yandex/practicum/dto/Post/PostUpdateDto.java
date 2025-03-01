package ru.yandex.practicum.dto.Post;

import org.springframework.web.multipart.MultipartFile;

public record PostUpdateDto(Long id,
                            String title,
                            String body,
                            String tags,
                            MultipartFile image) {}
