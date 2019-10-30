package com.example.placememo_project.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import static com.example.placememo_project.activity.MainActivity.mainContext;

public class LocationMemoActivity extends Fragment {

    public LocationMemoActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ((MainActivity)mainContext).locationView;
    }
}
