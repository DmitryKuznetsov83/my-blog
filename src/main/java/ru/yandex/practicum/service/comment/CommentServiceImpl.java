package ru.yandex.practicum.service.comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.comment.CommentRepository;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.dto.Comment.CommentCreateDto;
import ru.yandex.practicum.dto.Comment.CommentFullViewDto;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

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
        boolean successfulUpdate = commentRepository.updateComment(commentMapper.mapToComment(commentFullViewDto));
        if (!successfulUpdate) {
            log.warn("Comment id={}, postId={} not found", commentFullViewDto.id(), commentFullViewDto.postId());
        }
        return successfulUpdate;
    }

    @Override
    @Transactional
    public boolean deleteCommentById(long postId, long commentId) {
        boolean successfulDeletion = commentRepository.deleteCommentById(postId, commentId);
        if (successfulDeletion) {
            commentRepository.decreaseCommentCounter(postId);
        } else {
            log.warn("Comment id={}, postId={} not found", commentId, postId);
        }
        return successfulDeletion;
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
