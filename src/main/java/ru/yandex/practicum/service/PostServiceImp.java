package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.PostRepository;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.view_model.PostFullViewDto;
import ru.yandex.practicum.view_model.PostPreviewDto;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImp implements PostService{

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostServiceImp(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public List<PostPreviewDto> findPostsByPage(int page, int size) {
        return postMapper.mapToPostPreviewDtoList(postRepository.findByPage(page, size));
    }

    @Override
    public Optional<PostFullViewDto> findPostById(Long id) {
        return postRepository.findById(id).map(postMapper::mapToPostFullDto);
    }

    @Override
    public long createPost(PostFullViewDto post) {
        return postRepository.create(postMapper.mapToPost(post));
    }

    @Override
    public boolean updatePost(PostFullViewDto post) {
        return postRepository.update(postMapper.mapToPost(post));
    }

    @Override
    public boolean deletePostById(Long id) {
        boolean result = postRepository.deleteById(id);
        // WARNING
        return result;
    }

    @Override
    public long getPagesTotalCount(Integer pageSize) {
        long postsTotalCount = postRepository.postsCount();
        return (postsTotalCount - 1) / pageSize + 1;
    }

}