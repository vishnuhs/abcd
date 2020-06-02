package com.parkinncharge.parkinncharge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Register extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://XXX.XXX.X.XX/android_login_example/register.php";
    ProgressDialog progressDialog;

    private EditText signupInputName, signupInputEmail, signupInputPassword, signupInputphone;
    private Button btnSignUp;
    private Button btnLinkLogin;
    private RadioGroup genderRadioGroup;
    FirebaseFirestore db;
    String name,email,password,phone;
    String gend,name1,password1,phone1,email1;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        signupInputphone=(EditText) findViewById(R.id.phone_number);


        btnLinkLogin = (Button) findViewById(R.id.btn_link_login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
        db= FirebaseFirestore.getInstance();
        Intent intent=getIntent();
        uid=user.getUid();
        Log.e("userid", uid);

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        String gender;
        if(selectedId == R.id.female_radio_btn)
            gender = "Female";
        else
            gender = "Male";
        String identi=user.getEmail();
        //Toast.makeText(this, identi+"", Toast.LENGTH_SHORT).show();
        if(identi==null)
        {
            String identi1=user.getPhoneNumber();
            signupInputphone.setText(identi1);
            signupInputphone.setEnabled(false);
        }
        else {
            signupInputEmail.setText(identi);
            signupInputEmail.setEnabled(false);
        }



        gend=gender;
        newregister();

    }
    public void newregister() {

        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();




        btnLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u=generateString();
                name=signupInputName.getEditableText().toString();
                email=signupInputEmail.getEditableText().toString();
                password=signupInputPassword.getEditableText().toString();
                phone=signupInputphone.getEditableText().toString();
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("gender", gend);
                params.put("referral code",u);

                db.collection("register").document(uid).set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Register.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Could not enter into Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private String generateString()
    {
        char[] chars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder stringbuilder=new StringBuilder();
        Random rand=new Random();
        for(int i=0;i<6;i++)
        {
            char c=chars[rand.nextInt(chars.length)];
            stringbuilder.append(c);
        }
        return stringbuilder.toString();
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
