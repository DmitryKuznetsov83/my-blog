package ru.yandex.practicum.service.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.comment.CommentRepository;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.view_model.Comment.CommentCreateDto;
import ru.yandex.practicum.view_model.Comment.CommentFullViewDto;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentFullViewDto> getCommentsByPostId(long postId) {
        return commentMapper.mapToCommentFullViewDtoList(commentRepository.getCommentsByPostId(postId));
    }

    @Override
    @Transactional
    public void createComment(CommentCreateDto commentCreateDto) {
        commentRepository.createComment(commentMapper.mapToComment(commentCreateDto));
        commentRepository.increaseCommentCounter(commentCreateDto.postId());
    }

    @Override
    @Transactional
    public boolean updateComment(CommentFullViewDto commentFullViewDto) {
        return commentRepository.updateComment(commentMapper.mapToComment(commentFullViewDto));
    }

    @Override
    @Transactional
    public boolean deleteCommentById(long postId, long commentId) {
        boolean success = commentRepository.deleteCommentById(postId, commentId);
        if (success) {
            commentRepository.decreaseCommentCounter(postId);
        }
        return success;
    }

    @Override
    public void createCommentCounter(long postId) {
        commentRepository.createCommentCounter(postId);
    }

    @Override
    public boolean deleteCommentCounter(long postId) {
        return  commentRepository.deleteCommentCounter(postId);
    }
}
