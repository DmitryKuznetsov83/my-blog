package ru.yandex.practicum.dao.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcNativeCommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Comment> postRowMapper = (rs, rowNum) -> new Comment(
            rs.getLong("id"),
            rs.getLong("post_id"),
            rs.getString("body")
    );

    @Autowired
    public JdbcNativeCommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        String sql = "SELECT id, post_id, body FROM comments WHERE post_id = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, postRowMapper, postId);
    }

    @Override
    public void createComment(Comment comment) {
        String sql = "INSERT INTO comments(post_id, body) VALUES(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, comment.postId());
            ps.setString(2, comment.body());
            return ps;
        }, keyHolder);

        keyHolder.getKeys().get("id");
    }

    @Override
    public boolean updateComment(Comment comment) {
        String sql = "UPDATE comments SET body = ? WHERE post_id = ? AND id = ?";
        int rowsAffected = jdbcTemplate.update(sql, comment.body(), comment.postId(), comment.id());
        return rowsAffected != 0;
    }

    @Override
    public boolean deleteCommentById(Long postId, Long commentId) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM comments WHERE post_id = ? AND id = ?", postId, commentId);
        return rowsAffected != 0;
    }

    @Override
    public void createCommentCounter(long postId) {
        String sql = "INSERT INTO comment_counter(post_id, count) VALUES(?, 0)";
        jdbcTemplate.update(sql, postId);
    }

    @Override
    public boolean deleteCommentCounter(long postId) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM comment_counter WHERE post_id = ?", postId);
        return rowsAffected != 0;
    }

    @Override
    public void increaseCommentCounter(long postId) {
        String sql = "UPDATE comment_counter SET count = count + 1  WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

    @Override
    public void decreaseCommentCounter(Long postId) {
        String sql = "UPDATE comment_counter SET count = count - 1  WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

}
