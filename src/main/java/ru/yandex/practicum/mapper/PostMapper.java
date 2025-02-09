package ru.yandex.practicum.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.view_model.PostCreateDto;
import ru.yandex.practicum.view_model.PostFullViewDto;
import ru.yandex.practicum.view_model.PostPreviewDto;
import ru.yandex.practicum.view_model.PostUpdateDto;

import java.io.IOException;
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
                post.shortBody(),
                post.likeCounter(),
                post.tags(),
                post.image() != null);
    }

    public Post mapToPost(PostCreateDto postCreateDto) {
        return new Post(null,
                postCreateDto.title(),
                postCreateDto.body(),
                extractFirstLines(postCreateDto.body()),
                0L,
                postCreateDto.tags(),
                getImageBytes(postCreateDto.image()));
    }

    public Post mapToPost(PostUpdateDto postUpdateDto) {
        return new Post(postUpdateDto.id(),
                postUpdateDto.title(),
                postUpdateDto.body(),
                extractFirstLines(postUpdateDto.body()),
                0L,
                postUpdateDto.tags(),
                getImageBytes(postUpdateDto.image()));
    }

    public PostFullViewDto mapToPostFullDto(Post post) {
        return new PostFullViewDto(post.id(),
                post.title(),
                post.body(),
                post.likeCounter(),
                post.tags(),
                post.image() != null);
    }


    // PRIVATE

    private String extractFirstLines(String longString) {
        return longString.lines().limit(bodyPreviewNumberOfLines).collect(Collectors.joining("\n"));
    }

    private byte[] getImageBytes(MultipartFile multipartFile) {
        byte[] imageBytes = null;
        try {
            imageBytes = multipartFile.isEmpty() ? null : multipartFile.getBytes();
        } catch (IOException e) {
            // todo: LOG
        }
        return imageBytes;
    }

}