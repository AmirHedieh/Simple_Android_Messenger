package com.example.amas.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //buttons
        findViewById(R.id.ask_account_existence).setOnClickListener(this);

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

        if(id == R.id.ask_account_existence){
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
        }

    }
}
