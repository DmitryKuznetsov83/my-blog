package ru.yandex.practicum.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.view_model.PostFullViewDto;
import ru.yandex.practicum.view_model.PostPreviewDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    @Value("${myBlog.bodyPreviewNumberOfLines:3}")
    private Integer bodyPreviewNumberOfLines;

    public List<PostPreviewDto> mapToPostPreviewDtoList(List<Post> posts) {
        return posts.stream().map(this::mapToPostPreviewDto).toList();
    }

    public PostPreviewDto mapToPostPreviewDto(Post post) {
        return new PostPreviewDto(post.id(),
                post.title(),
                post.shortBody());
    }

    public Post mapToPost(PostFullViewDto postFullViewDto) {
        return new Post(postFullViewDto.id(),
                postFullViewDto.title(),
                postFullViewDto.body(),
                extractFirstLines(postFullViewDto.body()));
    }

    public PostFullViewDto mapToPostFullDto(Post post) {
        return new PostFullViewDto(post.id(),
                post.title(),
                post.body());
    }


    // PRIVATE

    private String extractFirstLines(String longString) {
        return longString.lines().limit(bodyPreviewNumberOfLines).collect(Collectors.joining("\n"));
    }

}