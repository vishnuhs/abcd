package com.parkinncharge.parkinncharge;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.parkinncharge.parkinncharge.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class scan_in extends Fragment implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    FirebaseFirestore db;
    FirebaseUser user;
    String uid;
    ViewGroup cont;
    String marker_name,book_id;
    Button directions;
    String lat,longi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //container.removeAllViews();
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        db= FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        Bundle b=new Bundle();

        db.collection("booking").document(uid).collection("let out").whereEqualTo("type","PRIVATE PARKING").whereEqualTo("status","Active").get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        marker_name=(String)documentSnapshot.get("marker");
                        b.putString("marker",marker_name);
                        //Log.d("marker name",documentSnapshot.getData()+"");
                        book_id=(String)documentSnapshot.get("order_id");
                        if (documentSnapshot.get("progress").equals("scanned")){
                            Toast.makeText(getActivity(), "You already have a booking in progress", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.ongoing_booking);
                        }
                        db.collection("users").document(marker_name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        lat=document.getString("Latitude");
                                        longi=document.getString("Longitude");
                                        directions.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                        Uri.parse("http://maps.google.com/maps?daddr="+lat+","+longi));
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Log.d("ScanIN", "No such document");
                                    }
                                } else {
                                    Log.d("ScanIN", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });
        //double lat,longi;
        //marker_name=b.getString("marker");
        //Toast.makeText(getActivity(), marker_name+"", Toast.LENGTH_SHORT).show();
        View root=inflater.inflate(R.layout.frag_scan_in, container, false);
        //marker_name=getArguments().getString("marker");
        //book_id=getArguments().getString("booking_id");
        //Log.d("ScanFragment",marker_name);
        //Log.d("ScanFragment",book_id);
        cont=container;
        //Toast.makeText(getActivity(), getActivity()+"", Toast.LENGTH_SHORT).show();
        directions=root.findViewById(R.id.directions_button);
        ScannerView = root.findViewById(R.id.zxscan_in);
        ScannerView.setResultHandler(this);


        final ImageButton flash_On=(ImageButton) root.findViewById(R.id.flashOn);
        final ImageButton flash_Off=(ImageButton) root.findViewById(R.id.flashOff);
        flash_Off.setVisibility(View.INVISIBLE);
        flash_On.setVisibility(View.VISIBLE);
        flash_On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannerView.setFlash(true);
                //Log.d("button","pressed");
                flash_Off.setVisibility(View.VISIBLE);
                flash_On.setVisibility(View.INVISIBLE);

            }
        });
        flash_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("button","pressed");
                ScannerView.setFlash(false);
                flash_Off.setVisibility(View.INVISIBLE);
                flash_On.setVisibility(View.VISIBLE);

            }
        });
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
    public void handleResult(Result result) {
        if(result.getText().equals(marker_name)){
            Toast.makeText(getActivity(), "Correct Scan", Toast.LENGTH_SHORT).show();
            //getActivity().getSupportFragmentManager().beginTransaction().remove(this);
            //Main2Activity.fragmanager.beginTransaction().add(R.id.nav_host_fragment,new ongoing_booking()).commit();
            //cont.removeAllViews();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            Date date = new Date();
            String[] date_time=formatter.format(date).split(" ");
            String scan_date=date_time[0];
            String scan_time=date_time[1]+" "+date_time[2];
            Log.d("Date",scan_date);
            db.collection("booking").document(uid).collection("let out").document(book_id).update("progress","scanned");
            db.collection("booking").document(uid).collection("let out").document(book_id).update("scan_in_date",scan_date);
            db.collection("booking").document(uid).collection("let out").document(book_id).update("scan_in_time",scan_time);
            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.ongoing_booking);


        }
        else{
            Toast.makeText(getActivity(),"Wrong Scan",Toast.LENGTH_LONG).show();
        }
        uid=user.getUid();

        //db.collection("booking").document(uid).collection("let out").

        //MainActivity.intimeTextView.setText(""+date+"/"+month+"/"+year+" "+hours+":"+minute+":"+second);
        //onBackPressed();



    }

    @Override
    public void onResume() {
        super.onResume();
        ScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        ScannerView.stopCamera();

    }

}
