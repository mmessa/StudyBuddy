package dao;

import java.util.List;

/**
 * Created by mattmessa on 4/28/16.
 */
public class User {

    private String name;
    private String email;
    private List groupIds;
    private List courseIds;

    public User(){}

    public User(String name, String email) {

        this.name = name;
        this.email = email;
        this.groupIds = null;
        this.courseIds = null;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setGroupIds(List groupIds) {
        this.groupIds = groupIds;
    }

    public void setCourseIds(List courseIds) {
        this.courseIds = courseIds;
    }


    public String getEmail() {

        return email;
    }


    public List getGroupIds() {

        return groupIds;
    }

    public List getCourseIds() {

        return courseIds;
    }

}

