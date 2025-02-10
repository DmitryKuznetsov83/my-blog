package ru.yandex.practicum.service.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.Post.PostRepository;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.Comment.CommentService;
import ru.yandex.practicum.service.Tag.TagService;
import ru.yandex.practicum.view_model.Post.PostCreateDto;
import ru.yandex.practicum.view_model.Post.PostFullViewDto;
import ru.yandex.practicum.view_model.Post.PostPreviewDto;
import ru.yandex.practicum.view_model.Post.PostUpdateDto;
import ru.yandex.practicum.view_model.Tag.TagDto;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImp implements PostService{

    private final PostRepository postRepository;
    private final TagService tagService;
    private final CommentService commentService;
    private final PostMapper postMapper;

    @Autowired
    public PostServiceImp(PostRepository postRepository, TagService tagService, CommentService commentService, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.tagService = tagService;
        this.commentService = commentService;
        this.postMapper = postMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostPreviewDto> findPostsByPage(int page, int size, String filterByTag) {
        return postMapper.mapToPostPreviewDtoList(postRepository.findPostByPage(page, size, filterByTag));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostFullViewDto> findPostById(Long id) {
        return postRepository.findPostById(id).map(postMapper::mapToPostFullDto);
    }

    @Override
    @Transactional
    public long createPost(PostCreateDto post) {
        long postId = postRepository.createPost(postMapper.mapToPost(post));
        postRepository.createLikeCounter(postId);
        commentService.createCommentCounter(postId);
        tagService.bindTagsToPost(postId, post.tags());
        return postId;
    }

    @Override
    @Transactional
    public boolean updatePost(PostUpdateDto post) {
        boolean successfulUpdate = postRepository.updatePost(postMapper.mapToPost(post));
        if (successfulUpdate) {
            tagService.updateTags(post.id(), post.tags());
        }
        return successfulUpdate;
        // WARNING post not found
    }

    @Override
    @Transactional
    public boolean deletePostById(Long id) {
        boolean success = postRepository.deleteLikeCounter(id);
        boolean success2 = commentService.deleteCommentCounter(id);
        tagService.unbindAllTagsFromPost(id);
        boolean result = postRepository.deletePostById(id);

        // WARNING post not found
        // WARNING counter not found
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public long getPagesTotalCount(Integer pageSize, String filterByTag) {
        long postsTotalCount = postRepository.postsCount(filterByTag);
        return (postsTotalCount - 1) / pageSize + 1;
    }

    @Override
    @Transactional
    public void likePost(Long id) {
        postRepository.likePost(id);
    }

    @Override
    public List<TagDto> findAllTags() {
        return tagService.findAllTags();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<byte[]> findImageByPostId(Long id) {
        return postRepository.findPostById(id).map(Post::image);
    }

}