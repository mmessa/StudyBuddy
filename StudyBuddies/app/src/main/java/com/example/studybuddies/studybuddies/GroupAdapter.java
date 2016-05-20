package com.example.studybuddies.studybuddies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dao.Course;
import dao.Group;

/**
 * Created by Mark on 5/18/2016.
 */
public class GroupAdapter extends ArrayAdapter<Group> {

    public GroupAdapter(Context context, ArrayList<Group> groups){
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Group group = getItem(position);
        Course course = MainActivity.daoService.getCourse(group.getCourseId());

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_list_layout, parent, false);
        }

        TextView course_number_and_title = (TextView) convertView.findViewById(R.id.group_list_course_title);
        TextView group_title = (TextView) convertView.findViewById(R.id.group_list_group_title);

        group_title.setText(group.getName());
        course_number_and_title.setText(Integer.toString(course.getNumber())+" "+course.getName());

        return convertView;

    }

}
