package com.parkinncharge.parkinncharge;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarkerHolder extends RecyclerView.ViewHolder {

    TextView markername,markerdist;
    Button book;
    public MarkerHolder(@NonNull View itemView) {
        super(itemView);
        markername=itemView.findViewById(R.id.marker_name);
        markerdist=itemView.findViewById(R.id.marker_distance);
        book=itemView.findViewById(R.id.book);

    }
}
