package com.parkinncharge.parkinncharge;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ongoing_booking extends Fragment {
    FirebaseFirestore db;
    String book_id,loc,date,time;
    FirebaseUser user;
    String uid;

    TextView booking_id,location,date_textview,time_textview;
    Chronometer elapsed;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_ongoing_booking, container, false);
        booking_id=root.findViewById(R.id.booking_id);
        location=root.findViewById(R.id.location);
        date_textview=root.findViewById(R.id.date);
        time_textview=root.findViewById(R.id.time);
        elapsed=root.findViewById(R.id.elapsed_time);
        db=FirebaseFirestore.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        db.collection("booking").document(uid).collection("let out").whereEqualTo("type","PRIVATE PARKING").whereEqualTo("status","Active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document:task.getResult()){
                                Log.d("OngoingFragment",document.getId()+"=>"+document.getData());

                                book_id=document.getId();
                                loc=(String)document.get("marker");
                                Log.d("OngoingFragment",loc);
                                date=document.getString("startDate");
                                time=document.getString("startTime");
                                booking_id.setText(book_id);
                                location.setText(loc);
                                date_textview.setText(date);
                                time_textview.setText(time);
                                SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                                Date start_date=new Date(),end_date;
                                try {
                                    start_date = formatter.parse(date+" "+time);
                                }catch (Exception e){

                                }
                                formatter=new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                                //Calendar cal=new Calendar() {
                                //}
                                end_date=new Date();
                                long diff = end_date.getTime() - start_date.getTime();
                                //elapsed.setFormat("HH:mm");
                                elapsed=root.findViewById(R.id.elapsed_time);


                                elapsed.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
                                    @Override
                                    public void onChronometerTick(Chronometer cArg) {
                                        long time = SystemClock.elapsedRealtime() - cArg.getBase();
                                        int h   = (int)(time /3600000);
                                        int m = (int)(time - h*3600000)/60000;
                                        int s= (int)(time - h*3600000- m*60000)/1000 ;
                                        String hh = h < 10 ? "0"+h: h+"";
                                        String mm = m < 10 ? "0"+m: m+"";
                                        String ss = s < 10 ? "0"+s: s+"";
                                        cArg.setText(hh+" Hrs:"+mm+" Min");
                                    }
                                });

                                elapsed.setBase(SystemClock.elapsedRealtime()-diff);
                                elapsed.start();


                                //long diffSeconds = diff / 1000 % 60;
                                //long diffMinutes = diff / (60 * 1000) % 60;
                                //long diffHours = diff / (60 * 60 * 1000);
                                //elapsed.setText(diffHours+" Hr : "+diffMinutes+"Min");
                                setvalues(book_id,loc,date,time);
                            }
                        }
                        else{
                            Toast.makeText(getActivity(), "Task Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Toast.makeText(getActivity(), loc+"", Toast.LENGTH_SHORT).show();





        return root;
    }

    public void setvalues(String book_id,String loc,String date,String time){
        this.book_id=book_id;
        this.loc=loc;
        this.date=date;
        this.time=time;
    }

}
