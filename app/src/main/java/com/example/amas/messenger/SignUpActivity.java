package com.example.amas.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;

    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;

    private CircleImageView profilePhotoImageView;

    private Button photo_selector_button;
    private Button registerButton;

    ProgressDialog dialog;

    Uri profilePhotoUri = null; //uri used for profile photo

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Register");

        mAuth = FirebaseAuth.getInstance();

        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        profilePhotoImageView = findViewById(R.id.profile_image_view);

        photo_selector_button = findViewById(R.id.photo_selector);
        registerButton = findViewById(R.id.register_button);

        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.back_to_sign_in).setOnClickListener(this);
        findViewById(R.id.photo_selector).setOnClickListener(this);

        dialog = new ProgressDialog(SignUpActivity.this);

//        fillRandomData();

        showAnimation();
    }

    private void fillRandomData(){
        Random rnd = new Random();

        String username = "";
        username += rnd.nextInt(100) + rnd.nextInt(100) + rnd.nextInt(100);

        String email = "";
        email += rnd.nextInt(50);
        email += rnd.nextInt(50) +  "@gmail.com";

        String password = "123456798";

        mUsername.setText(username);
        mEmail.setText(email);
        mPassword.setText(password);
    }

    private void showAnimation(){
        photo_selector_button.setAlpha(0f);
        registerButton.setAlpha(0f);

        mUsername.setRotationX(-90f);
        mEmail.setRotationX(-90f);
        mPassword.setRotationX(-90f);

        photo_selector_button.animate().alphaBy(1f).setDuration(1000);
        registerButton.animate().alphaBy(1f).setDuration(1000);

        mUsername.animate().rotationXBy(90f).setDuration(1000);
        mEmail.animate().rotationXBy(90f).setDuration(1000);
        mPassword.animate().rotationXBy(90f).setDuration(1000);
    }

    public void createAccount(String email, String password){
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        Log.d("main:",email + password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            uploadProfilePhotoToFireBaseStorage();

                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
//                            updateUI(user);
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                                try{
                                Toast.makeText(SignUpActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                catch (NullPointerException ex){
                                }
//                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void showProgressDialog() {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Registering user");
        dialog.setMessage("please wait");
        dialog.show();
    }

    private void hideProgressDialog(){
        dialog.hide();
    }

    public void openSignUpScreen(View view){
        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
    }

    private boolean validateForm() {
        boolean valid = true;

        if(profilePhotoImageView.getDrawable() == null){
            Toast.makeText(this, "Please choose a picture", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        String username = mUsername.getText().toString();
        if(TextUtils.isEmpty(username)){
            mUsername.setError("Required.");
            valid = false;
        } else {
            mUsername.setError(null);
        }

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Required.");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.register_button){
            createAccount(mEmail.getText().toString(), mPassword.getText().toString());
        }
        else if(id == R.id.back_to_sign_in){
            SignUpActivity.this.finish();
        }
        else if(id == R.id.photo_selector){
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setFixAspectRatio(true)
                    .setAspectRatio(1,1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profilePhotoUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profilePhotoUri);
                    profilePhotoImageView.setImageBitmap(bitmap);

                    photo_selector_button.setAlpha(0f);

                } catch(IOException ex){

                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadProfilePhotoToFireBaseStorage(){
        if(profilePhotoUri == null) {
            Toast.makeText(this, "Select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        String fileName = UUID.randomUUID().toString(); // make a random string for the name of the file

        final StorageReference sFirebase = FirebaseStorage.getInstance().getReference("/image/" + fileName);

        sFirebase.putFile(profilePhotoUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SignUpActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
                sFirebase.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                         registerUserOnFireBaseDataBase(uri.toString()); // cautious : NullPointer Exception may be thrown
                    }
                });
            }
        });
    }

    private void registerUserOnFireBaseDataBase(String photoUrl){
        String uid = mAuth.getUid();
        String username = mUsername.getText().toString();
        String email = mEmail.getText().toString();

        User user = new User(uid,username,photoUrl,email);

         DatabaseReference mData = FirebaseDatabase.getInstance().getReference("/users/" + uid);

         mData.setValue(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Log.d("Registration","User was added to database");
                 hideProgressDialog();
             }
         });
    }





}
