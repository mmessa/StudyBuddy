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
        this.userIds = null;
    }

    public int getGroupId() {

        return groupId;
    }

    public int getCourseId() {

        return courseId;
    }

    public String getName() {

        return name;
    }

    public List getUserIds() {

        return userIds;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserIds(List userIds) {
        this.userIds = userIds;
    }
}
