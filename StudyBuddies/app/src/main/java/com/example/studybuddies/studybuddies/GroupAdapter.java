package com.example.studybuddies.studybuddies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dao.Group;

/**
 * Created by icebramz on 5/18/16.
 */
public class GroupAdapter extends ArrayAdapter<Group> {

    public GroupAdapter(Context context, ArrayList<Group> groups) { super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Group group = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_list_layout, parent, false);
        }

        TextView group_name = (TextView) convertView.findViewById(R.id.group_title);

        group_name.setText(group.getName());

        return convertView;
    }

}
