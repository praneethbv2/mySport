package com.praneeth.studio.mysport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class addEvent extends AppCompatActivity {

    private EditText eName,eLocation,eTime,eDuration;
    private Button eCreate;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef,teamRef,eventRef,indTeamRef,teamId,eventId;
    private FirebaseUser user;
    private FirebaseAuth auth;
    String eventKey,teamKey;
    Map<String,String> eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        eName = findViewById(R.id.editText);
        eLocation = findViewById(R.id.editText2);
        eTime = findViewById(R.id.editText3);
        eDuration = findViewById(R.id.editText4);
        eCreate = findViewById(R.id.button2);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("Users").child(user.getUid());
        teamRef = firebaseDatabase.getReference("Teams");
        eventRef = firebaseDatabase.getReference("Events");



        eCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                userRef.child("teamid").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        teamKey = dataSnapshot.getValue().toString();
                        //Adding teamId under events
                        eventId = eventRef.child(teamKey).push();
                        eventKey = eventId.getKey();


                        String Name =  eName.getText().toString();
                        String Location = eLocation.getText().toString();
                        String Time = eTime.getText().toString();
                        String Duration = eDuration.getText().toString();

                        Map<String,String> eventDetails = new HashMap<>();
                        eventDetails.put("eventName",Name);
                        eventDetails.put("location",Location);
                        eventDetails.put("time",Time);
                        eventDetails.put("duration",Duration);
                        eventDetails.put("eventkey",eventKey);



                        eventId.child("Event Details").setValue(eventDetails);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






                startActivity(new Intent(addEvent.this,NavigationActivity.class));
                finish();


            }
        });










    }
}
