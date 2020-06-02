package com.parkinncharge.parkinncharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verifyphonenumber extends AppCompatActivity {

    private String Verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;
    private EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth=FirebaseAuth.getInstance();
        progressbar=findViewById(R.id.progressbar);
        edittext=findViewById(R.id.editTextCode);
        String phno=getIntent().getStringExtra("phonenumber");
        sendVerificationcode(phno);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code=edittext.getText().toString().trim();

                if(code.isEmpty()||code.length()<6)
                {
                    edittext.setError("Enter Code");
                    edittext.requestFocus();
                    return;
                }
                Verifycode(code);
            }
        });
    }

    private void Verifycode(String code)
    {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(Verificationid,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Intent intent=new Intent(Verifyphonenumber.this,progress1.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Verifyphonenumber.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    }
                });
    }

    private void sendVerificationcode(String number)
    {
        progressbar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Verificationid=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            /*String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                edittext.setText(code);
                Verifycode(code);
            }*/
            signInWithCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Verifyphonenumber.this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    };
}
