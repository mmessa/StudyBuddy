package dao;

import java.util.List;

/**
 * Created by mattmessa on 5/3/16.
 */
public class Course {

    private int courseId;
    private String name;
    private int number;
    private List groupIds;
    private List userIds;

    public Course(){

    }

    public Course(int courseId, String name, int number) {

        this.courseId = courseId;
        this.name = name;
        this.number = number;
    }

    public int getCourseId() {

        return courseId;
    }

    public String getName() {

        return name;
    }

    public int getNumber() {

        return number;
    }

    public List getGroupIds() {

        return groupIds;
    }

    public List getUserIds() {

        return userIds;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setGroupIds(List groupIds) {
        this.groupIds = groupIds;
    }

    public void setUserIds(List userIds) {
        this.userIds = userIds;
    }
}
