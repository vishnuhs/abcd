package com.parkinncharge.parkinncharge;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.razorpay.Checkout;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity{
    FirebaseFirestore db;
    ImageView imageView;
    Button buttonGallery ;
    Uri uri;
    Intent GalIntent, CropIntent ;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 5902 ;
    //private static final int RESULT_OK = 5903 ;
    final int CAMERA_REQUEST_CODE=101,LOCATION_REQUEST_CODE=100;
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){


            }else{
                setContentView(R.layout.permissions_required);
                Button allow_button=findViewById(R.id.allow_button);
                allow_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, LOCATION_REQUEST_CODE);
                    }
                });
            }
            case CAMERA_REQUEST_CODE:if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                // finishAffinity(getActivity());
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)

                    startActivity(new Intent(this,MainActivity.class));

            }else{
                setContentView(R.layout.permissions_required);
                Button allow_button=findViewById(R.id.allow_button);
                allow_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, LOCATION_REQUEST_CODE);
                    }
                });
            }

        }
    }
    private EditText edittext,edittext1,edittext2;
    private CheckBox checkbox;
    String code=null;
    private boolean flag=false;
    StorageReference mStorageref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        buttonGallery = (Button)findViewById(R.id.choosebutton);
        mStorageref= FirebaseStorage.getInstance().getReference("Images");
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonGallery.setEnabled(false);
                GetImageFromGallery();

            }
        });
        db= FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);

        FirebaseOptions options=new FirebaseOptions.Builder()
                .setApiKey("AIzaSyC5Kn8hMYhncXJa2CsAH5qTue5v8HmnXvI")
                .setApplicationId("com.parkinncharge.parkinncharge")
                .setProjectId("owner-afeaf")
                .build();
        FirebaseApp.initializeApp(this,options,"ownersapp");


        FirebaseOptions options1=new FirebaseOptions.Builder()
                .setApiKey("AIzaSyABcRbxWMARaSNyfY2fOz-PWykCQh1j2LA")
                .setApplicationId("com.parkinncharge.parkinncharge")
                .setProjectId("admin-a70aa")
                .build();
        FirebaseApp.initializeApp(this,options1,"adminapp");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, LOCATION_REQUEST_CODE);

        } else {


            Checkout.preload(getApplicationContext());
            if (!Places.isInitialized()) {
                Places.initialize(this, "AIzaSyCV-hvShgHkCA69x02KQreeprA-i2rZrIM");
            }
        }
        edittext=findViewById(R.id.editTextMobile);
        edittext1=findViewById(R.id.editTextName);
        edittext2=findViewById(R.id.editTextCode);
        checkbox=findViewById(R.id.checkBox);
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox.isChecked())
                {
                    edittext2.setVisibility(View.VISIBLE);
                    flag=true;
                }
                else
                {
                    edittext2.setVisibility(View.GONE);
                }
            }
        });
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = edittext.getText().toString().trim();
                String name= edittext1.getText().toString().trim();
                String rand=name.substring(0,4);
                String ref=rand+generateString();
                if(name.isEmpty())
                {
                    edittext1.setError("Name is Required");
                    edittext1.requestFocus();
                    return;
                }
                if (number.isEmpty() || number.length() < 10)
                {
                    edittext.setError("Number is Required");
                    edittext.requestFocus();
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
                String phno = "+91" + number;
                Map<String,String> user_data=new HashMap<>();
                user_data.put("number",phno);
                user_data.put("name",name);
                user_data.put("referral code",ref);
                user_data.put("referred code",code);
                db.collection("registered_user").document(""+phno).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Toast.makeText(MainActivity.this, "Existing User", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Verifyphonenumber.class);
                                intent.putExtra("phonenumber", phno);
                                startActivity(intent);
                            } else {
                                db.collection("registered_user").document(phno).set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "New User", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, Verifyphonenumber.class);
                                        intent.putExtra("phonenumber", phno);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "New User Register Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
                if(imageView.getDrawable()==null)
                {

                }
                else
                {

                }
            }
        });
    }

    private String getExtention(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
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

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Toast.makeText(MainActivity.this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,Main2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            uri = data.getData();

            ImageCropFunction();

        } else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();

                ImageCropFunction();

            }
        } else if (requestCode == 1) {

            if (data != null) {
                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }
}
