package ru.yandex.practicum.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcNativePostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Post> postRowMapper = (rs, rowNum) -> new Post(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("body"),
            rs.getString("short_body")
    );

    @Autowired
    public JdbcNativePostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Post> findByPage(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT id, title, body, short_body FROM posts ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, postRowMapper, size, offset);
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT id, title, body, short_body FROM posts WHERE id = ?";
        return jdbcTemplate.query(sql, postRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public long create(Post post) {
        String sql = "INSERT INTO posts(title, body, short_body) VALUES(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();


        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.title());
            ps.setString(2, post.body());
            ps.setString(3, post.shortBody());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean update(Post post) {
        String sql = "UPDATE posts SET title = ?, body = ?, short_body = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, post.title(), post.body(), post.shortBody(), post.id());
        return rowsAffected != 0;
    }

    @Override
    public boolean deleteById(Long id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
        return rowsAffected != 0;
    }

    @Override
    public long postsCount() {
        String sql = "SELECT COUNT(*) FROM posts";
        return jdbcTemplate.queryForObject(sql, long.class);
    }

}
