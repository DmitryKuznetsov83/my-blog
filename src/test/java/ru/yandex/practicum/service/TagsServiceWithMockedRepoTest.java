package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dao.tag.TagRepository;
import ru.yandex.practicum.dto.Tag.TagDto;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.service.tag.TagServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
public class TagsServiceWithMockedRepoTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Spy
    private TagMapper tagMapper = new TagMapper();

    @Test
    public void findAllTags_shouldAllTags() {
        List<Tag> mackTags = new ArrayList<>();
        mackTags.add(new Tag(1L, "tag_1"));
        mackTags.add(new Tag(2L, "tag_2"));
        mackTags.add(new Tag(3L, "tag_3"));

        Mockito.doReturn((mackTags))
                .when(tagRepository).findAllTags();

        List<TagDto> allTags = tagService.findAllTags();
        assertThat(allTags, hasSize(3));
        assertThat(allTags.getFirst().name(), is("tag_1"));
    }

}
