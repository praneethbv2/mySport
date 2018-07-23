package com.praneeth.studio.mysport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class welcomeScreen extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef,teamRef,eventRef,indTeamRef,teamId;
    private FirebaseUser user;
    private FirebaseAuth auth;
    Button join,create;
    String key,playername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);


        join = findViewById(R.id.join);
        create = findViewById(R.id.createteam);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();




        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("Users").child(user.getUid());
        teamRef = firebaseDatabase.getReference("Teams");







        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertcreateTeam();


            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlert();


            }
        });







    }


    private void showAlert() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.alert_dialog,null,false);

        final EditText catText = dialogView.findViewById(R.id.teamid);

        AlertDialog.Builder builder = new AlertDialog.Builder(welcomeScreen.this).setView(dialogView);

        builder.setMessage("Enter team id");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                key = catText.getText().toString();

                sharedPreferencesutils.setCurrentTeamId(welcomeScreen.this,key);

                userRef.child("teamid").setValue(key);

                userRef.child("fullname").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        playername = dataSnapshot.getValue().toString();

                        Map<String,String> joinedplayersData = new HashMap<>();
                        joinedplayersData.put(user.getUid(),playername);
                        Log.i("TAG","PLAYER NAME IS"+playername);
                        Log.i("TAG","PLAYERS DATA IS"+joinedplayersData);

                        teamRef.child(key).child("players").child(user.getUid()).setValue(playername);



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                startActivity(new Intent(welcomeScreen.this,NavigationActivity.class));






            }
        })
                .setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    private void showAlertcreateTeam() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.alert_createteam,null,false);

        final EditText teamName = dialogView.findViewById(R.id.teamname);

        AlertDialog.Builder builder = new AlertDialog.Builder(welcomeScreen.this).setView(dialogView);

        builder.setMessage("Enter team name");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String tName = teamName.getText().toString();





                teamId = teamRef.push();
                key = teamId.getKey();
                sharedPreferencesutils.setCurrentTeamId(welcomeScreen.this,key);


                userRef.child("teamid").setValue(key);

                userRef.child("fullname").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        playername = dataSnapshot.getValue().toString();

                        Log.i("TAG","PLAYER NAME IS"+playername);
                        Map<String,String> playersData = new HashMap<>();
                        playersData.put(user.getUid(),playername);
                        Log.i("TAG","PLAYER NAME IS"+playername);
                        Log.i("TAG","PLAYERS DATA IS"+playersData);

                        teamId.child("players").setValue(playersData);



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                teamId.child("teamname").setValue(tName);
                Intent tIntent = new Intent(welcomeScreen.this,NavigationActivity.class);
                tIntent.putExtra("unique",key);
                startActivity(tIntent);
                finish();

            }
        })
                .setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



}
