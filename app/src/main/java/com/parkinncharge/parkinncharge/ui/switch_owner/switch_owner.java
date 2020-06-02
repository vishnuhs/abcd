package com.parkinncharge.parkinncharge.ui.switch_owner;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.parkinncharge.parkinncharge.Owner_request_pojo;
import com.parkinncharge.parkinncharge.R;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class switch_owner extends Fragment {
    final String TAG="Switch Owner";
    FirebaseUser user;
    String name;
    int flag=0;
    Button confirmButton;
    View view;
    String uid,phone;
    FirebaseFirestore db,admindb;
    public switch_owner() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        phone=user.getPhoneNumber();
        db=FirebaseFirestore.getInstance();
        FirebaseApp adminapp=FirebaseApp.getInstance("adminapp");
        admindb=FirebaseFirestore.getInstance(adminapp);
        db.collection("registered_user").document(phone).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document=task.getResult();
                        if(document.exists()){
                            name=document.getString("name");
                            Log.d(TAG,""+name);
                            //Toast.makeText(getActivity(), ""+name, Toast.LENGTH_SHORT).show();
                            checkStatus();
                        }else{
                            Log.d(TAG,"No document found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Failed to get data");
                    }
                });



        view = inflater.inflate(R.layout.fragment_switch_owner, container, false);


        confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = Long.toString(System.currentTimeMillis());
                String msg = uid + time + phone;
                Toast.makeText(getActivity(), "" + phone, Toast.LENGTH_SHORT).show();
                String req_id="";
                try {
                    req_id = toHexString(getSHA(msg)).substring(0, 10).toUpperCase();
                    db.collection("registered_user").document(phone).update("owner_request", "true", "request_id", "" + req_id)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.switch_owner_after_request);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"Failed to update the data");
                                }
                            });

                } catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }
                admindb.collection("owner_verify_request").document(req_id)
                        .set(new Owner_request_pojo(name,phone))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Not added to admindb", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
        return view;

    }
    public void checkStatus(){
        db.collection("registered_user").document(phone).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if(document.getString("owner_request").equals("true")){
                                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.switch_owner_after_request);
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
    }
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

}

