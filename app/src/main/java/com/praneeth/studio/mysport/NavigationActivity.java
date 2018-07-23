package com.praneeth.studio.mysport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity {

private FirebaseDatabase firebaseDatabase;
private DatabaseReference userRef,teamRef,eventRef,indTeamRef,teamId,attendRef;
private FirebaseUser user;
private FirebaseAuth auth;
String key,thisEventKey,uId;
RecyclerView recyclerView;
List<Event> eventList;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:

                    return true;
                case R.id.navigation_schedule:
                    return true;
                case R.id.navigation_Team:

                    Intent teamIntent = new Intent(NavigationActivity.this,TeamActivity.class);


                    teamIntent.putExtra("uniqueTeam",key);

                    Log.i("TAG","key is "+ key);



                    startActivity(teamIntent);



                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        eventList = new ArrayList<>();

        key = sharedPreferencesutils.getCurrentTeamId(this);

        Log.i("TAG","my team id is "+ key);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uId = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("Users").child(user.getUid());
        teamRef = firebaseDatabase.getReference("Teams");
        eventRef = firebaseDatabase.getReference("Events");
        attendRef = firebaseDatabase.getReference("Attendees");
        //Getting teamid from userref
       /* userRef.child("teamid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                key = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        eventRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();

                for(DataSnapshot alleventsnapshot: dataSnapshot.getChildren()){

                    //   Log.i("TAG","all event SNAPSHOT IS "+ alleventsnapshot.toString());
                    for(DataSnapshot eventsnapshot: alleventsnapshot.getChildren())
                    {
                        Log.i("TAG","SNAPSHOT IS "+ eventsnapshot.toString());
                        Event eachevent = eventsnapshot.getValue(Event.class);
                        Log.i("TAG","each event is "+ eachevent.toString());


                        //  String debug = eventsnapshot.child("Event Details").child("duration").getValue().toString();

                        //    Log.i("TAG","debug string is "+ debug);

                        eventList.add(eachevent);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter adapter = new eventAdapter(NavigationActivity.this,eventList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new eventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                startActivity(new Intent(NavigationActivity.this,EventAttendees.class));


            }

            @Override
            public void onGoingClick(int position) {
                String myName = eventList.get(position).getEventName();
                Toast.makeText(NavigationActivity.this, "Clicked going in "+myName, Toast.LENGTH_SHORT).show();
                thisEventKey = eventList.get(position).getEventkey();

                attendRef.child(thisEventKey).child(uId).setValue("Going");
            }

            @Override
            public void onNotGoingClick(int position) {
                String myName = eventList.get(position).getEventName();
                Toast.makeText(NavigationActivity.this, "Clicked Notgoing in "+myName, Toast.LENGTH_SHORT).show();
                thisEventKey = eventList.get(position).getEventkey();

                attendRef.child(thisEventKey).child(uId).setValue("NotGoing");

            }

            @Override
            public void onStatusClick(int position)
            {
                String myName = eventList.get(position).getEventName();
                Toast.makeText(NavigationActivity.this, "Clicked Notgoing in "+myName, Toast.LENGTH_SHORT).show();
                thisEventKey = eventList.get(position).getEventkey();

                attendRef.child(thisEventKey).child(uId).setValue("null");

            }

        });







      //  createEventlist();
       // buildRecyclerView();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
  /*  private void createEventlist()
    {

        eventRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();

                for(DataSnapshot alleventsnapshot: dataSnapshot.getChildren()){

                 //   Log.i("TAG","all event SNAPSHOT IS "+ alleventsnapshot.toString());
                    for(DataSnapshot eventsnapshot: alleventsnapshot.getChildren())
                    {
                        Log.i("TAG","SNAPSHOT IS "+ eventsnapshot.toString());
                        Event eachevent = eventsnapshot.getValue(Event.class);
                        Log.i("TAG","each event is "+ eachevent.toString());


                        //  String debug = eventsnapshot.child("Event Details").child("duration").getValue().toString();

                        //    Log.i("TAG","debug string is "+ debug);

                        eventList.add(eachevent);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

/*
    private void buildRecyclerView()
    {
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter adapter = new eventAdapter(NavigationActivity.this,eventList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new eventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String myName = eventList.get(position).getEventName();
                Toast.makeText(NavigationActivity.this, "myName is "+myName, Toast.LENGTH_LONG).show();

            }
        });



    }
*/

//    @Override
//    protected void onStart() {
//        super.onStart();
//        createEventlist();
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.signout) {
            auth.signOut();
            startActivity(new Intent(NavigationActivity.this,MainActivity.class));
        }
        else if(item.getItemId()==R.id.create_event)
        {
            startActivity(new Intent(NavigationActivity.this,addEvent.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

   /* @Override
    protected void onStart() {
        super.onStart();

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }*/
}
