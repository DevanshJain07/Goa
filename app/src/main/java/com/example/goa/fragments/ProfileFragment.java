package com.example.goa.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.goa.MyPagerAdapter;
import com.example.goa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;



import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements Tab3.OnFragmentInteractionListener,Tab1.OnFragmentInteractionListener,Tab2.OnFragmentInteractionListener {
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private static ImageView mImageView;
    private Button mCameraButton;
    private static final int REQUEST_IMAGE_CAPTURE=101;
    MyPagerAdapter adapter;
    private Uri mainImageURI=null;
    private String user_id;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Bitmap compressedImageFile;


    private StorageReference image_path;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TabLayout tabLayout;
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Photos Uploaded"));
        tabLayout.addTab(tabLayout.newTab().setText("Questions Asked"));
        tabLayout.addTab(tabLayout.newTab().setText("Comments added"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = view.findViewById(R.id.viewPager);

        adapter = new MyPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore= FirebaseFirestore.getInstance();
//        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://goa-travel-guide.appspot.com");
        mImageView = view.findViewById(R.id.imageView);
        mCameraButton = view.findViewById(R.id.button);


        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){



                    if(task.getResult().exists()){



                        String image = task.getResult().getString("image");
                        mainImageURI = Uri.parse(image);

//                        Toast.makeText(getContext(),"MID-WAY", Toast.LENGTH_LONG).show();
                        RequestOptions placeholderRequest =new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.ic_launcher);            //DEFAULT IMAGE

                        Glide.with(getContext()).setDefaultRequestOptions(placeholderRequest).load(image).into(mImageView);                                //NOT LOADING

                    }

                }else{

                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(),"FIRESTORE Retrieve Error : "+error , Toast.LENGTH_LONG).show();

                }

            }
        });


        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {        //IMPORTANT

                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);           //PERMISSION REQUEST

                    } else {

                        chooseImage();

                    }

                } else {

                    chooseImage();

                }

            }
        });

        return view;

    }

    private void chooseImage(){

        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imageTakeIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }


    }

    private void storeFirestore(Task<UploadTask.TaskSnapshot> task) {


        image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Map<String, String> userMap = new HashMap<>();
                userMap.put("image", uri.toString());                             //CASE SENSITIVE
//
                firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getContext(), "The user Settings are Updated. ", Toast.LENGTH_LONG).show();

                        } else {

                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), "FIRESTORE Error : " + error, Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }
        });
    }

    public void uploadImage(){
        if ( mainImageURI != null) {
            user_id = firebaseAuth.getCurrentUser().getUid();

            image_path = storageReference.child("profile_images").child(user_id + ".jpg");
            image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                    if (task.isSuccessful()) {


                        storeFirestore(task);

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), "Error : " + error, Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mainImageURI= getImageUri(getContext(),imageBitmap);
                mImageView.setImageBitmap(imageBitmap);
                uploadImage();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
