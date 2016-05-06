package dao;

/**
 * Created by mattmessa on 5/3/16.
 */
public class Group {

    private int groupId;
    private String name;
    //private int ;

    public Group(){

    }

    public Group(int groupId, String name, int courseNum) {

        this.groupId = groupId;
        this.name = name;
        //this.courseNum = courseNum;
    }

    int getGroupId() {

        return groupId;
    }

    String getGroupName() {

        return name;
    }


}
