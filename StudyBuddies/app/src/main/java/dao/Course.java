package dao;

/**
 * Created by mattmessa on 5/3/16.
 */
public class Course {

    private int courseId;
    private String name;
    private int number;

    public Course(){

    }

    public Course(int courseId, String name, int number) {

        this.courseId = courseId;
        this.name = name;
        this.number = number;
    }

    int getCourseId() {

        return courseId;
    }

    String getCourseName() {

        return name;
    }

    int getCourseNum() {

        return number;
    }
}
