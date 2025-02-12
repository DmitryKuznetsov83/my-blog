package ru.yandex.practicum.dao.post;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.io.ByteArrayInputStream;
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
            rs.getString("short_body"),
            rs.getLong("like_count"),
            rs.getLong("comment_count"),
            rs.getString("tags"),
            rs.getBytes("image")
    );

    @Autowired
    public JdbcNativePostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // POSTS
    @Override
    public List<Post> findPostByPage(int page, int size, String filterByTag) {
        int offset = (page - 1) * size;
        if (StringUtils.isBlank(filterByTag)) {
            String sql = """
                    SELECT p.id, p.title, p.body, p.short_body, pc.count AS like_count, cc.count AS comment_count, p.tags, p.image
                            FROM posts p 
                                LEFT JOIN posts_like_count pc ON p.id = pc.post_id 
                                LEFT JOIN comment_counter cc ON p.id = cc.post_id 
                            ORDER BY p.id DESC 
                            LIMIT ? OFFSET ?
                    """;
            return jdbcTemplate.query(sql, postRowMapper, size, offset);
        } else {
            String sql = """
                SELECT p.id, p.title, p.body, p.short_body, pc.count AS like_count, cc.count AS comment_count, p.tags, p.image
                        FROM posts p 
                            LEFT JOIN posts_like_count pc ON p.id = pc.post_id                
                            LEFT JOIN comment_counter cc ON p.id = cc.post_id 
                        WHERE p.id IN 
                              (SELECT post_id FROM post_tag pt JOIN tags t on pt.tag_id = t.id WHERE t.name = ?)
                        ORDER BY p.id DESC 
                        LIMIT ? OFFSET ?
                """;
            return jdbcTemplate.query(sql, postRowMapper, filterByTag, size, offset);
        }

    }

    @Override
    public Optional<Post> findPostById(long id) {
        String sql = """
                SELECT p.id, p.title, p.body, p.short_body, pc.count AS like_count, 0 AS comment_count, p.tags, p.image
                FROM posts p 
                    LEFT JOIN posts_like_count pc ON p.id = pc.post_id
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, postRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public long createPost(Post post) {
        String sql = "INSERT INTO posts(title, body, short_body, tags, image) VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        ByteArrayInputStream imageInputStream = Optional.ofNullable(post.image()).map(ByteArrayInputStream::new).orElse(null);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.title());
            ps.setString(2, post.body());
            ps.setString(3, post.shortBody());
            ps.setString(4, post.tags());
            ps.setBinaryStream(5, imageInputStream);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean updatePost(Post post) {

        Integer rowsAffected = null;
        if (post.image() != null) {
            String sql = "UPDATE posts SET title = ?, body = ?, short_body = ?, tags = ?, image = ? WHERE id = ?";
            rowsAffected = jdbcTemplate.update(sql, post.title(), post.body(), post.shortBody(), post.tags(), post.image(), post.id());
        } else {
            String sql = "UPDATE posts SET title = ?, body = ?, short_body = ?, tags = ? WHERE id = ?";
            rowsAffected = jdbcTemplate.update(sql, post.title(), post.body(), post.shortBody(), post.tags(), post.id());
        }

        return rowsAffected != 0;
    }

    @Override
    public boolean deletePostById(long id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
        return rowsAffected != 0;
    }

    @Override
    public long postsCount(String filterByTag) {
        if (StringUtils.isBlank(filterByTag)) {
            String sql = "SELECT COUNT(*) FROM posts";
            return jdbcTemplate.queryForObject(sql, long.class);
        } else {
            String sql = """
                    SELECT COUNT(*) FROM posts
                    WHERE id IN
                            (SELECT post_id FROM post_tag pt JOIN tags t on pt.tag_id = t.id WHERE t.name = ?)
                    """;
            return jdbcTemplate.queryForObject(sql, long.class, filterByTag);
        }
    }


    // LIKES
    @Override
    public void createLikeCounter(long postId) {
        String sql = "INSERT INTO posts_like_count(post_id, count) VALUES(?, 0)";
        jdbcTemplate.update(sql, postId);
    }

    @Override
    public boolean deleteLikeCounter(long id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM posts_like_count WHERE post_id = ?", id);
        return rowsAffected != 0;
    }

    @Override
    public void likePost(long postId) {
        String sql = "UPDATE posts_like_count SET count = count + 1  WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

}
