package com.parkinncharge.parkinncharge;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;



    class booking_details_holder extends RecyclerView.ViewHolder {
        TextView type,order_id,startDate,startTime,endDate,endTime,amount;


        public booking_details_holder(@NonNull View itemView) {
            super(itemView);
            this.type=itemView.findViewById(R.id.type);
            this.order_id=itemView.findViewById(R.id.booking_id);
            this.startDate=itemView.findViewById(R.id.startDate);
            this.endDate=itemView.findViewById(R.id.endDate);
            this.startTime=itemView.findViewById(R.id.startTime);
            this.endTime=itemView.findViewById(R.id.endTime);
            this.amount=itemView.findViewById(R.id.amount);

        }
    }
