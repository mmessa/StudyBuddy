package dao;

import java.util.List;

/**
 * Created by mattmessa on 5/3/16.
 */
public class Group {

    private int groupId;
    private int courseId;
    private String name;
    private List userIds;

    public Group(){

    }

    public Group(int groupId, int courseId, String name) {

        this.groupId = groupId;
        this.courseId = courseId;
        this.name = name;
    }

    int getGroupId() {

        return groupId;
    }

    int getGroupCourseId() {

        return courseId;
    }

    String getGroupName() {

        return name;
    }

    List getGroupUserIds() {

        return userIds;
    }


}
