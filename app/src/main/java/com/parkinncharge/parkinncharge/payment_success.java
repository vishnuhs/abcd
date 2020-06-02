package com.parkinncharge.parkinncharge;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class payment_success extends Fragment {


    public payment_success() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_payment_success, container, false);;

        Button check_booking=root.findViewById(R.id.check_booking);
        check_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //container.removeAllViews();
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.current_booking);
            }
        });
        return root;
    }

}
