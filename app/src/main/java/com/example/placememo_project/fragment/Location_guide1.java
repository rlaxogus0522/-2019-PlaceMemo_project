package com.example.placememo_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.placememo_project.R;
import com.example.placememo_project.activity.MainActivity;

import static com.example.placememo_project.activity.MainActivity.mainContext;

public class Location_guide1 extends Fragment {

    public Location_guide1(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_location_guide1,container,false);
        return rootView;
    }
}
