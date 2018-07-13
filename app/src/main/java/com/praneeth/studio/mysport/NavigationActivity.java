package com.praneeth.studio.mysport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity {

private FirebaseDatabase firebaseDatabase;
private DatabaseReference userRef,teamRef,eventRef,indTeamRef,teamId;
private FirebaseUser user;
private FirebaseAuth auth;
String key;




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



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();







        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("Users").child(user.getUid());
        teamRef = firebaseDatabase.getReference("Teams");
        eventRef = firebaseDatabase.getReference("Events");



        userRef.child("teamid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                key = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });












        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.signout) {
            auth.signOut();
            startActivity(new Intent(NavigationActivity.this,MainActivity.class));
        }
        else if(item.getItemId()==R.id.create_team)
        {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
