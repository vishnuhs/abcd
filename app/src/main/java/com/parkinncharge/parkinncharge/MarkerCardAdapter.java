package com.parkinncharge.parkinncharge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.parkinncharge.parkinncharge.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MarkerCardAdapter extends RecyclerView.Adapter<MarkerHolder> {

    Context c;
    ArrayList<MarkerInfo> markers;
    //String location;

    public MarkerCardAdapter(Context c, ArrayList<MarkerInfo> markers) {
        this.c = c;
        this.markers = markers;
        //this.location=location;
    }

    @NonNull
    @Override
    public MarkerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.marker_layout,parent,false);
        return new MarkerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkerHolder holder, int position) {
        holder.markername.setText(markers.get(position).getMarkername());
        holder.markerdist.setText(String.format("%.2f",markers.get(position).getMarkerdist())+" Kms");
        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent payment=new Intent(c,Payment_Activity.class);
                payment.putExtra("amount", 10000.00);
                payment.putExtra("type","PRIVATE PARKING");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                Date date = new Date();
                String[] date_time=formatter.format(date).split(" ");
                payment.putExtra("booked_date",date_time[0]);
                payment.putExtra("time",date_time[1]+" "+date_time[2]);
                payment.putExtra("marker",markers.get(position).getMarkername());
                Toast.makeText(c, markers.get(position).getMarkername()+" Pressed", Toast.LENGTH_LONG).show();
                HomeFragment.marker_name=markers.get(position).getMarkername();
                //HomeFragment.marker_name=markers.get(position).getMarkername();
                ((Activity)c).startActivityForResult(payment,5);

                //AppCompatActivity activity = (AppCompatActivity) view.getContext();
                //Fragment myFragment = new ();
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return markers.size();
    }





}


