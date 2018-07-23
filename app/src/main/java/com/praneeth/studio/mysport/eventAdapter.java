package com.praneeth.studio.mysport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class eventAdapter extends RecyclerView.Adapter<eventAdapter.eventViewHolder> {


    private Context ctx;
    private List<Event> eventList;
    private OnItemClickListener mListener;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef,attendRef;
    private FirebaseUser user;
    private FirebaseAuth auth;
    String uId,status;




    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onGoingClick(int position);
        void onNotGoingClick(int position);
        void onStatusClick(int position);

    }


    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public eventAdapter(Context ctx, List<Event> eventList) {
        this.ctx = ctx;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public eventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.eventinfo,null);
        eventViewHolder viewHolder = new eventViewHolder(view,mListener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final eventViewHolder holder, int position) {

        Event eventc = eventList.get(position);
        holder.eName.setText(eventc.getEventName());
        holder.eLocation.setText(eventc.getLocation());
        holder.eTime.setText(eventc.getTime());

        String eventkey = eventc.getEventkey();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uId = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("Users").child(user.getUid());
        attendRef = firebaseDatabase.getReference("Attendees");

        attendRef.child(eventkey).child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                status = dataSnapshot.getValue().toString();
                if(status.equals("Going"))
                {
                    holder.statusSpinner.setSelection(1);
                }
                else if(status.equals("NotGoing"))
                {
                    holder.statusSpinner.setSelection(2);
                }
                if(status.equals("null"))
                {
                    holder.statusSpinner.setSelection(0);
                }


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });










    }



    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class eventViewHolder extends RecyclerView.ViewHolder
    {
        TextView eName,eLocation,eTime;
        Spinner statusSpinner;

        public eventViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            eName = itemView.findViewById(R.id.ename);
            eLocation = itemView.findViewById(R.id.elocation);
            eTime = itemView.findViewById(R.id.etime);
            statusSpinner = itemView.findViewById(R.id.spinner);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)  listener.onItemClick(position);
                    }
                }
            });
            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i==1)
                    {
                        if(listener!=null)
                        {
                            int position = getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION)  listener.onGoingClick(position);
                        }
                    }
                    else if(i==2)
                    {
                        if(listener!=null)
                        {
                            int position = getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION)  listener.onNotGoingClick(position);
                        }
                    }
                    else if(i==0)
                    {
                        if(listener!=null)
                        {
                            int position = getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION)  listener.onStatusClick(position);
                        }

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(ctx,R.array.status,android.R.layout.simple_spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            statusSpinner.setAdapter(arrayAdapter);


        }
    }
}
