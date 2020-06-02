package com.parkinncharge.parkinncharge.ui.switch_owner;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.parkinncharge.parkinncharge.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class switch_owner_after_request extends Fragment {
    TextView updateProgressTextView;
    FirebaseFirestore db;
    FirebaseUser user;
    final String TAG="After Switch";
    public switch_owner_after_request() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_switch_owner_after_request, container, false);
        updateProgressTextView=view.findViewById(R.id.updateProgressTextView);
        user= FirebaseAuth.getInstance().getCurrentUser();
        String phone=user.getPhoneNumber();
        db=FirebaseFirestore.getInstance();
        db.collection("registered_user").document(phone).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document=task.getResult();
                        if(document.exists()){
                            Log.d(TAG,"document data is there");
                            String req_id=document.getString("request_id");
                            updateProgressTextView.setText("You request with id "+req_id+" for being an owner is being processed. Kindly wait till our team reaches you!");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"failed to fetch data");
                    }
                });



        return view;
    }

}
