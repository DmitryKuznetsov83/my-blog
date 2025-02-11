package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.configuration.service.TagServiceConfiguration;
import ru.yandex.practicum.dao.tag.TagRepository;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.service.tag.TagService;
import ru.yandex.practicum.view_model.Tag.TagDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TagServiceConfiguration.class)
@ActiveProfiles("mock_repo")
public class TagsServiceWithMockedRepoTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

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
