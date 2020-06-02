package com.parkinncharge.parkinncharge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Main3Activity extends AppCompatActivity {
    private EditText edittext;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        edittext = findViewById(R.id.editTextMobile);
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String number = edittext.getText().toString().trim();
                if (number.isEmpty() || number.length() < 10)
                {
                    edittext.setError("Number is Required");
                    edittext.requestFocus();
                    return;
                }
                String phno = "+91" + number;
                db.collection("registered_user").document("" + phno).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String num=document.getId();
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                    Toast.makeText(Main3Activity.this, "Existing User", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Main3Activity.this, Verifyphonenumber.class);
                                    intent.putExtra("phonenumber", phno);
                                    startActivity(intent);
                            } else {
                                Log.i("error", "enter valid number");
                            }
                        } else {
                            Log.i("error", "enter valid number");
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, Main2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
