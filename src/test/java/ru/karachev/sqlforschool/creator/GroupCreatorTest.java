package ru.karachev.sqlforschool.creator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.karachev.sqlforschool.entity.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupCreatorTest {

    @InjectMocks
    private GroupCreator groupCreator ;

    @Mock
    private Random mockedRandom;

    @Test
    void createGroupsShouldReturnListOfGroupsWhenGettingIntMaxGroups(){
        List<Group> expected = new ArrayList<>();
        expected.add(Group.builder()
                .withGroupId(1)
                .withGroupName("AB-11")
                .build());
        expected.add(Group.builder()
                .withGroupId(2)
                .withGroupName("CD-22")
                .build());
        when(mockedRandom.nextInt(anyInt()))
                .thenReturn(0)
                .thenReturn(1)
                .thenReturn(11)
                .thenReturn(2)
                .thenReturn(3)
                .thenReturn(22);
        int maxId = 99;
        int maxGroups = 2;
        List<Group> actual = groupCreator.createGroups(maxId, maxGroups);
        assertThat(expected).isEqualTo(actual);
    }

}
