package ru.yandex.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.comment.CommentService;
import ru.yandex.practicum.dto.Comment.CommentCreateDto;
import ru.yandex.practicum.dto.Comment.CommentFullViewDto;

@Controller
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public String createComment(@PathVariable(name = "postId") long postId,
                              @RequestParam(name = "body") String body) {
        CommentCreateDto commentCreateDto = new CommentCreateDto(postId, body);
        commentService.createComment(commentCreateDto);
        return "redirect:/posts/" + postId;
    }

    @PostMapping(value = "/{commentId}", params = "_method=update")
    public String updateComment(@PathVariable(name = "postId") long postId,
                              @PathVariable(name = "commentId") long commentId,
                              @RequestParam(name = "body") String body) {
        CommentFullViewDto commentFullViewDto = new CommentFullViewDto(commentId, postId, body);
        return (commentService.updateComment(commentFullViewDto) ? "redirect:/posts/" + postId :  "status_404");
    }

    @PostMapping(value = "/{commentId}", params = "_method=delete")
    public String deleteComment(@PathVariable(name = "postId") long postId,
                              @PathVariable(name = "commentId") long commentId) {
        return (commentService.deleteCommentById(postId, commentId) ? "redirect:/posts/" + postId : "status_404");
    }
}
