package com.praneeth.studio.mysport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef,teamRef,eventRef,indTeamRef,teamId;
    private FirebaseUser user;
    private FirebaseAuth mauth;
    TextView teamName;
    ListView listView;
    ArrayList<String> teamArrayList;
    ArrayAdapter<String> teamArrayAdapter;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        key = sharedPreferencesutils.getCurrentTeamId(this);

        teamName = findViewById(R.id.teamname);
        listView = findViewById(R.id.listview);
        teamArrayList = new ArrayList<>();




        mauth = FirebaseAuth.getInstance();
        user = mauth.getCurrentUser();



        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("Users").child(user.getUid());
        teamRef = firebaseDatabase.getReference("Teams");
        eventRef = firebaseDatabase.getReference("Events");


        pushteamData(key);








    }

    private void pushteamData(String teamIdkey) {


        teamRef.child(teamIdkey).child("teamname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tName = dataSnapshot.getValue(String.class);
                teamName.setText(tName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        teamRef.child(key).child("players").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String player = dataSnapshot.getValue(String.class);
                Toast.makeText(TeamActivity.this, player, Toast.LENGTH_SHORT).show();
                Log.i("TAG","player is " +player);
                teamArrayList.add(player);
                teamArrayAdapter = new ArrayAdapter<>(TeamActivity.this,android.R.layout.simple_list_item_1,teamArrayList);
                listView.setAdapter(teamArrayAdapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.signout) {
            mauth.signOut();
            startActivity(new Intent(TeamActivity.this,MainActivity.class));
        }
        else if(item.getItemId()==R.id.create_event)
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
