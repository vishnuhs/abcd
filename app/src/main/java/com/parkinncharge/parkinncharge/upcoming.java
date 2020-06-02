package com.parkinncharge.parkinncharge;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class upcoming extends Fragment {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirestoreRecyclerAdapter<booking_details,booking_details_holder> adapter;
    String uid;
    RecyclerView  recyclerView;
    public upcoming() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_upcoming, container, false);
        //View root=container.getRootView();

        recyclerView=root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setAdapter(adapter);
        //TextView textView=root.findViewById(R.id.hello);

        uid=user.getUid();
        Query query=db.collection("booking").document(uid).collection("let out").whereEqualTo("status","Active");
        FirestoreRecyclerOptions<booking_details> options=new FirestoreRecyclerOptions.Builder<booking_details>()
                .setQuery(query,booking_details.class)
                .build();

        adapter=new FirestoreRecyclerAdapter<booking_details, booking_details_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull booking_details_holder holder, int position, @NonNull booking_details model) {
                holder.type.setText(model.getType());
                Log.d("Hiiiiiiiiiiii",holder.type.toString());
                holder.order_id.setText("BOOKING ID: "+model.getOrder_id());
                holder.startDate.setText(model.getStartDate());
                holder.startTime.setText(model.getStartTime());
                holder.endDate.setText(model.getEndDate());
                holder.endTime.setText(model.getEndTime());
                holder.amount.setText(model.getAmount());
            }

            @NonNull
            @Override
            public booking_details_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_details,parent,false);
                //Log.d("Hiiiiiiiiiiiiiiiiiii",v.toString());
                return new booking_details_holder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        return root;
    }
    private void setUpRecyclerView(){


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
