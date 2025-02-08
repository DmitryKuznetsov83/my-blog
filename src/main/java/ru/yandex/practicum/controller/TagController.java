package ru.yandex.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.service.Tag.TagService;
import ru.yandex.practicum.view_model.TagDto;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String getTags(Model model) {

        List<TagDto> tags = tagService.findAllTags();
        model.addAttribute("tags", tags);
        return "tags";
    }

}