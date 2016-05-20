package dao;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mattmessa on 5/3/16.
 */
public class Group {

    private int groupId;
    private int courseId;
    private String name;
    private List userIds;
    private HashMap<String,double[]> userLatLngs;

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

    public HashMap<String, double[]> getUserLatLngs(){
        return this.userLatLngs;
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

    public void setUserLatLngs(HashMap<String,double[]> latlngs) {
        this.userLatLngs = latlngs;
    }

}
