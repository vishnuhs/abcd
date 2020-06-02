package com.parkinncharge.parkinncharge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.parkinncharge.parkinncharge.MainActivity;
import com.parkinncharge.parkinncharge.R;

public class Refer extends Fragment {
    FirebaseFirestore db,admindb;
    private Button button1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Refer()
    {

    }



    @SuppressLint("FragmentLiveDataObserve")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final String TAG="Refer";
        db = FirebaseFirestore.getInstance();
        FirebaseApp adminapp=FirebaseApp.getInstance("adminapp");
        admindb=FirebaseFirestore.getInstance(adminapp);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        final TextView textView1 = root.findViewById(R.id.textView2);
        String phno=user.getPhoneNumber();
        Toast.makeText(getContext(), user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
        db.collection("registered_user").document(phno).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if(document.getString("phone").equals(phno))
                            {
                                String refer=document.getString("referral code");
                                textView1.setText(refer);
                                textView1.setEnabled(false);
                                }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG,"fAILED FETCH DATA");
                    }
                });
        button1=(Button) root.findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ref=textView1.getText().toString();
                String referral="The referral code is "+ref;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,referral);
                sendIntent.setType("text/plain");
                Intent.createChooser(sendIntent, "Share via");
                startActivity(sendIntent);
            }
        });
        return root;
    }



}

