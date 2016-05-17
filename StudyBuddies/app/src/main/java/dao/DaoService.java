package dao;

import com.example.studybuddies.studybuddies.MainActivity;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattmessa on 5/15/16.
 */

public class DaoService {


    private int nextGroupNumber;
    private int nextCourseNumber;

    public DaoService() {

    }

    public void addCourseListListener() {

        Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/Course");
        final Query queryRef = ref.orderByKey();

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    System.out.printf("in course list listener\n");
                    MainActivity.courseList.clear();
                    for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {

                        Course myCourse = courseSnapshot.getValue(Course.class);
                        System.out.println(myCourse);

                        MainActivity.courseList.add(myCourse);

                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void addGroupListListener() {

        Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/Group");
        final Query queryRef = ref.orderByKey();

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    System.out.printf("in group list listener\n");
                    MainActivity.groupList.clear();
                    for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {

                        Group myGroup = groupSnapshot.getValue(Group.class);
                        System.out.println(myGroup);

                        MainActivity.groupList.add(myGroup);

                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void addNextGroupNumberListener() {

        Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com").child("NextGroupNumber");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    System.out.print("here");
                    nextGroupNumber = ((Long) snapshot.getValue()).intValue();
                } else {
                    System.out.print("fail");

                    nextGroupNumber = 0;
                }
                System.out.print("just cuz");


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addNextCourseNumberListener() {
        Firebase ref2 = new Firebase("https://vivid-heat-5794.firebaseio.com/NextCourseNumber");
        ref2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    nextCourseNumber = ((Long) snapshot.getValue()).intValue();
                }
                else
                {
                    nextCourseNumber = 0;
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public int getNextGroupNumber() {

        int nextNum = nextGroupNumber;
        Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/");

        ref.child("NextGroupNumber").setValue(nextGroupNumber+1);

         return nextNum;
    }

    public int getNextCourseNumber() {

        int nextNum = nextCourseNumber;
        Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/");

        ref.child("NextCourseNumber").setValue(nextCourseNumber+1);

        return nextNum;
    }

    //adds group id to user list and user id to group list
    public void joinGroup(final int groupId) {
        Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/Group");
        final Query queryRef = ref.orderByChild("groupId").equalTo(groupId);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    System.out.printf("snapshot yes not null\n");
                    for (DataSnapshot groupSnapshot: dataSnapshot.getChildren()) {
                        Group myGroup = groupSnapshot.getValue(Group.class);
                        List userList = myGroup.getUserIds();
                        if (userList == null)
                        {
                            userList = new ArrayList();
                        }
                        userList.add(MainActivity.userId);

                        Firebase myFirebaseRef = new Firebase("https://vivid-heat-5794.firebaseio.com/");
                        Firebase groupRef = myFirebaseRef.child("Group").child(groupSnapshot.getKey());
                        System.out.println("snapshot key = " + dataSnapshot.getKey());
                        groupRef.child("userIds").setValue(userList);
                    }

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Firebase ref2 = new Firebase("https://vivid-heat-5794.firebaseio.com/User");
        final Query queryRef2 = ref2.orderByKey().equalTo(MainActivity.userId);

        queryRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    System.out.printf("snapshot not null\n");
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        User myUser = userSnapshot.getValue(User.class);
                        List groupList = myUser.getGroupIds();
                        if (groupList == null)
                        {
                            groupList = new ArrayList();
                        }
                        groupList.add(groupId);
                        System.out.printf("group id %d\n", groupId);
                        Firebase myFirebaseRef = new Firebase("https://vivid-heat-5794.firebaseio.com/");
                        Firebase groupRef = myFirebaseRef.child("User").child(userSnapshot.getKey());

                        groupRef.child("groupIds").setValue(groupList);
                    }

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void joinCourse(final int courseId) {
        Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/Course");
        final Query queryRef = ref.orderByChild("courseId").equalTo(courseId);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    System.out.printf("snapshot yes not null\n");
                    for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()) {
                        Course myCourse = courseSnapshot.getValue(Course.class);
                        List userList = myCourse.getUserIds();
                        if (userList == null)
                        {
                            userList = new ArrayList();
                        }
                        userList.add(MainActivity.userId);

                        Firebase myFirebaseRef = new Firebase("https://vivid-heat-5794.firebaseio.com/");
                        Firebase courseRef = myFirebaseRef.child("Course").child(courseSnapshot.getKey());
                        System.out.println("snapshot key = " + dataSnapshot.getKey());
                        courseRef.child("userIds").setValue(userList);
                    }

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Firebase ref2 = new Firebase("https://vivid-heat-5794.firebaseio.com/User");
        final Query queryRef2 = ref2.orderByKey().equalTo(MainActivity.userId);

        queryRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    System.out.printf("snapshot not null\n");
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        User myUser = userSnapshot.getValue(User.class);
                        List courseList = myUser.getCourseIds();
                        if (courseList == null)
                        {
                            courseList = new ArrayList();
                        }
                        courseList.add(courseId);
                        System.out.printf("group id %d\n", courseId);
                        Firebase myFirebaseRef = new Firebase("https://vivid-heat-5794.firebaseio.com/");
                        Firebase userRef = myFirebaseRef.child("User").child(userSnapshot.getKey());

                        userRef.child("courseIds").setValue(courseList);
                    }

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void createGroup(String name, int courseId) {
        Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/");

        System.out.println("getting next group number");
        final Integer groupKey = getNextGroupNumber();
        final Integer courseKey = courseId;
        Group group = new Group(groupKey, courseId, name);

        ref.child("Group").child(groupKey.toString()).setValue(group);

        Firebase ref2 = new Firebase("https://vivid-heat-5794.firebaseio.com/Course");
        final Query queryRef2 = ref2.orderByKey().equalTo(courseKey.toString());

        queryRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    System.out.printf("course snapshot not null\n");
                    for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()) {
                        Course myCourse = courseSnapshot.getValue(Course.class);
                        List groupList = myCourse.getGroupIds();
                        if (groupList == null)
                        {
                            groupList = new ArrayList();
                        }
                        groupList.add(groupKey);
                        System.out.printf("group id %d\n", groupKey);
                        Firebase myFirebaseRef = new Firebase("https://vivid-heat-5794.firebaseio.com/");
                        Firebase courseRef = myFirebaseRef.child("Course").child(courseSnapshot.getKey());

                        courseRef.child("groupIds").setValue(groupList);
                    }

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


    }

    public void createCourse(String name, int number) {
        Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/");

        System.out.println("getting next course number");
        Integer courseKey = getNextCourseNumber();
        Course course = new Course(courseKey, name, number);

        ref.child("Course").child(courseKey.toString()).setValue(course);
    }
}
