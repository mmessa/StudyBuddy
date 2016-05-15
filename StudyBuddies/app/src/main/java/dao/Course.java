package dao;

/**
 * Created by mattmessa on 5/3/16.
 */
public class Course {

    private int courseId;
    private String courseName;
    private int courseNum;

    public Course(){

    }

    public Course(int courseId, String courseName, int courseNum) {

        this.courseId = courseId;
        this.courseName = courseName;
        this.courseNum = courseNum;
    }

    public int getCourseId() {

        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCourseNum() {

        return courseNum;
    }
}
