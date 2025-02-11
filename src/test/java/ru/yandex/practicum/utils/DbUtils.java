package ru.yandex.practicum.utils;

import org.springframework.jdbc.core.JdbcTemplate;

public class DbUtils {

    public static void cleanupDb(JdbcTemplate jdbcTemplate) {

        jdbcTemplate.execute("DELETE FROM posts_like_count");
        jdbcTemplate.execute("DELETE FROM comment_counter");
        jdbcTemplate.execute("DELETE FROM post_tag");
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");

    }

}
