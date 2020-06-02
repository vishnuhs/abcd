package com.parkinncharge.parkinncharge;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class progress1 extends AppCompatActivity {
    private static final int IMAGE_PICKER_SELECT = 1000;


    String[] descriptionData = {"Photo", "My details", "Submit"};
    Button btn;
    private CircleImageView ProfileImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    Intent GalIntent, CropIntent;


    CircleImageView circleImageView;
    FirebaseFirestore db;
    private static final int IMAGE_PICK_CODE = 1001;
    StorageReference mStorageref;


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress1);
        circleImageView = findViewById(R.id.Profile_Image);
        mStorageref = FirebaseStorage.getInstance().getReference("Images");
        db = FirebaseFirestore.getInstance();
        circleImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GetImageFromGallery();


                // Intent gallery = new Intent();
                //  gallery.setType("image/*");
                //   gallery.setAction(Intent.ACTION_GET_CONTENT);

                //  startActivityForResult(Intent.createChooser(gallery, "Sellect Picture"), PICK_IMAGE);
            }

        });


        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.progress1);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateSize(30f);
        stateProgressBar.setStateNumberTextSize(15f);
        stateProgressBar.setStateLineThickness(5f);
        btn = findViewById(R.id.button19);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(progress1.this, progress2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                fileUploader();
            }
        });
    }

    public void GetImageFromGallery() {
        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            imageUri = data.getData();

            ImageCropFunction();

        } else if (requestCode == 2) {

            if (data != null) {

                imageUri = data.getData();

                ImageCropFunction();
            }
        } else if (requestCode == 1) {

            if (data != null) {
                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                circleImageView.setImageBitmap(bitmap);
            }
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(imageUri, "image/*");

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

    private void fileUploader() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String num=user.getPhoneNumber();
        String pno =num;
        StorageReference Ref = mStorageref.child(pno);
        UploadTask uploadTask = Ref.putFile(imageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return Ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    System.out.println("Upload " + downloadUri);
                    Toast.makeText(progress1.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    if (downloadUri != null) {
                        String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("link", photoStringLink);
                        db.collection("photourls").document(pno).set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(progress1.this, "Link Uploaded", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(progress1.this, "no link", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {

                }
            }
        });
    }
}
//@Override
//  public void onActivityResult(int requestCode, int resultCode, Intent data)
//  {
//   if (requestCode == IMAGE_PICKER_SELECT
//          && resultCode == Activity.RESULT_OK) {
//      String path = getPathFromCameraData(data, this.getActivity());
//      Log.i("PICTURE", "Path: " + path);

//   }
// }


// public static String getPathFromCameraData(Intent data, Context context) {
//     Uri selectedImage = data.getData();
//    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//    Cursor cursor = context.getContentResolver().query(selectedImage,
//         filePathColumn, null, null, null);
//   cursor.moveToFirst();
//  int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//  String picturePath = cursor.getString(columnIndex);
// cursor.close();
//  return picturePath;
// }
