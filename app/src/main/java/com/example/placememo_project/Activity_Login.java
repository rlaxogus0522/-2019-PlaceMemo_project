package com.example.placememo_project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.placememo_project.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class Activity_Login  extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loginBinding =

    }
}
