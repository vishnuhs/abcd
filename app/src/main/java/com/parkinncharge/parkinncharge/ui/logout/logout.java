package com.parkinncharge.parkinncharge.ui.logout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.parkinncharge.parkinncharge.MainActivity;
import com.parkinncharge.parkinncharge.Main3Activity;
import com.parkinncharge.parkinncharge.R;

public class logout extends Fragment {
    private logoutViewModel logoutViewModel;
    View root;
    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel =
                ViewModelProviders.of(this).get(logoutViewModel.class);
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.logout_activity, container, false);
        root.findViewById(R.id.buttonSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(), Main3Activity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
