package com.mataprojects.dailyshoppinglist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

    public class RegistrationActivity extends AppCompatActivity {

    private EditText emailRegistration;
    private EditText passwordRegistration;
    private Button signUpButton;

    private TextView signIn;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailRegistration= findViewById(R.id.email_text_registration);
        passwordRegistration = findViewById(R.id.password_text_registration);

        signUpButton= findViewById(R.id.signUpButton_registration);
        signIn = findViewById(R.id.signInText_registration);


        firebaseAuth =FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = emailRegistration.getText().toString().trim();
                String mPassword = passwordRegistration.getText().toString().trim();
                if (TextUtils.isEmpty(mEmail)) {
                    emailRegistration.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    emailRegistration.setError("Required Field...");
                    return;
                }

                progressDialog.setMessage("Processing");
                progressDialog.dismiss();
                firebaseAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"Somthing went Wrong",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });




    }
}
