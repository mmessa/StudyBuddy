package dao;

/**
 * Created by mattmessa on 4/28/16.
 */
public class User {

    private String name;
    private String email;

    public User(){

    }

    public User(String name, String email) {

        this.name = name;
        this.email = email;
    }

    String getUserName() {

        return name;
    }

    String getUserEmail() {

        return email;
    }
}
