package ru.yandex.practicum.dao.tag;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Tag;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class JdbcNativeTagRepository implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Tag> tagRowMapper = (rs, rowNum) -> new Tag(
            rs.getLong("id"),
            rs.getString("name")
    );

    public JdbcNativeTagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> findAllTags() {
        String sql = "SELECT id, name FROM tags ORDER BY id";
        return jdbcTemplate.query(sql, tagRowMapper);
    }

    @Override
    public List<Tag> findTagsByName(List<String> tagsInPostAsString) {
        if (tagsInPostAsString.isEmpty()) {
            return Collections.emptyList();
        }
        String placeholders = String.join(",", tagsInPostAsString.stream().map(s -> "?").toList());
        String sql = "SELECT id, name FROM tags WHERE name IN (" + placeholders + ")";
        return jdbcTemplate.query(sql, tagRowMapper, tagsInPostAsString.toArray());
    }

    @Override
    public List<Long> createTags(List<String> tags) {
        String sql = "INSERT INTO tags(name) VALUES(?)";
        List<Long> generatedIds = new ArrayList<>();
        tags.forEach((tag) -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tag);
                return ps;
            }, keyHolder);
            if (keyHolder.getKey() != null) {
                generatedIds.add(keyHolder.getKey().longValue());
            }
        });

        return generatedIds;
    }

    @Override
    public List<Long> findBoundedTags(long postId) {

        String sql = "SELECT tag_id FROM post_tag WHERE post_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, postId);

    }

    @Override
    public void bindTagsToPost(long postId, List<Long> tagIds) {
        if (tagIds.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO post_tag(tag_id, post_id) VALUES(?, ?)";

        List<Object[]> batchArgs = tagIds.stream()
                .map(tagId -> new Object[]{tagId, postId})
                .toList();

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public void unbindTagsFromPost(long postId, List<Long> tagIds) {
        if (tagIds.isEmpty()) {
            return;
        }
        String placeholders = String.join(",", tagIds.stream().map(s -> "?").toList());
        String sql = "DELETE FROM post_tag WHERE post_id = ? AND tag_id IN (" + placeholders + ")";
        Object[] params = new Object[tagIds.size() + 1];
        params[0] = postId;
        for (int i = 0; i < tagIds.size(); i++) {
            params[i + 1] = tagIds.get(i);
        }
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void unbindAllTagsFromPost(long postId) {
        String sql = "DELETE FROM post_tag WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

}