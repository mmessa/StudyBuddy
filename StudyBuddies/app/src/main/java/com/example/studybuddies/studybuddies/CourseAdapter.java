package com.example.studybuddies.studybuddies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dao.Course;

/**
 * Created by icebramz on 5/15/16.
 */
public class CourseAdapter extends ArrayAdapter<Course> {

    public CourseAdapter(Context context, ArrayList<Course> courses){
        super(context, 0, courses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Course course = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.course_list_layout, parent, false);
        }

        TextView course_name = (TextView) convertView.findViewById(R.id.course_title);
        TextView course_number = (TextView) convertView.findViewById(R.id.course_number);

        course_name.setText(course.getName());
        //int course_number_int = course.getCourseNum();
        String course_number_string = Integer.toString(course.getNumber());
        course_number.setText(course_number_string);

        return convertView;
    }

}
