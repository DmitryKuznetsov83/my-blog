package ru.yandex.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.comment.CommentService;
import ru.yandex.practicum.service.post.PostService;
import ru.yandex.practicum.dto.Comment.CommentFullViewDto;
import ru.yandex.practicum.dto.Post.PostCreateDto;
import ru.yandex.practicum.dto.Post.PostFullViewDto;
import ru.yandex.practicum.dto.Post.PostPreviewDto;
import ru.yandex.practicum.dto.Post.PostUpdateDto;
import ru.yandex.practicum.dto.Tag.TagDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
@Validated
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @Value("${myBlog.pagingOptions:10,20,50}")
    private String pagingOptionsString;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }


    // POSTS
    @GetMapping
    public String getPosts(Model model,
                           @RequestParam(name = "page", defaultValue = "1") @Positive Integer page,
                           @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                           @RequestParam(name = "filterByTag", required = false) String filterByTag) {

        List<PostPreviewDto> posts = postService.findPostsByPage(page, size, filterByTag);
        long pagesTotalCount = postService.getPagesTotalCount(size, filterByTag);
        List<String> tags = postService.findAllTags().stream().map(TagDto::name).sorted(Comparator.naturalOrder()).toList();

        model.addAttribute("totalPages", pagesTotalCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("posts", posts);
        model.addAttribute("pageSizes", pagingOptions(pagingOptionsString));
        model.addAttribute("tags", tags);
        model.addAttribute("currentTag", filterByTag);
        return "posts";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("mode", "new");
        model.addAttribute("post", new PostUpdateDto(null,null, null, null, null));
        return "post";
    }

    @GetMapping("/{id}")
    public String getPost(Model model, @PathVariable(name = "id") long id) {
        Optional<PostFullViewDto> maybePost = postService.findPostById(id);
        if (maybePost.isPresent()) {
            List<CommentFullViewDto> comments = commentService.getCommentsByPostId(id);
            model.addAttribute("mode", "update");
            model.addAttribute("post", maybePost.get());
            model.addAttribute("comments", comments);
            return "post";
        } else {
            return "status_404";
        }
    }

    @PostMapping
    public String createPost(@ModelAttribute @Valid PostCreateDto post) {
        long postId = postService.createPost(post);
        return "redirect:/posts/" + postId;
    }

    @PostMapping(params = "_method=update")
    public String updatePost(@ModelAttribute PostUpdateDto post) {
        return (postService.updatePost(post) ? "redirect:/posts/" + post.id() :  "status_404");
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String deletePost(@PathVariable(name = "id") long id) {
        return (postService.deletePostById(id) ? "redirect:/posts" : "status_404");
    }


    // LIKES
    @PostMapping(value = "/{id}/like")
    public String likePost(@PathVariable(name = "id") long id) {
        postService.likePost(id);
        return "redirect:/posts/" + id;
    }


    // IMAGES
    @GetMapping("{id}/image")
    public ResponseEntity<ByteArrayResource> images(@PathVariable(name = "id") long id) {
        Optional<byte[]> image = postService.findImageByPostId(id);
        if (image.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(image.get());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // или IMAGE_PNG
                .body(resource);
    }


    // PRIVATE
    private static List<Integer> pagingOptions(String pagingOptionsString) {
        return Arrays.stream(pagingOptionsString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

}