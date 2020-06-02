package com.parkinncharge.parkinncharge;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 */
public class earn extends Fragment {

    private EditText editText;
    FirebaseFirestore db;


    public earn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db= FirebaseFirestore.getInstance();
        View root = inflater.inflate(R.layout.fragment_earn, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String ph=user.getPhoneNumber();
        final String[] referralcode = {null};
        int[] count = {0};
        editText = (EditText) root.findViewById(R.id.referredtext);
        db.collection("registered_user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String referral=document.getString("referral code");
                                String number=document.getString("number");
                                if(ph.equals(number))
                                {
                                    referralcode[0] =referral;
                                }
                            }
                        } else {
                            Log.d("Hello", "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("registered_user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String referred=document.getString("referred code");
                                String number=document.getString("number");
                                Log.i("referral", "referral code is "+referred);
                                Log.i("referredcode", "referral code is "+referralcode[0]);
                                if(referralcode[0] .equals(referred))
                                {
                                    Log.i("matched", "referral code is "+referred);
                                    count[0]+=1;
                                    Log.i("count", "count value is "+count[0]);
                                }
                            }
                        } else {
                            Log.d("Hello", "Error getting documents: ", task.getException());
                        }
                        if(count[0]<10)
                        {
                            Log.i("countval", "count value is"+count[0]);
                            int x=10-count[0];
                            editText.setText("ASK "+ x +" MORE PEOPLE TO USE YOUR REFERRAL CODE TO EARN REWARDS");
                        }
                        else if(count[0]>10)
                        {
                            int rem=count[0]%10;
                            int y=10-rem;
                            editText.setText("ASK "+ y +" MORE PEOPLE TO USE YOUR REFERRAL CODE TO EARN REWARDS");
                        }
                        editText.setEnabled(false);
                    }
                });
     return root;
    }

}
