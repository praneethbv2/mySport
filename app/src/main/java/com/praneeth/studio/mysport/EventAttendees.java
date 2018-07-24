package com.praneeth.studio.mysport;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventAttendees extends AppCompatActivity {

    ListView goingview, notgoingview;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference attendRef,eventRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    ArrayList<String> goinglist,notgoinglist;
    String thiseventkey;
    ArrayAdapter<String> goingAdapter;
    ArrayAdapter<String> NotgoingAdapter;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendees);

        goinglist = new ArrayList<>();
        notgoinglist = new ArrayList<>();

        Intent incomingev = getIntent();
        thiseventkey = incomingev.getStringExtra("thiseventkey");



        goingview = findViewById(R.id.goinglist);

        notgoingview = findViewById(R.id.notgoinglist);

        firebaseDatabase = FirebaseDatabase.getInstance();
        attendRef = firebaseDatabase.getReference().child("Attendees");
        eventRef = attendRef.child(thiseventkey);

        Query goingquery = eventRef.orderByChild("status").equalTo("Going");
        goingquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                goinglist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey().toString();
                    goinglist.add(id);
                }
                Log.i("TAG","goinglist is"+goinglist);
                goingAdapter = new ArrayAdapter<>(EventAttendees.this,android.R.layout.simple_list_item_1,goinglist);
                goingview.setAdapter(goingAdapter);
                setListViewHeightBasedOnChildren(goingview);

                goingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query notgoingquery = eventRef.orderByChild("status").equalTo("NotGoing");
        notgoingquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notgoinglist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey().toString();

                    notgoinglist.add(id);
                }

                NotgoingAdapter = new ArrayAdapter<>(EventAttendees.this,android.R.layout.simple_list_item_1,notgoinglist);
                notgoingview.setAdapter(NotgoingAdapter);
                setListViewHeightBasedOnChildren(notgoingview);

                NotgoingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
