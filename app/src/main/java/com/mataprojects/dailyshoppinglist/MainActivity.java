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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAth;
    private EditText email;
    private EditText pass;
    private Button loginButton;
    private TextView signUp;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAth = FirebaseAuth.getInstance();
        if (firebaseAth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
        progressDialog = new ProgressDialog(this);

        email = findViewById(R.id.email_text_login);
        pass=findViewById(R.id.password_text_login);


        loginButton = findViewById(R.id.loginButton_loginSS);
        signUp = findViewById(R.id.signUText_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPassword = pass.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)){
                    email.setError("Required Field ...");
                    return;
                }
                if (TextUtils.isEmpty(mPassword)){
                    pass.setError("Required Text ...");
                    return;
                }

                progressDialog.setMessage("Processing...");
                progressDialog.show();

                firebaseAth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            Toast.makeText(getApplicationContext(),"Complete",Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }else {
                            Toast.makeText(getApplicationContext(),"Failed ",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });


    }
}
