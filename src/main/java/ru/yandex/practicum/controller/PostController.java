package ru.yandex.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.view_model.PostFullViewDto;
import ru.yandex.practicum.view_model.PostPreviewDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
@Validated
public class PostController {

    private final PostService service;

    @Value("${myBlog.pagingOptions:10,20,50}")
    private String pagingOptionsString;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public String getPosts(Model model,
                           @RequestParam(name = "page", defaultValue = "1") @Positive Integer page,
                           @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {

        List<PostPreviewDto> posts = service.findPostsByPage(page, size);
        long pagesTotalCount = service.getPagesTotalCount(10);
        model.addAttribute("totalPages", pagesTotalCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("posts", posts);
        model.addAttribute("pageSizes", pagingOptions(pagingOptionsString));
        return "posts";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        PostFullViewDto emptyPost = PostFullViewDto.emptyPost();
        model.addAttribute("post", emptyPost);
        return "post";
    }

    @GetMapping("/{id}")
    public String getPost(Model model, @PathVariable(name = "id") Long id) {
        Optional<PostFullViewDto> maybePost = service.findPostById(id);
        if (maybePost.isPresent()) {
            model.addAttribute("post", maybePost.get());
            return "post";
        } else {
            return "status_404";
        }
    }

    @PostMapping
    public String createPost(@ModelAttribute @Valid PostFullViewDto post) {
        if (post.isValidForCreation()) {
            long postId = service.createPost(post);
            return "redirect:/posts/" + postId;
        } else {
            return "status_400";
        }
    }

    @PostMapping(params = "_method=update")
    public String updatePost(@ModelAttribute PostFullViewDto post) {
        if (post.isValidForUpdate()) {
            return (service.updatePost(post) ? "redirect:/posts/" + post.id() :  "status_404");
        } else {
            return "status_400";
        }
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String deletePost(@PathVariable(name = "id") Long id) {
        if (service.deletePostById(id)) {
            return "redirect:/posts";
        } else {
            return "status_404";
        }

    }

    // PRIVATE

    private static List<Integer> pagingOptions(String pagingOptionsString) {
        return Arrays.stream(pagingOptionsString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

}



// валидации
// todo: добавить валидации для dto и model
// todo: 404, 400 - details
// todo: logging
// todo: clean HTML
// todo: при закрытиии поста пагинация не слетает
// todo: page size в классе