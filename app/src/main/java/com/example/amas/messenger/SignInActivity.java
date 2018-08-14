package com.example.amas.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * its a firebase feature that if a user signs in, the firebase Authentication keeps that user logged in until
 * user signs out, even restarting app doesn't signs out the user.
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

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

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        email = findViewById(R.id.sign_in_email);
        password = findViewById(R.id.sign_in_password);

        getSupportActionBar().setTitle("Sign in");

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

        if(id == R.id.sign_in_button){
            if(!validateInfo())
                return;

            signInToFirebase(email.getText().toString(), password.getText().toString());
        }

        if(id == R.id.ask_account_existence){
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
        }

    }

    private void signInToFirebase(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("Sign_in","Sign in was successful");
                    Toast.makeText(SignInActivity.this, "Signed in", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignInActivity.this,UserMessagingActivity.class);
                    startActivity(intent);
                }
                else{
                    Log.d("Sign_in","Sign in Failed!");
                    Toast.makeText(SignInActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateInfo(){
        Boolean valid = true;

        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Required.");
            valid = false;
        }

        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("Required.");
            valid = false;
        }

        return valid;
    }

}
