package com.example.goa.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.goa.R;

public class MyPicFragment extends Fragment {

    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)throws NullPointerException {
        View view = inflater.inflate(R.layout.fragment_mypic, container, false);
        imageView = view.findViewById(R.id.myImageView);
        try {
//        Bitmap bitmap= BitmapFactory.decodeFile(getActivity().getIntent().getStringExtra("image_path"));
            Bitmap bitmap = null;
            if (getArguments() != null) {
                bitmap = BitmapFactory.decodeFile(getArguments().getString("image_path"));
            }
            imageView.setImageBitmap(bitmap);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return view;
    }
}
