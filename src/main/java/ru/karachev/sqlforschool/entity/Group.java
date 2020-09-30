package ru.karachev.sqlforschool.entity;

import java.util.Objects;

public class Group {

    private final Integer groupId;
    private final String groupName;

    private Group(Builder builder) {
        this.groupId = builder.groupId;
        this.groupName = builder.groupName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        return Objects.equals(groupId, group.groupId) &&
                Objects.equals(groupName, group.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName);
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                '}';
    }

    public static class Builder {
        private Integer groupId;
        private String groupName;

        public Builder withGroupId(Integer groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withGroupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public Group build() {
            return new Group(this);
        }
    }
}
