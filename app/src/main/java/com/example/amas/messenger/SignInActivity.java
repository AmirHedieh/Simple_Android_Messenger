package com.example.amas.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //buttons
        findViewById(R.id.ask_account_existence).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.sign_in_email);
        password = findViewById(R.id.sign_in_password);

        showAnimation();
    }

    private void showAnimation(){
        email.setRotationX(-90f);
        password.setRotationX(-90f);
        
        email.animate().rotationXBy(90f).setDuration(1000);
        password.animate().rotationXBy(90f).setDuration(950);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.sign_in){
            if(!validateInfo())
                return;

            signInToFirebase(email.getText().toString(), password.getText().toString());
        }

        if(id == R.id.ask_account_existence){
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
        }

    }

    private void signInToFirebase(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("Sign_in","Sign in was successful");
                FirebaseUser user = authResult.getUser();
            }
        });
    }

    private boolean validateInfo(){
        Boolean valid = true;

        if(TextUtils.isEmpty(email.getText().toString())){
            valid = false;
            email.setError("Required.");
        }

        if(TextUtils.isEmpty(password.getText().toString())){
            valid = false;
            password.setError("Required.");
        }

        return valid;
    }

}
