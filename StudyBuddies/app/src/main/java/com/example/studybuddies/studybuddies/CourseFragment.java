package com.example.studybuddies.studybuddies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import dao.Course;
import dao.DaoService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //public static DaoService daoService = new DaoService();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        Button add_course_button = (Button) rootView.findViewById(R.id.add_course_button);
        final EditText course_name = (EditText) rootView.findViewById(R.id.course_name_edit_text);
        final EditText course_number = (EditText) rootView.findViewById(R.id.course_number_edit_text);

        final ArrayList<Course> array_of_courses = (ArrayList<Course>) MainActivity.courseList;
        final CourseAdapter adapter = new CourseAdapter(getActivity(), array_of_courses);
        final ListView course_list = (ListView) rootView.findViewById(R.id.course_list_view);
        course_list.setAdapter(adapter);

        add_course_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( (!course_name.getText().toString().equals("")) && (!course_number.getText().toString().equals("")) ){
                    String course_name_value = course_name.getText().toString();
                    String course_number_value_string = course_number.getText().toString();
                    int course_number_value = Integer.parseInt(course_number_value_string);

                    String course_added_notification = "Course Added";

                    MainActivity.daoService.createCourse(course_name_value,course_number_value);
                }
            }
        });

        course_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                CourseProfileFragment course_profile_fragment = new CourseProfileFragment();

                Bundle bundles = new Bundle();
                Course course_to_pass = adapter.getItem(position);
                int course_id_to_pass = course_to_pass.getCourseId();

                bundles.putInt("course_id_to_pass", course_id_to_pass);

                course_profile_fragment.setArguments(bundles);
                ft.replace( ((ViewGroup) (getView().getParent())).getId(), course_profile_fragment);
                ft.addToBackStack(null);
                ft.commit();

                Log.d("Button", "Clicked");
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
