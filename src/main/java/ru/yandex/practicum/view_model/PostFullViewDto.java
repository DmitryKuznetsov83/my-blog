package ru.yandex.practicum.view_model;

import io.micrometer.common.util.StringUtils;

public record PostFullViewDto(Long id, String title, String body) {

    public static PostFullViewDto emptyPost() {
        return new PostFullViewDto(null, null, null);
    }

    public boolean isValidForUpdate() {
        return StringUtils.isNotBlank(title)
                && StringUtils.isNotBlank(body)
                && id != null;
    }

    public boolean isValidForCreation() {
        return StringUtils.isNotBlank(title)
                && StringUtils.isNotBlank(body)
                && id == null;
    }

}
