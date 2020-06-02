package com.parkinncharge.parkinncharge.ui.profile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parkinncharge.parkinncharge.R;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class profile extends Fragment {
    private ProfileViewModel profileViewModel;
    View root;
    ImageView imageView;
    EditText editText,editText1,startDate,Gender,Email,Address,City;
    TextView text;
    Button buttonUpload, buttonGallery ;
    File file;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    ImageButton age,verify;
    Button save;
    Calendar testcal;
    DatePickerDialog dpick;


    public  static final int RequestPermissionCode  = 1 ;
    DisplayMetrics displayMetrics ;
    int width, height;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        imageView=root.findViewById(R.id.imageView3);
        editText=root.findViewById(R.id.profilename);
        editText1=root.findViewById(R.id.profilenumber);
        startDate=root.findViewById(R.id.startDate);
        Gender=root.findViewById(R.id.gender);
        Email=root.findViewById(R.id.email);
        Address=root.findViewById(R.id.address);
        City=root.findViewById(R.id.city);
        age= root.findViewById(R.id.calendarAgeButton);
        verify=root.findViewById(R.id.verifydata);
        text=root.findViewById(R.id.uploadtext);
        save=root.findViewById(R.id.profilesave);
        db= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("file");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String num=user.getPhoneNumber();
        db.collection("photourls")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String link=document.getString("link");
                                String id=document.getId();
                                if(num.equals(id))
                                {
                                    Glide.with(profile.this).load(link).into(imageView);
                                }
                            }
                        }
                    }
                });
        db.collection("registered_user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name=document.getString("name");
                                String number=document.getString("phone");
                                String dob=document.getString("DOB");
                                String gend=document.getString("gender");
                                String email=document.getString("email");
                                String address=document.getString("address");
                                String city=document.getString("city");
                                if(num.equals(number))
                                {
                                    editText.setText(name);
                                    editText.setEnabled(false);
                                    editText.setTextColor(Color.parseColor("#000000"));
                                    editText1.setText(number);
                                    editText1.setEnabled(false);
                                    editText1.setTextColor(Color.parseColor("#000000"));
                                    startDate.setText(dob);
                                    startDate.setTextColor(Color.parseColor("#000000"));
                                    Gender.setText(gend);
                                    Gender.setTextColor(Color.parseColor("#000000"));
                                    Email.setText(email);
                                    Email.setTextColor(Color.parseColor("#000000"));
                                    Address.setText(address);
                                    Address.setTextColor(Color.parseColor("#000000"));
                                    City.setText(city);
                                    City.setTextColor(Color.parseColor("#000000"));
                                }
                            }
                        } else {
                            Log.d("Hello", "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("file")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id=document.getId();
                                if(id!=null&&num.equals(id))
                                {
                                    verify.setVisibility(View.GONE);
                                    text.setText("File Uploaded Successfully!!!");
                                    text.setTextColor(Color.parseColor("#000000"));
                                    text.setEnabled(false);
                                }
                            }
                        } else {
                            Log.d("Hello", "Error getting documents: ", task.getException());
                        }
                    }
                });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDFfile();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedetails();
            }
        });


return root;
    }

    private void savedetails() {
        Map<String,String> profile_details=new HashMap<>();
        String nam=editText.getText().toString().trim();
        String num=editText1.getText().toString().trim();
        String dob=startDate.getText().toString().trim();
        String ema=Email.getText().toString().trim();
        String add=Address.getText().toString().trim();
        String cit=City.getText().toString().trim();
        String gende=Gender.getText().toString().trim();
        db.collection("registered_user").document(num).update("DOB",dob,"gender",gende,"email",ema,"address",add,"city",cit)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "details uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "New User Register Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectPDFfile()
    {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"SELECT PDF FILE TO UPLOAD"),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode== RESULT_OK &&data!=null&&data.getData()!=null)
        {
            uploadPDFfile(data.getData());
        }
    }

    private void uploadPDFfile(Uri data) {
        final ProgressDialog progressdialog=new ProgressDialog(getContext());
        progressdialog.setTitle("uploading...");
        progressdialog.show();

        String num=editText1.getText().toString().trim();
        StorageReference ref=storageReference.child(num);
        ref.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url=uri.getResult();
                        Map<String,String> user_data=new HashMap<>();
                        user_data.put("fileurl",url.toString());
                        user_data.put("status","");
                        db.collection("file").document(num).set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "uploaded", Toast.LENGTH_SHORT).show();
                                verify.setVisibility(View.GONE);
                                text.setText("File Uploaded Successfully!!!");
                                text.setTextColor(Color.parseColor("#000000"));
                                text.setEnabled(false);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "New User Register Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(getContext(),"File Uploaded",Toast.LENGTH_SHORT).show();
                        progressdialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress= (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                progressdialog.setMessage("uploaded: "+(int)progress+"%");


            }
        });
    }

    public void showCalendar()
    {
        Calendar cal = Calendar.getInstance();
        int date=cal.get(Calendar.DATE);
        int month=cal.get(Calendar.MONTH);
        int year=cal.get(Calendar.YEAR);

        testcal= Calendar.getInstance();

        dpick = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        testcal.set(Calendar.DATE,dayOfMonth);
                        testcal.set(Calendar.MONTH,monthOfYear);
                        testcal.set(Calendar.YEAR,year);
                        startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, date);
        dpick.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpick.show();
    }

}