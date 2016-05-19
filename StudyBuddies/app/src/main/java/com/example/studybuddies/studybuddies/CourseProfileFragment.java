package com.example.studybuddies.studybuddies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;

import dao.Course;
import dao.Group;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CourseProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseProfileFragment newInstance(String param1, String param2) {
        CourseProfileFragment fragment = new CourseProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_course_profile_layout, container, false);

        // Receive Course id passed in from course fragment
        Bundle bundle = getArguments();
        final int course_id_passed_in = bundle.getInt("course_id_to_pass");
        System.out.println(course_id_passed_in);
        Course current_course = MainActivity.daoService.getCourse(course_id_passed_in);

        // Set the app title to the course name
        String course_name = current_course.getName();
        int course_number = current_course.getNumber();
        String course_number_string = String.valueOf(course_number);
        String course_title = course_name + " " + course_number_string;
        ( (FragmentActivity) getActivity()).setTitle(course_title);

        Button add_group_button = (Button) rootView.findViewById(R.id.add_group_button);
        final EditText group_name = (EditText) rootView.findViewById(R.id.group_edit_text);

        final ArrayList<Group> array_of_groups = new ArrayList<Group>();
        Iterator<Group> group_iterator = MainActivity.groupList.iterator();

        // Loop through array_of_groups and find only groups that belong to this course
        while(group_iterator.hasNext()){
            Group group = group_iterator.next();

            if(group.getCourseId() == course_id_passed_in){
                array_of_groups.add(group);
            }
        }


        final GroupAdapter group_adapter = new GroupAdapter(getActivity(), array_of_groups);
        final ListView group_list = (ListView) rootView.findViewById(R.id.group_list_view);
        group_list.setAdapter(group_adapter);

        add_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!group_name.getText().toString().equals("")){
                    String group_name_value = group_name.getText().toString();
                    MainActivity.daoService.createGroup(group_name_value, course_id_passed_in);
                }
            }
        });


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
