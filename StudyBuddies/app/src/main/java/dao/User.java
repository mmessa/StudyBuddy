package dao;

import java.util.List;

/**
 * Created by mattmessa on 4/28/16.
 */
public class User {

    private int userId;
    private String name;
    private String email;
    private List groupIds;
    private List courseIds;

    public User(){

    }

    public User(int userId, String name, String email) {

        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    int getUserId() {

        return userId;
    }
    String getUserName() {

        return name;
    }

    String getUserEmail() {

        return email;
    }

    List getUserGroupIds() {

        return groupIds;
    }

    List getUserCourseIds() {

        return courseIds;
    }
}

