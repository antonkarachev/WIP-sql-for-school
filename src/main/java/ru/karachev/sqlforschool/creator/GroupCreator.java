package ru.karachev.sqlforschool.creator;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.entity.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupCreator {

    private static final Logger LOGGER = Logger.getLogger(GroupCreator.class);
    private static final int MAX_GROUPS = 10;
    private static final int PREFIX_LENGTH = 2;
    private static final int MAX_ID = 99;
    private static final String DASH = "-";
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static int getMaxGroups() {
        return MAX_GROUPS;
    }

    public List<Group> createGroups(Random random) {
        List<Group> groups = new ArrayList<>();

        for (int i = 0; i < MAX_GROUPS; i++) {
            groups.add(Group.builder()
                    .withGroupId(i + 1)
                    .withGroupName(createName(random))
                    .build());
        }

        LOGGER.info("Creating groups complete");
        return groups;
    }

    private String createName(Random random) {
        StringBuilder name = new StringBuilder();

        for (int i = 0; i < PREFIX_LENGTH; i++) {
            name.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return name.append(DASH)
                .append(random.nextInt(MAX_ID)).toString().toUpperCase();
    }

}
