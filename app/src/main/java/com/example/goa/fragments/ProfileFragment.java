package com.example.goa.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.goa.R;
import com.google.android.material.tabs.TabLayout;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements Tab3.OnFragmentInteractionListener,Tab1.OnFragmentInteractionListener,Tab2.OnFragmentInteractionListener {
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private static ImageView mImageView;
    private Button mCameraButton;
    private static final int REQUEST_IMAGE_CAPTURE=101;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TabLayout tabLayout;
        tabLayout = (TabLayout) (R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Photos Uploaded"));
        tabLayout.addTab(tabLayout.newTab().setText("Questions Asked"));
        tabLayout.addTab(tabLayout.newTab().setText("Comments added"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        final PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(new TabLayout.OnTabSelectedListener());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mImageView = view.findViewById(R.id.imageView);
        mCameraButton = view.findViewById(R.id.button);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (imageTakeIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


//    public void takePicture(View view) {
//        Intent imageTakeIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if(imageTakeIntent.resolveActivity(getActivity().getPackageManager())!=null){
//           startActivityForResult(imageTakeIntent,REQUEST_IMAGE_CAPTURE);
//        }
//    }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageView.setImageBitmap(imageBitmap);
            }
        }


    }

}
