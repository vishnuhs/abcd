package com.parkinncharge.parkinncharge;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class progress2 extends AppCompatActivity {
    String[] descriptionData = {"Photo", "My details", "Submit"};
    Button btn;
    private Button mDisplayDate;
    private EditText mDisplayDate1,editTextname,edittext2,editTextemail,editTextphone,editTextdob,editTextaddress,editTextcity,editText11;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RadioGroup genderRadioGroup;
    private CheckBox checkbox;
    String code=null;
    private boolean flag=false;
    private String req="false";
    FirebaseFirestore db;
    FirebaseUser user;
    String uid;
    ImageButton age;
    Calendar testcal;
    DatePickerDialog dpick;
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress2);
        db= FirebaseFirestore.getInstance();
        age= findViewById(R.id.calendarAgeButton);
        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
        mDisplayDate1= (EditText) findViewById( R.id.editText10 );
        mDisplayDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
        StateProgressBar stateProgressBar = (StateProgressBar)findViewById(R.id.progress2);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateSize(30f);
        stateProgressBar.setStateNumberTextSize(15f);
        stateProgressBar.setStateLineThickness(5f);
        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        String gender;
        if(selectedId == R.id.female_radio_btn)
            gender = "Female";
        else
            gender = "Male";


        btn = findViewById( R.id.button20);
        edittext2=findViewById(R.id.editTextCode);
        editTextname=findViewById(R.id.editText4);
        editTextemail=findViewById(R.id.editText8);
        editTextphone=findViewById(R.id.editText7);
        editTextdob=findViewById(R.id.editText10);
        editTextaddress=findViewById(R.id.editText9);
        editTextcity=findViewById(R.id.editText11);
        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextname.getText().toString().trim();
                String email= editTextemail.getText().toString().trim();
                String phone= editTextphone.getText().toString().trim();
                String dob= editTextdob.getText().toString().trim();
                String address= editTextaddress.getText().toString().trim();
                String city= editTextcity.getText().toString().trim();
                if(name.isEmpty())
                {
                    editTextname.setError("Name is Required");
                    editTextname.requestFocus();
                    return;
                }
                if(email.isEmpty())
                {
                    editTextemail.setError("Name is Required");
                    editTextemail.requestFocus();
                    return;
                }
                if(dob.isEmpty())
                {
                    editTextdob.setError("Name is Required");
                    editTextdob.requestFocus();
                    return;
                }
                if(address.isEmpty())
                {
                    editTextaddress.setError("Name is Required");
                    editTextaddress.requestFocus();
                    return;
                }
                if(city.isEmpty())
                {
                    editTextcity.setError("Name is Required");
                    editTextcity.requestFocus();
                    return;
                }
                if (phone.isEmpty() || phone.length() < 10)
                {
                    editTextphone.setError("Number is Required");
                    editTextphone.requestFocus();
                    return;
                }
                if(flag==true)
                {
                    code=edittext2.getText().toString().trim();
                    if(code.isEmpty()||code.length()<9)
                    {
                        edittext2.setError("code is Required");
                        edittext2.requestFocus();
                        return;
                    }
                }
                String time = Long.toString(System.currentTimeMillis());
                String msg = uid + time + phone;
                try {
                    String req_id = toHexString(getSHA(msg)).substring(0, 10).toUpperCase();
                    String phno = "+91" + phone;
                    String rand=name.substring(0,4);
                    String ref=rand+generateString();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("phone", phno);
                    params.put("gender", gender);
                    params.put("DOB", dob);
                    params.put("address", address);
                    params.put("city", city);
                    params.put("referral code",ref);
                    params.put("referred code",code);
                    params.put("owner_request",req);
                    params.put("request_id",req_id);
                    db.collection("registered_user").document(phno).set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(progress2.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),progress3.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(progress2.this, "Could not enter into Firestore", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } );
    }
    public void showCalendar()
    {
        Calendar cal = Calendar.getInstance();
        int date=cal.get(Calendar.DATE);
        int month=cal.get(Calendar.MONTH);
        int year=cal.get(Calendar.YEAR);

        testcal= Calendar.getInstance();

        dpick = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        testcal.set(Calendar.DATE,dayOfMonth);
                        testcal.set(Calendar.MONTH,monthOfYear);
                        testcal.set(Calendar.YEAR,year);
                        mDisplayDate1.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, date);
        dpick.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpick.show();
    }
    private String generateString()
    {
        char[] chars="0123456789".toCharArray();
        StringBuilder stringbuilder=new StringBuilder();
        Random rand=new Random();
        for(int i=0;i<5;i++)
        {
            char c=chars[rand.nextInt(chars.length)];
            stringbuilder.append(c);
        }
        return stringbuilder.toString();
    }
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
}
