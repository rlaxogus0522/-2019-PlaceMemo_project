package com.example.placememo_project.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.R;
import com.example.placememo_project.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLoginBinding loginBinding;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 522;
    public  static final int RC_SIGN_OUT = 526;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    ProgressDialog progressDialog;
    long backKeyPressedTime;
    int conected;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginBinding.GoogleLogin.setOnClickListener(this);
        loginBinding.GuestLogin.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("로그인중 입니다...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        Intent intent = getIntent();
        conected = intent.getIntExtra("conected",-1);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        updateUI(null);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Log.d("로그인","onActivityResult");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }else if ( requestCode == RC_SIGN_OUT){
            signOut();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("로그인","firebaseAuthWithGoogle");
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
                            Log.d("+++", ""+currentUser.getPhotoUrl());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user","google");
                            intent.putExtra("name",currentUser.getDisplayName());
                            intent.putExtra("email",currentUser.getEmail());
                            intent.putExtra("photo",currentUser.getPhotoUrl().toString());
                            intent.putExtra("UID",currentUser.getUid());
                            intent.putExtra("conected",conected);
                            startActivityForResult(intent,RC_SIGN_OUT);
                            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void signInAnonymously() {
        progressDialog.show();
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            currentUser = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("user","guest");
                            intent.putExtra("name",currentUser.getDisplayName());
                            intent.putExtra("email",currentUser.getEmail());
                            intent.putExtra("UID",currentUser.getUid());
                            startActivityForResult(intent,RC_SIGN_OUT);
                            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                        } else {
                            Log.d("실패","실패");

                        }

                        // [START_EXCLUDE]
                        progressDialog.dismiss();
                        // [END_EXCLUDE]
                    }
                });
        // [END signin_anonymously]
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            if(currentUser.getPhotoUrl()!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user", "google");
            intent.putExtra("name", currentUser.getDisplayName());
            intent.putExtra("email", currentUser.getEmail());
            intent.putExtra("photo", currentUser.getPhotoUrl().toString());
            intent.putExtra("UID",currentUser.getUid());
            intent.putExtra("conected",conected);
            startActivityForResult(intent, RC_SIGN_OUT);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }else{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", "guest");
                intent.putExtra("UID",currentUser.getUid());
                startActivityForResult(intent, RC_SIGN_OUT);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }
    }




    @Override
    public void onClick(View view) {
        if(view == loginBinding.GoogleLogin){
            Log.d("로그인","클릭함");
            signIn();
        }else if(view == loginBinding.GuestLogin){
            signInAnonymously();
//
//
//            Intent intent = new Intent(this,MainActivity.class);
//            intent.putExtra("user","guest");
//            startActivity(intent);
//            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
//            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "두번 눌러 앱 종료", Toast.LENGTH_SHORT).show();
        }
        //2번째 백버튼 클릭 (종료)
        else{
            finishAffinity();
        }
    }
}